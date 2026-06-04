package com.bank.liability.domain.certificatedeposit.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("cd_maturity_batch_log")
public class CDMaturityBatchLog {
    private Long id;
    private String batchNo;
    private String cdAccountNo;
    private String productCode;
    private BigDecimal principal;
    private BigDecimal interestRate;
    private BigDecimal interestAmount;
    private BigDecimal totalAmount;
    private String status;
    private LocalDate maturityDate;
    private LocalDate processingDate;
    private String errorMessage;
    private Integer version;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
