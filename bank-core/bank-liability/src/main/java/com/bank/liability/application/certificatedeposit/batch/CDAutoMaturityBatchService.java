package com.bank.liability.application.certificatedeposit.batch;

import com.bank.liability.application.batch.BatchResult;
import com.bank.liability.domain.certificatedeposit.entity.CDMaturityBatchLog;
import com.bank.liability.domain.certificatedeposit.entity.CertificateDepositAccount;
import com.bank.liability.domain.certificatedeposit.repository.CDMaturityBatchLogRepository;
import com.bank.liability.domain.certificatedeposit.repository.CertificateDepositAccountRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CDAutoMaturityBatchService {

    private static final BigDecimal DEMAND_DEPOSIT_RATE = new BigDecimal("0.35");
    private static final int SCALE = 2;

    private final CertificateDepositAccountRepository accountRepository;
    private final CDMaturityBatchLogRepository batchLogRepository;

    @Transactional
    public BatchResult executeBatch(CDAutoMaturityBatchCommand command) {
        LocalDate processingDate = command.getProcessingDate() != null
                ? command.getProcessingDate() : LocalDate.now();

        String batchNo = "CDMAT" + processingDate.toString().replace("-", "")
                + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        log.info("Starting CD auto maturity batch: {}, date: {}", batchNo, processingDate);

        List<CertificateDepositAccount> maturedAccounts = accountRepository.findAll()
                .stream()
                .filter(a -> "ACTIVE".equals(a.getStatus())
                        && a.getMaturityDate() != null
                        && !a.getMaturityDate().isAfter(processingDate))
                .toList();

        if (maturedAccounts.isEmpty()) {
            log.info("No matured certificate deposits found for date {}", processingDate);
            return BatchResult.builder()
                    .batchNo(batchNo)
                    .totalItems(0)
                    .successCount(0)
                    .failCount(0)
                    .status("COMPLETED")
                    .message("No matured certificate deposits to process")
                    .build();
        }

        int successCount = 0;
        int failCount = 0;

        for (CertificateDepositAccount account : maturedAccounts) {
            try {
                processMaturity(account, processingDate, batchNo);
                successCount++;
            } catch (Exception e) {
                log.error("Failed to process maturity for account {}: {}",
                        account.getCdAccountNo(), e.getMessage());
                recordBatchLog(batchNo, account, processingDate, "FAILED", e.getMessage());
                failCount++;
            }
        }

        log.info("CD auto maturity batch {} completed: success={}, fail={}",
                batchNo, successCount, failCount);

        return BatchResult.builder()
                .batchNo(batchNo)
                .totalItems(maturedAccounts.size())
                .successCount(successCount)
                .failCount(failCount)
                .status(failCount > 0 ? "PARTIALLY_COMPLETED" : "COMPLETED")
                .message("CD auto maturity batch executed")
                .build();
    }

    private void processMaturity(CertificateDepositAccount account,
                                  LocalDate processingDate, String batchNo) {
        BigDecimal interestAmount;

        if (account.getMaturityDate().isBefore(processingDate)) {
            interestAmount = account.getPrincipal()
                    .multiply(DEMAND_DEPOSIT_RATE)
                    .multiply(BigDecimal.valueOf(account.getTermMonths()))
                    .divide(BigDecimal.valueOf(1200), SCALE, BigDecimal.ROUND_HALF_UP);
            log.info("Account {} is overdue (maturity={}), applying demand rate {}",
                    account.getCdAccountNo(), account.getMaturityDate(), DEMAND_DEPOSIT_RATE);
        } else {
            interestAmount = account.calculateInterest();
        }

        account.setInterest(interestAmount);
        account.setTotalAmount(account.getPrincipal().add(interestAmount));
        account.setStatus("REDEEMED");
        account.setRedeemDate(LocalDate.now());
        account.setUpdatedAt(LocalDateTime.now());

        accountRepository.update(account);
        recordBatchLog(batchNo, account, processingDate, "SUCCESS", null);
    }

    private void recordBatchLog(String batchNo, CertificateDepositAccount account,
                                 LocalDate processingDate, String status, String errorMessage) {
        CDMaturityBatchLog logEntry = new CDMaturityBatchLog();
        logEntry.setBatchNo(batchNo);
        logEntry.setCdAccountNo(account.getCdAccountNo());
        logEntry.setProductCode(account.getProductCode());
        logEntry.setPrincipal(account.getPrincipal());
        logEntry.setInterestRate(account.getInterestRate());
        logEntry.setInterestAmount(account.getInterest());
        logEntry.setTotalAmount(account.getTotalAmount());
        logEntry.setStatus(status);
        logEntry.setMaturityDate(account.getMaturityDate());
        logEntry.setProcessingDate(processingDate);
        logEntry.setErrorMessage(errorMessage);
        logEntry.setVersion(0);
        logEntry.setCreatedAt(LocalDateTime.now());
        logEntry.setUpdatedAt(LocalDateTime.now());
        batchLogRepository.save(logEntry);
    }
}