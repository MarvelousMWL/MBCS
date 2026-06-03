package com.bank.liability.domain.enums;

import com.bank.common.domain.enums.BaseEnumType;

public enum SubAccountStatus implements BaseEnumType {
    NORMAL("0", "正常"),
    CLOSED("1", "已销户");
    
    private final String code;
    private final String description;
    
    SubAccountStatus(String code, String description) {
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
