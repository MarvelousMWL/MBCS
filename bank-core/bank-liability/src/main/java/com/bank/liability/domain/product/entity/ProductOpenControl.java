package com.bank.liability.domain.product.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("kdpf_kaihkz")
public class ProductOpenControl {
    private Long id; private String productCode;
    private String currencyCode;
    private String specifyAccountRule;
    private String accountGenRule;
    private String accountSeqRule;
    private String openRestrictFlag;
    private String restrictType;
    private String restrictPeriod;
    private String maturityDetermineMethod;
    private String interestStartMethod;
    private Integer earlyInterestDays;
    private Integer lateInterestDays;
    private String voucherType;
    private String fundSource;
    private String transferAccountNature;
    private String specifyCustomerAccountRule;
    private String customerAccountRule;
    private String customerOpenLimitFlag;
    private String openLimitType;
    private Integer maxOpenQuantity;
    private String exchangeFlag;
    private String forceVoucherFlag;
    private String prepaidInterestFlag;
    private String prepaidInterestCode;
    private String penaltyInterestFlag;
    private String penaltyInterestCode;
    private LocalDateTime createdAt; private LocalDateTime updatedAt;
}
