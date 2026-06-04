package com.bank.liability.application.customersubaccount.open;

import com.bank.liability.domain.customersubaccount.entity.CustomerSubAccount;
import com.bank.liability.domain.customersubaccount.service.CustomerSubAccountDomainService;
import com.bank.liability.domain.liabilityaccount.repository.LiabilityAccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OpenSubAccountServiceTest {

    @Mock private CustomerSubAccountDomainService customerSubAccountDomainService;
    @Mock private LiabilityAccountRepository liabilityAccountRepository;

    @InjectMocks
    private OpenSubAccountService openSubAccountService;

    @Test
    void open_shouldSucceed_whenGeneratingUniqueAccountNo() {
        OpenSubAccountCommand command = new OpenSubAccountCommand();
        command.setCustomerAccountNo("CACC001");
        command.setAccountType(0);

        when(liabilityAccountRepository.countAll()).thenReturn(5L);
        when(customerSubAccountDomainService.generateSubAccountSeq("CACC001", "0")).thenReturn("001CNY");

        CustomerSubAccount expected = new CustomerSubAccount();
        expected.setCustomerAccountNo("CACC001");
        expected.setSubAccountSeq("001CNY");
        when(customerSubAccountDomainService.createSubAccount("CACC001", "001CNY", "10000006", "0"))
                .thenReturn(expected);

        CustomerSubAccount result = openSubAccountService.open(command, "OPE001");

        assertNotNull(result);
        assertEquals("CACC001", result.getCustomerAccountNo());

        verify(liabilityAccountRepository).countAll();
        verify(customerSubAccountDomainService).generateSubAccountSeq("CACC001", "0");
        verify(customerSubAccountDomainService).createSubAccount("CACC001", "001CNY", "10000006", "0");
    }
}
