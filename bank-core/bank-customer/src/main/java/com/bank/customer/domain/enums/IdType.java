package com.bank.customer.domain.enums;

import com.bank.common.domain.enums.BaseEnumType;
import com.fasterxml.jackson.annotation.JsonValue;

public enum IdType implements BaseEnumType {
    ID_CARD(0, "身份证"),
    PASSPORT(1, "护照"),
    MILITARY_ID(2, "军官证");
    
    private final Integer code;
    private final String description;
    
    IdType(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
    
    @JsonValue
    @Override
    public Integer getCode() { return code; }
    
    @Override
    public String getDescription() { return description; }
}
