package com.bank.liability.domain.batch.repository;

import com.bank.liability.domain.batch.entity.InterestSettlementBatch;
import java.util.List;
import java.util.Optional;

public interface InterestSettlementBatchRepository {
    void save(InterestSettlementBatch batch);
    void update(InterestSettlementBatch batch);
    Optional<InterestSettlementBatch> findById(Long id);
    List<InterestSettlementBatch> findByBatchNo(String batchNo);
    List<InterestSettlementBatch> findByStatus(String status);
    List<InterestSettlementBatch> findBySettlementDate(java.time.LocalDate settlementDate);
    List<InterestSettlementBatch> findAll();
    long countByStatus(String status);
}
