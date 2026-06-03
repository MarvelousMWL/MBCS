package com.bank.liability.domain.customeraccount.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bank.liability.domain.enums.CustomerAccountStatus;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("acc_customer_account")
public class CustomerAccount {
    private Long id;
    private String customerAccountNo;
    private String customerNo;
    private String accountType;
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
