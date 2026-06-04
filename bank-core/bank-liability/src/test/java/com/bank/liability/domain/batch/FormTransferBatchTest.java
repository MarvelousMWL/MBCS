package com.bank.liability.domain.batch;

import com.bank.liability.domain.batch.entity.FormTransferBatch;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FormTransferBatchTest {

    @Test
    void shouldBeUnprocessed_whenJustCreated() {
        FormTransferBatch batch = new FormTransferBatch();
        batch.setStatus("UNPROCESSED");
        assertTrue(batch.isUnprocessed());
        assertFalse(batch.isProcessed());
        assertFalse(batch.isFailed());
    }

    @Test
    void shouldBeProcessed_whenStatusIsProcessed() {
        FormTransferBatch batch = new FormTransferBatch();
        batch.setStatus("PROCESSED");
        assertTrue(batch.isProcessed());
        assertFalse(batch.isUnprocessed());
        assertFalse(batch.isFailed());
    }

    @Test
    void shouldBeFailed_whenStatusIsFailed() {
        FormTransferBatch batch = new FormTransferBatch();
        batch.setStatus("FAILED");
        assertTrue(batch.isFailed());
        assertFalse(batch.isProcessed());
        assertFalse(batch.isUnprocessed());
    }

    @Test
    void shouldHaveAllFieldsSettable() {
        LocalDateTime now = LocalDateTime.now();
        FormTransferBatch batch = new FormTransferBatch();
        batch.setId(1L);
        batch.setBatchNo("FT20260604-TEST001");
        batch.setAccountNo("CDACC001");
        batch.setProductCode("CD001");
        batch.setSourceAccountType("REGULAR_DEPOSIT");
        batch.setTargetAccountType("CURRENT_DEPOSIT");
        batch.setPrincipal(BigDecimal.valueOf(100000));
        batch.setTransferAmount(BigDecimal.valueOf(100000));
        batch.setStatus("UNPROCESSED");
        batch.setProcessedDate(LocalDate.of(2026, 6, 4));
        batch.setVersion(0);
        batch.setCreatedAt(now);
        batch.setUpdatedAt(now);

        assertEquals(1L, batch.getId());
        assertEquals("FT20260604-TEST001", batch.getBatchNo());
        assertEquals("CDACC001", batch.getAccountNo());
        assertEquals("CD001", batch.getProductCode());
        assertEquals("REGULAR_DEPOSIT", batch.getSourceAccountType());
        assertEquals("CURRENT_DEPOSIT", batch.getTargetAccountType());
        assertEquals(0, BigDecimal.valueOf(100000).compareTo(batch.getPrincipal()));
        assertEquals(0, BigDecimal.valueOf(100000).compareTo(batch.getTransferAmount()));
        assertEquals("UNPROCESSED", batch.getStatus());
        assertEquals(LocalDate.of(2026, 6, 4), batch.getProcessedDate());
        assertEquals(0, batch.getVersion().intValue());
        assertEquals(now, batch.getCreatedAt());
        assertEquals(now, batch.getUpdatedAt());
    }
}
