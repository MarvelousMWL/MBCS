package com.bank.liability.domain.transaction.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bank.liability.domain.enums.TransactionStatus;
import com.bank.liability.domain.enums.TransactionType;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("acc_liability_transaction")
public class LiabilityTransaction {
    private Long id;
    private String transactionNo;
    private String liabilityAccountNo;
    private TransactionType transactionType;
    private BigDecimal amount;
    private BigDecimal balanceBefore;
    private BigDecimal balanceAfter;
    private LocalDateTime operateTime;
    private String operatorNo;
    private String remark;
    private String relatedTransactionNo;
    private TransactionStatus status;
    private LocalDateTime createdAt;

    public boolean isNormal() {
        return TransactionStatus.NORMAL.equals(this.status);
    }

    public boolean isCancellable() {
        return this.status == TransactionStatus.NORMAL;
    }
}
