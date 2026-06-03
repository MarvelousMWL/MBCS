package com.bank.customer.infrastructure.persistence;

import com.bank.customer.domain.entity.Customer;
import com.bank.customer.domain.repository.CustomerRepository;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CustomerRepositoryImpl implements CustomerRepository {

    private final CustomerMapper customerMapper;

    @Override
    public void save(Customer customer) {
        customerMapper.insert(customer);
    }

    @Override
    public void update(Customer customer) {
        customerMapper.updateById(customer);
    }

    @Override
    public Optional<Customer> findById(Long id) {
        return Optional.ofNullable(customerMapper.selectById(id));
    }

    @Override
    public Optional<Customer> findByCustomerNo(String customerNo) {
        LambdaQueryWrapper<Customer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Customer::getCustomerNo, customerNo);
        return Optional.ofNullable(customerMapper.selectOne(wrapper));
    }

    @Override
    public List<Customer> findAll() {
        return customerMapper.selectList(null);
    }

    @Override
    public boolean existsByCustomerNo(String customerNo) {
        LambdaQueryWrapper<Customer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Customer::getCustomerNo, customerNo);
        return customerMapper.exists(wrapper);
    }

    @Override
    public boolean existsByIdNumber(String idType, String idNumber) {
        LambdaQueryWrapper<Customer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Customer::getIdType, idType)
               .eq(Customer::getIdNumber, idNumber);
        return customerMapper.exists(wrapper);
    }
}
