package com.bank.liability.application.liabilityaccount.freeze;

import com.bank.common.exception.BusinessException;
import com.bank.liability.domain.enums.LiabilityAccountStatus;
import com.bank.liability.domain.liabilityaccount.entity.LiabilityAccount;
import com.bank.liability.domain.liabilityaccount.repository.LiabilityAccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FreezeLiabilityAccountServiceTest {

    @Mock private LiabilityAccountRepository liabilityAccountRepository;

    @InjectMocks
    private FreezeLiabilityAccountService freezeLiabilityAccountService;

    private FreezeLiabilityAccountCommand createCommand(String accountNo) {
        FreezeLiabilityAccountCommand cmd = new FreezeLiabilityAccountCommand();
        cmd.setLiabilityAccountNo(accountNo);
        cmd.setReason("测试冻结");
        return cmd;
    }

    @Test
    void freeze_shouldSucceed_whenAccountIsNormal() {
        LiabilityAccount account = new LiabilityAccount();
        account.setLiabilityAccountNo("LA001");
        account.setStatus(LiabilityAccountStatus.NORMAL);

        when(liabilityAccountRepository.findByLiabilityAccountNo("LA001")).thenReturn(Optional.of(account));
        doNothing().when(liabilityAccountRepository).update(account);

        freezeLiabilityAccountService.freeze(createCommand("LA001"), "OPE001");

        assertEquals(LiabilityAccountStatus.FROZEN, account.getStatus());
        assertNotNull(account.getUpdatedAt());
        verify(liabilityAccountRepository).update(account);
    }

    @Test
    void freeze_shouldThrowException_whenAccountNotExist() {
        when(liabilityAccountRepository.findByLiabilityAccountNo("NONEXIST")).thenReturn(Optional.empty());
        assertThrows(BusinessException.class, () -> freezeLiabilityAccountService.freeze(createCommand("NONEXIST"), "OPE001"));
    }

    @Test
    void freeze_shouldThrowException_whenAccountAlreadyFrozen() {
        LiabilityAccount account = new LiabilityAccount();
        account.setLiabilityAccountNo("LA001");
        account.setStatus(LiabilityAccountStatus.FROZEN);

        when(liabilityAccountRepository.findByLiabilityAccountNo("LA001")).thenReturn(Optional.of(account));
        assertThrows(BusinessException.class, () -> freezeLiabilityAccountService.freeze(createCommand("LA001"), "OPE001"));
    }

    @Test
    void unfreeze_shouldSucceed_whenAccountIsFrozen() {
        LiabilityAccount account = new LiabilityAccount();
        account.setLiabilityAccountNo("LA001");
        account.setStatus(LiabilityAccountStatus.FROZEN);

        when(liabilityAccountRepository.findByLiabilityAccountNo("LA001")).thenReturn(Optional.of(account));
        doNothing().when(liabilityAccountRepository).update(account);

        freezeLiabilityAccountService.unfreeze(createCommand("LA001"), "OPE001");

        assertEquals(LiabilityAccountStatus.NORMAL, account.getStatus());
        assertNotNull(account.getUpdatedAt());
        verify(liabilityAccountRepository).update(account);
    }

    @Test
    void unfreeze_shouldThrowException_whenAccountNotFrozen() {
        LiabilityAccount account = new LiabilityAccount();
        account.setLiabilityAccountNo("LA001");
        account.setStatus(LiabilityAccountStatus.NORMAL);

        when(liabilityAccountRepository.findByLiabilityAccountNo("LA001")).thenReturn(Optional.of(account));
        assertThrows(BusinessException.class, () -> freezeLiabilityAccountService.unfreeze(createCommand("LA001"), "OPE001"));
    }
}
