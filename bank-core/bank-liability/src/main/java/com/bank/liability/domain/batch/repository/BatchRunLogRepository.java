package com.bank.liability.domain.batch.repository;

import com.bank.liability.domain.batch.entity.BatchRunLog;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BatchRunLogRepository {
    void save(BatchRunLog log);
    void update(BatchRunLog log);
    Optional<BatchRunLog> findById(Long id);
    List<BatchRunLog> findByBatchTypeAndCalcDate(String batchType, LocalDate calcDate);
    List<BatchRunLog> findTopN(int n);
}
