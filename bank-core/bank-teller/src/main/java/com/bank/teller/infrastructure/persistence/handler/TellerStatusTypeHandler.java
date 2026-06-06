package com.bank.teller.infrastructure.persistence.handler;

import com.bank.common.infrastructure.persistence.handler.BaseEnumTypeHandler;
import com.bank.teller.domain.enums.TellerStatus;

public class TellerStatusTypeHandler extends BaseEnumTypeHandler<TellerStatus> {
    public TellerStatusTypeHandler() {
        super(TellerStatus.class);
    }
}