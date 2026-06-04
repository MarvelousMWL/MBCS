package com.bank.customer.application.query;

import com.bank.customer.domain.entity.Customer;
import com.bank.customer.domain.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerQueryServiceTest {

    @Mock private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerQueryService customerQueryService;

    @Test
    void findById_shouldReturnCustomer_whenExists() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setCustomerName("张三");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        Optional<Customer> result = customerQueryService.findById(1L);
        assertTrue(result.isPresent());
        assertEquals("张三", result.get().getCustomerName());
    }

    @Test
    void findById_shouldReturnEmpty_whenNotExists() {
        when(customerRepository.findById(999L)).thenReturn(Optional.empty());
        assertTrue(customerQueryService.findById(999L).isEmpty());
    }

    @Test
    void findByCustomerNo_shouldReturnCustomer_whenExists() {
        Customer customer = new Customer();
        customer.setCustomerNo("CUS001");
        customer.setCustomerName("张三");

        when(customerRepository.findByCustomerNo("CUS001")).thenReturn(Optional.of(customer));

        Optional<Customer> result = customerQueryService.findByCustomerNo("CUS001");
        assertTrue(result.isPresent());
        assertEquals("CUS001", result.get().getCustomerNo());
    }

    @Test
    void findByCustomerNo_shouldReturnEmpty_whenNotExists() {
        when(customerRepository.findByCustomerNo("CUS999")).thenReturn(Optional.empty());
        assertTrue(customerQueryService.findByCustomerNo("CUS999").isEmpty());
    }

    @Test
    void findAll_shouldReturnAllCustomers() {
        when(customerRepository.findAll()).thenReturn(List.of(new Customer(), new Customer()));

        List<Customer> result = customerQueryService.findAll();
        assertEquals(2, result.size());
        verify(customerRepository).findAll();
    }
}
