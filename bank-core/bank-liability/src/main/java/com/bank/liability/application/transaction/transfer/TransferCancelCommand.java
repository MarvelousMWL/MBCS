package com.bank.liability.application.transaction.transfer;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class TransferCancelCommand {
    @NotBlank(message = "\u539f\u8f6c\u8d26\u6d41\u6c34\u53f7\u4e0d\u80fd\u4e3a\u7a7a")
    private String originalTransferNo;

    private String operatorNo;
    private String remark;
}
