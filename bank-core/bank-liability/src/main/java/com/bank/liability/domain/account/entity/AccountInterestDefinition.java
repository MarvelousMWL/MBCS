package com.bank.liability.domain.account.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;import java.time.LocalDateTime;
@Data @TableName("kdaf_lixidy")
public class AccountInterestDefinition {
    private Long id;
    private String accountNo;
    private String interestType;
    private String interestFlag;
    private String interestAdjustMethod;
    private String standardInterestMethod;
    private String interestPaymentMethod;
    private String interestFrequency;
    private String paymentFrequency;
    private String taxFlag;
    private LocalDateTime createdAt; private LocalDateTime updatedAt;
}