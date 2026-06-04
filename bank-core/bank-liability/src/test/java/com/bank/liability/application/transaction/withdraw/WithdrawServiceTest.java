package com.bank.liability.application.transaction.withdraw;

import com.bank.common.exception.BusinessException;
import com.bank.liability.domain.enums.LiabilityAccountStatus;
import com.bank.liability.domain.enums.TransactionType;
import com.bank.liability.domain.liabilityaccount.entity.LiabilityAccount;
import com.bank.liability.domain.liabilityaccount.repository.LiabilityAccountRepository;
import com.bank.liability.domain.liabilityaccount.service.LiabilityAccountDomainService;
import com.bank.liability.domain.transaction.entity.LiabilityTransaction;
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
class WithdrawServiceTest {

    @Mock
    private LiabilityAccountRepository liabilityAccountRepository;

    @Mock
    private LiabilityAccountDomainService liabilityAccountDomainService;

    @Mock
    private TransactionDomainService transactionDomainService;

    @InjectMocks
    private WithdrawService withdrawService;

    @Test
    void withdraw_shouldSucceed_whenAccountIsNormal() {
        LiabilityAccount account = createNormalAccount(BigDecimal.valueOf(1000));
        WithdrawCommand command = createWithdrawCommand("ACC001", BigDecimal.valueOf(200));

        when(liabilityAccountRepository.findByLiabilityAccountNo("ACC001"))
                .thenReturn(Optional.of(account));
        doNothing().when(liabilityAccountDomainService).validateWithdraw(any(), any());
        // Simulate actual balance reduction
        doAnswer(inv -> {
            LiabilityAccount a = inv.getArgument(0);
            BigDecimal amt = inv.getArgument(1);
            a.setBalance(a.getBalance().subtract(amt));
            return null;
        }).when(liabilityAccountDomainService).withdraw(any(LiabilityAccount.class), any(BigDecimal.class));
        doNothing().when(liabilityAccountRepository).update(any());

        LiabilityTransaction tx = new LiabilityTransaction();
        tx.setTransactionNo("TX123");
        when(transactionDomainService.createTransaction(
                eq("ACC001"), eq(TransactionType.WITHDRAW), eq(BigDecimal.valueOf(200)),
                eq(BigDecimal.valueOf(1000)), eq(BigDecimal.valueOf(800)),
                eq("OPE001"), any(), isNull()
        )).thenReturn(tx);

        LiabilityTransaction result = withdrawService.withdraw(command);

        assertNotNull(result);
        assertEquals("TX123", result.getTransactionNo());
        verify(liabilityAccountRepository).findByLiabilityAccountNo("ACC001");
        verify(liabilityAccountDomainService).validateWithdraw(account, BigDecimal.valueOf(200));
        verify(liabilityAccountDomainService).withdraw(account, BigDecimal.valueOf(200));
        verify(liabilityAccountRepository).update(account);
    }

    @Test
    void withdraw_shouldThrowException_whenAccountNotExist() {
        WithdrawCommand command = createWithdrawCommand("NONEXIST", BigDecimal.valueOf(100));

        when(liabilityAccountRepository.findByLiabilityAccountNo("NONEXIST"))
                .thenReturn(Optional.empty());

        BusinessException ex = assertThrows(BusinessException.class,
                () -> withdrawService.withdraw(command));
        assertNotNull(ex.getMessage());
        verify(liabilityAccountRepository).findByLiabilityAccountNo("NONEXIST");
        verifyNoInteractions(liabilityAccountDomainService, transactionDomainService);
    }

    @Test
    void withdraw_shouldThrowException_whenAccountIsNotNormal() {
        LiabilityAccount account = createNormalAccount(BigDecimal.valueOf(1000));
        account.setStatus(LiabilityAccountStatus.FROZEN);
        WithdrawCommand command = createWithdrawCommand("ACC002", BigDecimal.valueOf(100));

        when(liabilityAccountRepository.findByLiabilityAccountNo("ACC002"))
                .thenReturn(Optional.of(account));

        BusinessException ex = assertThrows(BusinessException.class,
                () -> withdrawService.withdraw(command));
        assertNotNull(ex.getMessage());
        verify(liabilityAccountRepository).findByLiabilityAccountNo("ACC002");
        verifyNoInteractions(liabilityAccountDomainService, transactionDomainService);
    }

    private LiabilityAccount createNormalAccount(BigDecimal balance) {
        LiabilityAccount account = new LiabilityAccount();
        account.setId(1L);
        account.setLiabilityAccountNo("ACC001");
        account.setCustomerAccountNo("CACC001");
        account.setBalance(balance);
        account.setStatus(LiabilityAccountStatus.NORMAL);
        return account;
    }

    private WithdrawCommand createWithdrawCommand(String accountNo, BigDecimal amount) {
        WithdrawCommand command = new WithdrawCommand();
        command.setLiabilityAccountNo(accountNo);
        command.setAmount(amount);
        command.setOperatorNo("OPE001");
        command.setRemark("姝ｅ父鍙栨");
        return command;
    }
}
