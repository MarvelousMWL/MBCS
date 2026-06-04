package com.bank.customer.application.command.create;

import com.bank.common.domain.enums.BaseEnumType;
import com.bank.common.util.NoGenerator;
import com.bank.customer.domain.entity.Customer;
import com.bank.customer.domain.enums.CustomerStatus;
import com.bank.customer.domain.enums.IdType;
import com.bank.customer.domain.repository.CustomerRepository;
import com.bank.customer.domain.service.CustomerDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CreateCustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerDomainService customerDomainService;

    @Transactional
    public Customer create(CreateCustomerCommand command) {
        Customer customer = new Customer();
        customer.setCustomerNo(generateCustomerNo());
        customer.setCustomerName(command.getCustomerName());
        customer.setIdType(BaseEnumType.valueOfCode(IdType.class, command.getIdType()));
        customer.setIdNumber(command.getIdNumber());
        customer.setPhone(command.getPhone());
        customer.setAddress(command.getAddress());
        customer.setStatus(CustomerStatus.NORMAL);
        customer.setCreatedAt(LocalDateTime.now());
        customer.setUpdatedAt(LocalDateTime.now());

        customerDomainService.validateCreate(customer);
        customerRepository.save(customer);
        return customer;
    }

    private String generateCustomerNo() {
        Long count = customerRepository.findAll().stream().count();
        return NoGenerator.generateCustomerNo(count + 1);
    }
}