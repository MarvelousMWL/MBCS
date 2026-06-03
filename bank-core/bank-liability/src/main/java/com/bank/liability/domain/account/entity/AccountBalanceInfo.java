package com.bank.liability.domain.account.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;import java.time.LocalDateTime;
@Data @TableName("kdaf_yexx")
public class AccountBalanceInfo {
    private Long id;
    private String accountNo;
    private LocalDateTime createdAt; private LocalDateTime updatedAt;
}