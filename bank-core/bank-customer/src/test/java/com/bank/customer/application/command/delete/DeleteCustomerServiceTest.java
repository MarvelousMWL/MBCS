package com.bank.customer.application.command.delete;

import com.bank.common.exception.BusinessException;
import com.bank.customer.domain.entity.Customer;
import com.bank.customer.domain.enums.CustomerStatus;
import com.bank.customer.domain.repository.CustomerRepository;
import com.bank.customer.domain.service.CustomerDomainService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteCustomerServiceTest {

    @Mock private CustomerRepository customerRepository;
    @Mock private CustomerDomainService customerDomainService;

    @InjectMocks
    private DeleteCustomerService deleteCustomerService;

    @Test
    void delete_shouldSucceed_whenCustomerExistsAndValid() {
        Customer existing = new Customer();
        existing.setId(1L);
        existing.setCustomerName("张三");
        existing.setStatus(CustomerStatus.NORMAL);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(existing));
        doNothing().when(customerDomainService).validateDelete(1L);
        doNothing().when(customerRepository).update(existing);

        deleteCustomerService.delete(1L);

        assertEquals(CustomerStatus.CLOSED, existing.getStatus());
        assertNotNull(existing.getUpdatedAt());
        verify(customerRepository).findById(1L);
        verify(customerDomainService).validateDelete(1L);
        verify(customerRepository).update(existing);
    }

    @Test
    void delete_shouldThrowException_whenCustomerNotExist() {
        when(customerRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(BusinessException.class, () -> deleteCustomerService.delete(999L));
        verify(customerRepository).findById(999L);
        verifyNoInteractions(customerDomainService);
    }
}
