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
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DailyAccrualBatchServiceTest {

    @Mock private AccountRepository accountRepository;
    @Mock private AccountInterestDetailRepository interestDetailRepository;
    @Mock private BatchRunLogRepository batchRunLogRepository;
    @InjectMocks private DailyAccrualBatchService service;

    @Test
    void execute_withNoAccounts_shouldCompleteSuccessfully() {
        when(accountRepository.findAllAccounts()).thenReturn(Collections.emptyList());

        BatchResult result = service.execute(LocalDate.of(2026, 6, 11), LocalDate.of(2026, 6, 10));

        assertEquals("COMPLETED", result.getStatus());
        assertEquals(0, result.getTotalItems());
        assertEquals(0, result.getSuccessCount());
        assertEquals(0, result.getFailCount());
    }

    @Test
    void execute_withActiveAccounts_shouldAccrueInterest() {
        AccountInfo acc = new AccountInfo();
        acc.setAccountNo("10000001");
        acc.setProductCode("21000001");
        acc.setAccountStatus("0");
        acc.setCurrentBalance(BigDecimal.valueOf(10000));
        acc.setLastDayBalance(BigDecimal.valueOf(10000));
        acc.setBalanceUpdateDate(LocalDate.of(2026, 6, 10));

        when(accountRepository.findAllAccounts()).thenReturn(List.of(acc));

        BatchResult result = service.execute(LocalDate.of(2026, 6, 11), LocalDate.of(2026, 6, 10));

        assertEquals(1, result.getTotalItems());
        assertEquals(1, result.getSuccessCount());
        assertEquals(0, result.getFailCount());

        ArgumentCaptor<AccountInterestDetail> captor = ArgumentCaptor.forClass(AccountInterestDetail.class);
        verify(interestDetailRepository).save(captor.capture());
        AccountInterestDetail saved = captor.getValue();

        assertEquals("10000001", saved.getAccountNo());
        assertEquals("21000001", saved.getProductCode());
        assertEquals(LocalDate.of(2026, 6, 10), saved.getCalcDate());
        assertEquals("DAILY_ACCRUAL", saved.getBatchType());
        assertEquals("ACCRUED", saved.getStatus());
        // 10000 * 0.2 / 100 / 360 = 0.05555... ≈ 0.0555556
        assertEquals(0, BigDecimal.valueOf(0.0555556).compareTo(saved.getDailyInterest()));
    }

    @Test
    void execute_withBalanceUpdatedToday_shouldUseLastDayBalance() {
        AccountInfo acc = new AccountInfo();
        acc.setAccountNo("10000001");
        acc.setProductCode("21000001");
        acc.setAccountStatus("0");
        acc.setCurrentBalance(BigDecimal.valueOf(50000));
        acc.setLastDayBalance(BigDecimal.valueOf(20000));
        acc.setBalanceUpdateDate(LocalDate.of(2026, 6, 11)); // updated today

        when(accountRepository.findAllAccounts()).thenReturn(List.of(acc));

        service.execute(LocalDate.of(2026, 6, 11), LocalDate.of(2026, 6, 10));

        ArgumentCaptor<AccountInterestDetail> captor = ArgumentCaptor.forClass(AccountInterestDetail.class);
        verify(interestDetailRepository).save(captor.capture());
        AccountInterestDetail saved = captor.getValue();

        assertEquals("LAST_DAY", saved.getBalanceType());
        assertEquals(0, BigDecimal.valueOf(20000).compareTo(saved.getCalcBalance()));
    }

    @Test
    void execute_withBalanceNotUpdated_shouldUseCurrentBalance() {
        AccountInfo acc = new AccountInfo();
        acc.setAccountNo("10000001");
        acc.setProductCode("21000001");
        acc.setAccountStatus("0");
        acc.setCurrentBalance(BigDecimal.valueOf(20000));
        acc.setLastDayBalance(BigDecimal.valueOf(0));
        acc.setBalanceUpdateDate(LocalDate.of(2026, 6, 9)); // not updated today

        when(accountRepository.findAllAccounts()).thenReturn(List.of(acc));

        service.execute(LocalDate.of(2026, 6, 11), LocalDate.of(2026, 6, 10));

        ArgumentCaptor<AccountInterestDetail> captor = ArgumentCaptor.forClass(AccountInterestDetail.class);
        verify(interestDetailRepository).save(captor.capture());
        AccountInterestDetail saved = captor.getValue();

        assertEquals("CURRENT", saved.getBalanceType());
        assertEquals(0, BigDecimal.valueOf(20000).compareTo(saved.getCalcBalance()));
    }

    @Test
    void execute_withNegativeBalance_shouldSkipInterest() {
        AccountInfo acc = new AccountInfo();
        acc.setAccountNo("10000001");
        acc.setProductCode("21000001");
        acc.setAccountStatus("0");
        acc.setCurrentBalance(BigDecimal.valueOf(-100));
        acc.setLastDayBalance(BigDecimal.valueOf(-100));
        acc.setBalanceUpdateDate(LocalDate.of(2026, 6, 10));

        when(accountRepository.findAllAccounts()).thenReturn(List.of(acc));

        service.execute(LocalDate.of(2026, 6, 11), LocalDate.of(2026, 6, 10));

        ArgumentCaptor<AccountInterestDetail> captor = ArgumentCaptor.forClass(AccountInterestDetail.class);
        verify(interestDetailRepository).save(captor.capture());
        AccountInterestDetail saved = captor.getValue();
        assertEquals(BigDecimal.ZERO, saved.getDailyInterest());
    }
}
