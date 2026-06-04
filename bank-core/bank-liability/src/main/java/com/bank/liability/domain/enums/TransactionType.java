package com.bank.liability.domain.enums;

import com.bank.common.domain.enums.BaseEnumType;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TransactionType implements BaseEnumType {
    CUSTOMER_ACCOUNT_OPEN(1001, "客户账号开户"),
    CUSTOMER_ACCOUNT_CLOSE(1002, "客户账号销户"),
    LIABILITY_ACCOUNT_OPEN(1003, "负债账号开户"),
    LIABILITY_ACCOUNT_CLOSE(1004, "负债账号销户"),
    DEPOSIT(2001, "存款"),
    WITHDRAW(2002, "取款"),
    DEPOSIT_CANCEL(3001, "存款冲正"),
    WITHDRAW_CANCEL(3002, "取款冲正"),
    TRANSFER(2003, "行内转账"),
    TRANSFER_CANCEL(3003, "转账冲正");
    
    private final Integer code;
    private final String description;
    
    TransactionType(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
    
    @JsonValue
    @Override
    public Integer getCode() { return code; }
    
    @Override
    public String getDescription() { return description; }
}
