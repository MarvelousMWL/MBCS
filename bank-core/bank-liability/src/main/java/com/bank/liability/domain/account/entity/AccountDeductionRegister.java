package com.bank.liability.domain.account.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;import java.time.LocalDateTime;
@Data @TableName("kdaf_kouhdj")
public class AccountDeductionRegister {
    private Long id;
    private String deductionNo;
    private String deductionMethod;
    private String freezeNo;
    private String customerAccount;
    private String accountNo;
    private LocalDateTime createdAt; private LocalDateTime updatedAt;
}