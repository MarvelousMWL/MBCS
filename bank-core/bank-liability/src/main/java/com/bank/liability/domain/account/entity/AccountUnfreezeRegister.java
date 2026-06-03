package com.bank.liability.domain.account.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;import java.time.LocalDateTime;
@Data @TableName("kdaf_jiedj")
public class AccountUnfreezeRegister {
    private Long id;
    private String freezeNo;
    private String accountNo;
    private LocalDateTime createdAt; private LocalDateTime updatedAt;
}