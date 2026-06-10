package com.bank.liability.application.certificatedeposit.batch;

import com.bank.liability.application.batch.BatchResult;
import com.bank.liability.domain.certificatedeposit.entity.CDMaturityBatchLog;
import com.bank.liability.domain.certificatedeposit.entity.CertificateDepositAccount;
import com.bank.liability.domain.certificatedeposit.repository.CDMaturityBatchLogRepository;
import com.bank.liability.domain.certificatedeposit.repository.CertificateDepositAccountRepository;
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
class CDAutoMaturityBatchServiceTest {

    @Mock
    private CertificateDepositAccountRepository accountRepository;

    @Mock
    private CDMaturityBatchLogRepository batchLogRepository;

    @InjectMocks
    private CDAutoMaturityBatchService service;

    private CertificateDepositAccount createActiveAccount(String cdAccountNo,
                                                           LocalDate maturityDate,
                                                           BigDecimal principal,
                                                           BigDecimal interestRate,
                                                           Integer termMonths) {
        CertificateDepositAccount account = new CertificateDepositAccount();
        account.setCdAccountNo(cdAccountNo);
        account.setProductCode("CD001");
        account.setMaturityDate(maturityDate);
        account.setPrincipal(principal);
        account.setInterestRate(interestRate);
        account.setTermMonths(termMonths);
        account.setStatus("ACTIVE");
        account.setInterest(BigDecimal.ZERO);
        account.setTotalAmount(BigDecimal.ZERO);
        return account;
    }

    @Test
    void executeBatch_shouldReturnCompletedWithNoItems_whenNoMaturedAccounts() {
        when(accountRepository.findAll()).thenReturn(Collections.emptyList());

        CDAutoMaturityBatchCommand command = new CDAutoMaturityBatchCommand();
        command.setProcessingDate(LocalDate.of(2026, 6, 4));
        BatchResult result = service.executeBatch(command);

        assertEquals("COMPLETED", result.getStatus());
        assertEquals(0, result.getTotalItems());
        assertEquals(0, result.getSuccessCount());
        assertEquals(0, result.getFailCount());
        verify(accountRepository, times(1)).findAll();
        verifyNoInteractions(batchLogRepository);
    }

    @Test
    void executeBatch_shouldProcessNormalMaturitySuccessfully() {
        CertificateDepositAccount account = createActiveAccount(
                "CDACC001", LocalDate.of(2026, 6, 4),
                BigDecimal.valueOf(100000), new BigDecimal("2.5"), 12);

        when(accountRepository.findAll()).thenReturn(List.of(account));

        CDAutoMaturityBatchCommand command = new CDAutoMaturityBatchCommand();
        command.setProcessingDate(LocalDate.of(2026, 6, 4));
        BatchResult result = service.executeBatch(command);

        assertEquals(1, result.getTotalItems());
        assertEquals(1, result.getSuccessCount());
        assertEquals(0, result.getFailCount());
        assertEquals("COMPLETED", result.getStatus());

        ArgumentCaptor<CertificateDepositAccount> accountCaptor =
                ArgumentCaptor.forClass(CertificateDepositAccount.class);
        verify(accountRepository).update(accountCaptor.capture());
        CertificateDepositAccount updated = accountCaptor.getValue();
        assertEquals("REDEEMED", updated.getStatus());
        assertNotNull(updated.getRedeemDate());

        // Interest: 100000 * 2.5 * 12 / 1200 = 2500.00
        assertEquals(0, new BigDecimal("2500.00").compareTo(updated.getInterest()));

        ArgumentCaptor<CDMaturityBatchLog> logCaptor =
                ArgumentCaptor.forClass(CDMaturityBatchLog.class);
        verify(batchLogRepository).save(logCaptor.capture());
        CDMaturityBatchLog savedLog = logCaptor.getValue();
        assertEquals("SUCCESS", savedLog.getStatus());
        assertEquals("CDACC001", savedLog.getCdAccountNo());
    }

