package com.bank.liability.application.certificatedeposit.query;

import com.bank.liability.domain.certificatedeposit.entity.CertificateDepositAccount;
import com.bank.liability.domain.certificatedeposit.entity.CertificateDepositProduct;
import com.bank.liability.domain.certificatedeposit.repository.CertificateDepositAccountRepository;
import com.bank.liability.domain.certificatedeposit.repository.CertificateDepositProductRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CDQueryService {

    private final CertificateDepositProductRepository productRepository;
    private final CertificateDepositAccountRepository accountRepository;

    public List<CertificateDepositProduct> listAllProducts() {
        return productRepository.findAll();
    }

    public CertificateDepositProduct findProductByCode(String productCode) {
        return productRepository.findByProductCode(productCode)
                .orElse(null);
    }

    public List<CertificateDepositAccount> listAccountsByCustomer(String customerAccountNo) {
        return accountRepository.findByCustomerAccountNo(customerAccountNo);
    }

    public List<CertificateDepositAccount> listAccountsByProduct(String productCode) {
        return accountRepository.findByProductCode(productCode);
    }

    public CertificateDepositAccount findAccountByNo(String cdAccountNo) {
        return accountRepository.findByCdAccountNo(cdAccountNo).orElse(null);
    }

    public List<CertificateDepositAccount> listAllAccounts() {
        return accountRepository.findAll();
    }
}
