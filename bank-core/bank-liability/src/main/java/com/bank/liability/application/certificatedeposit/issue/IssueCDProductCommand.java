package com.bank.liability.application.certificatedeposit.issue;

import java.math.BigDecimal;
import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Data;

@Data
public class IssueCDProductCommand {
    @NotBlank(message = "产品编码不能为空")
    private String productCode;
    @NotBlank(message = "产品名称不能为空")
    private String productName;
    @NotNull(message = "总额度不能为空")
    @Positive(message = "总额度必须大于0")
    private BigDecimal totalQuota;
    @NotNull(message = "最低认购金额不能为空")
    @Positive(message = "最低认购金额必须大于0")
    private BigDecimal minSubscriptionAmount;
    private BigDecimal maxSubscriptionAmountPerCustomer;
    @NotNull(message = "期限月数不能为空")
    @Positive(message = "期限月数必须大于0")
    private Integer termMonths;
    @NotNull(message = "利率不能为空")
    private BigDecimal interestRate;
    @NotNull(message = "发行开始日期不能为空")
    private LocalDate issueStartDate;
    @NotNull(message = "发行结束日期不能为空")
    private LocalDate issueEndDate;
    private String customerType;
    private String currency;
}
