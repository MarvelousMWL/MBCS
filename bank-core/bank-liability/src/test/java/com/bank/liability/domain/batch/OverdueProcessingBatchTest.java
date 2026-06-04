package com.bank.liability.domain.batch;

import com.bank.liability.domain.batch.entity.OverdueProcessingBatch;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OverdueProcessingBatchTest {

    @Test
    void shouldBeUnprocessed_whenJustCreated() {
        OverdueProcessingBatch batch = new OverdueProcessingBatch();
        batch.setStatus("UNPROCESSED");
        assertTrue(batch.isUnprocessed());
        assertFalse(batch.isProcessed());
        assertFalse(batch.isFailed());
    }

    @Test
    void shouldBeProcessed_whenStatusIsProcessed() {
        OverdueProcessingBatch batch = new OverdueProcessingBatch();
        batch.setStatus("PROCESSED");
        assertTrue(batch.isProcessed());
        assertFalse(batch.isUnprocessed());
        assertFalse(batch.isFailed());
    }

    @Test
    void shouldBeFailed_whenStatusIsFailed() {
        OverdueProcessingBatch batch = new OverdueProcessingBatch();
        batch.setStatus("FAILED");
        assertTrue(batch.isFailed());
        assertFalse(batch.isProcessed());
        assertFalse(batch.isUnprocessed());
    }

    @Test
    void shouldCalculateOverdueInterestCorrectly() {
        OverdueProcessingBatch batch = new OverdueProcessingBatch();
        batch.setPrincipal(BigDecimal.valueOf(100000));
        batch.setCurrentInterestRate(BigDecimal.valueOf(1.5));
        batch.setOverdueDays(30);
        BigDecimal interest = batch.calculateOverdueInterest();
        assertEquals(0, BigDecimal.valueOf(125.00).compareTo(interest));
    }

    @Test
    void shouldCalculateZeroInterest_whenPrincipalIsZero() {
        OverdueProcessingBatch batch = new OverdueProcessingBatch();
        batch.setPrincipal(BigDecimal.ZERO);
        batch.setCurrentInterestRate(BigDecimal.valueOf(1.5));
        batch.setOverdueDays(30);
        BigDecimal interest = batch.calculateOverdueInterest();
        assertEquals(0, BigDecimal.ZERO.compareTo(interest));
    }

    @Test
    void shouldReturnZeroInterest_whenFieldsAreNull() {
        OverdueProcessingBatch batch = new OverdueProcessingBatch();
        assertEquals(BigDecimal.ZERO, batch.calculateOverdueInterest());
    }

    @Test
    void shouldHaveAllFieldsSettable() {
        LocalDateTime now = LocalDateTime.now();
        OverdueProcessingBatch batch = new OverdueProcessingBatch();
        batch.setId(1L);
        batch.setBatchNo("OP20260604-TEST001");
        batch.setAccountNo("CDACC001");
        batch.setProductCode("CD001");
        batch.setPrincipal(BigDecimal.valueOf(100000));
        batch.setCurrentInterestRate(BigDecimal.valueOf(1.5));
        batch.setOverdueDays(30);
        batch.setCalculatedInterest(BigDecimal.valueOf(125.00));
        batch.setStatus("UNPROCESSED");
        batch.setProcessedDate(LocalDate.of(2026, 6, 4));
        batch.setVersion(0);
        batch.setCreatedAt(now);
        batch.setUpdatedAt(now);

        assertEquals(1L, batch.getId());
        assertEquals("OP20260604-TEST001", batch.getBatchNo());
        assertEquals("CDACC001", batch.getAccountNo());
        assertEquals("CD001", batch.getProductCode());
        assertEquals(0, BigDecimal.valueOf(100000).compareTo(batch.getPrincipal()));
        assertEquals(0, BigDecimal.valueOf(1.5).compareTo(batch.getCurrentInterestRate()));
        assertEquals(30, batch.getOverdueDays().intValue());
        assertEquals(0, BigDecimal.valueOf(125.00).compareTo(batch.getCalculatedInterest()));
        assertEquals("UNPROCESSED", batch.getStatus());
        assertEquals(LocalDate.of(2026, 6, 4), batch.getProcessedDate());
        assertEquals(0, batch.getVersion().intValue());
        assertEquals(now, batch.getCreatedAt());
        assertEquals(now, batch.getUpdatedAt());
    }
}
