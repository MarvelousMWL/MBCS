package com.bank.liability.domain.customersubaccount.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bank.liability.domain.enums.SubAccountStatus;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("acc_customer_sub_account")
public class CustomerSubAccount {
    private Long id;
    private String customerAccountNo;
    private String subAccountSeq;
    private String liabilityAccountNo;
    private String accountType;
    private SubAccountStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
