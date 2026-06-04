package com.bank.customer.domain.enums;

import com.bank.common.domain.enums.BaseEnumType;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CustomerStatus implements BaseEnumType {
    NORMAL(0, "正常"),
    STOPPED(1, "停用"),
    CLOSED(2, "销户");
    
    private final Integer code;
    private final String description;
    
    CustomerStatus(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
    
    @JsonValue
    @Override
    public Integer getCode() { return code; }
    
    @Override
    public String getDescription() { return description; }
}
