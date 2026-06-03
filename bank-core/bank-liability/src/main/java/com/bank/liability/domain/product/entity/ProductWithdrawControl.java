package com.bank.liability.domain.product.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data @TableName("kdpf_zhiqkz")
public class ProductWithdrawControl {
    private Long id; private String productCode;
    private String currencyCode;
    private String cashWithdrawFlag;
    private String transferWithdrawFlag;
    private String withdrawCtrlMode;
    private String withdrawCtrlMethod;
    private String customCtrlMethod;
    private String withdrawAppointment;
    private Integer withdrawProtectOrder;
    private String protectNature;
    private String amountCtrlMode;
    private BigDecimal singleMinAmount;
    private BigDecimal singleMaxAmount;
    private String timesCtrlMode;
    private Integer minWithdrawTimes;
    private Integer maxWithdrawTimes;
    private String withdrawPlanFlag;
    private String planAdjustCycleMethod;
    private String planAdjustCycle;
    private String planEndDateMethod;
    private String planCtrlMode;
    private String defaultStandard;
    private String defaultHandlingMethod;
    private BigDecimal retainMinBalance;
    private LocalDateTime createdAt; private LocalDateTime updatedAt;
}
