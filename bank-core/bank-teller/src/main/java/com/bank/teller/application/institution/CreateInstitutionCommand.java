package com.bank.teller.application.institution;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CreateInstitutionCommand {
    @NotBlank(message = "机构编号不能为空")
    private String institutionNo;

    @NotBlank(message = "机构名称不能为空")
    private String institutionName;

    @NotBlank(message = "机构级别不能为空")
    private String institutionLevel;

    private String parentInstitutionNo;
}
