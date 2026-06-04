package com.bank.liability.domain.certificatedeposit.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("cd_account")
public class CertificateDepositAccount {
    private Long id;
    private String cdAccountNo;
    private String customerAccountNo;
    private String productCode;
    private BigDecimal principal;
    private BigDecimal interest;
    private BigDecimal totalAmount;
    private String status;
    private LocalDate subscribeDate;
    private LocalDate maturityDate;
    private LocalDate redeemDate;
    private String interestTransferAccount;
    private BigDecimal interestRate;
    private Integer termMonths;
    private Integer version;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public boolean isActive() {
        return "ACTIVE".equals(this.status);
    }

    public boolean isRedeemed() {
        return "REDEEMED".equals(this.status);
    }

    public boolean isFrozen() {
        return "FROZEN".equals(this.status);
    }

    public boolean isPledged() {
        return "PLEDGED".equals(this.status);
    }

    public boolean isLost() {
        return "LOST".equals(this.status);
    }

    public boolean isAbnormalStatus() {
        return isFrozen() || isPledged() || isLost();
    }

    public BigDecimal calculateInterest() {
        return principal.multiply(interestRate).multiply(BigDecimal.valueOf(termMonths))
            .divide(BigDecimal.valueOf(1200), 2, BigDecimal.ROUND_HALF_UP);
    }
}
