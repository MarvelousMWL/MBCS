package com.bank.liability.domain.account.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;import java.time.LocalDateTime;
@Data @TableName("kdaf_yefsmx")
public class BalanceTransactionDetail {
    private Long id;
    private String accountNo;
    private String customerAccountType;
    private String balanceFieldName;
    private LocalDateTime createdAt; private LocalDateTime updatedAt;
}