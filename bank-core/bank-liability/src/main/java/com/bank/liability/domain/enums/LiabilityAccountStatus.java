package com.bank.liability.domain.enums;

import com.bank.common.domain.enums.BaseEnumType;
import com.fasterxml.jackson.annotation.JsonValue;

public enum LiabilityAccountStatus implements BaseEnumType {
    NORMAL(0, "正常"),
    STOPPED(1, "停用"),
    CLOSED(2, "已销户"),
    FROZEN(3, "冻结");
    
    private final Integer code;
    private final String description;
    
    LiabilityAccountStatus(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
    
    @JsonValue
    @Override
    public Integer getCode() { return code; }
    
    @Override
    public String getDescription() { return description; }
}
