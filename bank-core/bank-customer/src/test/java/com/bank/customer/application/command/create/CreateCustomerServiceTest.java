package com.bank.customer.application.command.create;

import com.bank.customer.domain.entity.Customer;
import com.bank.customer.domain.enums.CustomerStatus;
import com.bank.customer.domain.repository.CustomerRepository;
import com.bank.customer.domain.service.CustomerDomainService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateCustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerDomainService customerDomainService;

    @InjectMocks
    private CreateCustomerService createCustomerService;

    @Test
    void create_shouldSucceed_whenCommandIsValid() {
        CreateCustomerCommand command = new CreateCustomerCommand();
        command.setCustomerName("李四");
        command.setIdType("ID_CARD");
        command.setIdNumber("110101199002022345");
        command.setPhone("13900139000");
        command.setAddress("上海市浦东新区");

        when(customerRepository.findAll()).thenReturn(new ArrayList<>());
        doNothing().when(customerDomainService).validateCreate(any(Customer.class));
        doNothing().when(customerRepository).save(any(Customer.class));

        Customer result = createCustomerService.create(command);

        assertNotNull(result);
        assertEquals("000000001", result.getCustomerNo());
        assertEquals("李四", result.getCustomerName());
        assertEquals(CustomerStatus.NORMAL, result.getStatus());

        ArgumentCaptor<Customer> customerCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerRepository).save(customerCaptor.capture());

        Customer savedCustomer = customerCaptor.getValue();
        assertEquals("000000001", savedCustomer.getCustomerNo());
        assertEquals("李四", savedCustomer.getCustomerName());
        assertEquals("ID_CARD", savedCustomer.getIdType());
        assertEquals("110101199002022345", savedCustomer.getIdNumber());
        assertEquals("13900139000", savedCustomer.getPhone());
        assertEquals("上海市浦东新区", savedCustomer.getAddress());
        assertEquals(CustomerStatus.NORMAL, savedCustomer.getStatus());
        assertNotNull(savedCustomer.getCreatedAt());
        assertNotNull(savedCustomer.getUpdatedAt());

        verify(customerRepository).findAll();
        verify(customerDomainService).validateCreate(any(Customer.class));
    }
}
