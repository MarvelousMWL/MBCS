package com.bank.liability.domain.product.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("kdpf_chpshx")
public class ProductDefinition {
    private Long id;
    private String productCode;
    private String productName;
    private LocalDate effectiveDate;
    private LocalDate expiryDate;
    private String productStatus;
    private String currentFixedFlag;
    private String customerType;
    private String productType;
    private String depositType;
    private String defaultCurrency;
    private String withdrawScope;
    private String depositScope;
    private String cashWithdrawalFlag;
    private String ttWithdrawalFlag;
    private String maturityFlag;
    private String overdraftFlag;
    private String chargeFlag;
    private String accountClassifyFlag;
    private String channelCtrlMode;
    private String currencyCtrlMode;
    private String institutionCtrlMode;
    private String customerCtrlMode;
    private String voucherCtrlMode;
    private String termCtrlMode;
    private String simpleInterestFlag;
    private String settlementFlag;
    private String exchangeFlag;
    private String exchangeSellFlag;
    private String balanceSyncFlag;
    private String formTransferFlag;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public boolean isActive() {
        return "0".equals(this.productStatus);
    }
}
