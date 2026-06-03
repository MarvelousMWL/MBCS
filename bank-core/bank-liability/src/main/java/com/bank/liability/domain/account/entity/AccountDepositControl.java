package com.bank.liability.domain.account.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;import java.time.LocalDateTime;
@Data @TableName("kdaf_cunrkz")
public class AccountDepositControl {
    private Long id;
    private String accountNo;
    private String cashDepositFlag;
    private String transferDepositFlag;
    private String depositCtrlMode;
    private String depositCtrlMethod;
    private String amountCtrlMode;
    private LocalDateTime createdAt; private LocalDateTime updatedAt;
}