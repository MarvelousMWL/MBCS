package com.bank.liability.application.certificatedeposit.redemption;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RedeemCDCommand {
    @NotBlank(message = "存单账号不能为空")
    private String cdAccountNo;
    private String operatorNo;
    private String redeemType;
}
