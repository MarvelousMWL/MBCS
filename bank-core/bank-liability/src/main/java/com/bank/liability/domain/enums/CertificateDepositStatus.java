package com.bank.liability.domain.enums;

import com.bank.common.domain.enums.BaseEnumType;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CertificateDepositStatus implements BaseEnumType {
    PENDING(0, "待生效"),
    ACTIVE(1, "正常"),
    FROZEN(2, "冻结"),
    REDEEMED(3, "已兑付"),
    CANCELLED(4, "已撤销");
    
    private final Integer code;
    private final String description;
    
    CertificateDepositStatus(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
    
    @JsonValue
    @Override
    public Integer getCode() { return code; }
    
    @Override
    public String getDescription() { return description; }
}
