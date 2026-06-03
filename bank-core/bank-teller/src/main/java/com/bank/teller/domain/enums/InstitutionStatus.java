package com.bank.teller.domain.enums;

import com.bank.common.domain.enums.BaseEnumType;

public enum InstitutionStatus implements BaseEnumType {
    NORMAL("0", "正常"),
    STOPPED("1", "停用");
    
    private final String code;
    private final String description;
    
    InstitutionStatus(String code, String description) {
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
