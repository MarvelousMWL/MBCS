package com.bank.liability.application.liabilityaccount.open;

import com.bank.common.exception.BusinessException;
import com.bank.common.util.NoGenerator;
import com.bank.liability.domain.customeraccount.repository.CustomerAccountRepository;
import com.bank.liability.domain.customersubaccount.entity.CustomerSubAccount;
import com.bank.liability.domain.customersubaccount.service.CustomerSubAccountDomainService;
import com.bank.liability.domain.enums.LiabilityAccountType;
import com.bank.liability.domain.liabilityaccount.entity.LiabilityAccount;
import com.bank.liability.domain.liabilityaccount.repository.LiabilityAccountRepository;
import com.bank.liability.domain.liabilityaccount.service.LiabilityAccountDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OpenLiabilityAccountService {

    private final LiabilityAccountRepository liabilityAccountRepository;
    private final CustomerAccountRepository customerAccountRepository;
    private final LiabilityAccountDomainService liabilityAccountDomainService;
    private final CustomerSubAccountDomainService customerSubAccountDomainService;

    @Transactional
    public LiabilityAccount open(OpenLiabilityAccountCommand command, String operatorNo) {
        customerAccountRepository.findByCustomerAccountNo(command.getCustomerAccountNo())
                .orElseThrow(() -> new BusinessException("客户账号不存在"));

        String accountType = command.getAccountType();
        String subAccountSeq = customerSubAccountDomainService.generateSubAccountSeq(command.getCustomerAccountNo(), accountType);
        String liabilityAccountNo = generateLiabilityAccountNo();

        LiabilityAccount liabilityAccount = new LiabilityAccount();
        liabilityAccount.setLiabilityAccountNo(liabilityAccountNo);
        liabilityAccount.setCustomerAccountNo(command.getCustomerAccountNo());
        liabilityAccount.setSubAccountSeq(subAccountSeq);
        liabilityAccount.setAccountType(accountType);
        liabilityAccount.setBalance(BigDecimal.ZERO);
        liabilityAccount.setStatus(com.bank.liability.domain.enums.LiabilityAccountStatus.NORMAL);
        liabilityAccount.setOpenDate(LocalDateTime.now());
        liabilityAccount.setCreatedAt(LocalDateTime.now());
        liabilityAccount.setUpdatedAt(LocalDateTime.now());
        liabilityAccount.setVersion(0);

        liabilityAccountDomainService.validateCreate(liabilityAccount);
        liabilityAccountRepository.save(liabilityAccount);

        CustomerSubAccount subAccount = customerSubAccountDomainService.createSubAccount(
                command.getCustomerAccountNo(), subAccountSeq, liabilityAccountNo, accountType);

        return liabilityAccount;
    }

    private String generateLiabilityAccountNo() {
        Long count = liabilityAccountRepository.countAll();
        return NoGenerator.generateLiabilityAccountNo(count + 1);
    }
}
