package com.bank.liability.domain.account.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;import java.time.LocalDateTime;
@Data @TableName("kdaf_lilvdy")
public class AccountRateDefinition {
    private Long id;
    private String accountNo;
    private String liabilityRateType;
    private String rateCode;
    private String rateTermFlag;
    private String rateDetermineDate;
    private String rateAdjustFrequency;
    private String rateUpdateMethod;
    private String source;
    private LocalDateTime createdAt; private LocalDateTime updatedAt;
}