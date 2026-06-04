package com.bank.liability.domain.batch.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("kdpb_overdue_processing_batch")
public class OverdueProcessingBatch {
    private Long id;
    private String batchNo;
    private String accountNo;
    private String productCode;
    private BigDecimal principal;
    private BigDecimal currentInterestRate;
    private Integer overdueDays;
    private BigDecimal calculatedInterest;
    private String status;
    private LocalDate processedDate;
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

    public boolean isUnprocessed() {
        return "UNPROCESSED".equals(this.status);
    }

    public BigDecimal calculateOverdueInterest() {
        if (principal == null || currentInterestRate == null || overdueDays == null) {
            return BigDecimal.ZERO;
        }
        return principal.multiply(currentInterestRate)
            .multiply(BigDecimal.valueOf(overdueDays))
            .divide(BigDecimal.valueOf(100 * 360), 2, BigDecimal.ROUND_HALF_UP);
    }
}
