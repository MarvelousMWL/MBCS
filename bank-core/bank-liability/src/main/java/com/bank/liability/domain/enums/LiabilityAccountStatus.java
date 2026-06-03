package com.bank.liability.domain.enums;

import com.bank.common.domain.enums.BaseEnumType;

public enum LiabilityAccountStatus implements BaseEnumType {
    NORMAL("0", "正常"),
    STOPPED("1", "停用"),
    CLOSED("2", "已销户"),
    FROZEN("3", "冻结");
    
    private final String code;
    private final String description;
    
    LiabilityAccountStatus(String code, String description) {
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
