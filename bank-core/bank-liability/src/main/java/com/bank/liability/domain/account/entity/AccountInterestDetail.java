package com.bank.liability.domain.account.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("kdpl_zhlxmx")
public class AccountInterestDetail {
    private Long id;
    private String batchNo;
    private String accountNo;
    private String productCode;
    private LocalDate calcDate;
    private String balanceType;
    private BigDecimal calcBalance;
    private BigDecimal interestRate;
    private BigDecimal dailyInterest;
    private BigDecimal accruedInterest;
    private String batchType;
    private String status;
    private String errorMessage;
    private Integer version;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
