package com.bank.teller.domain.enums;

import com.bank.common.domain.enums.BaseEnumType;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TellerStatus implements BaseEnumType {
    NORMAL(0, "正常"),
    STOPPED(1, "停用"),
    RESIGNED(2, "离职");
    
    private final Integer code;
    private final String description;
    
    TellerStatus(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
    
    @JsonValue
    @Override
    public Integer getCode() { return code; }
    
    @Override
    public String getDescription() { return description; }
}
