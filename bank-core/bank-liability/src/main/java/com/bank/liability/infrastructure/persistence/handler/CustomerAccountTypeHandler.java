package com.bank.liability.infrastructure.persistence.handler;

import com.bank.common.infrastructure.persistence.handler.BaseEnumTypeHandler;
import com.bank.liability.domain.enums.CustomerAccountType;

public class CustomerAccountTypeHandler extends BaseEnumTypeHandler<CustomerAccountType> {
    public CustomerAccountTypeHandler() {
        super(CustomerAccountType.class);
    }
}