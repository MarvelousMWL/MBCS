package com.bank.liability.application.noticedeposit.withdraw;

import java.math.BigDecimal;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NoticeWithdrawCommand {
    @NotBlank(message = "通知存款账号不能为空")
    private String noticeDepositAccountNo;
}