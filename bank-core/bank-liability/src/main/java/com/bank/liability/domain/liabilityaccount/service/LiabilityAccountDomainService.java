package com.bank.liability.domain.liabilityaccount.service;

import com.bank.common.exception.BusinessException;
import com.bank.common.util.Assert;
import com.bank.liability.domain.liabilityaccount.entity.LiabilityAccount;
import com.bank.liability.domain.liabilityaccount.repository.LiabilityAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class LiabilityAccountDomainService {

    private final LiabilityAccountRepository liabilityAccountRepository;

    public void validateCreate(LiabilityAccount liabilityAccount) {
        Assert.notBlank(liabilityAccount.getCustomerAccountNo(), "客户账号不能为空");
        Assert.notNull(liabilityAccount.getAccountType(), "负债类型不能为空");
    }

    public void validateDeposit(BigDecimal amount) {
        Assert.isTrue(amount.compareTo(BigDecimal.ZERO) > 0, "存款金额必须大于0");
    }

    public void validateWithdraw(LiabilityAccount account, BigDecimal amount) {
        Assert.isTrue(amount.compareTo(BigDecimal.ZERO) > 0, "取款金额必须大于0");
        if (account.getBalance().compareTo(amount) < 0) {
            throw new BusinessException("余额不足");
        }
    }

    public void validateClose(LiabilityAccount account) {
        if (!account.canClose()) {
            throw new BusinessException("负债账号余额不为0或状态不允许销户");
        }
    }

    public void deposit(LiabilityAccount account, BigDecimal amount) {
        account.deposit(amount);
    }

    public void withdraw(LiabilityAccount account, BigDecimal amount) {
        account.withdraw(amount);
    }
}
