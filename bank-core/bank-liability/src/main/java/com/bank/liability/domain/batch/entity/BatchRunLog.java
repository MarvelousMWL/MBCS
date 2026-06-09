package com.bank.liability.domain.batch.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("kdpb_batch_run_log")
public class BatchRunLog {
    private Long id;
    private String batchNo;
    private String batchType;
    private LocalDate systemDate;
    private LocalDate calcDate;
    private Integer totalAccounts;
    private Integer successCount;
    private Integer failCount;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private String errorMessage;
    private LocalDateTime createdAt;
}
