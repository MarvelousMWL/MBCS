package com.bank.customer.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bank.customer.domain.enums.CustomerStatus;
import com.bank.customer.domain.enums.IdType;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("crm_customer")
public class Customer {
    private Long id;
    private String customerNo;
    private String customerName;
    @TableField(typeHandler = com.bank.customer.infrastructure.persistence.handler.IdTypeHandler.class)
    private IdType idType;
    private String idNumber;
    private String phone;
    private String address;
    private CustomerStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public boolean isNormal() {
        return CustomerStatus.NORMAL.equals(this.status);
    }

    public boolean canOperate() {
        return this.status == CustomerStatus.NORMAL;
    }
}
