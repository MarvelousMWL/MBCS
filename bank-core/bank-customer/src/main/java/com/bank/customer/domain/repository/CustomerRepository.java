package com.bank.customer.domain.repository;

import com.bank.customer.domain.entity.Customer;
import java.util.List;
import java.util.Optional;

public interface CustomerRepository {
    void save(Customer customer);
    void update(Customer customer);
    Optional<Customer> findById(Long id);
    Optional<Customer> findByCustomerNo(String customerNo);
    List<Customer> findAll();
    boolean existsByCustomerNo(String customerNo);
    boolean existsByIdNumber(String idType, String idNumber);
}
