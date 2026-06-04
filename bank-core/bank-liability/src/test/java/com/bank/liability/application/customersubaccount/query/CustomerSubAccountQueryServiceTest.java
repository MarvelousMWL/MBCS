package com.bank.liability.application.customersubaccount.query;

import com.bank.liability.domain.customersubaccount.entity.CustomerSubAccount;
import com.bank.liability.domain.customersubaccount.repository.CustomerSubAccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerSubAccountQueryServiceTest {

    @Mock private CustomerSubAccountRepository customerSubAccountRepository;

    @InjectMocks
    private CustomerSubAccountQueryService customerSubAccountQueryService;

    @Test
    void findByCustomerAccountNo_shouldReturnSubAccounts() {
        when(customerSubAccountRepository.findByCustomerAccountNo("CACC001"))
                .thenReturn(List.of(new CustomerSubAccount()));
        assertEquals(1, customerSubAccountQueryService.findByCustomerAccountNo("CACC001").size());
    }

    @Test
    void findByCustomerAccountNoAndAccountType_shouldReturnFilteredResults() {
        when(customerSubAccountRepository.findByCustomerAccountNoAndAccountType("CACC001", "DEMAND"))
                .thenReturn(List.of(new CustomerSubAccount(), new CustomerSubAccount()));
        assertEquals(2, customerSubAccountQueryService.findByCustomerAccountNoAndAccountType("CACC001", "DEMAND").size());
    }
}
