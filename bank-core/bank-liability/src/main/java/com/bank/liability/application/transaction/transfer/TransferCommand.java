package com.bank.liability.application.transaction.transfer;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
public class TransferCommand {
    @NotBlank(message = "\u8f6c\u51fa\u8d26\u6237\u4e0d\u80fd\u4e3a\u7a7a")
    private String fromAccountNo;

    @NotBlank(message = "\u8f6c\u5165\u8d26\u6237\u4e0d\u80fd\u4e3a\u7a7a")
    private String toAccountNo;

    @NotNull(message = "\u8f6c\u8d26\u91d1\u989d\u4e0d\u80fd\u4e3a\u7a7a")
    @Positive(message = "\u8f6c\u8d26\u91d1\u989d\u5fc5\u987b\u5927\u4e8e0")
    private BigDecimal amount;

    private String operatorNo;
    private String remark;
}
