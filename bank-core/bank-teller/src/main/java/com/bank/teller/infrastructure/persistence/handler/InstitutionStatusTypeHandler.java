package com.bank.teller.infrastructure.persistence.handler;

import com.bank.common.infrastructure.persistence.handler.BaseEnumTypeHandler;
import com.bank.teller.domain.enums.InstitutionStatus;

public class InstitutionStatusTypeHandler extends BaseEnumTypeHandler<InstitutionStatus> {
    public InstitutionStatusTypeHandler() {
        super(InstitutionStatus.class);
    }
}