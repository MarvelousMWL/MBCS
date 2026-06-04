package com.bank.liability.application.certificatedeposit.issue;

import com.bank.common.exception.BusinessException;
import com.bank.liability.domain.certificatedeposit.entity.CertificateDepositProduct;
import com.bank.liability.domain.certificatedeposit.repository.CertificateDepositProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IssueCDProductServiceTest {

    @Mock private CertificateDepositProductRepository productRepository;
    @InjectMocks private IssueCDProductService issueCDProductService;

    private IssueCDProductCommand createCommand(String productCode) {
        IssueCDProductCommand cmd = new IssueCDProductCommand();
        cmd.setProductCode(productCode);
        cmd.setProductName("Test CD Product");
        cmd.setTotalQuota(BigDecimal.valueOf(10000000));
        cmd.setMinSubscriptionAmount(BigDecimal.valueOf(200000));
        cmd.setMaxSubscriptionAmountPerCustomer(BigDecimal.valueOf(5000000));
        cmd.setTermMonths(12);
        cmd.setInterestRate(BigDecimal.valueOf(3.5));
        cmd.setIssueStartDate(LocalDate.now());
        cmd.setIssueEndDate(LocalDate.now().plusMonths(3));
        cmd.setCustomerType("CORPORATE");
        cmd.setCurrency("156");
        return cmd;
    }

    @Test
    void issue_shouldSucceed_whenValidCommand() {
        IssueCDProductCommand cmd = createCommand("CD2026001");
        when(productRepository.existsByProductCode("CD2026001")).thenReturn(false);

        CertificateDepositProduct result = issueCDProductService.issue(cmd);

        assertNotNull(result);
        assertEquals("CD2026001", result.getProductCode());
        assertEquals(BigDecimal.valueOf(10000000), result.getTotalQuota());
        assertEquals(BigDecimal.valueOf(10000000), result.getRemainingQuota());
        assertEquals("ACTIVE", result.getStatus());
        verify(productRepository).save(any(CertificateDepositProduct.class));
    }

    @Test
    void issue_shouldThrowException_whenProductCodeExists() {
        IssueCDProductCommand cmd = createCommand("CD2026001");
        when(productRepository.existsByProductCode("CD2026001")).thenReturn(true);

        assertThrows(BusinessException.class, () -> issueCDProductService.issue(cmd));
        verify(productRepository, never()).save(any());
    }

    @Test
    void issue_shouldThrowException_whenEndDateBeforeStartDate() {
        IssueCDProductCommand cmd = createCommand("CD2026001");
        cmd.setIssueStartDate(LocalDate.of(2026, 6, 1));
        cmd.setIssueEndDate(LocalDate.of(2026, 5, 1));
        when(productRepository.existsByProductCode("CD2026001")).thenReturn(false);

        assertThrows(BusinessException.class, () -> issueCDProductService.issue(cmd));
        verify(productRepository, never()).save(any());
    }
}
