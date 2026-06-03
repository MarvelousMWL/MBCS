package com.bank.liability.application.customeraccount.close;

import com.bank.common.exception.BusinessException;
import com.bank.liability.domain.customeraccount.entity.CustomerAccount;
import com.bank.liability.domain.customeraccount.repository.CustomerAccountRepository;
import com.bank.liability.domain.customeraccount.service.CustomerAccountDomainService;
import com.bank.liability.domain.liabilityaccount.repository.LiabilityAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CloseCustomerAccountService {

    private final CustomerAccountRepository customerAccountRepository;
    private final LiabilityAccountRepository liabilityAccountRepository;
    private final CustomerAccountDomainService customerAccountDomainService;

    @Transactional
    public void close(CloseCustomerAccountCommand command, String operatorNo) {
        CustomerAccount customerAccount = customerAccountRepository
                .findByCustomerAccountNo(command.getCustomerAccountNo())
                .orElseThrow(() -> new BusinessException("客户账号不存在"));

        if (liabilityAccountRepository.findByCustomerAccountNo(customerAccount.getCustomerAccountNo()).stream()
                .anyMatch(a -> a.getStatus() == com.bank.liability.domain.enums.LiabilityAccountStatus.NORMAL)) {
            throw new BusinessException("该客户账号下还有未销户的负债账号，不允许销户");
        }

        customerAccountDomainService.validateClose(customerAccount);
        customerAccount.setStatus(com.bank.liability.domain.enums.CustomerAccountStatus.CLOSED);
        customerAccount.setCloseDate(LocalDateTime.now());
        customerAccount.setUpdatedAt(LocalDateTime.now());

        customerAccountRepository.update(customerAccount);
    }
}
