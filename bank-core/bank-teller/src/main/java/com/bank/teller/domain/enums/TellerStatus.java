package com.bank.teller.domain.enums;

import com.bank.common.domain.enums.BaseEnumType;

public enum TellerStatus implements BaseEnumType {
    NORMAL("0", "正常"),
    STOPPED("1", "停用"),
    RESIGNED("2", "离职");
    
    private final String code;
    private final String description;
    
    TellerStatus(String code, String description) {
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
