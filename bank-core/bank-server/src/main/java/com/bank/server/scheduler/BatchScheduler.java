package com.bank.server.scheduler;

import com.bank.liability.application.batch.BatchOrchestratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BatchScheduler {

    private final BatchOrchestratorService batchOrchestratorService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void runMidnightBatch() {
        log.info("=== Scheduled midnight batch triggered ===");
        batchOrchestratorService.executeAll();
    }
}
