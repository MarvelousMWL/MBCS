package com.bank.liability.domain.batch.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("kdpb_interest_settlement_batch")
public class InterestSettlementBatch {
    private Long id;
    private String batchNo;
    private String accountNo;
    private String productCode;
    private BigDecimal principal;
    private BigDecimal interestRate;
    private BigDecimal calculatedInterest;
    private String status;
    private LocalDate settlementDate;
    private String errorMessage;
    private Integer version;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public boolean isProcessed() {
        return "PROCESSED".equals(this.status);
    }

    public boolean isFailed() {
        return "FAILED".equals(this.status);
    }

    public BigDecimal calculateSettlementInterest() {
        return principal.multiply(interestRate)
            .divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP);
    }
}
