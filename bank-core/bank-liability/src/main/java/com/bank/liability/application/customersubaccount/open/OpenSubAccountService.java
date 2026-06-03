package com.bank.liability.application.customersubaccount.open;

import com.bank.common.util.NoGenerator;
import com.bank.liability.domain.customersubaccount.entity.CustomerSubAccount;
import com.bank.liability.domain.customersubaccount.service.CustomerSubAccountDomainService;
import com.bank.liability.domain.liabilityaccount.repository.LiabilityAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OpenSubAccountService {

    private final CustomerSubAccountDomainService customerSubAccountDomainService;
    private final LiabilityAccountRepository liabilityAccountRepository;

    @Transactional
    public CustomerSubAccount open(OpenSubAccountCommand command, String operatorNo) {
        String subAccountSeq = customerSubAccountDomainService.generateSubAccountSeq(
                command.getCustomerAccountNo(), command.getAccountType());

        String liabilityAccountNo = generateLiabilityAccountNo();

        return customerSubAccountDomainService.createSubAccount(
                command.getCustomerAccountNo(), subAccountSeq, liabilityAccountNo, command.getAccountType());
    }

    private String generateLiabilityAccountNo() {
        Long count = liabilityAccountRepository.countAll();
        return NoGenerator.generateLiabilityAccountNo(count + 1);
    }
}
