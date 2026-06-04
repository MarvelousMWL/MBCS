package com.bank.liability.application.customeraccount.close;

import com.bank.common.exception.BusinessException;
import com.bank.liability.domain.customeraccount.entity.CustomerAccount;
import com.bank.liability.domain.customeraccount.repository.CustomerAccountRepository;
import com.bank.liability.domain.customeraccount.service.CustomerAccountDomainService;
import com.bank.liability.domain.enums.LiabilityAccountStatus;
import com.bank.liability.domain.liabilityaccount.entity.LiabilityAccount;
import com.bank.liability.domain.liabilityaccount.repository.LiabilityAccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CloseCustomerAccountServiceTest {

    @Mock private CustomerAccountRepository customerAccountRepository;
    @Mock private LiabilityAccountRepository liabilityAccountRepository;
    @Mock private CustomerAccountDomainService customerAccountDomainService;

    @InjectMocks
    private CloseCustomerAccountService closeCustomerAccountService;

    @Test
    void close_shouldSucceed_whenNoActiveLiabilityAccount() {
        CustomerAccount customerAccount = new CustomerAccount();
        customerAccount.setCustomerAccountNo("CACC001");
        customerAccount.setStatus(com.bank.liability.domain.enums.CustomerAccountStatus.NORMAL);

        CloseCustomerAccountCommand command = new CloseCustomerAccountCommand();
        command.setCustomerAccountNo("CACC001");

        when(customerAccountRepository.findByCustomerAccountNo("CACC001")).thenReturn(Optional.of(customerAccount));
        when(liabilityAccountRepository.findByCustomerAccountNo("CACC001")).thenReturn(Collections.emptyList());
        doNothing().when(customerAccountDomainService).validateClose(customerAccount);
        doNothing().when(customerAccountRepository).update(customerAccount);

        closeCustomerAccountService.close(command, "OPE001");

        assertEquals(com.bank.liability.domain.enums.CustomerAccountStatus.CLOSED, customerAccount.getStatus());
        assertNotNull(customerAccount.getCloseDate());
        verify(customerAccountRepository).findByCustomerAccountNo("CACC001");
        verify(liabilityAccountRepository).findByCustomerAccountNo("CACC001");
        verify(customerAccountDomainService).validateClose(customerAccount);
        verify(customerAccountRepository).update(customerAccount);
    }

    @Test
    void close_shouldThrowException_whenHasActiveLiabilityAccount() {
        CustomerAccount customerAccount = new CustomerAccount();
        customerAccount.setCustomerAccountNo("CACC001");

        CloseCustomerAccountCommand command = new CloseCustomerAccountCommand();
        command.setCustomerAccountNo("CACC001");

        LiabilityAccount activeAccount = new LiabilityAccount();
        activeAccount.setLiabilityAccountNo("LA001");
        activeAccount.setStatus(LiabilityAccountStatus.NORMAL);

        when(customerAccountRepository.findByCustomerAccountNo("CACC001")).thenReturn(Optional.of(customerAccount));
        when(liabilityAccountRepository.findByCustomerAccountNo("CACC001")).thenReturn(List.of(activeAccount));

        assertThrows(BusinessException.class, () -> closeCustomerAccountService.close(command, "OPE001"));
        verify(customerAccountRepository).findByCustomerAccountNo("CACC001");
        verify(liabilityAccountRepository).findByCustomerAccountNo("CACC001");
        verifyNoInteractions(customerAccountDomainService);
    }

    @Test
    void close_shouldThrowException_whenCustomerAccountNotExist() {
        CloseCustomerAccountCommand command = new CloseCustomerAccountCommand();
        command.setCustomerAccountNo("NONEXIST");

        when(customerAccountRepository.findByCustomerAccountNo("NONEXIST")).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> closeCustomerAccountService.close(command, "OPE001"));
    }
}
