package com.bank.liability.application.liabilityaccount.open;

import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OpenLiabilityAccountCommand {
    @NotNull(message = "客户账号不能为空")
    private String customerAccountNo;

    @NotNull(message = "负债类型不能为空")
    private Integer accountType = 0;
}
