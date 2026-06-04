package com.bank.liability.domain.certificatedeposit.repository;

import com.bank.liability.domain.certificatedeposit.entity.CertificateDepositProduct;
import java.util.List;
import java.util.Optional;

public interface CertificateDepositProductRepository {
    void save(CertificateDepositProduct product);
    void update(CertificateDepositProduct product);
    Optional<CertificateDepositProduct> findById(Long id);
    Optional<CertificateDepositProduct> findByProductCode(String productCode);
    List<CertificateDepositProduct> findAll();
    boolean existsByProductCode(String productCode);
}
