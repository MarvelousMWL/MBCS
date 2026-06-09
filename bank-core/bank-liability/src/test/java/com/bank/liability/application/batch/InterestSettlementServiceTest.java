package com.bank.liability.application.batch;

import com.bank.liability.domain.account.entity.AccountInfo;
import com.bank.liability.domain.account.entity.AccountInterestDetail;
import com.bank.liability.domain.account.repository.AccountInterestDetailRepository;
import com.bank.liability.domain.account.repository.AccountRepository;
import com.bank.liability.domain.batch.repository.BatchRunLogRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InterestSettlementServiceTest {

    @Mock private AccountRepository accountRepository;
    @Mock private AccountInterestDetailRepository interestDetailRepository;
    @Mock private BatchRunLogRepository batchRunLogRepository;
    @InjectMocks private InterestSettlementService service;

    @Test
    void execute_onNonQuarterlyDate_shouldSkip() {
        BatchResult result = service.execute(LocalDate.of(2026, 6, 10), LocalDate.of(2026, 6, 9));
        assertEquals("SKIPPED", result.getStatus());
    }

    @Test
    void execute_onQuarterlyDate_shouldSettle() {
        AccountInfo acc = new AccountInfo();
        acc.setAccountNo("10000001");
        acc.setProductCode("21000001");
        acc.setAccountStatus("0");
        acc.setCurrentBalance(BigDecimal.valueOf(10000));

        AccountInterestDetail detail = new AccountInterestDetail();
        detail.setAccountNo("10000001");
        detail.setDailyInterest(BigDecimal.valueOf(0.10));
        detail.setStatus("ACCRUED");
        detail.setBatchType("DAILY_ACCRUAL");

        when(accountRepository.findAllAccounts()).thenReturn(List.of(acc));
        when(interestDetailRepository.findByStatusAndBatchType("ACCRUED", "DAILY_ACCRUAL"))
                .thenReturn(List.of(detail, detail, detail));

        BatchResult result = service.execute(LocalDate.of(2026, 6, 22), LocalDate.of(2026, 6, 21));

        assertEquals("COMPLETED", result.getStatus());
        verify(accountRepository).updateAccount(any());
    }

    @Test
    void execute_onQuarterlyDateWithNoAccounts_shouldComplete() {
        when(accountRepository.findAllAccounts()).thenReturn(Collections.emptyList());

        BatchResult result = service.execute(LocalDate.of(2026, 6, 22), LocalDate.of(2026, 6, 21));

        assertEquals("COMPLETED", result.getStatus());
        assertEquals(0, result.getTotalItems());
    }
}
