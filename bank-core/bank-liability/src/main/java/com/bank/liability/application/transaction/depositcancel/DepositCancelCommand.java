package com.bank.liability.application.transaction.depositcancel;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DepositCancelCommand {
    @NotBlank(message = "原交易流水号不能为空")
    private String originalTransactionNo;

    private String operatorNo;

    private String remark;
}
