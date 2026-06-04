package com.bank.liability.domain.certificatedeposit.repository;

import com.bank.liability.domain.certificatedeposit.entity.CertificateDepositAccount;
import java.util.List;
import java.util.Optional;

public interface CertificateDepositAccountRepository {
    void save(CertificateDepositAccount account);
    void update(CertificateDepositAccount account);
    Optional<CertificateDepositAccount> findById(Long id);
    Optional<CertificateDepositAccount> findByCdAccountNo(String cdAccountNo);
    List<CertificateDepositAccount> findByCustomerAccountNo(String customerAccountNo);
    List<CertificateDepositAccount> findByProductCode(String productCode);
    List<CertificateDepositAccount> findAll();
    boolean existsByCdAccountNo(String cdAccountNo);
    Long countByProductCode(String productCode);
}
