package com.bank.liability.application.batch;

import com.bank.common.exception.BusinessException;
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
public class DailyAccrualBatchService {

    private static final int SCALE = 2;
    private static final int CALC_SCALE = 8;
    private static final BigDecimal DAYS_PER_YEAR = new BigDecimal("360");
    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100");

    private final AccountRepository accountRepository;
    private final AccountInterestDetailRepository interestDetailRepository;
    private final BatchRunLogRepository batchRunLogRepository;

    public BatchResult execute(LocalDate systemDate, LocalDate calcDate) {
        if (systemDate == null) systemDate = LocalDate.now();
        if (calcDate == null) calcDate = systemDate.minusDays(1);

        String batchNo = "ACR" + calcDate.toString().replace("-", "")
                + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        log.info("=== Daily Accrual Batch ===");
        log.info("  batchNo={}, systemDate={}, calcDate={}", batchNo, systemDate, calcDate);

        BatchRunLog runLog = createRunLog(batchNo, systemDate, calcDate);

        List<AccountInfo> accounts;
        try {
            accounts = accountRepository.findAllAccounts().stream()
                    .filter(a -> "0".equals(a.getAccountStatus()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Failed to query accounts: {}", e.getMessage());
            failRunLog(runLog, "Failed to query accounts: " + e.getMessage());
            return BatchResult.builder().batchNo(batchNo).status("FAILED")
                    .message("Account query failed: " + e.getMessage()).build();
        }

        log.info("Found {} active accounts to process", accounts.size());

        int successCount = 0;
        int failCount = 0;

        for (AccountInfo account : accounts) {
            try {
                processAccount(account, batchNo, systemDate, calcDate);
                successCount++;
            } catch (Exception e) {
                log.error("Failed to process account {}: {}", account.getAccountNo(), e.getMessage());
                recordFailure(account, batchNo, calcDate, e.getMessage());
                failCount++;
            }
        }

        runLog.setTotalAccounts(accounts.size());
        runLog.setSuccessCount(successCount);
        runLog.setFailCount(failCount);
        runLog.setEndTime(LocalDateTime.now());
        runLog.setStatus(failCount > 0 ? "COMPLETED_WITH_ERRORS" : "COMPLETED");
        batchRunLogRepository.update(runLog);

        log.info("=== Daily Accrual Batch {} completed: total={}, success={}, fail={} ===",
                batchNo, accounts.size(), successCount, failCount);

        return BatchResult.builder()
                .batchNo(batchNo)
                .totalItems(accounts.size())
                .successCount(successCount)
                .failCount(failCount)
                .status(runLog.getStatus())
                .message("Daily accrual for " + calcDate + " completed")
                .build();
    }

    private void processAccount(AccountInfo account, String batchNo,
                                 LocalDate systemDate, LocalDate calcDate) {
        BigDecimal rate = resolveInterestRate(account);
        if (rate == null || rate.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("rate is null or <= 0 for account " + account.getAccountNo());
        }

        LocalDate balUpdateDate = account.getBalanceUpdateDate();
        BigDecimal calcPrincipal;
        String balanceType;

        if (balUpdateDate != null && balUpdateDate.equals(systemDate)) {
            calcPrincipal = account.getLastDayBalance();
            balanceType = "LAST_DAY";
        } else {
            calcPrincipal = account.getCurrentBalance();
            balanceType = "CURRENT";
        }

        if (calcPrincipal == null || calcPrincipal.compareTo(BigDecimal.ZERO) <= 0) {
            log.debug("Account {} balance is zero/negative ({} = {}), skip interest",
                    account.getAccountNo(), balanceType, calcPrincipal);
            recordZeroBalance(account, batchNo, calcDate, rate, balanceType, calcPrincipal);
            return;
        }

        BigDecimal dailyInterest = calcPrincipal
                .multiply(rate)
                .divide(ONE_HUNDRED.multiply(DAYS_PER_YEAR), CALC_SCALE, RoundingMode.HALF_UP)
                .setScale(SCALE, RoundingMode.HALF_UP);

        AccountInterestDetail detail = new AccountInterestDetail();
        detail.setBatchNo(batchNo);
        detail.setAccountNo(account.getAccountNo());
        detail.setProductCode(account.getProductCode());
        detail.setCalcDate(calcDate);
        detail.setBalanceType(balanceType);
        detail.setCalcBalance(calcPrincipal);
        detail.setInterestRate(rate);
        detail.setDailyInterest(dailyInterest);
        detail.setAccruedInterest(BigDecimal.ZERO);
        detail.setBatchType("DAILY_ACCRUAL");
        detail.setStatus("ACCRUED");
        detail.setVersion(0);
        detail.setCreatedAt(LocalDateTime.now());
        detail.setUpdatedAt(LocalDateTime.now());

        interestDetailRepository.save(detail);

        log.debug("  Account {}: balance={} ({}), rate={}%, dailyInterest={}",
                account.getAccountNo(), calcPrincipal, balanceType, rate, dailyInterest);
    }

    private void recordFailure(AccountInfo account, String batchNo,
                                LocalDate calcDate, String errorMsg) {
        AccountInterestDetail detail = new AccountInterestDetail();
        detail.setBatchNo(batchNo);
        detail.setAccountNo(account.getAccountNo());
        detail.setProductCode(account.getProductCode());
        detail.setCalcDate(calcDate);
        detail.setBalanceType("NONE");
        detail.setCalcBalance(BigDecimal.ZERO);
        detail.setInterestRate(BigDecimal.ZERO);
        detail.setDailyInterest(BigDecimal.ZERO);
        detail.setAccruedInterest(BigDecimal.ZERO);
        detail.setBatchType("DAILY_ACCRUAL");
        detail.setStatus("FAILED");
        detail.setErrorMessage(errorMsg);
        detail.setVersion(0);
        detail.setCreatedAt(LocalDateTime.now());
        detail.setUpdatedAt(LocalDateTime.now());
        interestDetailRepository.save(detail);
    }

    private void recordZeroBalance(AccountInfo account, String batchNo,
                                    LocalDate calcDate, BigDecimal rate,
                                    String balanceType, BigDecimal balance) {
        AccountInterestDetail detail = new AccountInterestDetail();
        detail.setBatchNo(batchNo);
        detail.setAccountNo(account.getAccountNo());
        detail.setProductCode(account.getProductCode());
        detail.setCalcDate(calcDate);
        detail.setBalanceType(balanceType);
        detail.setCalcBalance(balance != null ? balance : BigDecimal.ZERO);
        detail.setInterestRate(rate);
        detail.setDailyInterest(BigDecimal.ZERO);
        detail.setAccruedInterest(BigDecimal.ZERO);
        detail.setBatchType("DAILY_ACCRUAL");
        detail.setStatus("ACCRUED");
        detail.setErrorMessage("Zero balance, no interest accrued");
        detail.setVersion(0);
        detail.setCreatedAt(LocalDateTime.now());
        detail.setUpdatedAt(LocalDateTime.now());
        interestDetailRepository.save(detail);
    }

    private BigDecimal resolveInterestRate(AccountInfo account) {
        String pc = account.getProductCode();
        if (pc == null) return DEFAULT_RATE;
        return DEFAULT_RATES.getOrDefault(pc, DEFAULT_RATE);
    }

    private BatchRunLog createRunLog(String batchNo, LocalDate systemDate, LocalDate calcDate) {
        BatchRunLog log = new BatchRunLog();
        log.setBatchNo(batchNo);
        log.setBatchType("DAILY_ACCRUAL");
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

    private static final BigDecimal DEFAULT_RATE = new BigDecimal("0.35");

    private static final Map<String, BigDecimal> DEFAULT_RATES = Map.ofEntries(
        Map.entry("21000001", new BigDecimal("0.35")),
        Map.entry("21000002", new BigDecimal("0.35")),
        Map.entry("21000003", new BigDecimal("1.50")),
        Map.entry("21000004", new BigDecimal("1.00")),
        Map.entry("21000005", new BigDecimal("1.50")),
        Map.entry("21000006", new BigDecimal("1.00")),
        Map.entry("21000007", new BigDecimal("0.35")),
        Map.entry("21000008", new BigDecimal("1.50")),
        Map.entry("21000009", new BigDecimal("0.35")),
        Map.entry("21000010", new BigDecimal("1.50")),
        Map.entry("21000015", new BigDecimal("0.35")),
        Map.entry("21000016", new BigDecimal("1.50")),
        Map.entry("21000017", new BigDecimal("0.35")),
        Map.entry("21000018", new BigDecimal("0.35")),
        Map.entry("21000019", new BigDecimal("0.35")),
        Map.entry("21000020", new BigDecimal("0.35"))
    );
}
