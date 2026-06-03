package com.bank.liability.domain.account.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;import java.time.LocalDateTime;
@Data @TableName("kdaf_zhiqkz")
public class AccountWithdrawControl {
    private Long id;
    private String accountNo;
    private String cashWithdrawFlag;
    private String transferWithdrawFlag;
    private String withdrawCtrlMode;
    private String amountCtrlMode;
    private LocalDateTime createdAt; private LocalDateTime updatedAt;
}