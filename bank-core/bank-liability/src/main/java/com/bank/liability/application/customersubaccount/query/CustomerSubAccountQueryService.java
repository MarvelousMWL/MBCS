package com.bank.liability.application.customersubaccount.query;

import com.bank.liability.domain.customersubaccount.entity.CustomerSubAccount;
import com.bank.liability.domain.customersubaccount.repository.CustomerSubAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerSubAccountQueryService {

    private final CustomerSubAccountRepository customerSubAccountRepository;

    public List<CustomerSubAccount> findByCustomerAccountNo(String customerAccountNo) {
        return customerSubAccountRepository.findByCustomerAccountNo(customerAccountNo);
    }

    public List<CustomerSubAccount> findByCustomerAccountNoAndAccountType(String customerAccountNo, String accountType) {
        return customerSubAccountRepository.findByCustomerAccountNoAndAccountType(customerAccountNo, accountType);
    }
}
