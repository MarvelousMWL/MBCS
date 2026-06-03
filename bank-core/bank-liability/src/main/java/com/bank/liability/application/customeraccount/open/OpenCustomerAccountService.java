package com.bank.liability.application.customeraccount.open;

import com.bank.common.util.NoGenerator;
import com.bank.liability.domain.customeraccount.entity.CustomerAccount;
import com.bank.liability.domain.customeraccount.repository.CustomerAccountRepository;
import com.bank.liability.domain.customeraccount.service.CustomerAccountDomainService;
import com.bank.liability.domain.enums.CustomerAccountStatus;
import com.bank.liability.domain.enums.TransactionType;
import com.bank.liability.domain.transaction.service.TransactionDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OpenCustomerAccountService {

    private final CustomerAccountRepository customerAccountRepository;
    private final CustomerAccountDomainService customerAccountDomainService;
    private final TransactionDomainService transactionDomainService;

    @Transactional
    public CustomerAccount open(OpenCustomerAccountCommand command, String operatorNo) {
        CustomerAccount customerAccount = new CustomerAccount();
        customerAccount.setCustomerAccountNo(generateCustomerAccountNo());
        customerAccount.setCustomerNo(command.getCustomerNo());
        customerAccount.setAccountType(command.getAccountType());
        customerAccount.setStatus(CustomerAccountStatus.NORMAL);
        customerAccount.setOpenDate(LocalDateTime.now());
        customerAccount.setCreatedAt(LocalDateTime.now());
        customerAccount.setUpdatedAt(LocalDateTime.now());

        customerAccountDomainService.validateCreate(customerAccount);
        customerAccountRepository.save(customerAccount);

        return customerAccount;
    }

    private String generateCustomerAccountNo() {
        Long count = customerAccountRepository.findAll().stream().count();
        return NoGenerator.generateCustomerAccountNo(count + 1);
    }
}
