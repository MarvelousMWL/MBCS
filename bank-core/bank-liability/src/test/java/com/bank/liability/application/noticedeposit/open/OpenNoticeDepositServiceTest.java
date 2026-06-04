package com.bank.liability.application.noticedeposit.open;

import com.bank.common.exception.BusinessException;
import com.bank.liability.domain.noticedeposit.entity.NoticeDepositAccount;
import com.bank.liability.domain.noticedeposit.repository.NoticeDepositAccountRepository;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OpenNoticeDepositServiceTest {

    @Mock
    private NoticeDepositAccountRepository accountRepository;

    @InjectMocks
    private OpenNoticeDepositService openNoticeDepositService;

    private OpenNoticeDepositCommand createCommand(String noticeType, BigDecimal amount) {
        OpenNoticeDepositCommand cmd = new OpenNoticeDepositCommand();
        cmd.setCustomerAccountNo("CUST001");
        cmd.setCurrentAccountNo("CA001");
        cmd.setNoticeType(noticeType);
        cmd.setAmount(amount);
        cmd.setInterestRate(BigDecimal.valueOf(1.5));
        cmd.setCurrentInterestRate(BigDecimal.valueOf(0.35));
        return cmd;
    }

    @Test
    void open_shouldSucceed_whenDay7ValidCommand() {
        OpenNoticeDepositCommand cmd = createCommand("DAY7", BigDecimal.valueOf(600000));

        NoticeDepositAccount result = openNoticeDepositService.open(cmd);

        assertNotNull(result);
        assertEquals("CUST001", result.getCustomerAccountNo());
        assertEquals("CA001", result.getCurrentAccountNo());
        assertEquals("DAY7", result.getNoticeType());
        assertEquals(BigDecimal.valueOf(600000), result.getPrincipal());
        assertEquals("ACTIVE", result.getStatus());
        assertEquals(BigDecimal.ZERO, result.getInterest());
        assertEquals(BigDecimal.valueOf(600000), result.getTotalAmount());
        verify(accountRepository).save(any(NoticeDepositAccount.class));
    }

    @Test
    void open_shouldSucceed_whenDay1ValidCommand() {
        OpenNoticeDepositCommand cmd = createCommand("DAY1", BigDecimal.valueOf(1000000));

        NoticeDepositAccount result = openNoticeDepositService.open(cmd);

        assertNotNull(result);
        assertEquals("DAY1", result.getNoticeType());
        assertEquals("ACTIVE", result.getStatus());
        verify(accountRepository).save(any(NoticeDepositAccount.class));
    }

    @Test
    void open_shouldThrowException_whenAmountBelowMinimum() {
        OpenNoticeDepositCommand cmd = createCommand("DAY7", BigDecimal.valueOf(400000));

        BusinessException ex = assertThrows(BusinessException.class, () -> openNoticeDepositService.open(cmd));
        assertTrue(ex.getMessage().contains("50"));
        verify(accountRepository, never()).save(any());
    }

    @Test
    void open_shouldThrowException_whenNoticeTypeInvalid() {
        OpenNoticeDepositCommand cmd = createCommand("INVALID", BigDecimal.valueOf(600000));

        assertThrows(BusinessException.class, () -> openNoticeDepositService.open(cmd));
        verify(accountRepository, never()).save(any());
    }
}