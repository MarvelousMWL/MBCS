package com.bank.liability.infrastructure.persistence.handler;

import com.bank.common.infrastructure.persistence.handler.BaseEnumTypeHandler;
import com.bank.liability.domain.enums.LiabilityAccountType;

public class LiabilityAccountTypeHandler extends BaseEnumTypeHandler<LiabilityAccountType> {
    public LiabilityAccountTypeHandler() {
        super(LiabilityAccountType.class);
    }
}