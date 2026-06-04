package com.bank.liability.domain.transfer.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("acc_transfer_record")
public class TransferRecord {
    private Long id;
    private String transferNo;
    private String fromAccountNo;
    private String toAccountNo;
    private BigDecimal amount;
    private String fromTransactionNo;
    private String toTransactionNo;
    private String status;
    private String operatorNo;
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public boolean isCancellable() {
        return "NORMAL".equals(this.status);
    }
}
