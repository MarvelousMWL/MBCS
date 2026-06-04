package com.bank.liability.application.certificatedeposit.redemption;

import com.bank.common.exception.BusinessException;
import com.bank.liability.domain.certificatedeposit.entity.CertificateDepositAccount;
import com.bank.liability.domain.certificatedeposit.repository.CertificateDepositAccountRepository;
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
class RedeemCDServiceTest {

    @Mock private CertificateDepositAccountRepository accountRepository;
    @InjectMocks private RedeemCDService redeemCDService;

    private CertificateDepositAccount createActiveAccount(String cdAccountNo, BigDecimal principal, BigDecimal rate, int months) {
        CertificateDepositAccount a = new CertificateDepositAccount();
        a.setCdAccountNo(cdAccountNo);
        a.setCustomerAccountNo("CUST001");
        a.setProductCode("CD2026001");
        a.setPrincipal(principal);
        a.setInterest(BigDecimal.ZERO);
        a.setTotalAmount(principal);
        a.setStatus("ACTIVE");
        a.setSubscribeDate(LocalDate.now().minusMonths(months));
        a.setMaturityDate(LocalDate.now().plusDays(30));
        a.setInterestRate(rate);
        a.setTermMonths(months);
        return a;
    }

    private RedeemCDCommand createCommand(String cdAccountNo) {
        RedeemCDCommand cmd = new RedeemCDCommand();
        cmd.setCdAccountNo(cdAccountNo);
        cmd.setOperatorNo("OPE001");
        cmd.setRedeemType("MATURITY");
        return cmd;
    }

    @Test
    void redeem_shouldSucceed_whenAccountIsActive() {
        CertificateDepositAccount account = createActiveAccount("CD001", BigDecimal.valueOf(1000000), BigDecimal.valueOf(3.5), 12);
        RedeemCDCommand cmd = createCommand("CD001");
        when(accountRepository.findByCdAccountNo("CD001")).thenReturn(Optional.of(account));

        CertificateDepositAccount result = redeemCDService.redeem(cmd);

        assertNotNull(result);
        assertEquals("REDEEMED", result.getStatus());
        assertNotNull(result.getRedeemDate());
        assertEquals(0, BigDecimal.valueOf(35000).compareTo(result.getInterest()));
        assertTrue(result.getTotalAmount().compareTo(BigDecimal.valueOf(1000000)) > 0);
        verify(accountRepository).update(account);
    }

    @Test
    void redeem_shouldThrowException_whenAccountNotExist() {
        RedeemCDCommand cmd = createCommand("CD999");
        when(accountRepository.findByCdAccountNo("CD999")).thenReturn(Optional.empty());
        assertThrows(BusinessException.class, () -> redeemCDService.redeem(cmd));
    }

    @Test
    void redeem_shouldThrowException_whenAlreadyRedeemed() {
        CertificateDepositAccount account = createActiveAccount("CD001", BigDecimal.valueOf(1000000), BigDecimal.valueOf(3.5), 12);
        account.setStatus("REDEEMED");
        RedeemCDCommand cmd = createCommand("CD001");
        when(accountRepository.findByCdAccountNo("CD001")).thenReturn(Optional.of(account));
        assertThrows(BusinessException.class, () -> redeemCDService.redeem(cmd));
    }
}
