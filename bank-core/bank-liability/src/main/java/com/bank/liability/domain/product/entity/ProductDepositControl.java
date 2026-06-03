package com.bank.liability.domain.product.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data @TableName("kdpf_cunrkz")
public class ProductDepositControl {
    private Long id; private String productCode;
    private String currencyCode;
    private String cashDepositFlag;
    private String transferDepositFlag;
    private String depositCtrlMode;
    private String depositCtrlMethod;
    private String amountCtrlMode;
    private BigDecimal singleMinAmount;
    private BigDecimal singleMaxAmount;
    private String timesCtrlMode;
    private Integer minDepositTimes;
    private Integer maxDepositTimes;
    private String depositPlanFlag;
    private String planAdjustMethod;
    private String planAdjustCycle;
    private String planEndDateMethod;
    private String planGenMethod;
    private Integer missDepositGraceDays;
    private String missDepositSupplement;
    private Integer maxSupplementTimes;
    private String depositDefaultStd;
    private Integer missDepositTimes;
    private String defaultHandlingMethod;
    private String planCtrlMode;
    private String depositProcessOrder;
    private BigDecimal firstMinAmount;
    private BigDecimal firstIncrement;
    private BigDecimal retainMaxBalance;
    private String depositFrequency;
    private LocalDateTime createdAt; private LocalDateTime updatedAt;
}
