package com.bank.liability.application.batch;

import com.bank.liability.domain.account.entity.AccountInfo;
import com.bank.liability.domain.account.entity.AccountInterestDetail;
import com.bank.liability.domain.account.repository.AccountInterestDetailRepository;
import com.bank.liability.domain.account.repository.AccountRepository;
import com.bank.liability.domain.batch.entity.BatchRunLog;
import com.bank.liability.domain.batch.repository.BatchRunLogRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class InterestSettlementService {

    private final AccountRepository accountRepository;
    private final AccountInterestDetailRepository interestDetailRepository;
    private final BatchRunLogRepository batchRunLogRepository;

    @Transactional
    public BatchResult execute(LocalDate systemDate, LocalDate calcDate) {
        if (systemDate == null) systemDate = LocalDate.now();
        if (calcDate == null) calcDate = systemDate.minusDays(1);

        int calcMonth = calcDate.getMonthValue();
        int calcDay = calcDate.getDayOfMonth();

        boolean isQuarterlySettlement = (calcMonth == 3 || calcMonth == 6 || calcMonth == 9 || calcMonth == 12)
                && calcDay == 21;

        String batchNo = "SET" + calcDate.toString().replace("-", "")
                + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        log.info("=== Interest Settlement ===");
        log.info("  batchNo={}, systemDate={}, calcDate={}, isQuarterlySettlement={}",
                batchNo, systemDate, calcDate, isQuarterlySettlement);

        BatchRunLog runLog = createRunLog(batchNo, systemDate, calcDate);

        if (!isQuarterlySettlement) {
            log.info("Today ({}) is not a quarterly settlement date, skipping", calcDate);
            runLog.setTotalAccounts(0);
            runLog.setSuccessCount(0);
            runLog.setFailCount(0);
            runLog.setEndTime(LocalDateTime.now());
            runLog.setStatus("SKIPPED");
            runLog.setErrorMessage("Not a settlement date (quarterly: 3/6/9/12 month 21st)");
            batchRunLogRepository.update(runLog);
            return BatchResult.builder()
                    .batchNo(batchNo).totalItems(0).successCount(0).failCount(0)
                    .status("SKIPPED")
                    .message("Not a settlement date, skipped")
                    .build();
        }

        List<AccountInfo> accounts;
        try {
            accounts = accountRepository.findAllAccounts().stream()
                    .filter(a -> "0".equals(a.getAccountStatus()))
                    .filter(a -> isDemandProduct(a.getProductCode()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Failed to query accounts for settlement: {}", e.getMessage());
            failRunLog(runLog, "Account query failed: " + e.getMessage());
            return BatchResult.builder().batchNo(batchNo).status("FAILED")
                    .message("Account query failed: " + e.getMessage()).build();
        }

        log.info("Found {} demand accounts for interest settlement", accounts.size());
        int successCount = 0;
        int failCount = 0;

        for (AccountInfo account : accounts) {
            try {
                settleAccountInterest(account, batchNo, calcDate, systemDate);
                successCount++;
            } catch (Exception e) {
                log.error("Failed to settle interest for account {}: {}",
                        account.getAccountNo(), e.getMessage());
                failCount++;
            }
        }

        runLog.setTotalAccounts(accounts.size());
        runLog.setSuccessCount(successCount);
        runLog.setFailCount(failCount);
        runLog.setEndTime(LocalDateTime.now());
        runLog.setStatus(failCount > 0 ? "COMPLETED_WITH_ERRORS" : "COMPLETED");
        batchRunLogRepository.update(runLog);

        log.info("=== Settlement batch {} completed: total={}, success={}, fail={} ===",
                batchNo, accounts.size(), successCount, failCount);

        return BatchResult.builder()
                .batchNo(batchNo)
                .totalItems(accounts.size())
                .successCount(successCount)
                .failCount(failCount)
                .status(runLog.getStatus())
                .message("Interest settlement for " + calcDate + " completed")
                .build();
    }

    private void settleAccountInterest(AccountInfo account, String batchNo,
                                        LocalDate calcDate, LocalDate systemDate) {
        List<AccountInterestDetail> accruals = interestDetailRepository
                .findByStatusAndBatchType("ACCRUED", "DAILY_ACCRUAL")
                .stream()
                .filter(d -> account.getAccountNo().equals(d.getAccountNo()))
                .collect(Collectors.toList());

        BigDecimal totalInterest = BigDecimal.ZERO;
        for (AccountInterestDetail d : accruals) {
            totalInterest = totalInterest.add(d.getDailyInterest());
        }

        if (totalInterest.compareTo(BigDecimal.ZERO) <= 0) {
            log.debug("Account {} has no accrued interest to settle", account.getAccountNo());
            return;
        }

        BigDecimal newBalance = account.getCurrentBalance().add(totalInterest);

        for (AccountInterestDetail d : accruals) {
            d.setAccruedInterest(totalInterest);
            d.setStatus("SETTLED");
            d.setErrorMessage("Settled in batch " + batchNo + " on " + systemDate);
            interestDetailRepository.update(d);
        }

        account.setCurrentBalance(newBalance);
        account.setBalanceUpdateDate(systemDate);
        account.setLastBusinessDate(systemDate);
        account.setUpdatedAt(LocalDateTime.now());
        accountRepository.updateAccount(account);

        log.info("  Account {} settled: interest={}, newBalance={}",
                account.getAccountNo(), totalInterest, newBalance);
    }

    private boolean isDemandProduct(String productCode) {
        if (productCode == null) return false;
        return DEMAND_PRODUCTS.contains(productCode);
    }

    private BatchRunLog createRunLog(String batchNo, LocalDate systemDate, LocalDate calcDate) {
        BatchRunLog log = new BatchRunLog();
        log.setBatchNo(batchNo);
        log.setBatchType("SETTLEMENT");
        log.setSystemDate(systemDate);
        log.setCalcDate(calcDate);
        log.setTotalAccounts(0);
        log.setSuccessCount(0);
        log.setFailCount(0);
        log.setStartTime(LocalDateTime.now());
        log.setStatus("RUNNING");
        log.setCreatedAt(LocalDateTime.now());
        batchRunLogRepository.save(log);
        return log;
    }

    private void failRunLog(BatchRunLog runLog, String errorMsg) {
        runLog.setEndTime(LocalDateTime.now());
        runLog.setStatus("FAILED");
        runLog.setErrorMessage(errorMsg);
        batchRunLogRepository.update(runLog);
    }

    private static final List<String> DEMAND_PRODUCTS = List.of(
            "21000001", "21000002", "21000007", "21000009",
            "21000015", "21000017", "21000018", "21000019", "21000020"
    );
}
