package com.bank.liability.application.batch;

import com.bank.common.exception.BusinessException;
import com.bank.liability.domain.batch.entity.AutoRedemptionBatch;
import com.bank.liability.domain.batch.repository.AutoRedemptionBatchRepository;
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
public class AutoRedemptionBatchService {

    private final AutoRedemptionBatchRepository repository;

    @Transactional
    public BatchResult executeBatch(LocalDate processingDate) {
        if (processingDate == null) {
            processingDate = LocalDate.now();
        }

        String batchNo = "AR" + processingDate.toString().replace("-", "") + "-" +
                UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        log.info("Starting auto redemption batch: {}, date: {}", batchNo, processingDate);

        List<AutoRedemptionBatch> pendingItems = repository.findByStatus("UNPROCESSED");
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

        for (AutoRedemptionBatch item : pendingItems) {
            try {
                processItem(item, batchNo, processingDate);
                successCount++;
            } catch (Exception e) {
                log.error("Failed to process redemption item {}: {}", item.getId(), e.getMessage());
                item.setStatus("FAILED");
                item.setErrorMessage(e.getMessage());
                repository.update(item);
                failCount++;
            }
        }

        log.info("Auto redemption batch {} completed: success={}, fail={}", batchNo, successCount, failCount);

        return BatchResult.builder()
                .batchNo(batchNo)
                .totalItems(pendingItems.size())
                .successCount(successCount)
                .failCount(failCount)
                .status(failCount > 0 ? "PARTIALLY_COMPLETED" : "COMPLETED")
                .message("Auto redemption batch executed")
                .build();
    }

    public BatchResult addRedemptionItem(String accountNo, String productCode,
                                          BigDecimal principal, BigDecimal interest,
                                          LocalDate maturityDate, String rolloverType) {
        if (principal == null || principal.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("principal must be greater than 0");
        }
        if (maturityDate == null) {
            throw new BusinessException("maturity date cannot be null");
        }

        AutoRedemptionBatch item = new AutoRedemptionBatch();
        item.setAccountNo(accountNo);
        item.setProductCode(productCode);
        item.setPrincipal(principal);
        item.setInterest(interest != null ? interest : BigDecimal.ZERO);
        item.setTotalAmount(principal.add(item.getInterest()));
        item.setMaturityDate(maturityDate);
        item.setRolloverType(rolloverType != null ? rolloverType : "AUTO_TRANSFER_TO_CURRENT");
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
                .message("Redemption item added successfully")
                .build();
    }

    private void processItem(AutoRedemptionBatch item, String batchNo, LocalDate processingDate) {
        if (item.getMaturityDate().isAfter(processingDate)) {
            throw new BusinessException("account not yet matured, maturity date: " + item.getMaturityDate());
        }

        BigDecimal totalAmount = item.getPrincipal().add(item.getInterest());
        if (totalAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("total amount must be greater than 0");
        }

        item.setBatchNo(batchNo);
        item.setTotalAmount(totalAmount);
        item.setProcessedDate(processingDate);
        item.setStatus("PROCESSED");
        item.setUpdatedAt(LocalDateTime.now());
        repository.update(item);
    }
}