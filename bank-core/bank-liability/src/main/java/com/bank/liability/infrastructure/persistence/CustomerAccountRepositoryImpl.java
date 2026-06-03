package com.bank.liability.infrastructure.persistence;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bank.liability.domain.customeraccount.entity.CustomerAccount;
import com.bank.liability.domain.customeraccount.repository.CustomerAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CustomerAccountRepositoryImpl implements CustomerAccountRepository {

    private final CustomerAccountMapper customerAccountMapper;

    @Override
    public void save(CustomerAccount customerAccount) {
        customerAccountMapper.insert(customerAccount);
    }

    @Override
    public void update(CustomerAccount customerAccount) {
        customerAccountMapper.updateById(customerAccount);
    }

    @Override
    public Optional<CustomerAccount> findById(Long id) {
        return Optional.ofNullable(customerAccountMapper.selectById(id));
    }

    @Override
    public Optional<CustomerAccount> findByCustomerAccountNo(String customerAccountNo) {
        LambdaQueryWrapper<CustomerAccount> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CustomerAccount::getCustomerAccountNo, customerAccountNo);
        return Optional.ofNullable(customerAccountMapper.selectOne(wrapper));
    }

    @Override
    public List<CustomerAccount> findByCustomerNo(String customerNo) {
        LambdaQueryWrapper<CustomerAccount> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CustomerAccount::getCustomerNo, customerNo);
        return customerAccountMapper.selectList(wrapper);
    }

    @Override
    public List<CustomerAccount> findAll() {
        return customerAccountMapper.selectList(null);
    }

    @Override
    public boolean existsByCustomerAccountNo(String customerAccountNo) {
        LambdaQueryWrapper<CustomerAccount> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CustomerAccount::getCustomerAccountNo, customerAccountNo);
        return customerAccountMapper.exists(wrapper);
    }
}
