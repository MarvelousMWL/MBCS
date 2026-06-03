package com.bank.customer.application.query;

import com.bank.customer.domain.entity.Customer;
import com.bank.customer.domain.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerQueryService {

    private final CustomerRepository customerRepository;

    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    public Optional<Customer> findByCustomerNo(String customerNo) {
        return customerRepository.findByCustomerNo(customerNo);
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }
}
