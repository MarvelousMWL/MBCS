package com.bank.liability.domain.liabilityaccount.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.bank.liability.domain.enums.LiabilityAccountStatus;
import com.bank.liability.domain.enums.LiabilityAccountType;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("acc_liability_account")
public class LiabilityAccount {
    private Long id;
    private String liabilityAccountNo;
    private String customerAccountNo;
    private String subAccountSeq;
    @TableField(typeHandler = com.bank.liability.infrastructure.persistence.handler.LiabilityAccountTypeHandler.class)
    private LiabilityAccountType accountType;
    private BigDecimal balance;
    private LiabilityAccountStatus status;
    private LocalDateTime openDate;
    private LocalDateTime closeDate;
    private Integer version;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public boolean isNormal() {
        return LiabilityAccountStatus.NORMAL.equals(this.status);
    }

    public boolean canClose() {
        return this.status == LiabilityAccountStatus.NORMAL && this.balance.compareTo(BigDecimal.ZERO) == 0;
    }

    public void deposit(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }

    public void withdraw(BigDecimal amount) {
        this.balance = this.balance.subtract(amount);
    }
}
