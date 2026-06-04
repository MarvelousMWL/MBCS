package com.bank.liability.domain.enums;

import com.bank.common.domain.enums.BaseEnumType;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CustomerAccountType implements BaseEnumType {
    PERSONAL(0, "个人"),
    CORPORATE(1, "企业");
    
    private final Integer code;
    private final String description;
    
    CustomerAccountType(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
    
    @JsonValue
    @Override
    public Integer getCode() { return code; }
    
    @Override
    public String getDescription() { return description; }
}
