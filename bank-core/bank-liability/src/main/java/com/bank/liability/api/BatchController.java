package com.bank.liability.api;

import com.bank.common.result.Result;
import com.bank.liability.application.batch.AutoRedemptionBatchService;
import com.bank.liability.application.batch.BatchOrchestratorService;
import com.bank.liability.application.batch.BatchResult;
import com.bank.liability.application.batch.DailyAccrualBatchService;
import com.bank.liability.application.batch.FormTransferBatchService;
import com.bank.liability.application.batch.InterestSettlementBatchService;
import com.bank.liability.application.batch.InterestSettlementService;
import com.bank.liability.application.batch.OverdueProcessingBatchService;
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
    private final OverdueProcessingBatchService overdueProcessingBatchService;
    private final FormTransferBatchService formTransferBatchService;
    private final DailyAccrualBatchService dailyAccrualBatchService;
    private final InterestSettlementService interestSettlementService;
    private final BatchOrchestratorService batchOrchestratorService;

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

    @PostMapping("/overdue-processing/execute")
    public Result<BatchResult> executeOverdueProcessing(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate processingDate) {
        return Result.success(overdueProcessingBatchService.executeBatch(processingDate));
    }

    @PostMapping("/form-transfer/execute")
    public Result<BatchResult> executeFormTransfer(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate processingDate) {
        return Result.success(formTransferBatchService.executeBatch(processingDate));
    }

    @PostMapping("/orchestrate")
    public Result<String> orchestrate() {
        batchOrchestratorService.executeAll();
        return Result.success("Batch orchestration triggered");
    }

    @PostMapping("/daily-accrual/execute")
    public Result<BatchResult> executeDailyAccrual(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate calcDate) {
        LocalDate systemDate = LocalDate.now();
        if (calcDate == null) calcDate = systemDate.minusDays(1);
        return Result.success(dailyAccrualBatchService.execute(systemDate, calcDate));
    }

    @PostMapping("/settlement/execute")
    public Result<BatchResult> executeSettlement(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate calcDate) {
        LocalDate systemDate = LocalDate.now();
        if (calcDate == null) calcDate = systemDate.minusDays(1);
        return Result.success(interestSettlementService.execute(systemDate, calcDate));
    }
}
