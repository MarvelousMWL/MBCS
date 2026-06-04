package com.bank.liability.application.noticedeposit.withdraw;

import com.bank.common.exception.BusinessException;
import com.bank.liability.domain.noticedeposit.entity.NoticeDepositAccount;
import com.bank.liability.domain.noticedeposit.entity.NoticeDepositBooking;
import com.bank.liability.domain.noticedeposit.repository.NoticeDepositAccountRepository;
import com.bank.liability.domain.noticedeposit.repository.NoticeDepositBookingRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NoticeWithdrawServiceTest {

    @Mock
    private NoticeDepositAccountRepository accountRepository;

    @Mock
    private NoticeDepositBookingRepository bookingRepository;

    @InjectMocks
    private NoticeWithdrawService withdrawService;

    private NoticeDepositAccount createActiveAccount(String accountNo, String noticeType, BigDecimal principal, BigDecimal rate) {
        NoticeDepositAccount account = new NoticeDepositAccount();
        account.setNoticeDepositAccountNo(accountNo);
        account.setStatus("ACTIVE");
        account.setNoticeType(noticeType);
        account.setPrincipal(principal);
        account.setInterestRate(rate);
        account.setCurrentInterestRate(BigDecimal.valueOf(0.35));
        account.setVersion(0);
        return account;
    }

    private NoticeDepositBooking createPendingBooking(String accountNo, LocalDate expectedWithdrawDate, BigDecimal amount) {
        NoticeDepositBooking booking = new NoticeDepositBooking();
        booking.setNoticeDepositAccountNo(accountNo);
        booking.setBookingAmount(amount);
        booking.setExpectedWithdrawDate(expectedWithdrawDate);
        booking.setStatus("PENDING");
        return booking;
    }

    @Test
    void withdraw_shouldSucceed_withValidBooking() {
        String accountNo = "ND2026001000000001";
        BigDecimal principal = BigDecimal.valueOf(600000);
        BigDecimal rate = BigDecimal.valueOf(1.5);
        NoticeDepositAccount account = createActiveAccount(accountNo, "DAY7", principal, rate);
        NoticeWithdrawCommand cmd = new NoticeWithdrawCommand();
        cmd.setNoticeDepositAccountNo(accountNo);

        NoticeDepositBooking booking = createPendingBooking(accountNo, LocalDate.now().minusDays(1), principal);

        when(accountRepository.findByNoticeDepositAccountNo(accountNo)).thenReturn(Optional.of(account));
        when(bookingRepository.findTopByNoticeDepositAccountNoOrderByCreatedAtDesc(accountNo)).thenReturn(Optional.of(booking));

        NoticeDepositAccount result = withdrawService.withdraw(cmd);

        assertEquals("WITHDRAWN", result.getStatus());
        assertTrue(result.getInterest().compareTo(BigDecimal.ZERO) > 0);
        assertEquals(result.getPrincipal().add(result.getInterest()), result.getTotalAmount());
        verify(accountRepository).update(account);
        verify(bookingRepository).update(booking);
        assertEquals("EXECUTED", booking.getStatus());
    }

    @Test
    void withdraw_shouldSucceed_withoutBooking_useCurrentRate() {
        String accountNo = "ND2026001000000002";
        BigDecimal principal = BigDecimal.valueOf(500000);
        BigDecimal rate = BigDecimal.valueOf(1.5);
        NoticeDepositAccount account = createActiveAccount(accountNo, "DAY1", principal, rate);
        NoticeWithdrawCommand cmd = new NoticeWithdrawCommand();
        cmd.setNoticeDepositAccountNo(accountNo);

        when(accountRepository.findByNoticeDepositAccountNo(accountNo)).thenReturn(Optional.of(account));
        when(bookingRepository.findTopByNoticeDepositAccountNoOrderByCreatedAtDesc(accountNo)).thenReturn(Optional.empty());

        NoticeDepositAccount result = withdrawService.withdraw(cmd);

        assertEquals("WITHDRAWN", result.getStatus());
        assertNotNull(result.getInterest());
        verify(accountRepository).update(account);
        verify(bookingRepository, never()).update(any());
    }

    @Test
    void withdraw_shouldUseCurrentRate_whenBookingNotYetDue() {
        String accountNo = "ND2026001000000003";
        BigDecimal principal = BigDecimal.valueOf(500000);
        NoticeDepositAccount account = createActiveAccount(accountNo, "DAY7", principal, BigDecimal.valueOf(1.5));
        NoticeWithdrawCommand cmd = new NoticeWithdrawCommand();
        cmd.setNoticeDepositAccountNo(accountNo);

        NoticeDepositBooking booking = createPendingBooking(accountNo, LocalDate.now().plusDays(1), principal);

        when(accountRepository.findByNoticeDepositAccountNo(accountNo)).thenReturn(Optional.of(account));
        when(bookingRepository.findTopByNoticeDepositAccountNoOrderByCreatedAtDesc(accountNo)).thenReturn(Optional.of(booking));

        NoticeDepositAccount result = withdrawService.withdraw(cmd);

        assertEquals("WITHDRAWN", result.getStatus());
        assertTrue(result.getInterest().compareTo(BigDecimal.ZERO) > 0);
        verify(accountRepository).update(account);
        assertEquals("PENDING", booking.getStatus());
    }
}