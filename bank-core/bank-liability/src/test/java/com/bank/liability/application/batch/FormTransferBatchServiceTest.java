package com.bank.liability.application.batch;

import com.bank.common.exception.BusinessException;
import com.bank.liability.domain.batch.entity.FormTransferBatch;
import com.bank.liability.domain.batch.repository.FormTransferBatchRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FormTransferBatchServiceTest {

    @Mock
    private FormTransferBatchRepository repository;

    @InjectMocks
    private FormTransferBatchService service;

    @Test
    void executeBatch_shouldReturnCompletedWithNoItems() {
        when(repository.findByStatus("UNPROCESSED")).thenReturn(Collections.emptyList());

        BatchResult result = service.executeBatch(LocalDate.of(2026, 6, 4));

        assertEquals("COMPLETED", result.getStatus());
        assertEquals(0, result.getTotalItems());
        assertEquals(0, result.getSuccessCount());
        assertEquals(0, result.getFailCount());
        verify(repository, times(1)).findByStatus("UNPROCESSED");
    }

    @Test
    void executeBatch_shouldProcessAllItemsSuccessfully() {
        FormTransferBatch item = new FormTransferBatch();
        item.setId(1L);
        item.setAccountNo("CDACC001");
        item.setProductCode("CD001");
        item.setSourceAccountType("REGULAR_DEPOSIT");
        item.setTargetAccountType("CURRENT_DEPOSIT");
        item.setPrincipal(BigDecimal.valueOf(100000));
        item.setStatus("UNPROCESSED");
        item.setVersion(0);

        when(repository.findByStatus("UNPROCESSED")).thenReturn(List.of(item));

        BatchResult result = service.executeBatch(LocalDate.of(2026, 6, 4));

        assertEquals(1, result.getTotalItems());
        assertEquals(1, result.getSuccessCount());
        assertEquals(0, result.getFailCount());
        assertEquals("COMPLETED", result.getStatus());

        ArgumentCaptor<FormTransferBatch> captor = ArgumentCaptor.forClass(FormTransferBatch.class);
        verify(repository).update(captor.capture());
        FormTransferBatch updated = captor.getValue();
        assertEquals("PROCESSED", updated.getStatus());
        assertEquals(0, BigDecimal.valueOf(100000).compareTo(updated.getTransferAmount()));
        assertNotNull(updated.getBatchNo());
        assertEquals(LocalDate.of(2026, 6, 4), updated.getProcessedDate());
    }

    @Test
    void executeBatch_shouldHandleFailureGracefully() {
        FormTransferBatch item = new FormTransferBatch();
        item.setId(1L);
        item.setAccountNo("CDACC001");
        item.setPrincipal(BigDecimal.ZERO);
        item.setStatus("UNPROCESSED");
        item.setVersion(0);

        when(repository.findByStatus("UNPROCESSED")).thenReturn(List.of(item));

        BatchResult result = service.executeBatch(LocalDate.of(2026, 6, 4));

        assertEquals(1, result.getTotalItems());
        assertEquals(0, result.getSuccessCount());
        assertEquals(1, result.getFailCount());
        assertEquals("PARTIALLY_COMPLETED", result.getStatus());
    }

    @Test
    void addTransferItem_shouldThrow_whenPrincipalIsNull() {
        assertThrows(BusinessException.class, () ->
            service.addTransferItem("CDACC001", "CD001", null, "REGULAR_DEPOSIT", "CURRENT_DEPOSIT"));
    }

    @Test
    void addTransferItem_shouldThrow_whenPrincipalIsZero() {
        assertThrows(BusinessException.class, () ->
            service.addTransferItem("CDACC001", "CD001", BigDecimal.ZERO, "REGULAR_DEPOSIT", "CURRENT_DEPOSIT"));
    }

    @Test
    void addTransferItem_shouldThrow_whenSourceTypeIsBlank() {
        assertThrows(BusinessException.class, () ->
            service.addTransferItem("CDACC001", "CD001", BigDecimal.valueOf(100000), "", "CURRENT_DEPOSIT"));
    }

    @Test
    void addTransferItem_shouldThrow_whenTargetTypeIsBlank() {
        assertThrows(BusinessException.class, () ->
            service.addTransferItem("CDACC001", "CD001", BigDecimal.valueOf(100000), "REGULAR_DEPOSIT", ""));
    }

    @Test
    void addTransferItem_shouldThrow_whenTypesAreSame() {
        assertThrows(BusinessException.class, () ->
            service.addTransferItem("CDACC001", "CD001", BigDecimal.valueOf(100000), "REGULAR_DEPOSIT", "REGULAR_DEPOSIT"));
    }

    @Test
    void addTransferItem_shouldSucceed() {
        doNothing().when(repository).save(any());

        BatchResult result = service.addTransferItem(
                "CDACC001", "CD001", BigDecimal.valueOf(100000), "REGULAR_DEPOSIT", "CURRENT_DEPOSIT");

        assertEquals("QUEUED", result.getStatus());
        assertEquals(1, result.getTotalItems());
        assertEquals(1, result.getSuccessCount());
        verify(repository).save(any());
    }
}
