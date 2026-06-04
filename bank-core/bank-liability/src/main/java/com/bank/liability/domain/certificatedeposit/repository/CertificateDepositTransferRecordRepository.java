package com.bank.liability.domain.certificatedeposit.repository;

import com.bank.liability.domain.certificatedeposit.entity.CertificateDepositTransferRecord;
import java.util.List;
import java.util.Optional;

public interface CertificateDepositTransferRecordRepository {
    void save(CertificateDepositTransferRecord record);
    void update(CertificateDepositTransferRecord record);
    Optional<CertificateDepositTransferRecord> findById(Long id);
    List<CertificateDepositTransferRecord> findByCdAccountNo(String cdAccountNo);
}
