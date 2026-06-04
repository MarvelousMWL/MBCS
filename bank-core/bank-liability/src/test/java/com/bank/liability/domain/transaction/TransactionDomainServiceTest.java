package com.bank.liability.domain.transaction.service;

import com.bank.common.exception.BusinessException;
import com.bank.liability.domain.enums.TransactionStatus;
import com.bank.liability.domain.enums.TransactionType;
import com.bank.liability.domain.transaction.entity.LiabilityTransaction;
import com.bank.liability.domain.transaction.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionDomainServiceTest {

    @Mock private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionDomainService transactionDomainService;

    @Test
    void createTransaction_shouldSucceed_whenAllFieldsValid() {
        LiabilityTransaction result = transactionDomainService.createTransaction(
                "LA001", TransactionType.DEPOSIT, BigDecimal.valueOf(100),
                BigDecimal.valueOf(500), BigDecimal.valueOf(600),
                "OPE001", "姝ｅ父瀛樻", null);

        assertNotNull(result);
        assertNotNull(result.getTransactionNo());
        assertTrue(result.getTransactionNo().startsWith("TX"));
        assertEquals("LA001", result.getLiabilityAccountNo());
        assertEquals(TransactionType.DEPOSIT, result.getTransactionType());
        assertEquals(BigDecimal.valueOf(100), result.getAmount());
        assertEquals(BigDecimal.valueOf(500), result.getBalanceBefore());
        assertEquals(BigDecimal.valueOf(600), result.getBalanceAfter());
        assertEquals("OPE001", result.getOperatorNo());
        assertEquals("姝ｅ父瀛樻", result.getRemark());
        assertNull(result.getRelatedTransactionNo());
        assertEquals(TransactionStatus.NORMAL, result.getStatus());
        assertNotNull(result.getOperateTime());
        assertNotNull(result.getCreatedAt());

        verify(transactionRepository).save(result);
    }

    @Test
    void createTransaction_shouldSaveWithRelatedTransactionNo() {
        transactionDomainService.createTransaction(
                "LA001", TransactionType.DEPOSIT_CANCEL, BigDecimal.valueOf(100),
                BigDecimal.valueOf(600), BigDecimal.valueOf(500),
                "OPE001", "鍐叉", "TX001");

        ArgumentCaptor<LiabilityTransaction> captor = ArgumentCaptor.forClass(LiabilityTransaction.class);
        verify(transactionRepository).save(captor.capture());
        assertEquals("TX001", captor.getValue().getRelatedTransactionNo());
    }

    @Test
    void validateCancel_shouldSucceed_whenTransactionIsNormal() {
        LiabilityTransaction tx = new LiabilityTransaction();
        tx.setTransactionNo("TX001");
        tx.setStatus(TransactionStatus.NORMAL);

        when(transactionRepository.findByTransactionNo("TX001")).thenReturn(Optional.of(tx));

        assertDoesNotThrow(() -> transactionDomainService.validateCancel("TX001"));
        verify(transactionRepository).findByTransactionNo("TX001");
    }

    @Test
    void validateCancel_shouldThrowException_whenTransactionNotExists() {
        when(transactionRepository.findByTransactionNo("TX999")).thenReturn(Optional.empty());
        assertThrows(BusinessException.class, () -> transactionDomainService.validateCancel("TX999"));
    }

    @Test
    void validateCancel_shouldThrowException_whenTransactionAlreadyCancelled() {
        LiabilityTransaction tx = new LiabilityTransaction();
        tx.setTransactionNo("TX001");
        tx.setStatus(TransactionStatus.CANCELLED);

        when(transactionRepository.findByTransactionNo("TX001")).thenReturn(Optional.of(tx));
        assertThrows(BusinessException.class, () -> transactionDomainService.validateCancel("TX001"));
    }

    @Test
    void cancelTransaction_shouldCreateCancelTransaction() {
        LiabilityTransaction original = new LiabilityTransaction();
        original.setTransactionNo("TX001");
        original.setLiabilityAccountNo("LA001");
        original.setTransactionType(TransactionType.DEPOSIT);
        original.setAmount(BigDecimal.valueOf(200));
        original.setBalanceBefore(BigDecimal.ZERO);
        original.setBalanceAfter(BigDecimal.valueOf(200));

        LiabilityTransaction result = transactionDomainService.cancelTransaction(original, "OPE001");

        assertNotNull(result);
        assertEquals(TransactionType.DEPOSIT_CANCEL, result.getTransactionType());
        assertEquals("LA001", result.getLiabilityAccountNo());
        assertEquals(BigDecimal.valueOf(200), result.getAmount());
        assertEquals(BigDecimal.valueOf(200), result.getBalanceBefore());
        assertEquals(BigDecimal.ZERO, result.getBalanceAfter());
        assertEquals("OPE001", result.getOperatorNo());
        assertEquals("TX001", result.getRelatedTransactionNo());

        verify(transactionRepository, times(1)).save(any());
    }
}
