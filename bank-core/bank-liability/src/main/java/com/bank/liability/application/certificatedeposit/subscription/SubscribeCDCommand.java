package com.bank.liability.application.certificatedeposit.subscription;

import java.math.BigDecimal;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Data;

@Data
public class SubscribeCDCommand {
    @NotBlank(message = "产品编码不能为空")
    private String productCode;
    @NotBlank(message = "客户账号不能为空")
    private String customerAccountNo;
    @NotNull(message = "认购金额不能为空")
    @Positive(message = "认购金额必须大于0")
    private BigDecimal amount;
    private String interestTransferAccount;
    private String operatorNo;
}
