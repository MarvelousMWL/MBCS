package com.bank.teller.infrastructure.persistence.handler;

import com.bank.common.infrastructure.persistence.handler.BaseEnumTypeHandler;
import com.bank.teller.domain.enums.TellerType;

public class TellerTypeTypeHandler extends BaseEnumTypeHandler<TellerType> {
    public TellerTypeTypeHandler() {
        super(TellerType.class);
    }
}