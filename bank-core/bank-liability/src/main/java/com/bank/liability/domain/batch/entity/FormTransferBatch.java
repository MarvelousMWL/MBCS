package com.bank.liability.domain.batch.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("kdpb_form_transfer_batch")
public class FormTransferBatch {
    private Long id;
    private String batchNo;
    private String accountNo;
    private String productCode;
    private String sourceAccountType;
    private String targetAccountType;
    private BigDecimal principal;
    private BigDecimal transferAmount;
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
}
