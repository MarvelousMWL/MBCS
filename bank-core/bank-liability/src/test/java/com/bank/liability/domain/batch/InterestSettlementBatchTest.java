package com.bank.liability.domain.batch;

import com.bank.liability.domain.batch.entity.InterestSettlementBatch;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InterestSettlementBatchTest {

    @Test
    void shouldBeUnprocessed_whenJustCreated() {
        InterestSettlementBatch batch = new InterestSettlementBatch();
        batch.setStatus("UNPROCESSED");
        assertFalse(batch.isProcessed());
        assertFalse(batch.isFailed());
    }

    @Test
    void shouldBeProcessed_whenStatusIsProcessed() {
        InterestSettlementBatch batch = new InterestSettlementBatch();
        batch.setStatus("PROCESSED");
        assertTrue(batch.isProcessed());
        assertFalse(batch.isFailed());
    }

    @Test
    void shouldBeFailed_whenStatusIsFailed() {
        InterestSettlementBatch batch = new InterestSettlementBatch();
        batch.setStatus("FAILED");
        assertTrue(batch.isFailed());
        assertFalse(batch.isProcessed());
    }

    @Test
    void shouldCalculateSettlementInterestCorrectly() {
        InterestSettlementBatch batch = new InterestSettlementBatch();
        batch.setPrincipal(BigDecimal.valueOf(10000));
        batch.setInterestRate(BigDecimal.valueOf(3.5));
        BigDecimal interest = batch.calculateSettlementInterest();
        assertEquals(0, BigDecimal.valueOf(350.00).compareTo(interest));
    }

    @Test
    void shouldCalculateZeroInterest_whenPrincipalIsZero() {
        InterestSettlementBatch batch = new InterestSettlementBatch();
        batch.setPrincipal(BigDecimal.ZERO);
        batch.setInterestRate(BigDecimal.valueOf(3.5));
        BigDecimal interest = batch.calculateSettlementInterest();
        assertEquals(0, BigDecimal.ZERO.compareTo(interest));
    }

    @Test
    void shouldHaveAllFieldsSettable() {
        LocalDateTime now = LocalDateTime.now();
        InterestSettlementBatch batch = new InterestSettlementBatch();
        batch.setId(1L);
        batch.setBatchNo("IS20260604-TEST001");
        batch.setAccountNo("ACC001");
        batch.setProductCode("PD001");
        batch.setPrincipal(BigDecimal.valueOf(50000));
        batch.setInterestRate(BigDecimal.valueOf(2.5));
        batch.setCalculatedInterest(BigDecimal.valueOf(1250));
        batch.setStatus("UNPROCESSED");
        batch.setSettlementDate(LocalDate.of(2026, 6, 4));
        batch.setVersion(0);
        batch.setCreatedAt(now);
        batch.setUpdatedAt(now);

        assertEquals(1L, batch.getId());
        assertEquals("IS20260604-TEST001", batch.getBatchNo());
        assertEquals("ACC001", batch.getAccountNo());
        assertEquals("PD001", batch.getProductCode());
        assertEquals(0, BigDecimal.valueOf(50000).compareTo(batch.getPrincipal()));
        assertEquals(0, BigDecimal.valueOf(2.5).compareTo(batch.getInterestRate()));
        assertEquals(0, BigDecimal.valueOf(1250).compareTo(batch.getCalculatedInterest()));
        assertEquals("UNPROCESSED", batch.getStatus());
        assertEquals(LocalDate.of(2026, 6, 4), batch.getSettlementDate());
        assertEquals(0, batch.getVersion().intValue());
        assertEquals(now, batch.getCreatedAt());
        assertEquals(now, batch.getUpdatedAt());
    }
}