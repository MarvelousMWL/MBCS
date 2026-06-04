package com.bank.liability.domain.transfer.repository;

import com.bank.liability.domain.transfer.entity.TransferRecord;
import java.util.Optional;

public interface TransferRecordRepository {
    void save(TransferRecord record);
    void update(TransferRecord record);
    Optional<TransferRecord> findByTransferNo(String transferNo);
}
