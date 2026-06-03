package com.bank.liability.domain.product.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("kdpf_chpshx")
public class ProductDefinition {
    private Long id;
    private String productCode;
    private String productDescription;
    private String marketingDescription;
    private LocalDate effectiveDate;
    private LocalDate expiryDate;
    private String currentFixedFlag;
    private String customerType;
    private String productType;
    private String defaultCurrency;
    private String cashExchangeFlag;
    private String transferExchangeFlag;
    private String withdrawScope;
    private String depositScope;
    private String channelCtrlMode;
    private String currencyCtrlMode;
    private String institutionCtrlMode;
    private String customerCtrlMode;
    private String accountClassifyFlag;
    private String voucherCtrlMode;
    private String termCtrlMode;
    private String defaultAccountType;
    private String productStatus;
    private String maturityFlag;
    private String overdraftFlag;
    private String chargeFlag;
    private String simpleInterestFlag;
    private String settlementFlag;
    private String autoExchangeFlag;
    private String autoExchangeSellFlag;
    private String exchangeFlag;
    private String balanceSyncFlag;
    private String formTransferFlag;
    private BigDecimal totalLimit;
    private String registeredFlag;
    private String productSwitchFlag;
    private String costCenter;
    private String depositType;
    private String conversionCurrency;
    private String productDesigner;
    private String productManager;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public boolean isActive() { return "0".equals(productStatus); }
}
