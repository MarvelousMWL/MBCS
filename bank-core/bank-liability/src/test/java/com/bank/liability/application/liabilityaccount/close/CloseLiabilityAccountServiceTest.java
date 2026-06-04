package com.bank.liability.application.liabilityaccount.close;

import com.bank.common.exception.BusinessException;
import com.bank.liability.domain.customersubaccount.entity.CustomerSubAccount;
import com.bank.liability.domain.customersubaccount.repository.CustomerSubAccountRepository;
import com.bank.liability.domain.enums.LiabilityAccountStatus;
import com.bank.liability.domain.enums.SubAccountStatus;
import com.bank.liability.domain.liabilityaccount.entity.LiabilityAccount;
import com.bank.liability.domain.liabilityaccount.repository.LiabilityAccountRepository;
import com.bank.liability.domain.liabilityaccount.service.LiabilityAccountDomainService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CloseLiabilityAccountServiceTest {

    @Mock private LiabilityAccountRepository liabilityAccountRepository;
    @Mock private LiabilityAccountDomainService liabilityAccountDomainService;
    @Mock private CustomerSubAccountRepository customerSubAccountRepository;

    @InjectMocks
    private CloseLiabilityAccountService closeLiabilityAccountService;

    @Test
    void close_shouldSucceed_whenAccountExistsAndValid() {
        LiabilityAccount account = new LiabilityAccount();
        account.setLiabilityAccountNo("LA001");
        account.setStatus(LiabilityAccountStatus.NORMAL);
        account.setBalance(BigDecimal.ZERO);

        CloseLiabilityAccountCommand command = new CloseLiabilityAccountCommand();
        command.setLiabilityAccountNo("LA001");

        when(liabilityAccountRepository.findByLiabilityAccountNo("LA001")).thenReturn(Optional.of(account));
        doNothing().when(liabilityAccountDomainService).validateClose(account);
        doNothing().when(liabilityAccountRepository).update(account);

        CustomerSubAccount subAccount = new CustomerSubAccount();
        subAccount.setLiabilityAccountNo("LA001");
        subAccount.setStatus(SubAccountStatus.NORMAL);
        when(customerSubAccountRepository.findByLiabilityAccountNo("LA001")).thenReturn(Optional.of(subAccount));
        doNothing().when(customerSubAccountRepository).update(subAccount);

        closeLiabilityAccountService.close(command, "OPE001");

        assertEquals(LiabilityAccountStatus.CLOSED, account.getStatus());
        assertNotNull(account.getCloseDate());
        verify(liabilityAccountRepository).findByLiabilityAccountNo("LA001");
        verify(liabilityAccountDomainService).validateClose(account);
        verify(liabilityAccountRepository).update(account);
        verify(customerSubAccountRepository).findByLiabilityAccountNo("LA001");
        verify(customerSubAccountRepository).update(subAccount);
        assertEquals(SubAccountStatus.CLOSED, subAccount.getStatus());
    }

    @Test
    void close_shouldSucceed_whenNoSubAccount() {
        LiabilityAccount account = new LiabilityAccount();
        account.setLiabilityAccountNo("LA001");
        account.setStatus(LiabilityAccountStatus.NORMAL);
        account.setBalance(BigDecimal.ZERO);

        CloseLiabilityAccountCommand command = new CloseLiabilityAccountCommand();
        command.setLiabilityAccountNo("LA001");

        when(liabilityAccountRepository.findByLiabilityAccountNo("LA001")).thenReturn(Optional.of(account));
        doNothing().when(liabilityAccountDomainService).validateClose(account);
        doNothing().when(liabilityAccountRepository).update(account);
        when(customerSubAccountRepository.findByLiabilityAccountNo("LA001")).thenReturn(Optional.empty());

        closeLiabilityAccountService.close(command, "OPE001");

        assertEquals(LiabilityAccountStatus.CLOSED, account.getStatus());
        verify(liabilityAccountRepository).update(account);
        verify(customerSubAccountRepository).findByLiabilityAccountNo("LA001");
        verify(customerSubAccountRepository, never()).update(any());
    }

    @Test
    void close_shouldThrowException_whenAccountNotExist() {
        CloseLiabilityAccountCommand command = new CloseLiabilityAccountCommand();
        command.setLiabilityAccountNo("NONEXIST");

        when(liabilityAccountRepository.findByLiabilityAccountNo("NONEXIST")).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> closeLiabilityAccountService.close(command, "OPE001"));
        verify(liabilityAccountRepository).findByLiabilityAccountNo("NONEXIST");
        verifyNoInteractions(liabilityAccountDomainService, customerSubAccountRepository);
    }
}
