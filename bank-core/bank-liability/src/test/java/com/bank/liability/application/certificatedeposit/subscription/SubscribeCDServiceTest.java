package com.bank.liability.application.certificatedeposit.subscription;

import com.bank.common.exception.BusinessException;
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
import java.time.LocalDate;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubscribeCDServiceTest {

    @Mock private CertificateDepositProductRepository productRepository;
    @Mock private CertificateDepositAccountRepository accountRepository;
    @InjectMocks private SubscribeCDService subscribeCDService;

    private CertificateDepositProduct createActiveProduct(BigDecimal remaining, BigDecimal minAmount) {
        CertificateDepositProduct p = new CertificateDepositProduct();
        p.setProductCode("CD2026001");
        p.setProductName("Test CD");
        p.setTotalQuota(BigDecimal.valueOf(10000000));
        p.setRemainingQuota(remaining);
        p.setMinSubscriptionAmount(minAmount);
        p.setMaxSubscriptionAmountPerCustomer(BigDecimal.valueOf(5000000));
        p.setTermMonths(12);
        p.setInterestRate(BigDecimal.valueOf(3.5));
        p.setIssueStartDate(LocalDate.now().minusDays(1));
        p.setIssueEndDate(LocalDate.now().plusMonths(2));
        p.setStatus("ACTIVE");
        return p;
    }

    private SubscribeCDCommand createCommand(String productCode, BigDecimal amount) {
        SubscribeCDCommand cmd = new SubscribeCDCommand();
        cmd.setProductCode(productCode);
        cmd.setCustomerAccountNo("CUST001");
        cmd.setAmount(amount);
        cmd.setOperatorNo("OPE001");
        return cmd;
    }

    @Test
    void subscribe_shouldSucceed_whenValidRequest() {
        CertificateDepositProduct product = createActiveProduct(BigDecimal.valueOf(5000000), BigDecimal.valueOf(200000));
        SubscribeCDCommand cmd = createCommand("CD2026001", BigDecimal.valueOf(1000000));

        when(productRepository.findByProductCode("CD2026001")).thenReturn(Optional.of(product));

        CertificateDepositAccount result = subscribeCDService.subscribe(cmd);

        assertNotNull(result);
        assertEquals("CUST001", result.getCustomerAccountNo());
        assertEquals(BigDecimal.valueOf(1000000), result.getPrincipal());
        assertEquals("ACTIVE", result.getStatus());
        assertEquals(BigDecimal.valueOf(4000000), product.getRemainingQuota());
        verify(productRepository).update(product);
        verify(accountRepository).save(any(CertificateDepositAccount.class));
    }

    @Test
    void subscribe_shouldThrowException_whenProductNotExist() {
        SubscribeCDCommand cmd = createCommand("CD999", BigDecimal.valueOf(1000000));
        when(productRepository.findByProductCode("CD999")).thenReturn(Optional.empty());
        assertThrows(BusinessException.class, () -> subscribeCDService.subscribe(cmd));
    }

    @Test
    void subscribe_shouldThrowException_whenProductNotActive() {
        CertificateDepositProduct product = createActiveProduct(BigDecimal.valueOf(5000000), BigDecimal.valueOf(200000));
        product.setStatus("CANCELLED");
        SubscribeCDCommand cmd = createCommand("CD2026001", BigDecimal.valueOf(1000000));
        when(productRepository.findByProductCode("CD2026001")).thenReturn(Optional.of(product));
        assertThrows(BusinessException.class, () -> subscribeCDService.subscribe(cmd));
    }

    @Test
    void subscribe_shouldThrowException_whenOutsideIssuePeriod() {
        CertificateDepositProduct product = createActiveProduct(BigDecimal.valueOf(5000000), BigDecimal.valueOf(200000));
        product.setIssueStartDate(LocalDate.now().plusDays(10));
        SubscribeCDCommand cmd = createCommand("CD2026001", BigDecimal.valueOf(1000000));
        when(productRepository.findByProductCode("CD2026001")).thenReturn(Optional.of(product));
        assertThrows(BusinessException.class, () -> subscribeCDService.subscribe(cmd));
    }

    @Test
    void subscribe_shouldThrowException_whenInsufficientQuota() {
        CertificateDepositProduct product = createActiveProduct(BigDecimal.valueOf(500000), BigDecimal.valueOf(200000));
        SubscribeCDCommand cmd = createCommand("CD2026001", BigDecimal.valueOf(1000000));
        when(productRepository.findByProductCode("CD2026001")).thenReturn(Optional.of(product));
        assertThrows(BusinessException.class, () -> subscribeCDService.subscribe(cmd));
    }

    @Test
    void subscribe_shouldThrowException_whenAmountBelowMinimum() {
        CertificateDepositProduct product = createActiveProduct(BigDecimal.valueOf(5000000), BigDecimal.valueOf(200000));
        SubscribeCDCommand cmd = createCommand("CD2026001", BigDecimal.valueOf(100000));
        when(productRepository.findByProductCode("CD2026001")).thenReturn(Optional.of(product));
        assertThrows(BusinessException.class, () -> subscribeCDService.subscribe(cmd));
    }
}
