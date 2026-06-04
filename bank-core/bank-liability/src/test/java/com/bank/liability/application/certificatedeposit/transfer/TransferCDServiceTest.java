package com.bank.liability.application.certificatedeposit.transfer;

import com.bank.common.exception.BusinessException;
import com.bank.liability.domain.certificatedeposit.entity.CertificateDepositAccount;
import com.bank.liability.domain.certificatedeposit.entity.CertificateDepositTransferRecord;
import com.bank.liability.domain.certificatedeposit.repository.CertificateDepositAccountRepository;
import com.bank.liability.domain.certificatedeposit.repository.CertificateDepositTransferRecordRepository;
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
class TransferCDServiceTest {

    @Mock private CertificateDepositAccountRepository accountRepository;
    @Mock private CertificateDepositTransferRecordRepository transferRecordRepository;
    @InjectMocks private TransferCDService transferCDService;

    private CertificateDepositAccount createActiveAccount(String cdAccountNo, String customerNo,
                                                           BigDecimal principal, BigDecimal rate, int months) {
        CertificateDepositAccount a = new CertificateDepositAccount();
        a.setCdAccountNo(cdAccountNo);
        a.setCustomerAccountNo(customerNo);
        a.setProductCode("CD2026001");
        a.setPrincipal(principal);
        a.setInterest(BigDecimal.ZERO);
        a.setTotalAmount(principal);
        a.setStatus("ACTIVE");
        a.setSubscribeDate(LocalDate.now().minusMonths(3));
        a.setMaturityDate(LocalDate.now().plusMonths(9));
        a.setInterestTransferAccount(customerNo);
        a.setInterestRate(rate);
        a.setTermMonths(months);
        a.setVersion(0);
        return a;
    }

    private TransferCDCommand createTransferCommand(String cdAccountNo, String transferor, String transferee,
                                                     BigDecimal price, String pricingType) {
        TransferCDCommand cmd = new TransferCDCommand();
        cmd.setCdAccountNo(cdAccountNo);
        cmd.setTransferorAccountNo(transferor);
        cmd.setTransfereeAccountNo(transferee);
        cmd.setTransferPrice(price);
        cmd.setPricingType(pricingType);
        cmd.setHandlingFee(BigDecimal.ZERO);
        cmd.setOperatorNo("OPE001");
        return cmd;
    }

    @Test
    void transfer_shouldSucceed_whenValidRequestWithSystemPricing() {
        // Arrange
        BigDecimal principal = BigDecimal.valueOf(1000000);
        BigDecimal rate = BigDecimal.valueOf(3.5);
        int months = 12;
        BigDecimal expectedInterest = principal.multiply(rate).multiply(BigDecimal.valueOf(months))
                .divide(BigDecimal.valueOf(1200), 2, BigDecimal.ROUND_HALF_UP);
        BigDecimal systemPrice = principal.add(expectedInterest);

        CertificateDepositAccount account = createActiveAccount("CD001", "TRANSFEROR001",
                principal, rate, months);
        TransferCDCommand cmd = createTransferCommand("CD001", "TRANSFEROR001", "TRANSFEREE001",
                systemPrice, "SYSTEM");

        when(accountRepository.findByCdAccountNo("CD001")).thenReturn(Optional.of(account));

        // Act
        CertificateDepositAccount result = transferCDService.transfer(cmd);

        // Assert
        assertNotNull(result);
        assertEquals("TRANSFEREE001", result.getCustomerAccountNo());
        assertEquals("TRANSFEREE001", result.getInterestTransferAccount());
        verify(accountRepository).update(account);
        verify(transferRecordRepository).save(any(CertificateDepositTransferRecord.class));
    }

    @Test
    void transfer_shouldSucceed_whenValidRequestWithCustomerPricing() {
        CertificateDepositAccount account = createActiveAccount("CD002", "TRANSFEROR001",
                BigDecimal.valueOf(1000000), BigDecimal.valueOf(3.5), 12);
        TransferCDCommand cmd = createTransferCommand("CD002", "TRANSFEROR001", "TRANSFEREE002",
                BigDecimal.valueOf(1050000), "CUSTOMER");

        when(accountRepository.findByCdAccountNo("CD002")).thenReturn(Optional.of(account));

        CertificateDepositAccount result = transferCDService.transfer(cmd);

        assertNotNull(result);
        assertEquals("TRANSFEREE002", result.getCustomerAccountNo());
        assertEquals("TRANSFEREE002", result.getInterestTransferAccount());
        verify(accountRepository).update(account);
        verify(transferRecordRepository).save(any(CertificateDepositTransferRecord.class));
    }

