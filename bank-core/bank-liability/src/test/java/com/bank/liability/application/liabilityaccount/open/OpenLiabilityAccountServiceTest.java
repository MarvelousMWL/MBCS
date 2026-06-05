package com.bank.liability.application.liabilityaccount.open;

import com.bank.common.exception.BusinessException;
import com.bank.common.util.NoGenerator;
import com.bank.liability.domain.customeraccount.repository.CustomerAccountRepository;
import com.bank.liability.domain.customersubaccount.entity.CustomerSubAccount;
import com.bank.liability.domain.customersubaccount.service.CustomerSubAccountDomainService;
import com.bank.liability.domain.enums.LiabilityAccountStatus;
import com.bank.liability.domain.enums.LiabilityAccountType;
import com.bank.liability.domain.liabilityaccount.entity.LiabilityAccount;
import com.bank.liability.domain.liabilityaccount.repository.LiabilityAccountRepository;
import com.bank.liability.domain.liabilityaccount.service.LiabilityAccountDomainService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OpenLiabilityAccountServiceTest {

    @Mock private LiabilityAccountRepository liabilityAccountRepository;
    @Mock private CustomerAccountRepository customerAccountRepository;
    @Mock private LiabilityAccountDomainService liabilityAccountDomainService;
    @Mock private CustomerSubAccountDomainService customerSubAccountDomainService;

    @InjectMocks
    private OpenLiabilityAccountService openLiabilityAccountService;

    @Test
    void open_shouldSucceed_whenCustomerAccountExists() {
        OpenLiabilityAccountCommand command = new OpenLiabilityAccountCommand();
        command.setCustomerAccountNo("CACC001");
        command.setAccountType(0);

        when(customerAccountRepository.findByCustomerAccountNo("CACC001"))
                .thenReturn(Optional.of(new com.bank.liability.domain.customeraccount.entity.CustomerAccount()));
        when(liabilityAccountRepository.countAll()).thenReturn(0L);
        when(customerSubAccountDomainService.generateSubAccountSeq("CACC001", "0")).thenReturn("001CNY");
        doNothing().when(liabilityAccountDomainService).validateCreate(any());
        doNothing().when(liabilityAccountRepository).save(any());
        when(customerSubAccountDomainService.createSubAccount("CACC001", "001CNY", "10000001", "0"))
                .thenReturn(new CustomerSubAccount());

        LiabilityAccount result = openLiabilityAccountService.open(command, "OPE001");

        assertNotNull(result);
        assertEquals("CACC001", result.getCustomerAccountNo());
        assertEquals(LiabilityAccountType.DEMAND, result.getAccountType());
        assertEquals("001CNY", result.getSubAccountSeq());
        assertEquals(BigDecimal.ZERO, result.getBalance());
        assertEquals(LiabilityAccountStatus.NORMAL, result.getStatus());

        verify(liabilityAccountRepository).save(any());
        verify(liabilityAccountDomainService).validateCreate(any());
        verify(customerSubAccountDomainService).createSubAccount("CACC001", "001CNY", "10000001", "0");
    }

    @Test
    void open_shouldThrowException_whenCustomerAccountNotExist() {
        OpenLiabilityAccountCommand command = new OpenLiabilityAccountCommand();
        command.setCustomerAccountNo("NONEXIST");

        when(customerAccountRepository.findByCustomerAccountNo("NONEXIST"))
                .thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> openLiabilityAccountService.open(command, "OPE001"));
        verify(customerAccountRepository).findByCustomerAccountNo("NONEXIST");
        verifyNoInteractions(liabilityAccountRepository, liabilityAccountDomainService, customerSubAccountDomainService);
    }
}
