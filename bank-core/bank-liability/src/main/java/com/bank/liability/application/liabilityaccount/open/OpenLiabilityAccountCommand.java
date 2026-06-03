package com.bank.liability.application.liabilityaccount.open;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OpenLiabilityAccountCommand {
    @NotBlank(message = "客户账号不能为空")
    private String customerAccountNo;

    @NotBlank(message = "负债类型不能为空")
    private String accountType = "DEMAND";
}
