package com.bank.liability.domain.noticedeposit.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("notice_deposit_booking")
public class NoticeDepositBooking {
    private Long id;
    private String noticeDepositAccountNo;
    private LocalDate bookingDate;
    private BigDecimal bookingAmount;
    private LocalDate expectedWithdrawDate;
    private String status;
    private Integer version;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public boolean isPending() {
        return "PENDING".equals(this.status);
    }

    public boolean isExecuted() {
        return "EXECUTED".equals(this.status);
    }

    public boolean isCancelled() {
        return "CANCELLED".equals(this.status);
    }
}