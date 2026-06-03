package com.bank.liability.domain.customeraccount.service;

import com.bank.common.exception.BusinessException;
import com.bank.common.util.Assert;
import com.bank.liability.domain.customeraccount.entity.CustomerAccount;
import com.bank.liability.domain.customeraccount.repository.CustomerAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerAccountDomainService {

    private final CustomerAccountRepository customerAccountRepository;

    public void validateCreate(CustomerAccount customerAccount) {
        Assert.notBlank(customerAccount.getCustomerNo(), "客户号不能为空");
        Assert.notBlank(customerAccount.getAccountType(), "账号类型不能为空");
    }

    public void validateClose(CustomerAccount customerAccount) {
        if (!customerAccount.canClose()) {
            throw new BusinessException("客户账号状态不允许销户");
        }
    }

    public void close(CustomerAccount customerAccount) {
        customerAccount.setStatus(com.bank.liability.domain.enums.CustomerAccountStatus.CLOSED);
    }
}
