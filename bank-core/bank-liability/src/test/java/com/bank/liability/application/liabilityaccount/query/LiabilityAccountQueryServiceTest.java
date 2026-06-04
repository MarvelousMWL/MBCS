package com.bank.liability.application.liabilityaccount.query;

import com.bank.liability.domain.liabilityaccount.entity.LiabilityAccount;
import com.bank.liability.domain.liabilityaccount.repository.LiabilityAccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LiabilityAccountQueryServiceTest {

    @Mock private LiabilityAccountRepository liabilityAccountRepository;

    @InjectMocks
    private LiabilityAccountQueryService liabilityAccountQueryService;

    @Test
    void findByLiabilityAccountNo_shouldReturnAccount_whenExists() {
        LiabilityAccount account = new LiabilityAccount();
        account.setLiabilityAccountNo("LA001");
        when(liabilityAccountRepository.findByLiabilityAccountNo("LA001")).thenReturn(Optional.of(account));

        Optional<LiabilityAccount> result = liabilityAccountQueryService.findByLiabilityAccountNo("LA001");
        assertTrue(result.isPresent());
        assertEquals("LA001", result.get().getLiabilityAccountNo());
    }

    @Test
    void findByLiabilityAccountNo_shouldReturnEmpty_whenNotExists() {
        when(liabilityAccountRepository.findByLiabilityAccountNo("LA999")).thenReturn(Optional.empty());
        assertTrue(liabilityAccountQueryService.findByLiabilityAccountNo("LA999").isEmpty());
    }

    @Test
    void findByCustomerAccountNo_shouldReturnAccounts() {
        when(liabilityAccountRepository.findByCustomerAccountNo("CACC001")).thenReturn(List.of(new LiabilityAccount()));
        assertEquals(1, liabilityAccountQueryService.findByCustomerAccountNo("CACC001").size());
    }

    @Test
    void findAll_shouldReturnAllAccounts() {
        when(liabilityAccountRepository.findAll()).thenReturn(List.of(new LiabilityAccount(), new LiabilityAccount()));
        assertEquals(2, liabilityAccountQueryService.findAll().size());
    }
}
