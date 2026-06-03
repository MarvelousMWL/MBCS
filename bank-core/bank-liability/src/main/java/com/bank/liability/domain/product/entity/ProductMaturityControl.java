package com.bank.liability.domain.product.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data @TableName("kdpf_daoqkz")
public class ProductMaturityControl {
    private Long id; private String productCode;
    private String currencyCode;
    private String holidayProcessMethod;
    private String depositDelayFlag;
    private Integer maturityGraceDays;
    private String renewFlag;
    private String changeRenewProductFlag;
    private String renewProductCode;
    private String renewRateAdjustMethod;
    private String interestSettlementMethod;
    private LocalDateTime createdAt; private LocalDateTime updatedAt;
}
