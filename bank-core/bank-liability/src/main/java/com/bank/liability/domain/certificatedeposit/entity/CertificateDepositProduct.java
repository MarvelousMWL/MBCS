package com.bank.liability.domain.certificatedeposit.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("cd_product")
public class CertificateDepositProduct {
    private Long id;
    private String productCode;
    private String productName;
    private BigDecimal totalQuota;
    private BigDecimal remainingQuota;
    private BigDecimal minSubscriptionAmount;
    private BigDecimal maxSubscriptionAmountPerCustomer;
    private Integer termMonths;
    private BigDecimal interestRate;
    private LocalDate issueStartDate;
    private LocalDate issueEndDate;
    private LocalDate maturityDate;
    private String status;
    private String customerType;
    private String currency;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public boolean isInIssuePeriod() {
        LocalDate today = LocalDate.now();
        return (issueStartDate == null || !today.isBefore(issueStartDate))
            && (issueEndDate == null || !today.isAfter(issueEndDate));
    }

    public boolean hasSufficientQuota(BigDecimal amount) {
        return remainingQuota != null && remainingQuota.compareTo(amount) >= 0;
    }

    public void deductQuota(BigDecimal amount) {
        this.remainingQuota = this.remainingQuota.subtract(amount);
    }
}
