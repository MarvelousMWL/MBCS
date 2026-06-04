package com.bank.liability.application.batch;

import com.bank.common.exception.BusinessException;
import com.bank.liability.domain.batch.entity.InterestSettlementBatch;
import com.bank.liability.domain.batch.repository.InterestSettlementBatchRepository;
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
public class InterestSettlementBatchService {

    private final InterestSettlementBatchRepository repository;

    @Transactional
    public BatchResult executeBatch(LocalDate settlementDate) {
        if (settlementDate == null) {
            settlementDate = LocalDate.now();
        }

        String batchNo = "IS" + settlementDate.toString().replace("-", "") + "-" +
                UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        log.info("Starting interest settlement batch: {}, date: {}", batchNo, settlementDate);

        List<InterestSettlementBatch> pendingItems = repository.findByStatus("UNPROCESSED");
        if (pendingItems.isEmpty()) {
            return BatchResult.builder()
                    .batchNo(batchNo)
                    .totalItems(0)
                    .successCount(0)
                    .failCount(0)
                    .status("COMPLETED")
                    .message("No pending items to process")
                    .build();
        }

        int successCount = 0;
        int failCount = 0;

        for (InterestSettlementBatch item : pendingItems) {
            try {
                processItem(item, batchNo, settlementDate);
                successCount++;
            } catch (Exception e) {
                log.error("Failed to process settlement item {}: {}", item.getId(), e.getMessage());
                item.setStatus("FAILED");
                item.setErrorMessage(e.getMessage());
                repository.update(item);
                failCount++;
            }
        }

        log.info("Interest settlement batch {} completed: success={}, fail={}", batchNo, successCount, failCount);

        return BatchResult.builder()
                .batchNo(batchNo)
                .totalItems(pendingItems.size())
                .successCount(successCount)
                .failCount(failCount)
                .status(failCount > 0 ? "PARTIALLY_COMPLETED" : "COMPLETED")
                .message("Interest settlement batch executed")
                .build();
    }

    public BatchResult addSettlementItem(String accountNo, String productCode,
                                          BigDecimal principal, BigDecimal interestRate) {
        if (principal == null || principal.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("principal must be greater than 0");
        }
        if (interestRate == null || interestRate.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("interest rate cannot be negative");
        }

        InterestSettlementBatch item = new InterestSettlementBatch();
        item.setAccountNo(accountNo);
        item.setProductCode(productCode);
        item.setPrincipal(principal);
        item.setInterestRate(interestRate);
        item.setStatus("UNPROCESSED");
        item.setSettlementDate(LocalDate.now());
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
                .message("Settlement item added successfully")
                .build();
    }

    private void processItem(InterestSettlementBatch item, String batchNo, LocalDate settlementDate) {
        BigDecimal calculatedInterest = item.calculateSettlementInterest();
        if (calculatedInterest.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("calculated interest is negative");
        }

        item.setBatchNo(batchNo);
        item.setCalculatedInterest(calculatedInterest);
        item.setSettlementDate(settlementDate);
        item.setStatus("PROCESSED");
        item.setUpdatedAt(LocalDateTime.now());
        repository.update(item);
    }
}