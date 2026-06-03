package com.bank.liability.domain.account.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;import java.time.LocalDateTime;
@Data @TableName("kdaf_dongj")
public class AccountFreezeRegister {
    private Long id;
    private String freezeNo;
    private String freezeOperationFlag;
    private LocalDateTime createdAt; private LocalDateTime updatedAt;
}