package com.bank.liability.application.customeraccount.open;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OpenCustomerAccountCommand {
    @NotBlank(message = "客户号不能为空")
    private String customerNo;

    private String accountType = "PERSONAL";
}
