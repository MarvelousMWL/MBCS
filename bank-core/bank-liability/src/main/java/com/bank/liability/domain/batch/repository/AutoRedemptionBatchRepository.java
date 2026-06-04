package com.bank.liability.domain.batch.repository;

import com.bank.liability.domain.batch.entity.AutoRedemptionBatch;
import java.util.List;
import java.util.Optional;

public interface AutoRedemptionBatchRepository {
    void save(AutoRedemptionBatch batch);
    void update(AutoRedemptionBatch batch);
    Optional<AutoRedemptionBatch> findById(Long id);
    List<AutoRedemptionBatch> findByBatchNo(String batchNo);
    List<AutoRedemptionBatch> findByStatus(String status);
    List<AutoRedemptionBatch> findByMaturityDateBefore(java.time.LocalDate date);
    List<AutoRedemptionBatch> findAll();
    long countByStatus(String status);
}
