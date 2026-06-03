package com.bank.teller.domain.enums;

import com.bank.common.domain.enums.BaseEnumType;

public enum TellerType implements BaseEnumType {
    NORMAL("0", "普通柜员"),
    VAULT("1", "库管柜员");
    
    private final String code;
    private final String description;
    
    TellerType(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    @Override
    public String getCode() {
        return code;
    }
    
    @Override
    public String getDescription() {
        return description;
    }
}
