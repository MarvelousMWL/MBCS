package com.bank.liability.application.customeraccount.open;

import com.bank.liability.domain.customeraccount.entity.CustomerAccount;
import com.bank.liability.domain.customeraccount.repository.CustomerAccountRepository;
import com.bank.liability.domain.customeraccount.service.CustomerAccountDomainService;
import com.bank.liability.domain.enums.CustomerAccountStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OpenCustomerAccountServiceTest {

    @Mock private CustomerAccountRepository customerAccountRepository;
    @Mock private CustomerAccountDomainService customerAccountDomainService;

    @InjectMocks
    private OpenCustomerAccountService openCustomerAccountService;

    @Test
    void open_shouldSucceed_whenCommandIsValid() {
        OpenCustomerAccountCommand command = new OpenCustomerAccountCommand();
        command.setCustomerNo("CUS001");
        command.setAccountType(0);

        when(customerAccountRepository.findAll()).thenReturn(new ArrayList<>());
        doNothing().when(customerAccountDomainService).validateCreate(any());
        doNothing().when(customerAccountRepository).save(any());

        CustomerAccount result = openCustomerAccountService.open(command, "OPE001");

        assertNotNull(result);
        assertEquals("CUS001", result.getCustomerNo());
        assertEquals(0, result.getAccountType().getCode());
        assertEquals(CustomerAccountStatus.NORMAL, result.getStatus());

        ArgumentCaptor<CustomerAccount> captor = ArgumentCaptor.forClass(CustomerAccount.class);
        verify(customerAccountRepository).save(captor.capture());
        CustomerAccount saved = captor.getValue();
        assertEquals("CUS001", saved.getCustomerNo());
        assertEquals(0, saved.getAccountType().getCode());
        assertEquals(CustomerAccountStatus.NORMAL, saved.getStatus());
        assertNotNull(saved.getOpenDate());
        assertNotNull(saved.getCreatedAt());

        verify(customerAccountDomainService).validateCreate(any());
    }
}
