package com.bank.liability.application.transaction.withdrawcancel;

import com.bank.common.exception.BusinessException;
import com.bank.liability.domain.enums.LiabilityAccountStatus;
import com.bank.liability.domain.enums.TransactionType;
import com.bank.liability.domain.liabilityaccount.entity.LiabilityAccount;
import com.bank.liability.domain.liabilityaccount.repository.LiabilityAccountRepository;
import com.bank.liability.domain.liabilityaccount.service.LiabilityAccountDomainService;
import com.bank.liability.domain.transaction.entity.LiabilityTransaction;
import com.bank.liability.domain.transaction.repository.TransactionRepository;
import com.bank.liability.domain.transaction.service.TransactionDomainService;
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
class WithdrawCancelServiceTest {

    @Mock private TransactionRepository transactionRepository;
    @Mock private LiabilityAccountRepository liabilityAccountRepository;
    @Mock private LiabilityAccountDomainService liabilityAccountDomainService;
    @Mock private TransactionDomainService transactionDomainService;

    @InjectMocks
    private WithdrawCancelService withdrawCancelService;

    private LiabilityTransaction createOriginalTx(String txNo, String accountNo, BigDecimal amount) {
        LiabilityTransaction tx = new LiabilityTransaction();
        tx.setTransactionNo(txNo);
        tx.setLiabilityAccountNo(accountNo);
        tx.setTransactionType(TransactionType.WITHDRAW);
        tx.setAmount(amount);
        tx.setBalanceBefore(BigDecimal.valueOf(1000));
        tx.setBalanceAfter(BigDecimal.valueOf(800));
        return tx;
    }

    private LiabilityAccount createAccount(BigDecimal balance) {
        LiabilityAccount a = new LiabilityAccount();
        a.setLiabilityAccountNo("ACC001");
        a.setBalance(balance);
        a.setStatus(LiabilityAccountStatus.NORMAL);
        return a;
    }

    private WithdrawCancelCommand createCommand(String originalTxNo) {
        WithdrawCancelCommand cmd = new WithdrawCancelCommand();
        cmd.setOriginalTransactionNo(originalTxNo);
        cmd.setOperatorNo("OPE001");
        return cmd;
    }

    @Test
    void cancel_shouldSucceed_whenConditionsMet() {
        LiabilityTransaction original = createOriginalTx("TX001", "ACC001", BigDecimal.valueOf(200));
        LiabilityAccount account = createAccount(BigDecimal.valueOf(800));
        WithdrawCancelCommand command = createCommand("TX001");

        when(transactionRepository.findByTransactionNo("TX001")).thenReturn(Optional.of(original));
        doNothing().when(transactionDomainService).validateCancel("TX001");
        when(liabilityAccountRepository.findByLiabilityAccountNo("ACC001")).thenReturn(Optional.of(account));
        doNothing().when(liabilityAccountDomainService).deposit(account, BigDecimal.valueOf(200));
        doNothing().when(liabilityAccountRepository).update(account);
        when(transactionDomainService.cancelTransaction(original, "OPE001")).thenReturn(original);

        LiabilityTransaction result = withdrawCancelService.cancel(command);
        assertNotNull(result);
        verify(transactionRepository).findByTransactionNo("TX001");
        verify(transactionDomainService).validateCancel("TX001");
        verify(liabilityAccountRepository).findByLiabilityAccountNo("ACC001");
        verify(liabilityAccountDomainService).deposit(account, BigDecimal.valueOf(200));
        verify(liabilityAccountRepository).update(account);
        verify(transactionDomainService).cancelTransaction(original, "OPE001");
    }

    @Test
    void cancel_shouldThrowException_whenOriginalTxNotExist() {
        when(transactionRepository.findByTransactionNo("TX999")).thenReturn(Optional.empty());
        assertThrows(BusinessException.class, () -> withdrawCancelService.cancel(createCommand("TX999")));
    }

    @Test
    void cancel_shouldThrowException_whenOriginalTxIsNotWithdraw() {
        LiabilityTransaction tx = createOriginalTx("TX001", "ACC001", BigDecimal.valueOf(200));
        tx.setTransactionType(TransactionType.DEPOSIT);
        when(transactionRepository.findByTransactionNo("TX001")).thenReturn(Optional.of(tx));
        assertThrows(BusinessException.class, () -> withdrawCancelService.cancel(createCommand("TX001")));
    }

    @Test
    void cancel_shouldThrowException_whenAccountNotExist() {
        LiabilityTransaction original = createOriginalTx("TX001", "ACC999", BigDecimal.valueOf(200));
        when(transactionRepository.findByTransactionNo("TX001")).thenReturn(Optional.of(original));
        doNothing().when(transactionDomainService).validateCancel("TX001");
        when(liabilityAccountRepository.findByLiabilityAccountNo("ACC999")).thenReturn(Optional.empty());
        assertThrows(BusinessException.class, () -> withdrawCancelService.cancel(createCommand("TX001")));
    }

    @Test
    void cancel_shouldThrowException_whenAccountNotNormal() {
        LiabilityTransaction original = createOriginalTx("TX001", "ACC001", BigDecimal.valueOf(200));
        LiabilityAccount account = createAccount(BigDecimal.valueOf(800));
        account.setStatus(LiabilityAccountStatus.FROZEN);
        when(transactionRepository.findByTransactionNo("TX001")).thenReturn(Optional.of(original));
        doNothing().when(transactionDomainService).validateCancel("TX001");
        when(liabilityAccountRepository.findByLiabilityAccountNo("ACC001")).thenReturn(Optional.of(account));
        assertThrows(BusinessException.class, () -> withdrawCancelService.cancel(createCommand("TX001")));
    }
}
