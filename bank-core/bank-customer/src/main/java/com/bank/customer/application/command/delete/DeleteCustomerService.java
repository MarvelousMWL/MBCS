package com.bank.customer.application.command.delete;

import com.bank.customer.domain.entity.Customer;
import com.bank.customer.domain.enums.CustomerStatus;
import com.bank.customer.domain.repository.CustomerRepository;
import com.bank.customer.domain.service.CustomerDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DeleteCustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerDomainService customerDomainService;

    @Transactional
    public void delete(Long id) {
        Customer exist = customerRepository.findById(id)
                .orElseThrow(() -> new com.bank.common.exception.BusinessException("客户不存在"));

        customerDomainService.validateDelete(id);

        exist.setStatus(CustomerStatus.CLOSED);
        exist.setUpdatedAt(LocalDateTime.now());
        customerRepository.update(exist);
    }
}
