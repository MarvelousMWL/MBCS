package com.bank.liability.domain.account.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;import java.time.LocalDateTime;
@Data @TableName("kdaf_daoqkz")
public class AccountMaturityDefinition {
    private Long id;
    private String accountNo;
    private String accountInstitution;
    private LocalDateTime createdAt; private LocalDateTime updatedAt;
}