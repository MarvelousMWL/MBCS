package com.bank.liability.application.batch;

import com.bank.common.exception.BusinessException;
import com.bank.liability.domain.batch.entity.InterestSettlementBatch;
import com.bank.liability.domain.batch.repository.InterestSettlementBatchRepository;
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
class InterestSettlementBatchServiceTest {

    @Mock
    private InterestSettlementBatchRepository repository;

    @InjectMocks
    private InterestSettlementBatchService service;

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
        InterestSettlementBatch item = new InterestSettlementBatch();
        item.setId(1L);
        item.setAccountNo("ACC001");
        item.setProductCode("PD001");
        item.setPrincipal(BigDecimal.valueOf(10000));
        item.setInterestRate(BigDecimal.valueOf(3.5));
        item.setStatus("UNPROCESSED");
        item.setVersion(0);

        when(repository.findByStatus("UNPROCESSED")).thenReturn(List.of(item));

        BatchResult result = service.executeBatch(LocalDate.of(2026, 6, 4));

        assertEquals(1, result.getTotalItems());
        assertEquals(1, result.getSuccessCount());
        assertEquals(0, result.getFailCount());
        assertEquals("COMPLETED", result.getStatus());

        ArgumentCaptor<InterestSettlementBatch> captor = ArgumentCaptor.forClass(InterestSettlementBatch.class);
        verify(repository).update(captor.capture());
        InterestSettlementBatch updated = captor.getValue();
        assertEquals("PROCESSED", updated.getStatus());
        assertEquals(0, BigDecimal.valueOf(350.00).compareTo(updated.getCalculatedInterest()));
        assertNotNull(updated.getBatchNo());
    }

    @Test
    void executeBatch_shouldHandleFailureGracefully() {
        InterestSettlementBatch item = new InterestSettlementBatch();
        item.setId(1L);
        item.setAccountNo("ACC001");
        item.setPrincipal(BigDecimal.valueOf(-100));
        item.setInterestRate(BigDecimal.valueOf(3.5));
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
    void addSettlementItem_shouldThrow_whenPrincipalIsZero() {
        assertThrows(BusinessException.class, () ->
            service.addSettlementItem("ACC001", "PD001", BigDecimal.ZERO, BigDecimal.valueOf(3.5)));
    }

    @Test
    void addSettlementItem_shouldThrow_whenPrincipalIsNull() {
        assertThrows(BusinessException.class, () ->
            service.addSettlementItem("ACC001", "PD001", null, BigDecimal.valueOf(3.5)));
    }

    @Test
    void addSettlementItem_shouldThrow_whenRateIsNegative() {
        assertThrows(BusinessException.class, () ->
            service.addSettlementItem("ACC001", "PD001", BigDecimal.valueOf(10000), BigDecimal.valueOf(-1)));
    }

    @Test
    void addSettlementItem_shouldSucceed() {
        doNothing().when(repository).save(any());

        BatchResult result = service.addSettlementItem(
                "ACC001", "PD001", BigDecimal.valueOf(10000), BigDecimal.valueOf(3.5));

        assertEquals("QUEUED", result.getStatus());
        assertEquals(1, result.getTotalItems());
        assertEquals(1, result.getSuccessCount());
        verify(repository).save(any());
    }
}