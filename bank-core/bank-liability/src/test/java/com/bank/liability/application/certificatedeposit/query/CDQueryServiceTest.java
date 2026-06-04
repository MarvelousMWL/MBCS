package com.bank.liability.application.certificatedeposit.query;

import com.bank.liability.domain.certificatedeposit.entity.CertificateDepositAccount;
import com.bank.liability.domain.certificatedeposit.entity.CertificateDepositProduct;
import com.bank.liability.domain.certificatedeposit.repository.CertificateDepositAccountRepository;
import com.bank.liability.domain.certificatedeposit.repository.CertificateDepositProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CDQueryServiceTest {

    @Mock private CertificateDepositProductRepository productRepository;
    @Mock private CertificateDepositAccountRepository accountRepository;
    @InjectMocks private CDQueryService cdQueryService;

    @Test
    void listAllProducts_shouldReturnAll() {
        when(productRepository.findAll()).thenReturn(Arrays.asList(new CertificateDepositProduct(), new CertificateDepositProduct()));
        assertEquals(2, cdQueryService.listAllProducts().size());
    }

    @Test
    void listAllProducts_shouldReturnEmpty_whenNone() {
        when(productRepository.findAll()).thenReturn(Collections.emptyList());
        assertTrue(cdQueryService.listAllProducts().isEmpty());
    }

    @Test
    void findProductByCode_shouldReturnProduct() {
        CertificateDepositProduct p = new CertificateDepositProduct();
        p.setProductCode("CD001");
        when(productRepository.findByProductCode("CD001")).thenReturn(Optional.of(p));
        assertNotNull(cdQueryService.findProductByCode("CD001"));
        assertEquals("CD001", cdQueryService.findProductByCode("CD001").getProductCode());
    }

    @Test
    void findProductByCode_shouldReturnNull_whenNotFound() {
        when(productRepository.findByProductCode("CD999")).thenReturn(Optional.empty());
        assertNull(cdQueryService.findProductByCode("CD999"));
    }

    @Test
    void listAccountsByCustomer_shouldReturnAccounts() {
        CertificateDepositAccount a = new CertificateDepositAccount();
        a.setCustomerAccountNo("CUST001");
        when(accountRepository.findByCustomerAccountNo("CUST001")).thenReturn(Arrays.asList(a));
        assertEquals(1, cdQueryService.listAccountsByCustomer("CUST001").size());
    }

    @Test
    void findAccountByNo_shouldReturnAccount() {
        CertificateDepositAccount a = new CertificateDepositAccount();
        a.setCdAccountNo("CD001");
        when(accountRepository.findByCdAccountNo("CD001")).thenReturn(Optional.of(a));
        assertNotNull(cdQueryService.findAccountByNo("CD001"));
    }
}
