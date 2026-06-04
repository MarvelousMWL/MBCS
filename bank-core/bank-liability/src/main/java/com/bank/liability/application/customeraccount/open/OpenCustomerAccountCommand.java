package com.bank.liability.application.customeraccount.open;

import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OpenCustomerAccountCommand {
    @NotNull(message = "客户号不能为空")
    private String customerNo;

    private Integer accountType = 0;
}
