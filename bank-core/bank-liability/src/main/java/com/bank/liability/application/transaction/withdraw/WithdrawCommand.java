package com.bank.liability.application.transaction.withdraw;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class WithdrawCommand {
    @NotBlank(message = "负债账号不能为空")
    private String liabilityAccountNo;

    @NotNull(message = "取款金额不能为空")
    @Positive(message = "取款金额必须大于0")
    private BigDecimal amount;

    private String operatorNo;

    private String remark;
}
