package com.bank.liability.domain.customersubaccount.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bank.liability.domain.enums.SubAccountStatus;
import com.bank.liability.domain.enums.LiabilityAccountType;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("acc_customer_sub_account")
public class CustomerSubAccount {
    private Long id;
    private String customerAccountNo;
    private String subAccountSeq;
    private String liabilityAccountNo;
    @TableField(typeHandler = com.bank.liability.infrastructure.persistence.handler.LiabilityAccountTypeHandler.class)
    private LiabilityAccountType accountType;
    private SubAccountStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
