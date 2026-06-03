package com.bank.teller.application.institution;

import com.bank.teller.domain.enums.InstitutionStatus;
import lombok.Data;

@Data
public class UpdateInstitutionCommand {
    private String institutionName;
    private String institutionLevel;
    private String parentInstitutionNo;
    private InstitutionStatus status;
}
