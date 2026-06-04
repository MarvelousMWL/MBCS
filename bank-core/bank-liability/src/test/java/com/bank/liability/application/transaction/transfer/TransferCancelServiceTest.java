package com.bank.liability.application.transaction.transfer;

import com.bank.common.exception.BusinessException;
import com.bank.liability.domain.enums.LiabilityAccountStatus;
import com.bank.liability.domain.enums.LiabilityAccountType;
import com.bank.liability.domain.liabilityaccount.entity.LiabilityAccount;
import com.bank.liability.domain.liabilityaccount.repository.LiabilityAccountRepository;
import com.bank.liability.domain.liabilityaccount.service.LiabilityAccountDomainService;
import com.bank.liability.domain.transaction.entity.LiabilityTransaction;
import com.bank.liability.domain.transaction.service.TransactionDomainService;
import com.bank.liability.domain.transfer.entity.TransferRecord;
import com.bank.liability.domain.transfer.repository.TransferRecordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferCancelServiceTest {

    @Mock
    private TransferRecordRepository transferRecordRepository;

    @Mock
    private LiabilityAccountRepository liabilityAccountRepository;

    @Mock
    private LiabilityAccountDomainService liabilityAccountDomainService;

    @Mock
    private TransactionDomainService transactionDomainService;

    @InjectMocks
    private TransferCancelService transferCancelService;

    @Test
    void cancel_shouldSucceed_whenTransferIsNormal() {
        TransferRecord record = createNormalTransferRecord("TF001", "FROM001", "TO001", BigDecimal.valueOf(200));
        TransferCancelCommand command = createCancelCommand("TF001");

        LiabilityAccount fromAccount = createNormalAccount("FROM001", BigDecimal.valueOf(800));
        LiabilityAccount toAccount = createNormalAccount("TO001", BigDecimal.valueOf(700));

        when(transferRecordRepository.findByTransferNo("TF001"))
                .thenReturn(Optional.of(record));
        when(liabilityAccountRepository.findByLiabilityAccountNo("TO001"))
                .thenReturn(Optional.of(toAccount));
        when(liabilityAccountRepository.findByLiabilityAccountNo("FROM001"))
                .thenReturn(Optional.of(fromAccount));

        doNothing().when(liabilityAccountDomainService).validateWithdraw(any(LiabilityAccount.class), any(BigDecimal.class));
        doNothing().when(liabilityAccountDomainService).withdraw(any(LiabilityAccount.class), any(BigDecimal.class));
        doNothing().when(liabilityAccountDomainService).deposit(any(LiabilityAccount.class), any(BigDecimal.class));
        doNothing().when(liabilityAccountRepository).update(any(LiabilityAccount.class));
        // createTransaction returns LiabilityTransaction, not void
        when(transactionDomainService.createTransaction(any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(new LiabilityTransaction());
        doNothing().when(transferRecordRepository).update(any(TransferRecord.class));

        TransferRecord result = transferCancelService.cancel(command);

        assertNotNull(result);
        assertEquals("CANCELLED", result.getStatus());

        verify(transferRecordRepository).findByTransferNo("TF001");
        verify(liabilityAccountRepository).findByLiabilityAccountNo("TO001");
        verify(liabilityAccountRepository).findByLiabilityAccountNo("FROM001");
        verify(liabilityAccountDomainService).validateWithdraw(toAccount, BigDecimal.valueOf(200));
        verify(liabilityAccountDomainService).withdraw(toAccount, BigDecimal.valueOf(200));
        verify(liabilityAccountDomainService).deposit(fromAccount, BigDecimal.valueOf(200));
        verify(liabilityAccountRepository, times(2)).update(any(LiabilityAccount.class));
        verify(transactionDomainService, times(2)).createTransaction(any(), any(), any(), any(), any(), any(), any(), any());
        verify(transferRecordRepository).update(any(TransferRecord.class));
    }

    @Test
    void cancel_shouldThrowException_whenTransferRecordNotExist() {
        TransferCancelCommand command = createCancelCommand("NONEXIST");

        when(transferRecordRepository.findByTransferNo("NONEXIST"))
                .thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class,
                () -> transferCancelService.cancel(command));
        assertNotNull(exception.getMessage());

        verify(transferRecordRepository).findByTransferNo("NONEXIST");
        verifyNoInteractions(liabilityAccountRepository, liabilityAccountDomainService, transactionDomainService);
    }

    @Test
    void cancel_shouldThrowException_whenTransferAlreadyCancelled() {
        TransferRecord record = createNormalTransferRecord("TF001", "FROM001", "TO001", BigDecimal.valueOf(200));
        record.setStatus("CANCELLED");
        TransferCancelCommand command = createCancelCommand("TF001");

        when(transferRecordRepository.findByTransferNo("TF001"))
                .thenReturn(Optional.of(record));

        BusinessException exception = assertThrows(BusinessException.class,
                () -> transferCancelService.cancel(command));
        assertNotNull(exception.getMessage());

        verify(transferRecordRepository).findByTransferNo("TF001");
        verifyNoInteractions(liabilityAccountRepository, liabilityAccountDomainService, transactionDomainService);
    }

    @Test
    void cancel_shouldThrowException_whenToAccountNotNormal() {
        TransferRecord record = createNormalTransferRecord("TF001", "FROM001", "TO001", BigDecimal.valueOf(200));
        TransferCancelCommand command = createCancelCommand("TF001");

        LiabilityAccount toAccount = createNormalAccount("TO001", BigDecimal.valueOf(700));
        toAccount.setStatus(LiabilityAccountStatus.FROZEN);

        when(transferRecordRepository.findByTransferNo("TF001"))
                .thenReturn(Optional.of(record));
        when(liabilityAccountRepository.findByLiabilityAccountNo("TO001"))
                .thenReturn(Optional.of(toAccount));

        BusinessException exception = assertThrows(BusinessException.class,
                () -> transferCancelService.cancel(command));
        assertNotNull(exception.getMessage());

        verify(transferRecordRepository).findByTransferNo("TF001");
        verify(liabilityAccountRepository).findByLiabilityAccountNo("TO001");
        verifyNoMoreInteractions(liabilityAccountRepository);
        verifyNoInteractions(liabilityAccountDomainService, transactionDomainService);
    }

    private TransferRecord createNormalTransferRecord(String transferNo, String from, String to, BigDecimal amount) {
        TransferRecord record = new TransferRecord();
        record.setId(1L);
        record.setTransferNo(transferNo);
        record.setFromAccountNo(from);
        record.setToAccountNo(to);
        record.setAmount(amount);
        record.setFromTransactionNo("TX_FROM_001");
        record.setToTransactionNo("TX_TO_001");
        record.setStatus("NORMAL");
        record.setOperatorNo("OPE001");
        record.setCreatedAt(LocalDateTime.now());
        record.setUpdatedAt(LocalDateTime.now());
        return record;
    }

    private TransferCancelCommand createCancelCommand(String originalTransferNo) {
        TransferCancelCommand command = new TransferCancelCommand();
        command.setOriginalTransferNo(originalTransferNo);
        command.setOperatorNo("OPE001");
        command.setRemark("\u8f6c\u8d26\u51b2\u6b63");
        return command;
    }

    private LiabilityAccount createNormalAccount(String accountNo, BigDecimal balance) {
        LiabilityAccount account = new LiabilityAccount();
        account.setId(1L);
        account.setLiabilityAccountNo(accountNo);
        account.setCustomerAccountNo("CACC001");
        account.setAccountType(LiabilityAccountType.DEMAND);
        account.setBalance(balance);
        account.setStatus(LiabilityAccountStatus.NORMAL);
        return account;
    }
}
