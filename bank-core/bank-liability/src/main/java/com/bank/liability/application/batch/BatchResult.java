package com.bank.liability.application.batch;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchResult {
    private String batchNo;
    private int totalItems;
    private int successCount;
    private int failCount;
    private String status;
    private String message;
}
