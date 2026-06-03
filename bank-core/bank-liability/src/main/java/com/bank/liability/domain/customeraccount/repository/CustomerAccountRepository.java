package com.bank.liability.domain.customeraccount.repository;

import com.bank.liability.domain.customeraccount.entity.CustomerAccount;
import java.util.List;
import java.util.Optional;

public interface CustomerAccountRepository {
    void save(CustomerAccount customerAccount);
    void update(CustomerAccount customerAccount);
    Optional<CustomerAccount> findById(Long id);
    Optional<CustomerAccount> findByCustomerAccountNo(String customerAccountNo);
    List<CustomerAccount> findByCustomerNo(String customerNo);
    List<CustomerAccount> findAll();
    boolean existsByCustomerAccountNo(String customerAccountNo);
}
