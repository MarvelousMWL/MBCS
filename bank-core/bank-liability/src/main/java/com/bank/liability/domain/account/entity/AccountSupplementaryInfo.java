package com.bank.liability.domain.account.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;import java.time.LocalDateTime;
@Data @TableName("kdaf_xxbc")
public class AccountSupplementaryInfo {
    private Long id;
    private String accountNo;
    private String holderNature;
    private String holderCreditCode;
    private String holderIdType;
    private String holderIdNo;
    private String holderLegalPerson;
    private LocalDateTime createdAt; private LocalDateTime updatedAt;
}