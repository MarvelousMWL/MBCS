package com.bank.liability.domain.customersubaccount.service;

import com.bank.common.domain.enums.BaseEnumType;
import com.bank.common.exception.BusinessException;
import com.bank.common.util.Assert;
import com.bank.liability.domain.customersubaccount.entity.CustomerSubAccount;
import com.bank.liability.domain.customersubaccount.repository.CustomerSubAccountRepository;
import com.bank.liability.domain.enums.LiabilityAccountType;
import com.bank.liability.domain.enums.SubAccountStatus;
import com.bank.liability.domain.liabilityaccount.entity.LiabilityAccount;
import com.bank.liability.domain.liabilityaccount.repository.LiabilityAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerSubAccountDomainService {

    private final CustomerSubAccountRepository customerSubAccountRepository;
    private final LiabilityAccountRepository liabilityAccountRepository;

    private static final String DEMAND_SUB_SEQ_PREFIX = "001CNY";
    private static final String TIME_SUB_SEQ_FORMAT = "%06d";

    public String generateSubAccountSeq(String customerAccountNo, String accountType) {
        if (LiabilityAccountType.DEMAND.getCode().toString().equals(accountType) || LiabilityAccountType.DEMAND.name().equals(accountType)) {
            return DEMAND_SUB_SEQ_PREFIX;
        } else if (LiabilityAccountType.TERM.getCode().toString().equals(accountType) || LiabilityAccountType.TERM.name().equals(accountType)) {
            Optional<LiabilityAccount> maxAccount = liabilityAccountRepository.findMaxSubAccountSeqByCustomerAccountNoAndAccountType(customerAccountNo, accountType);
            int nextSeq = 1;
            if (maxAccount.isPresent() && maxAccount.get().getSubAccountSeq() != null) {
                try {
                    nextSeq = Integer.parseInt(maxAccount.get().getSubAccountSeq()) + 1;
                } catch (NumberFormatException e) {
                    nextSeq = 1;
                }
            }
            return String.format(TIME_SUB_SEQ_FORMAT, nextSeq);
        } else {
            throw new BusinessException("不支持的账户类型: " + accountType);
        }
    }

    public void validateCreate(String customerAccountNo, String subAccountSeq, String accountType) {
        Assert.notBlank(customerAccountNo, "客户账号不能为空");
        Assert.notBlank(subAccountSeq, "子账户序号不能为空");
        Assert.notBlank(accountType, "账户类型不能为空");
        if (customerSubAccountRepository.existsByCustomerAccountNoAndSubAccountSeq(customerAccountNo, subAccountSeq)) {
            throw new BusinessException("该客户账号下已存在子账户序号: " + subAccountSeq);
        }
    }

    public CustomerSubAccount createSubAccount(String customerAccountNo, String subAccountSeq, String liabilityAccountNo, String accountType) {
        validateCreate(customerAccountNo, subAccountSeq, accountType);
        CustomerSubAccount subAccount = new CustomerSubAccount();
        subAccount.setCustomerAccountNo(customerAccountNo);
        subAccount.setSubAccountSeq(subAccountSeq);
        subAccount.setLiabilityAccountNo(liabilityAccountNo);
        subAccount.setAccountType(BaseEnumType.valueOfCode(LiabilityAccountType.class, Integer.valueOf(accountType)));
        subAccount.setStatus(SubAccountStatus.NORMAL);
        customerSubAccountRepository.save(subAccount);
        return subAccount;
    }
}

