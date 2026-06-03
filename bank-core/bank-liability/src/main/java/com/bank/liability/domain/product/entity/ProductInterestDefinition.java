package com.bank.liability.domain.product.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data @TableName("kdpf_lixidy")
public class ProductInterestDefinition {
    private Long id; private String productCode;
    private String currencyCode;
    private String interestType;
    private String interestFlag;
    private String interestBalanceFlag;
    private String interestRuleCode;
    private String taxFlag;
    private BigDecimal minInterestAmount;
    private BigDecimal maxInterestAmount;
    private String interestAdjustMethod;
    private String standardInterestMethod;
    private String interestPaymentMethod;
    private String interestStartMethod;
    private String interestFrequency;
    private String paymentFrequency;
    private String ratePlanFlag;
    private String dateEndMethod;
    private String interestBasis;
    private String accrualFrequency;
    private String avgCycleDaysMethod;
    private String avgBalanceCycleType;
    private String avgBalanceMethod;
    private String specifyTerm;
    private LocalDateTime createdAt; private LocalDateTime updatedAt;
}
