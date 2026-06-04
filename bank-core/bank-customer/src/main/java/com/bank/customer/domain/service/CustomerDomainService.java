package com.bank.customer.domain.service;

import com.bank.common.exception.BusinessException;
import com.bank.common.util.Assert;
import com.bank.customer.domain.entity.Customer;
import com.bank.customer.domain.enums.CustomerStatus;
import com.bank.customer.domain.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerDomainService {

    private final CustomerRepository customerRepository;

    public void validateCreate(Customer customer) {
        Assert.notBlank(customer.getCustomerName(), "客户姓名不能为空");
        Assert.notNull(customer.getIdType(), "证件类型不能为空");
        Assert.notBlank(customer.getIdNumber(), "证件号码不能为空");
        if (customerRepository.existsByIdNumber(customer.getIdType().getCode().toString(), customer.getIdNumber())) {
            throw new BusinessException("该证件号码已存在");
        }
    }

    public void validateUpdate(Customer customer) {
        Assert.notNull(customer.getId(), "客户ID不能为空");
        Customer exist = customerRepository.findById(customer.getId())
                .orElseThrow(() -> new BusinessException("客户不存在"));
        if (!exist.isNormal()) {
            throw new BusinessException("只有正常状态的客户可以修改");
        }
    }

    public void validateDelete(Long id) {
        Customer exist = customerRepository.findById(id)
                .orElseThrow(() -> new BusinessException("客户不存在"));
        if (!exist.isNormal()) {
            throw new BusinessException("只有正常状态的客户可以删除");
        }
    }

    public void changeStatus(Customer customer, CustomerStatus status) {
        customer.setStatus(status);
    }
}