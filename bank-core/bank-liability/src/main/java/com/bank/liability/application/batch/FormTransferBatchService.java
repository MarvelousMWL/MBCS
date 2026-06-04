package com.bank.liability.application.batch;

import com.bank.common.exception.BusinessException;
import com.bank.liability.domain.batch.entity.FormTransferBatch;
import com.bank.liability.domain.batch.repository.FormTransferBatchRepository;
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
public class FormTransferBatchService {

    private final FormTransferBatchRepository repository;

    @Transactional
    public BatchResult executeBatch(LocalDate processingDate) {
        if (processingDate == null) {
            processingDate = LocalDate.now();
        }

        String batchNo = "FT" + processingDate.toString().replace("-", "") + "-" +
                UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        log.info("Starting form transfer batch: {}, date: {}", batchNo, processingDate);

        List<FormTransferBatch> pendingItems = repository.findByStatus("UNPROCESSED");
        if (pendingItems.isEmpty()) {
            return BatchResult.builder()
                    .batchNo(batchNo)
                    .totalItems(0)
                    .successCount(0)
                    .failCount(0)
                    .status("COMPLETED")
                    .message("No pending form transfer items to process")
                    .build();
        }

        int successCount = 0;
        int failCount = 0;

        for (FormTransferBatch item : pendingItems) {
            try {
                processItem(item, batchNo, processingDate);
                successCount++;
            } catch (Exception e) {
                log.error("Failed to process form transfer item {}: {}", item.getId(), e.getMessage());
                item.setStatus("FAILED");
                item.setErrorMessage(e.getMessage());
                repository.update(item);
                failCount++;
            }
        }

        log.info("Form transfer batch {} completed: success={}, fail={}", batchNo, successCount, failCount);

        return BatchResult.builder()
                .batchNo(batchNo)
                .totalItems(pendingItems.size())
                .successCount(successCount)
                .failCount(failCount)
                .status(failCount > 0 ? "PARTIALLY_COMPLETED" : "COMPLETED")
                .message("Form transfer batch executed")
                .build();
    }

    public BatchResult addTransferItem(String accountNo, String productCode,
                                        BigDecimal principal, String sourceAccountType,
                                        String targetAccountType) {
        if (principal == null || principal.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("principal must be greater than 0");
        }
        if (sourceAccountType == null || sourceAccountType.isBlank()) {
            throw new BusinessException("source account type cannot be blank");
        }
        if (targetAccountType == null || targetAccountType.isBlank()) {
            throw new BusinessException("target account type cannot be blank");
        }
        if (sourceAccountType.equals(targetAccountType)) {
            throw new BusinessException("source and target account types must be different");
        }

        FormTransferBatch item = new FormTransferBatch();
        item.setAccountNo(accountNo);
        item.setProductCode(productCode);
        item.setPrincipal(principal);
        item.setTransferAmount(principal);
        item.setSourceAccountType(sourceAccountType);
        item.setTargetAccountType(targetAccountType);
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
                .message("Form transfer item added successfully")
                .build();
    }

    private void processItem(FormTransferBatch item, String batchNo, LocalDate processingDate) {
        if (item.getPrincipal() == null || item.getPrincipal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("principal must be greater than 0");
        }

        item.setBatchNo(batchNo);
        item.setTransferAmount(item.getPrincipal());
        item.setProcessedDate(processingDate);
        item.setStatus("PROCESSED");
        item.setUpdatedAt(LocalDateTime.now());
        repository.update(item);
    }
}
