package com.bank.customer.application.command.update;

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
class UpdateCustomerServiceTest {

    @Mock private CustomerRepository customerRepository;
    @Mock private CustomerDomainService customerDomainService;

    @InjectMocks
    private UpdateCustomerService updateCustomerService;

    @Test
    void update_shouldSucceed_whenCustomerExists() {
        Customer existing = new Customer();
        existing.setId(1L);
        existing.setCustomerName("张三");
        existing.setPhone("13800138000");
        existing.setAddress("北京市");

        UpdateCustomerCommand command = new UpdateCustomerCommand();
        command.setId(1L);
        command.setCustomerName("张三（更新）");
        command.setPhone("13900139000");
        command.setAddress("上海市");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(existing));
        doNothing().when(customerDomainService).validateUpdate(existing);
        doNothing().when(customerRepository).update(existing);

        Customer result = updateCustomerService.update(command);

        assertNotNull(result);
        assertEquals("张三（更新）", result.getCustomerName());
        assertEquals("13900139000", result.getPhone());
        assertEquals("上海市", result.getAddress());
        assertNotNull(result.getUpdatedAt());

        verify(customerRepository).findById(1L);
        verify(customerDomainService).validateUpdate(existing);
        verify(customerRepository).update(existing);
    }

    @Test
    void update_shouldThrowException_whenCustomerNotExist() {
        UpdateCustomerCommand command = new UpdateCustomerCommand();
        command.setId(999L);

        when(customerRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> updateCustomerService.update(command));
        verify(customerRepository).findById(999L);
        verifyNoInteractions(customerDomainService);
    }
}
