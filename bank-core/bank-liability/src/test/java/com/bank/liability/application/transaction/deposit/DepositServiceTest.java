package com.bank.liability.application.transaction.deposit;

import com.bank.common.exception.BusinessException;
import com.bank.liability.domain.enums.LiabilityAccountStatus;
import com.bank.liability.domain.enums.LiabilityAccountType;
import com.bank.liability.domain.enums.TransactionType;
import com.bank.liability.domain.liabilityaccount.entity.LiabilityAccount;
import com.bank.liability.domain.liabilityaccount.repository.LiabilityAccountRepository;
import com.bank.liability.domain.liabilityaccount.service.LiabilityAccountDomainService;
import com.bank.liability.domain.transaction.entity.LiabilityTransaction;
import com.bank.liability.domain.transaction.repository.TransactionRepository;
import com.bank.liability.domain.transaction.service.TransactionDomainService;
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
class DepositServiceTest {

    @Mock
    private LiabilityAccountRepository liabilityAccountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private LiabilityAccountDomainService liabilityAccountDomainService;

    @Mock
    private TransactionDomainService transactionDomainService;

    @InjectMocks
    private DepositService depositService;

    @Test
    void deposit_shouldSucceed_whenAccountIsNormal() {
        LiabilityAccount account = createNormalAccount(BigDecimal.valueOf(500));
        DepositCommand command = createDepositCommand("ACC001", BigDecimal.valueOf(200));

        when(liabilityAccountRepository.findByLiabilityAccountNo("ACC001"))
                .thenReturn(Optional.of(account));
        doNothing().when(liabilityAccountDomainService).validateDeposit(any(BigDecimal.class));
        doNothing().when(liabilityAccountDomainService).deposit(any(LiabilityAccount.class), any(BigDecimal.class));
        doNothing().when(liabilityAccountRepository).update(any(LiabilityAccount.class));

        LiabilityTransaction expectedTransaction = new LiabilityTransaction();
        expectedTransaction.setTransactionNo("TX123456");
        when(transactionDomainService.createTransaction(
                eq("ACC001"),
                eq(TransactionType.DEPOSIT),
                eq(BigDecimal.valueOf(200)),
                any(BigDecimal.class),
                any(BigDecimal.class),
                eq("OPE001"),
                eq("正常存款"),
                isNull()
        )).thenReturn(expectedTransaction);

        LiabilityTransaction result = depositService.deposit(command);

        assertNotNull(result);
        assertEquals("TX123456", result.getTransactionNo());

        verify(liabilityAccountRepository).findByLiabilityAccountNo("ACC001");
        verify(liabilityAccountDomainService).validateDeposit(BigDecimal.valueOf(200));
        verify(liabilityAccountDomainService).deposit(account, BigDecimal.valueOf(200));
        verify(liabilityAccountRepository).update(account);
        verify(transactionDomainService).createTransaction(
                eq("ACC001"),
                eq(TransactionType.DEPOSIT),
                eq(BigDecimal.valueOf(200)),
                any(BigDecimal.class),
                any(BigDecimal.class),
                eq("OPE001"),
                eq("正常存款"),
                isNull()
        );
    }

    @Test
    void deposit_shouldThrowException_whenAccountNotExist() {
        DepositCommand command = createDepositCommand("NONEXIST", BigDecimal.valueOf(100));

        when(liabilityAccountRepository.findByLiabilityAccountNo("NONEXIST"))
                .thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class,
                () -> depositService.deposit(command));
        assertNotNull(exception.getMessage());

        verify(liabilityAccountRepository).findByLiabilityAccountNo("NONEXIST");
        verifyNoInteractions(liabilityAccountDomainService, transactionDomainService);
    }

    @Test
    void deposit_shouldThrowException_whenAccountStatusIsNotNormal() {
        LiabilityAccount account = createNormalAccount(BigDecimal.valueOf(500));
        account.setStatus(LiabilityAccountStatus.STOPPED);

        DepositCommand command = createDepositCommand("ACC002", BigDecimal.valueOf(100));

        when(liabilityAccountRepository.findByLiabilityAccountNo("ACC002"))
                .thenReturn(Optional.of(account));

        BusinessException exception = assertThrows(BusinessException.class,
                () -> depositService.deposit(command));
        assertNotNull(exception.getMessage());

        verify(liabilityAccountRepository).findByLiabilityAccountNo("ACC002");
        verifyNoInteractions(liabilityAccountDomainService, transactionDomainService);
    }

    private LiabilityAccount createNormalAccount(BigDecimal balance) {
        LiabilityAccount account = new LiabilityAccount();
        account.setId(1L);
        account.setLiabilityAccountNo("ACC001");
        account.setCustomerAccountNo("CACC001");
        account.setAccountType(LiabilityAccountType.DEMAND);
        account.setBalance(balance);
        account.setStatus(LiabilityAccountStatus.NORMAL);
        return account;
    }

    private DepositCommand createDepositCommand(String accountNo, BigDecimal amount) {
        DepositCommand command = new DepositCommand();
        command.setLiabilityAccountNo(accountNo);
        command.setAmount(amount);
        command.setOperatorNo("OPE001");
        command.setRemark("正常存款");
        return command;
    }
}
