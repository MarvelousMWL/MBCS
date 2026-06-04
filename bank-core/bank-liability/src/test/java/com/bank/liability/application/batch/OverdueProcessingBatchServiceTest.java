package com.bank.liability.application.batch;

import com.bank.common.exception.BusinessException;
import com.bank.liability.domain.batch.entity.OverdueProcessingBatch;
import com.bank.liability.domain.batch.repository.OverdueProcessingBatchRepository;
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
class OverdueProcessingBatchServiceTest {

    @Mock
    private OverdueProcessingBatchRepository repository;

    @InjectMocks
    private OverdueProcessingBatchService service;

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
        OverdueProcessingBatch item = new OverdueProcessingBatch();
        item.setId(1L);
        item.setAccountNo("CDACC001");
        item.setProductCode("CD001");
        item.setPrincipal(BigDecimal.valueOf(100000));
        item.setCurrentInterestRate(BigDecimal.valueOf(1.5));
        item.setOverdueDays(30);
        item.setStatus("UNPROCESSED");
        item.setVersion(0);

        when(repository.findByStatus("UNPROCESSED")).thenReturn(List.of(item));

        BatchResult result = service.executeBatch(LocalDate.of(2026, 6, 4));

        assertEquals(1, result.getTotalItems());
        assertEquals(1, result.getSuccessCount());
        assertEquals(0, result.getFailCount());
        assertEquals("COMPLETED", result.getStatus());

        ArgumentCaptor<OverdueProcessingBatch> captor = ArgumentCaptor.forClass(OverdueProcessingBatch.class);
        verify(repository).update(captor.capture());
        OverdueProcessingBatch updated = captor.getValue();
        assertEquals("PROCESSED", updated.getStatus());
        assertEquals(0, BigDecimal.valueOf(125.00).compareTo(updated.getCalculatedInterest()));
        assertNotNull(updated.getBatchNo());
    }

    @Test
    void executeBatch_shouldHandleFailureGracefully() {
        OverdueProcessingBatch item = new OverdueProcessingBatch();
        item.setId(1L);
        item.setAccountNo("CDACC001");
        item.setPrincipal(BigDecimal.valueOf(100000));
        item.setCurrentInterestRate(BigDecimal.valueOf(1.5));
        item.setOverdueDays(0);
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
    void addOverdueItem_shouldThrow_whenPrincipalIsNull() {
        assertThrows(BusinessException.class, () ->
            service.addOverdueItem("CDACC001", "CD001", null, BigDecimal.valueOf(1.5),
                    LocalDate.of(2026, 6, 1)));
    }

    @Test
    void addOverdueItem_shouldThrow_whenPrincipalIsZero() {
        assertThrows(BusinessException.class, () ->
            service.addOverdueItem("CDACC001", "CD001", BigDecimal.ZERO, BigDecimal.valueOf(1.5),
                    LocalDate.of(2026, 6, 1)));
    }

    @Test
    void addOverdueItem_shouldThrow_whenRateIsNegative() {
        assertThrows(BusinessException.class, () ->
            service.addOverdueItem("CDACC001", "CD001", BigDecimal.valueOf(100000), BigDecimal.valueOf(-1),
                    LocalDate.of(2026, 6, 1)));
    }

    @Test
    void addOverdueItem_shouldThrow_whenMaturityDateIsNull() {
        assertThrows(BusinessException.class, () ->
            service.addOverdueItem("CDACC001", "CD001", BigDecimal.valueOf(100000), BigDecimal.valueOf(1.5), null));
    }

    @Test
    void addOverdueItem_shouldThrow_whenNotYetOverdue() {
        assertThrows(BusinessException.class, () ->
            service.addOverdueItem("CDACC001", "CD001", BigDecimal.valueOf(100000), BigDecimal.valueOf(1.5),
                    LocalDate.of(2026, 6, 10)));
    }

    @Test
    void addOverdueItem_shouldSucceed() {
        doNothing().when(repository).save(any());

        BatchResult result = service.addOverdueItem(
                "CDACC001", "CD001", BigDecimal.valueOf(100000), BigDecimal.valueOf(1.5),
                LocalDate.of(2026, 5, 1));

        assertEquals("QUEUED", result.getStatus());
        assertEquals(1, result.getTotalItems());
        assertEquals(1, result.getSuccessCount());
        verify(repository).save(any());
    }
}
