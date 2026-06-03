package com.bank.customer.application.command.create;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateCustomerCommand {
    @NotBlank(message = "客户姓名不能为空")
    private String customerName;

    @NotBlank(message = "证件类型不能为空")
    private String idType;

    @NotBlank(message = "证件号码不能为空")
    private String idNumber;

    private String phone;
    private String address;
}
