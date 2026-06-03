package com.bank.liability.application.customeraccount.close;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CloseCustomerAccountCommand {
    @NotBlank(message = "客户账号不能为空")
    private String customerAccountNo;
}
