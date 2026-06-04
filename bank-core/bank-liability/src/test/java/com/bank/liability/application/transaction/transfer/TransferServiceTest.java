package com.bank.liability.application.transaction.transfer;

import com.bank.common.exception.BusinessException;
import com.bank.liability.domain.enums.LiabilityAccountStatus;
import com.bank.liability.domain.enums.LiabilityAccountType;
import com.bank.liability.domain.enums.TransactionType;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {

    @Mock
    private LiabilityAccountRepository liabilityAccountRepository;

    @Mock
    private LiabilityAccountDomainService liabilityAccountDomainService;

    @Mock
    private TransactionDomainService transactionDomainService;

    @Mock
    private TransferRecordRepository transferRecordRepository;

    @InjectMocks
    private TransferService transferService;

    @Test
    void transfer_shouldSucceed_whenBothAccountsAreNormal() {
        LiabilityAccount fromAccount = createNormalAccount("FROM001", BigDecimal.valueOf(1000));
        LiabilityAccount toAccount = createNormalAccount("TO001", BigDecimal.valueOf(500));
        TransferCommand command = createTransferCommand("FROM001", "TO001", BigDecimal.valueOf(200));

        when(liabilityAccountRepository.findByLiabilityAccountNo("FROM001"))
                .thenReturn(Optional.of(fromAccount));
        when(liabilityAccountRepository.findByLiabilityAccountNo("TO001"))
                .thenReturn(Optional.of(toAccount));

        doNothing().when(liabilityAccountDomainService).validateWithdraw(any(LiabilityAccount.class), any(BigDecimal.class));
        doNothing().when(liabilityAccountDomainService).withdraw(any(LiabilityAccount.class), any(BigDecimal.class));
        doNothing().when(liabilityAccountDomainService).deposit(any(LiabilityAccount.class), any(BigDecimal.class));
        doNothing().when(liabilityAccountRepository).update(any(LiabilityAccount.class));

        LiabilityTransaction fromTx = new LiabilityTransaction();
        fromTx.setTransactionNo("TX_FROM_001");
        LiabilityTransaction toTx = new LiabilityTransaction();
        toTx.setTransactionNo("TX_TO_001");

        when(transactionDomainService.createTransaction(
                eq("FROM001"), eq(TransactionType.TRANSFER), eq(BigDecimal.valueOf(200)),
                any(BigDecimal.class), any(BigDecimal.class), eq("OPE001"), eq("\u6b63\u5e38\u8f6c\u8d26"), any()
        )).thenReturn(fromTx);

        when(transactionDomainService.createTransaction(
                eq("TO001"), eq(TransactionType.TRANSFER), eq(BigDecimal.valueOf(200)),
                any(BigDecimal.class), any(BigDecimal.class), eq("OPE001"), eq("\u6b63\u5e38\u8f6c\u8d26"), any()
        )).thenReturn(toTx);

        doNothing().when(transferRecordRepository).save(any(TransferRecord.class));

        TransferRecord result = transferService.transfer(command);

        assertNotNull(result);
        assertEquals("FROM001", result.getFromAccountNo());
        assertEquals("TO001", result.getToAccountNo());
        assertEquals(BigDecimal.valueOf(200), result.getAmount());
        assertEquals("NORMAL", result.getStatus());

        verify(liabilityAccountRepository).findByLiabilityAccountNo("FROM001");
        verify(liabilityAccountRepository).findByLiabilityAccountNo("TO001");
        verify(liabilityAccountDomainService).validateWithdraw(fromAccount, BigDecimal.valueOf(200));
        verify(liabilityAccountDomainService).withdraw(fromAccount, BigDecimal.valueOf(200));
        verify(liabilityAccountDomainService).deposit(toAccount, BigDecimal.valueOf(200));
        verify(liabilityAccountRepository, times(2)).update(any(LiabilityAccount.class));
        verify(transactionDomainService, times(2)).createTransaction(any(), any(), any(), any(), any(), any(), any(), any());
        verify(transferRecordRepository).save(any(TransferRecord.class));
    }

    @Test
    void transfer_shouldThrowException_whenFromAccountNotExist() {
        TransferCommand command = createTransferCommand("NONEXIST", "TO001", BigDecimal.valueOf(100));

        when(liabilityAccountRepository.findByLiabilityAccountNo("NONEXIST"))
                .thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class,
                () -> transferService.transfer(command));
        assertNotNull(exception.getMessage());

        verify(liabilityAccountRepository).findByLiabilityAccountNo("NONEXIST");
        verifyNoMoreInteractions(liabilityAccountRepository);
        verifyNoInteractions(liabilityAccountDomainService, transactionDomainService, transferRecordRepository);
    }

    @Test
    void transfer_shouldThrowException_whenToAccountNotExist() {
        LiabilityAccount fromAccount = createNormalAccount("FROM001", BigDecimal.valueOf(1000));
        TransferCommand command = createTransferCommand("FROM001", "NONEXIST", BigDecimal.valueOf(100));

        when(liabilityAccountRepository.findByLiabilityAccountNo("FROM001"))
                .thenReturn(Optional.of(fromAccount));
        when(liabilityAccountRepository.findByLiabilityAccountNo("NONEXIST"))
                .thenReturn(Optional.empty());

        // validateWithdraw is called before toAccount lookup, so we need to expect it
        doNothing().when(liabilityAccountDomainService).validateWithdraw(fromAccount, BigDecimal.valueOf(100));

        BusinessException exception = assertThrows(BusinessException.class,
                () -> transferService.transfer(command));
        assertNotNull(exception.getMessage());

        verify(liabilityAccountRepository).findByLiabilityAccountNo("FROM001");
        verify(liabilityAccountRepository).findByLiabilityAccountNo("NONEXIST");
        verify(liabilityAccountDomainService).validateWithdraw(fromAccount, BigDecimal.valueOf(100));
        verifyNoMoreInteractions(liabilityAccountDomainService, liabilityAccountRepository);
        verifyNoInteractions(transactionDomainService, transferRecordRepository);
    }

    @Test
    void transfer_shouldThrowException_whenFromAccountStatusNotNormal() {
        LiabilityAccount fromAccount = createNormalAccount("FROM001", BigDecimal.valueOf(1000));
        fromAccount.setStatus(LiabilityAccountStatus.STOPPED);
        TransferCommand command = createTransferCommand("FROM001", "TO001", BigDecimal.valueOf(100));

        when(liabilityAccountRepository.findByLiabilityAccountNo("FROM001"))
                .thenReturn(Optional.of(fromAccount));

        BusinessException exception = assertThrows(BusinessException.class,
                () -> transferService.transfer(command));
        assertNotNull(exception.getMessage());

        verify(liabilityAccountRepository).findByLiabilityAccountNo("FROM001");
        verifyNoMoreInteractions(liabilityAccountRepository);
        verifyNoInteractions(liabilityAccountDomainService, transactionDomainService, transferRecordRepository);
    }

    @Test
    void transfer_shouldThrowException_whenToAccountStatusNotNormal() {
        LiabilityAccount fromAccount = createNormalAccount("FROM001", BigDecimal.valueOf(1000));
        LiabilityAccount toAccount = createNormalAccount("TO001", BigDecimal.valueOf(500));
        toAccount.setStatus(LiabilityAccountStatus.FROZEN);
        TransferCommand command = createTransferCommand("FROM001", "TO001", BigDecimal.valueOf(100));

        when(liabilityAccountRepository.findByLiabilityAccountNo("FROM001"))
                .thenReturn(Optional.of(fromAccount));
        when(liabilityAccountRepository.findByLiabilityAccountNo("TO001"))
                .thenReturn(Optional.of(toAccount));

        // validateWithdraw is called before checking toAccount status
        doNothing().when(liabilityAccountDomainService).validateWithdraw(fromAccount, BigDecimal.valueOf(100));

        BusinessException exception = assertThrows(BusinessException.class,
                () -> transferService.transfer(command));
        assertNotNull(exception.getMessage());

        verify(liabilityAccountRepository).findByLiabilityAccountNo("FROM001");
        verify(liabilityAccountRepository).findByLiabilityAccountNo("TO001");
        verify(liabilityAccountDomainService).validateWithdraw(fromAccount, BigDecimal.valueOf(100));
        verifyNoMoreInteractions(liabilityAccountDomainService, liabilityAccountRepository);
        verifyNoInteractions(transactionDomainService, transferRecordRepository);
    }

    @Test
    void transfer_shouldThrowException_whenAccountsAreSame() {
        TransferCommand command = createTransferCommand("SAME001", "SAME001", BigDecimal.valueOf(100));

        BusinessException exception = assertThrows(BusinessException.class,
                () -> transferService.transfer(command));
        assertNotNull(exception.getMessage());

        verifyNoInteractions(liabilityAccountRepository, liabilityAccountDomainService, transactionDomainService, transferRecordRepository);
    }

    @Test
    void transfer_shouldThrowException_whenInsufficientBalance() {
        LiabilityAccount fromAccount = createNormalAccount("FROM001", BigDecimal.valueOf(50));
        TransferCommand command = createTransferCommand("FROM001", "TO001", BigDecimal.valueOf(100));

        when(liabilityAccountRepository.findByLiabilityAccountNo("FROM001"))
                .thenReturn(Optional.of(fromAccount));

        doThrow(new BusinessException("\u4f59\u989d\u4e0d\u8db3"))
                .when(liabilityAccountDomainService).validateWithdraw(fromAccount, BigDecimal.valueOf(100));

        BusinessException exception = assertThrows(BusinessException.class,
                () -> transferService.transfer(command));
        assertNotNull(exception.getMessage());

        verify(liabilityAccountRepository).findByLiabilityAccountNo("FROM001");
        verify(liabilityAccountDomainService).validateWithdraw(fromAccount, BigDecimal.valueOf(100));
        verifyNoMoreInteractions(liabilityAccountRepository);
        verifyNoInteractions(transactionDomainService, transferRecordRepository);
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

    private TransferCommand createTransferCommand(String from, String to, BigDecimal amount) {
        TransferCommand command = new TransferCommand();
        command.setFromAccountNo(from);
        command.setToAccountNo(to);
        command.setAmount(amount);
        command.setOperatorNo("OPE001");
        command.setRemark("\u6b63\u5e38\u8f6c\u8d26");
        return command;
    }
}
