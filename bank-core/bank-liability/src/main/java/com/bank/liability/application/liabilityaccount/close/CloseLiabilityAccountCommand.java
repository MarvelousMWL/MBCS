package com.bank.liability.application.liabilityaccount.close;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CloseLiabilityAccountCommand {
    @NotBlank(message = "负债账号不能为空")
    private String liabilityAccountNo;
}
