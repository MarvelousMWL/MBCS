package com.bank.teller.application.teller.create;

import com.bank.teller.domain.enums.TellerType;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CreateTellerCommand {
    @NotBlank(message = "柜员编号不能为空")
    private String tellerNo;

    @NotBlank(message = "柜员姓名不能为空")
    private String tellerName;

    @NotBlank(message = "机构编号不能为空")
    private String institutionNo;

    @NotNull(message = "柜员类型不能为空")
    private TellerType tellerType;

    @NotBlank(message = "密码不能为空")
    private String password;
}
