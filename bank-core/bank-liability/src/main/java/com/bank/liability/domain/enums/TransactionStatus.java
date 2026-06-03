package com.bank.liability.domain.enums;

import com.bank.common.domain.enums.BaseEnumType;

public enum TransactionStatus implements BaseEnumType {
    NORMAL("0", "正常"),
    CANCELLED("1", "已冲正");
    
    private final String code;
    private final String description;
    
    TransactionStatus(String code, String description) {
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
