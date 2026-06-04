package com.bank.liability.domain.customeraccount.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.bank.liability.domain.enums.CustomerAccountStatus;
import com.bank.liability.domain.enums.CustomerAccountType;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("acc_customer_account")
public class CustomerAccount {
    private Long id;
    private String customerAccountNo;
    private String customerNo;
    @TableField(typeHandler = com.bank.liability.infrastructure.persistence.handler.CustomerAccountTypeHandler.class)
    private CustomerAccountType accountType;
    private CustomerAccountStatus status;
    private LocalDateTime openDate;
    private LocalDateTime closeDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public boolean isNormal() {
        return CustomerAccountStatus.NORMAL.equals(this.status);
    }

    public boolean canClose() {
        return this.status == CustomerAccountStatus.NORMAL;
    }
}
