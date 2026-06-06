package com.bank.liability.application.transaction.transfer;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class TransferCancelCommand {
    @NotBlank(message = "原转账流水号不能为空")
    private String originalTransferNo;

    private String operatorNo;
    private String remark;
}
