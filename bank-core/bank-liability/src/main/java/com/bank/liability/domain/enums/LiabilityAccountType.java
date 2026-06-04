package com.bank.liability.domain.enums;

import com.bank.common.domain.enums.BaseEnumType;
import com.fasterxml.jackson.annotation.JsonValue;

public enum LiabilityAccountType implements BaseEnumType {
    DEMAND(0, "活期存款"),
    TERM(1, "定期存款");
    
    private final Integer code;
    private final String description;
    
    LiabilityAccountType(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
    
    @JsonValue
    @Override
    public Integer getCode() { return code; }
    
    @Override
    public String getDescription() { return description; }
}