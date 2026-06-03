package com.bank.liability.domain.product.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data @TableName("kdpf_lilvdy")
public class ProductRateDefinition {
    private Long id; private String productCode;
    private String currencyCode;
    private String liabilityRateType;
    private String rateGearMethod;
    private String rateCode;
    private String rateCodeType;
    private String rateTerm;
    private String rateTermFlag;
    private String rateBalanceFlag;
    private String rateDetermineDate;
    private String rateDetermineMethod;
    private String rateAdjustFrequency;
    private String rateChangeAdjustRate;
    private String rateChangeAdjustInt;
    private String discountAdjustFrequency;
    private String discountChangeFlag;
    private String avgBalanceType;
    private String specifyTerm;
    private String rateUpdateMethod;
    private LocalDateTime createdAt; private LocalDateTime updatedAt;
}
