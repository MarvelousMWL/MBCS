package com.bank.liability.domain.batch;

import com.bank.liability.domain.batch.entity.AutoRedemptionBatch;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AutoRedemptionBatchTest {

    @Test
    void shouldBeUnprocessed_whenJustCreated() {
        AutoRedemptionBatch batch = new AutoRedemptionBatch();
        batch.setStatus("UNPROCESSED");
        assertTrue(batch.isUnprocessed());
        assertFalse(batch.isProcessed());
        assertFalse(batch.isFailed());
    }

    @Test
    void shouldBeProcessed_whenStatusIsProcessed() {
        AutoRedemptionBatch batch = new AutoRedemptionBatch();
        batch.setStatus("PROCESSED");
        assertTrue(batch.isProcessed());
        assertFalse(batch.isUnprocessed());
        assertFalse(batch.isFailed());
    }

    @Test
    void shouldBeFailed_whenStatusIsFailed() {
        AutoRedemptionBatch batch = new AutoRedemptionBatch();
        batch.setStatus("FAILED");
        assertTrue(batch.isFailed());
        assertFalse(batch.isProcessed());
        assertFalse(batch.isUnprocessed());
    }

    @Test
    void shouldHaveAllFieldsSettable() {
        LocalDateTime now = LocalDateTime.now();
        AutoRedemptionBatch batch = new AutoRedemptionBatch();
        batch.setId(1L);
        batch.setBatchNo("AR20260604-TEST001");
        batch.setAccountNo("CDACC001");
        batch.setProductCode("CD001");
        batch.setPrincipal(BigDecimal.valueOf(100000));
        batch.setInterest(BigDecimal.valueOf(3500));
        batch.setTotalAmount(BigDecimal.valueOf(103500));
        batch.setMaturityDate(LocalDate.of(2026, 6, 4));
        batch.setRolloverType("PRINCIPAL_ONLY");
        batch.setStatus("UNPROCESSED");
        batch.setProcessedDate(LocalDate.of(2026, 6, 4));
        batch.setVersion(0);
        batch.setCreatedAt(now);
        batch.setUpdatedAt(now);

        assertEquals(1L, batch.getId());
        assertEquals("AR20260604-TEST001", batch.getBatchNo());
        assertEquals("CDACC001", batch.getAccountNo());
        assertEquals("CD001", batch.getProductCode());
        assertEquals(0, BigDecimal.valueOf(100000).compareTo(batch.getPrincipal()));
        assertEquals(0, BigDecimal.valueOf(3500).compareTo(batch.getInterest()));
        assertEquals(0, BigDecimal.valueOf(103500).compareTo(batch.getTotalAmount()));
        assertEquals(LocalDate.of(2026, 6, 4), batch.getMaturityDate());
        assertEquals("PRINCIPAL_ONLY", batch.getRolloverType());
        assertEquals("UNPROCESSED", batch.getStatus());
        assertEquals(LocalDate.of(2026, 6, 4), batch.getProcessedDate());
        assertEquals(0, batch.getVersion().intValue());
        assertEquals(now, batch.getCreatedAt());
        assertEquals(now, batch.getUpdatedAt());
    }
}