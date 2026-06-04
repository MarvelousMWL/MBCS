package com.bank.liability.application.noticedeposit.open;

import java.math.BigDecimal;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Data;

@Data
public class OpenNoticeDepositCommand {
    @NotBlank(message = "客户账号不能为空")
    private String customerAccountNo;
    @NotBlank(message = "活期结算账号不能为空")
    private String currentAccountNo;
    @NotNull(message = "通知类型不能为空")
    private String noticeType;
    @NotNull(message = "存入金额不能为空")
    @Positive(message = "存入金额必须大于0")
    private BigDecimal amount;
    @NotNull(message = "通知存款利率不能为空")
    private BigDecimal interestRate;
    private BigDecimal currentInterestRate;
}