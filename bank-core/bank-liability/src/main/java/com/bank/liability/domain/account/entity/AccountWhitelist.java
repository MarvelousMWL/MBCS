package com.bank.liability.domain.account.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;import java.time.LocalDateTime;
@Data @TableName("kdaf_bmd")
public class AccountWhitelist {
    private Long id;
    private Integer seqNo;
    private String customerAccount;
    private String accountNo;
    private String counterFinInstitutionName;
    private String counterFinInstitutionCode;
    private String counterName;
    private String counterAccount;
    private LocalDateTime createdAt; private LocalDateTime updatedAt;
}