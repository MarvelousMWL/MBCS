package com.bank.liability.application.customersubaccount.open;

import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OpenSubAccountCommand {

    @NotNull(message = "客户账号不能为空")
    private String customerAccountNo;

    @NotNull(message = "账户类型不能为空")
    private Integer accountType = 0;
}
