package com.bank.customer.application.command.update;

import com.bank.customer.domain.entity.Customer;
import com.bank.customer.domain.repository.CustomerRepository;
import com.bank.customer.domain.service.CustomerDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UpdateCustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerDomainService customerDomainService;

    @Transactional
    public Customer update(UpdateCustomerCommand command) {
        Customer exist = customerRepository.findById(command.getId())
                .orElseThrow(() -> new com.bank.common.exception.BusinessException("客户不存在"));

        customerDomainService.validateUpdate(exist);

        exist.setCustomerName(command.getCustomerName());
        exist.setPhone(command.getPhone());
        exist.setAddress(command.getAddress());
        exist.setUpdatedAt(LocalDateTime.now());

        customerRepository.update(exist);
        return exist;
    }
}