    @Test
    void transfer_shouldThrowException_whenAccountNotExist() {
        TransferCDCommand cmd = createTransferCommand("CD999", "TRANSFEROR001", "TRANSFEREE001",
                BigDecimal.valueOf(1000000), "SYSTEM");
        when(accountRepository.findByCdAccountNo("CD999")).thenReturn(Optional.empty());
        assertThrows(BusinessException.class, () -> transferCDService.transfer(cmd));
    }

    @Test
    void transfer_shouldThrowException_whenAccountStatusNotActive() {
        CertificateDepositAccount account = createActiveAccount("CD001", "TRANSFEROR001",
                BigDecimal.valueOf(1000000), BigDecimal.valueOf(3.5), 12);
        account.setStatus("REDEEMED");
        TransferCDCommand cmd = createTransferCommand("CD001", "TRANSFEROR001", "TRANSFEREE001",
                BigDecimal.valueOf(1000000), "CUSTOMER");
        when(accountRepository.findByCdAccountNo("CD001")).thenReturn(Optional.of(account));
        assertThrows(BusinessException.class, () -> transferCDService.transfer(cmd));
    }

    @Test
    void transfer_shouldThrowException_whenTransferorNotMatch() {
        CertificateDepositAccount account = createActiveAccount("CD001", "TRANSFEROR001",
                BigDecimal.valueOf(1000000), BigDecimal.valueOf(3.5), 12);
        TransferCDCommand cmd = createTransferCommand("CD001", "WRONG_OWNER", "TRANSFEREE001",
                BigDecimal.valueOf(1000000), "CUSTOMER");
        when(accountRepository.findByCdAccountNo("CD001")).thenReturn(Optional.of(account));
        assertThrows(BusinessException.class, () -> transferCDService.transfer(cmd));
    }

    @Test
    void transfer_shouldThrowException_whenTransferorEqualsTransferee() {
        CertificateDepositAccount account = createActiveAccount("CD001", "SAME_USER",
                BigDecimal.valueOf(1000000), BigDecimal.valueOf(3.5), 12);
        TransferCDCommand cmd = createTransferCommand("CD001", "SAME_USER", "SAME_USER",
                BigDecimal.valueOf(1000000), "CUSTOMER");
        when(accountRepository.findByCdAccountNo("CD001")).thenReturn(Optional.of(account));
        assertThrows(BusinessException.class, () -> transferCDService.transfer(cmd));
    }

    @Test
    void transfer_shouldThrowException_whenInvalidPricingType() {
        CertificateDepositAccount account = createActiveAccount("CD001", "TRANSFEROR001",
                BigDecimal.valueOf(1000000), BigDecimal.valueOf(3.5), 12);
        TransferCDCommand cmd = createTransferCommand("CD001", "TRANSFEROR001", "TRANSFEREE001",
                BigDecimal.valueOf(1000000), "INVALID");
        when(accountRepository.findByCdAccountNo("CD001")).thenReturn(Optional.of(account));
        assertThrows(BusinessException.class, () -> transferCDService.transfer(cmd));
    }

    @Test
    void transfer_shouldThrowException_whenSystemPriceMismatch() {
        CertificateDepositAccount account = createActiveAccount("CD001", "TRANSFEROR001",
                BigDecimal.valueOf(1000000), BigDecimal.valueOf(3.5), 12);
        TransferCDCommand cmd = createTransferCommand("CD001", "TRANSFEROR001", "TRANSFEREE001",
                BigDecimal.valueOf(500000), "SYSTEM");
        when(accountRepository.findByCdAccountNo("CD001")).thenReturn(Optional.of(account));
        assertThrows(BusinessException.class, () -> transferCDService.transfer(cmd));
    }
}
