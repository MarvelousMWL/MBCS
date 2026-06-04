package com.bank.liability.application.noticedeposit.booking;

import java.math.BigDecimal;
import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Data;

@Data
public class NoticeWithdrawBookingCommand {
    @NotBlank(message = "通知存款账号不能为空")
    private String noticeDepositAccountNo;
    @NotNull(message = "预约金额不能为空")
    @Positive(message = "预约金额必须大于0")
    private BigDecimal bookingAmount;
    private LocalDate bookingDate;
}