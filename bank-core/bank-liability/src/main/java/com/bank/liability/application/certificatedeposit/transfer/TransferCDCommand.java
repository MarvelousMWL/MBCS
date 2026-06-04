package com.bank.liability.application.certificatedeposit.transfer;

import java.math.BigDecimal;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Data;

@Data
public class TransferCDCommand {
    @NotBlank(message = "存单账号不能为空")
    private String cdAccountNo;

    @NotBlank(message = "转出人账号不能为空")
    private String transferorAccountNo;

    @NotBlank(message = "转入人账号不能为空")
    private String transfereeAccountNo;

    @NotNull(message = "转让价格不能为空")
    @Positive(message = "转让价格必须大于0")
    private BigDecimal transferPrice;

    @NotBlank(message = "定价方式不能为空")
    private String pricingType;

    private BigDecimal handlingFee;

    private String operatorNo;
}
