package com.bank.liability.api;

import com.bank.common.result.Result;
import com.bank.liability.application.batch.AutoRedemptionBatchService;
import com.bank.liability.application.batch.BatchResult;
import com.bank.liability.application.batch.InterestSettlementBatchService;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/liability/batch")
@RequiredArgsConstructor
public class BatchController {

    private final InterestSettlementBatchService interestSettlementBatchService;
    private final AutoRedemptionBatchService autoRedemptionBatchService;

    @PostMapping("/interest-settlement/execute")
    public Result<BatchResult> executeInterestSettlement(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate settlementDate) {
        return Result.success(interestSettlementBatchService.executeBatch(settlementDate));
    }

    @PostMapping("/auto-redemption/execute")
    public Result<BatchResult> executeAutoRedemption(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate processingDate) {
        return Result.success(autoRedemptionBatchService.executeBatch(processingDate));
    }
}
