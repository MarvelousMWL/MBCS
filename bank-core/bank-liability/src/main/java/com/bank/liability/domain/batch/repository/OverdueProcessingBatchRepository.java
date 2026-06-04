package com.bank.liability.domain.batch.repository;

import com.bank.liability.domain.batch.entity.OverdueProcessingBatch;
import java.util.List;
import java.util.Optional;

public interface OverdueProcessingBatchRepository {
    void save(OverdueProcessingBatch batch);
    void update(OverdueProcessingBatch batch);
    Optional<OverdueProcessingBatch> findById(Long id);
    List<OverdueProcessingBatch> findByBatchNo(String batchNo);
    List<OverdueProcessingBatch> findByStatus(String status);
    List<OverdueProcessingBatch> findByAccountNo(String accountNo);
    List<OverdueProcessingBatch> findAll();
    long countByStatus(String status);
}
