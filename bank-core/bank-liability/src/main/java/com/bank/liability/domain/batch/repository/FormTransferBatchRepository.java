package com.bank.liability.domain.batch.repository;

import com.bank.liability.domain.batch.entity.FormTransferBatch;
import java.util.List;
import java.util.Optional;

public interface FormTransferBatchRepository {
    void save(FormTransferBatch batch);
    void update(FormTransferBatch batch);
    Optional<FormTransferBatch> findById(Long id);
    List<FormTransferBatch> findByBatchNo(String batchNo);
    List<FormTransferBatch> findByStatus(String status);
    List<FormTransferBatch> findByAccountNo(String accountNo);
    List<FormTransferBatch> findAll();
    long countByStatus(String status);
}
