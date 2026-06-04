package com.bank.liability.domain.noticedeposit.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("notice_deposit_account")
public class NoticeDepositAccount {
    private Long id;
    private String noticeDepositAccountNo;
    private String customerAccountNo;
    private String currentAccountNo;
    private BigDecimal principal;
    private BigDecimal interest;
    private BigDecimal totalAmount;
    private String noticeType;
    private String status;
    private LocalDate openDate;
    private LocalDate maturityDate;
    private BigDecimal interestRate;
    private BigDecimal currentInterestRate;
    private Integer version;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public boolean isActive() {
        return "ACTIVE".equals(this.status);
    }

    public boolean isWithdrawn() {
        return "WITHDRAWN".equals(this.status);
    }

    public BigDecimal calculateInterest() {
        return principal.multiply(interestRate).multiply(BigDecimal.valueOf(getNoticeDays()))
            .divide(BigDecimal.valueOf(36500), 2, BigDecimal.ROUND_HALF_UP);
    }

    public int getNoticeDays() {
        return "DAY1".equals(this.noticeType) ? 1 : 7;
    }
}