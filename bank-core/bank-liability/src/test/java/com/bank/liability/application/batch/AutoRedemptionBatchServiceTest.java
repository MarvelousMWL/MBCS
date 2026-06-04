package com.bank.liability.application.batch;

import com.bank.common.exception.BusinessException;
import com.bank.liability.domain.batch.entity.AutoRedemptionBatch;
import com.bank.liability.domain.batch.repository.AutoRedemptionBatchRepository;
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
class AutoRedemptionBatchServiceTest {

    @Mock
    private AutoRedemptionBatchRepository repository;

    @InjectMocks
    private AutoRedemptionBatchService service;

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
        AutoRedemptionBatch item = new AutoRedemptionBatch();
        item.setId(1L);
        item.setAccountNo("CDACC001");
        item.setProductCode("CD001");
        item.setPrincipal(BigDecimal.valueOf(100000));
        item.setInterest(BigDecimal.valueOf(3500));
        item.setTotalAmount(BigDecimal.valueOf(103500));
        item.setMaturityDate(LocalDate.of(2026, 6, 3));
        item.setRolloverType("PRINCIPAL_ONLY");
        item.setStatus("UNPROCESSED");
        item.setVersion(0);

        when(repository.findByStatus("UNPROCESSED")).thenReturn(List.of(item));

        BatchResult result = service.executeBatch(LocalDate.of(2026, 6, 4));

        assertEquals(1, result.getTotalItems());
        assertEquals(1, result.getSuccessCount());
        assertEquals(0, result.getFailCount());
        assertEquals("COMPLETED", result.getStatus());

        ArgumentCaptor<AutoRedemptionBatch> captor = ArgumentCaptor.forClass(AutoRedemptionBatch.class);
        verify(repository).update(captor.capture());
        AutoRedemptionBatch updated = captor.getValue();
        assertEquals("PROCESSED", updated.getStatus());
        assertNotNull(updated.getBatchNo());
        assertEquals(LocalDate.of(2026, 6, 4), updated.getProcessedDate());
    }

    @Test
    void executeBatch_shouldFail_whenMaturityDateIsAfterProcessingDate() {
        AutoRedemptionBatch item = new AutoRedemptionBatch();
        item.setId(1L);
        item.setAccountNo("CDACC001");
        item.setPrincipal(BigDecimal.valueOf(100000));
        item.setInterest(BigDecimal.valueOf(3500));
        item.setMaturityDate(LocalDate.of(2026, 6, 10));
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
    void addRedemptionItem_shouldThrow_whenPrincipalIsNull() {
        assertThrows(BusinessException.class, () ->
            service.addRedemptionItem("CDACC001", "CD001", null, BigDecimal.valueOf(3500),
                    LocalDate.of(2026, 6, 4), "PRINCIPAL_ONLY"));
    }

    @Test
    void addRedemptionItem_shouldThrow_whenMaturityDateIsNull() {
        assertThrows(BusinessException.class, () ->
            service.addRedemptionItem("CDACC001", "CD001", BigDecimal.valueOf(100000),
                    BigDecimal.valueOf(3500), null, "PRINCIPAL_ONLY"));
    }

    @Test
    void addRedemptionItem_shouldSucceed() {
        doNothing().when(repository).save(any());

        BatchResult result = service.addRedemptionItem(
                "CDACC001", "CD001", BigDecimal.valueOf(100000),
                BigDecimal.valueOf(3500), LocalDate.of(2026, 6, 4), "PRINCIPAL_ONLY");

        assertEquals("QUEUED", result.getStatus());
        assertEquals(1, result.getTotalItems());
        assertEquals(1, result.getSuccessCount());
        verify(repository).save(any());
    }
}