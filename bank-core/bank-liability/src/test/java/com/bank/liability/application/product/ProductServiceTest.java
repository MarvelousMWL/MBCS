package com.bank.liability.application.product;

import com.bank.liability.domain.product.entity.*;
import com.bank.liability.domain.product.repository.ProductRepository;
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
class ProductServiceTest {

    @Mock private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void listAll_shouldReturnAllProducts() {
        ProductDefinition p1 = new ProductDefinition();
        p1.setProductCode("P001");
        ProductDefinition p2 = new ProductDefinition();
        p2.setProductCode("P002");

        when(productRepository.findAll()).thenReturn(List.of(p1, p2));

        List<ProductDefinition> result = productService.listAll();
        assertEquals(2, result.size());
        assertEquals("P001", result.get(0).getProductCode());
        verify(productRepository).findAll();
    }

    @Test
    void getByProductCode_shouldReturnProduct_whenExists() {
        ProductDefinition p = new ProductDefinition();
        p.setProductCode("P001");
        when(productRepository.findByProductCode("P001")).thenReturn(Optional.of(p));

        ProductDefinition result = productService.getByProductCode("P001");
        assertNotNull(result);
        assertEquals("P001", result.getProductCode());
    }

    @Test
    void getByProductCode_shouldThrowException_whenNotExists() {
        when(productRepository.findByProductCode("P999")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> productService.getByProductCode("P999"));
    }

    @Test
    void save_shouldSucceed_whenProductCodeProvided() {
        ProductDefinition def = new ProductDefinition();
        def.setProductCode("P001");
        doNothing().when(productRepository).save(def);

        productService.save(def);
        verify(productRepository).save(def);
    }

    @Test
    void save_shouldThrowException_whenProductCodeNull() {
        ProductDefinition def = new ProductDefinition();
        assertThrows(RuntimeException.class, () -> productService.save(def));
        verify(productRepository, never()).save(any());
    }

    @Test
    void update_shouldSucceed_whenProductExists() {
        ProductDefinition existing = new ProductDefinition();
        existing.setId(1L);
        existing.setProductCode("P001");

        ProductDefinition update = new ProductDefinition();
        update.setProductCode("P001");
        update.setProductDescription("Updated Description");

        when(productRepository.findByProductCode("P001")).thenReturn(Optional.of(existing));
        doNothing().when(productRepository).update(update);

        productService.update(update);
        assertEquals(1L, update.getId());
        verify(productRepository).update(update);
    }

    @Test
    void update_shouldThrowException_whenProductNotExists() {
        ProductDefinition def = new ProductDefinition();
        def.setProductCode("P999");
        when(productRepository.findByProductCode("P999")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> productService.update(def));
        verify(productRepository, never()).update(any());
    }
}
