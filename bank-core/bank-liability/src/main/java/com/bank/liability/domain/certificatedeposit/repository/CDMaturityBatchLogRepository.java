package com.bank.liability.domain.certificatedeposit.repository;

import com.bank.liability.domain.certificatedeposit.entity.CDMaturityBatchLog;
import java.util.List;
import java.util.Optional;

public interface CDMaturityBatchLogRepository {
    void save(CDMaturityBatchLog log);
    void update(CDMaturityBatchLog log);
    Optional<CDMaturityBatchLog> findById(Long id);
    List<CDMaturityBatchLog> findByBatchNo(String batchNo);
    List<CDMaturityBatchLog> findAll();
}