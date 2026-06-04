package com.bank.customer.infrastructure.persistence.handler;

import com.bank.common.infrastructure.persistence.handler.BaseEnumTypeHandler;
import com.bank.customer.domain.enums.IdType;

public class IdTypeHandler extends BaseEnumTypeHandler<IdType> {
    public IdTypeHandler() {
        super(IdType.class);
    }
}