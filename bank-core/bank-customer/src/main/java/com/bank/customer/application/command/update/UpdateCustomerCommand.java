package com.bank.customer.application.command.update;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateCustomerCommand {
    private Long id;

    @NotBlank(message = "客户姓名不能为空")
    private String customerName;

    private String phone;
    private String address;
}
