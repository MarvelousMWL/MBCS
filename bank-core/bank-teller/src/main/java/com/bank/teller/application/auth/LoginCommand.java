package com.bank.teller.application.auth;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginCommand {
    @NotBlank(message = "机构编号不能为空")
    private String institutionNo;

    @NotBlank(message = "柜员编号不能为空")
    private String tellerNo;

    @NotBlank(message = "密码不能为空")
    private String password;
}
