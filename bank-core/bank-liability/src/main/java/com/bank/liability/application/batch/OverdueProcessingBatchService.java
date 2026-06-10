package com.bank.liability.application.batch;

import com.bank.common.exception.BusinessException;
import com.bank.liability.domain.batch.entity.OverdueProcessingBatch;
import com.bank.liability.domain.batch.repository.OverdueProcessingBatchRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OverdueProcessingBatchService {

    private final OverdueProcessingBatchRepository repository;

    @Transactional
    public BatchResult executeBatch(LocalDate processingDate) {
        if (processingDate == null) {
            processingDate = LocalDate.now();
        }

        String batchNo = "OP" + processingDate.toString().replace("-", "") + "-" +
                UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        log.info("Starting overdue processing batch: {}, date: {}", batchNo, processingDate);

        List<OverdueProcessingBatch> pendingItems = repository.findByStatus("UNPROCESSED");
        if (pendingItems.isEmpty()) {
            return BatchResult.builder()
                    .batchNo(batchNo)
                    .totalItems(0)
                    .successCount(0)
                    .failCount(0)
                    .status("COMPLETED")
                    .message("No pending overdue items to process")
                    .build();
        }

        int successCount = 0;
        int failCount = 0;

        for (OverdueProcessingBatch item : pendingItems) {
            try {
                processItem(item, batchNo, processingDate);
                successCount++;
            } catch (Exception e) {
                log.error("Failed to process overdue item {}: {}", item.getId(), e.getMessage());
                item.setStatus("FAILED");
                item.setErrorMessage(e.getMessage());
                repository.update(item);
                failCount++;
            }
        }

        log.info("Overdue processing batch {} completed: success={}, fail={}", batchNo, successCount, failCount);

        return BatchResult.builder()
                .batchNo(batchNo)
                .totalItems(pendingItems.size())
                .successCount(successCount)
                .failCount(failCount)
                .status(failCount > 0 ? "PARTIALLY_COMPLETED" : "COMPLETED")
                .message("Overdue processing batch executed")
                .build();
    }

    public BatchResult addOverdueItem(String accountNo, String productCode,
                                       BigDecimal principal, BigDecimal currentInterestRate,
                                       LocalDate maturityDate) {
        if (principal == null || principal.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("principal must be greater than 0");
        }
        if (currentInterestRate == null || currentInterestRate.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("current interest rate cannot be negative");
        }
        if (maturityDate == null) {
            throw new BusinessException("maturity date cannot be null");
        }
        if (!maturityDate.isBefore(LocalDate.now())) {
            throw new BusinessException("account not yet overdue, maturity date: " + maturityDate);
        }

        int overdueDays = (int) ChronoUnit.DAYS.between(maturityDate.plusDays(1), LocalDate.now());

        OverdueProcessingBatch item = new OverdueProcessingBatch();
        item.setAccountNo(accountNo);
        item.setProductCode(productCode);
        item.setPrincipal(principal);
        item.setCurrentInterestRate(currentInterestRate);
        item.setOverdueDays(overdueDays);
        item.setStatus("UNPROCESSED");
        item.setVersion(0);
        item.setCreatedAt(LocalDateTime.now());
        item.setUpdatedAt(LocalDateTime.now());

        repository.save(item);

        return BatchResult.builder()
                .batchNo("PENDING")
                .totalItems(1)
                .successCount(1)
                .failCount(0)
                .status("QUEUED")
                .message("Overdue processing item added successfully")
                .build();
    }

    private void processItem(OverdueProcessingBatch item, String batchNo, LocalDate processingDate) {
        if (item.getOverdueDays() == null || item.getOverdueDays() <= 0) {
            throw new BusinessException("overdue days must be greater than 0");
        }

        BigDecimal calculatedInterest = item.calculateOverdueInterest();
        if (calculatedInterest.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("calculated interest is negative");
        }

        item.setBatchNo(batchNo);
        item.setCalculatedInterest(calculatedInterest);
        item.setProcessedDate(processingDate);
        item.setStatus("PROCESSED");
        item.setUpdatedAt(LocalDateTime.now());
        repository.update(item);
    }
}
