package com.bank.liability.application.customersubaccount.open;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OpenSubAccountCommand {

    @NotBlank(message = "客户账号不能为空")
    private String customerAccountNo;

    @NotBlank(message = "账户类型不能为空")
    private String accountType = "DEMAND";
}
