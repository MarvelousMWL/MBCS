package com.bank.liability.infrastructure.persistence;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bank.liability.domain.customersubaccount.entity.CustomerSubAccount;
import com.bank.liability.domain.customersubaccount.repository.CustomerSubAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CustomerSubAccountRepositoryImpl implements CustomerSubAccountRepository {

    private final CustomerSubAccountMapper customerSubAccountMapper;

    @Override
    public void save(CustomerSubAccount customerSubAccount) {
        customerSubAccountMapper.insert(customerSubAccount);
    }

    @Override
    public void update(CustomerSubAccount customerSubAccount) {
        LambdaUpdateWrapper<CustomerSubAccount> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(CustomerSubAccount::getLiabilityAccountNo, customerSubAccount.getLiabilityAccountNo())
               .set(CustomerSubAccount::getStatus, customerSubAccount.getStatus())
               .set(CustomerSubAccount::getUpdatedAt, customerSubAccount.getUpdatedAt());
        customerSubAccountMapper.update(null, wrapper);
    }

    @Override
    public Optional<CustomerSubAccount> findById(Long id) {
        return Optional.ofNullable(customerSubAccountMapper.selectById(id));
    }

    @Override
    public Optional<CustomerSubAccount> findByLiabilityAccountNo(String liabilityAccountNo) {
        LambdaQueryWrapper<CustomerSubAccount> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CustomerSubAccount::getLiabilityAccountNo, liabilityAccountNo);
        return Optional.ofNullable(customerSubAccountMapper.selectOne(wrapper));
    }

    @Override
    public Optional<CustomerSubAccount> findByCustomerAccountNoAndSubAccountSeq(String customerAccountNo, String subAccountSeq) {
        LambdaQueryWrapper<CustomerSubAccount> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CustomerSubAccount::getCustomerAccountNo, customerAccountNo)
               .eq(CustomerSubAccount::getSubAccountSeq, subAccountSeq);
        return Optional.ofNullable(customerSubAccountMapper.selectOne(wrapper));
    }

    @Override
    public List<CustomerSubAccount> findByCustomerAccountNo(String customerAccountNo) {
        LambdaQueryWrapper<CustomerSubAccount> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CustomerSubAccount::getCustomerAccountNo, customerAccountNo);
        return customerSubAccountMapper.selectList(wrapper);
    }

    @Override
    public List<CustomerSubAccount> findByCustomerAccountNoAndAccountType(String customerAccountNo, String accountType) {
        LambdaQueryWrapper<CustomerSubAccount> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CustomerSubAccount::getCustomerAccountNo, customerAccountNo)
               .eq(CustomerSubAccount::getAccountType, accountType);
        return customerSubAccountMapper.selectList(wrapper);
    }

    @Override
    public boolean existsByCustomerAccountNoAndSubAccountSeq(String customerAccountNo, String subAccountSeq) {
        LambdaQueryWrapper<CustomerSubAccount> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CustomerSubAccount::getCustomerAccountNo, customerAccountNo)
               .eq(CustomerSubAccount::getSubAccountSeq, subAccountSeq);
        return customerSubAccountMapper.exists(wrapper);
    }

    @Override
    public Long countByCustomerAccountNoAndAccountType(String customerAccountNo, String accountType) {
        LambdaQueryWrapper<CustomerSubAccount> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CustomerSubAccount::getCustomerAccountNo, customerAccountNo)
               .eq(CustomerSubAccount::getAccountType, accountType);
        return customerSubAccountMapper.selectCount(wrapper);
    }
}
