package com.bank.customer.domain.service;

import com.bank.common.exception.BusinessException;
import com.bank.customer.domain.entity.Customer;
import com.bank.customer.domain.enums.CustomerStatus;
import com.bank.customer.domain.enums.IdType;
import com.bank.customer.domain.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerDomainServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerDomainService customerDomainService;

    @Test
    void validateCreate_shouldSucceed_whenAllFieldsValid() {
        Customer customer = createNormalCustomer();
        String idTypeCode = String.valueOf(customer.getIdType().getCode());
        when(customerRepository.existsByIdNumber(idTypeCode, customer.getIdNumber()))
                .thenReturn(false);
        assertDoesNotThrow(() -> customerDomainService.validateCreate(customer));
        verify(customerRepository).existsByIdNumber(idTypeCode, customer.getIdNumber());
    }

    @Test
    void validateCreate_shouldThrowException_whenIdNumberAlreadyExists() {
        Customer customer = createNormalCustomer();
        String idTypeCode = String.valueOf(customer.getIdType().getCode());
        when(customerRepository.existsByIdNumber(idTypeCode, customer.getIdNumber()))
                .thenReturn(true);
        BusinessException exception = assertThrows(BusinessException.class,
                () -> customerDomainService.validateCreate(customer));
        assertNotNull(exception.getMessage());
        verify(customerRepository).existsByIdNumber(idTypeCode, customer.getIdNumber());
    }

    @Test
    void validateUpdate_shouldSucceed_whenCustomerExistsAndIsNormal() {
        Customer customer = createNormalCustomer();
        customer.setId(1L);
        Customer existingCustomer = createNormalCustomer();
        existingCustomer.setId(1L);
        existingCustomer.setStatus(CustomerStatus.NORMAL);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(existingCustomer));
        assertDoesNotThrow(() -> customerDomainService.validateUpdate(customer));
        verify(customerRepository).findById(1L);
    }

    @Test
    void validateUpdate_shouldThrowException_whenCustomerNotExist() {
        Customer customer = createNormalCustomer();
        customer.setId(999L);
        when(customerRepository.findById(999L)).thenReturn(Optional.empty());
        BusinessException exception = assertThrows(BusinessException.class,
                () -> customerDomainService.validateUpdate(customer));
        assertNotNull(exception.getMessage());
        verify(customerRepository).findById(999L);
    }

    @Test
    void validateUpdate_shouldThrowException_whenCustomerStatusIsNotNormal() {
        Customer customer = createNormalCustomer();
        customer.setId(1L);
        Customer existingCustomer = createNormalCustomer();
        existingCustomer.setId(1L);
        existingCustomer.setStatus(CustomerStatus.STOPPED);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(existingCustomer));
        BusinessException exception = assertThrows(BusinessException.class,
                () -> customerDomainService.validateUpdate(customer));
        assertNotNull(exception.getMessage());
        verify(customerRepository).findById(1L);
    }

    @Test
    void validateDelete_shouldSucceed_whenCustomerExistsAndIsNormal() {
        Customer existingCustomer = createNormalCustomer();
        existingCustomer.setId(1L);
        existingCustomer.setStatus(CustomerStatus.NORMAL);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(existingCustomer));
        assertDoesNotThrow(() -> customerDomainService.validateDelete(1L));
        verify(customerRepository).findById(1L);
    }

    @Test
    void validateDelete_shouldThrowException_whenCustomerStatusIsNotNormal() {
        Customer existingCustomer = createNormalCustomer();
        existingCustomer.setId(1L);
        existingCustomer.setStatus(CustomerStatus.CLOSED);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(existingCustomer));
        BusinessException exception = assertThrows(BusinessException.class,
                () -> customerDomainService.validateDelete(1L));
        assertNotNull(exception.getMessage());
        verify(customerRepository).findById(1L);
    }

    private Customer createNormalCustomer() {
        Customer customer = new Customer();
        customer.setCustomerName("张三");
        customer.setIdType(IdType.ID_CARD);
        customer.setIdNumber("110101199001011234");
        customer.setPhone("13800138000");
        customer.setAddress("北京市朝阳区");
        customer.setStatus(CustomerStatus.NORMAL);
        return customer;
    }
}