    @Test
    void executeBatch_shouldApplyDemandRateForOverdueAccount() {
        CertificateDepositAccount account = createActiveAccount(
                "CDACC002", LocalDate.of(2026, 5, 15),
                BigDecimal.valueOf(100000), new BigDecimal("2.5"), 12);

        when(accountRepository.findAll()).thenReturn(List.of(account));

        CDAutoMaturityBatchCommand command = new CDAutoMaturityBatchCommand();
        command.setProcessingDate(LocalDate.of(2026, 6, 4));
        BatchResult result = service.executeBatch(command);

        assertEquals(1, result.getTotalItems());
        assertEquals(1, result.getSuccessCount());
        assertEquals("COMPLETED", result.getStatus());

        ArgumentCaptor<CertificateDepositAccount> accountCaptor =
                ArgumentCaptor.forClass(CertificateDepositAccount.class);
        verify(accountRepository).update(accountCaptor.capture());
        CertificateDepositAccount updated = accountCaptor.getValue();
        assertEquals("REDEEMED", updated.getStatus());

        // Demand rate interest: 100000 * 0.2 * 19(overdue days with plusDays(1)) / 36000 = 10.56
        System.out.println("DEBUG ACTUAL INTEREST: " + updated.getInterest()); assertEquals(0, new BigDecimal("10.56").compareTo(updated.getInterest()));
    }

    @Test
    void executeBatch_shouldHandleExceptionAndRecordFailure() {
        CertificateDepositAccount account1 = createActiveAccount(
                "CDACC003", LocalDate.of(2026, 6, 4),
                BigDecimal.valueOf(50000), new BigDecimal("3.0"), 6);
        CertificateDepositAccount account2 = createActiveAccount(
                "CDACC004", LocalDate.of(2026, 6, 4),
                BigDecimal.valueOf(80000), new BigDecimal("2.75"), 12);

        when(accountRepository.findAll()).thenReturn(List.of(account1, account2));
        doThrow(new RuntimeException("DB error")).when(accountRepository).update(account1);

        CDAutoMaturityBatchCommand command = new CDAutoMaturityBatchCommand();
        command.setProcessingDate(LocalDate.of(2026, 6, 4));
        BatchResult result = service.executeBatch(command);

        assertEquals(2, result.getTotalItems());
        assertEquals(1, result.getSuccessCount());
        assertEquals(1, result.getFailCount());
        assertEquals("PARTIALLY_COMPLETED", result.getStatus());

        ArgumentCaptor<CDMaturityBatchLog> logCaptor =
                ArgumentCaptor.forClass(CDMaturityBatchLog.class);
        verify(batchLogRepository, times(2)).save(logCaptor.capture());
        List<CDMaturityBatchLog> savedLogs = logCaptor.getAllValues();
        assertEquals(2, savedLogs.size());

        assertEquals("FAILED", savedLogs.get(0).getStatus());
        assertEquals("CDACC003", savedLogs.get(0).getCdAccountNo());
        assertEquals("DB error", savedLogs.get(0).getErrorMessage());

        assertEquals("SUCCESS", savedLogs.get(1).getStatus());
        assertEquals("CDACC004", savedLogs.get(1).getCdAccountNo());
    }

    @Test
    void executeBatch_shouldUseDefaultProcessingDate_whenNotProvided() {
        CertificateDepositAccount account = createActiveAccount(
                "CDACC005", LocalDate.now(),
                BigDecimal.valueOf(100000), new BigDecimal("2.0"), 12);

        when(accountRepository.findAll()).thenReturn(List.of(account));

        CDAutoMaturityBatchCommand command = new CDAutoMaturityBatchCommand();
        BatchResult result = service.executeBatch(command);

        assertEquals(1, result.getTotalItems());
        assertEquals(1, result.getSuccessCount());
        assertEquals("COMPLETED", result.getStatus());
        verify(accountRepository).update(any());
    }
}
