package com.bank.liability.application.batch;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BatchOrchestratorService {

    private final DailyAccrualBatchService dailyAccrualBatchService;
    private final InterestSettlementService interestSettlementService;
    private final AutoRedemptionBatchService autoRedemptionBatchService;
    private final OverdueProcessingBatchService overdueProcessingBatchService;
    private final FormTransferBatchService formTransferBatchService;
    private final InterestSettlementBatchService interestSettlementBatchService;

    public void executeAll() {
        LocalDate systemDate = LocalDate.now();
        LocalDate calcDate = systemDate.minusDays(1);

        log.info("");
        log.info("============================================");
        log.info("  MBCS Batch Orchestrator - START");
        log.info("  SystemDate: {}, CalcDate: {}", systemDate, calcDate);
        log.info("============================================");

        long startTime = System.currentTimeMillis();

        try {
            // Step 1: Daily interest accrual
            log.info("");
            log.info("--- Step 1/6: Daily Interest Accrual ---");
            BatchResult r1 = dailyAccrualBatchService.execute(systemDate, calcDate);
            log.info("  Result: status={}, total={}, success={}, fail={}",
                    r1.getStatus(), r1.getTotalItems(), r1.getSuccessCount(), r1.getFailCount());

            // Step 2: Interest settlement (quarterly)
            log.info("");
            log.info("--- Step 2/6: Interest Settlement ---");
            BatchResult r2 = interestSettlementService.execute(systemDate, calcDate);
            log.info("  Result: status={}, total={}, success={}, fail={}",
                    r2.getStatus(), r2.getTotalItems(), r2.getSuccessCount(), r2.getFailCount());

            // Step 3: Auto redemption
            log.info("");
            log.info("--- Step 3/6: Auto Redemption ---");
            BatchResult r3 = autoRedemptionBatchService.executeBatch(calcDate);
            log.info("  Result: status={}, total={}, success={}, fail={}",
                    r3.getStatus(), r3.getTotalItems(), r3.getSuccessCount(), r3.getFailCount());

            // Step 4: Overdue processing
            log.info("");
            log.info("--- Step 4/6: Overdue Processing ---");
            BatchResult r4 = overdueProcessingBatchService.executeBatch(calcDate);
            log.info("  Result: status={}, total={}, success={}, fail={}",
                    r4.getStatus(), r4.getTotalItems(), r4.getSuccessCount(), r4.getFailCount());

            // Step 5: Form transfer
            log.info("");
            log.info("--- Step 5/6: Form Transfer ---");
            BatchResult r5 = formTransferBatchService.executeBatch(calcDate);
            log.info("  Result: status={}, total={}, success={}, fail={}",
                    r5.getStatus(), r5.getTotalItems(), r5.getSuccessCount(), r5.getFailCount());

            // Step 6: Legacy interest settlement batch (for pre-inserted items)
            log.info("");
            log.info("--- Step 6/6: Legacy Interest Settlement Batch ---");
            BatchResult r6 = interestSettlementBatchService.executeBatch(calcDate);
            log.info("  Result: status={}, total={}, success={}, fail={}",
                    r6.getStatus(), r6.getTotalItems(), r6.getSuccessCount(), r6.getFailCount());

            long elapsed = System.currentTimeMillis() - startTime;
            log.info("");
            log.info("============================================");
            log.info("  MBCS Batch Orchestrator - COMPLETE");
            log.info("  Elapsed: {}ms", elapsed);
            log.info("============================================");

        } catch (Exception e) {
            log.error("Batch orchestrator failed: {}", e.getMessage(), e);
        }
    }
}
