package com.bank.liability.application.liabilityaccount.freeze;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class FreezeLiabilityAccountCommand {
    @NotBlank(message = "负债账号不能为空")
    private String liabilityAccountNo;
    private String reason;
}
