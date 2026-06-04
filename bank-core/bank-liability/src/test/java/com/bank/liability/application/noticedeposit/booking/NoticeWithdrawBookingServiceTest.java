package com.bank.liability.application.noticedeposit.booking;

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
class NoticeWithdrawBookingServiceTest {

    @Mock
    private NoticeDepositAccountRepository accountRepository;

    @Mock
    private NoticeDepositBookingRepository bookingRepository;

    @InjectMocks
    private NoticeWithdrawBookingService bookingService;

    private NoticeDepositAccount createActiveAccount(String accountNo, String noticeType, BigDecimal principal) {
        NoticeDepositAccount account = new NoticeDepositAccount();
        account.setNoticeDepositAccountNo(accountNo);
        account.setStatus("ACTIVE");
        account.setNoticeType(noticeType);
        account.setPrincipal(principal);
        return account;
    }

    @Test
    void book_shouldSucceed_whenDay7() {
        String accountNo = "ND2026001000000001";
        NoticeDepositAccount account = createActiveAccount(accountNo, "DAY7", BigDecimal.valueOf(600000));
        NoticeWithdrawBookingCommand cmd = new NoticeWithdrawBookingCommand();
        cmd.setNoticeDepositAccountNo(accountNo);
        cmd.setBookingAmount(BigDecimal.valueOf(600000));

        when(accountRepository.findByNoticeDepositAccountNo(accountNo)).thenReturn(Optional.of(account));
        when(bookingRepository.existsPendingByNoticeDepositAccountNo(accountNo)).thenReturn(false);

        NoticeDepositBooking result = bookingService.book(cmd);

        assertNotNull(result);
        assertEquals(accountNo, result.getNoticeDepositAccountNo());
        assertEquals(BigDecimal.valueOf(600000), result.getBookingAmount());
        assertEquals("PENDING", result.getStatus());
        assertEquals(LocalDate.now().plusDays(7), result.getExpectedWithdrawDate());
        verify(bookingRepository).save(any(NoticeDepositBooking.class));
    }

    @Test
    void book_shouldSucceed_whenDay1() {
        String accountNo = "ND2026001000000002";
        NoticeDepositAccount account = createActiveAccount(accountNo, "DAY1", BigDecimal.valueOf(500000));
        NoticeWithdrawBookingCommand cmd = new NoticeWithdrawBookingCommand();
        cmd.setNoticeDepositAccountNo(accountNo);
        cmd.setBookingAmount(BigDecimal.valueOf(500000));

        when(accountRepository.findByNoticeDepositAccountNo(accountNo)).thenReturn(Optional.of(account));
        when(bookingRepository.existsPendingByNoticeDepositAccountNo(accountNo)).thenReturn(false);

        NoticeDepositBooking result = bookingService.book(cmd);

        assertNotNull(result);
        assertEquals("PENDING", result.getStatus());
        assertEquals(LocalDate.now().plusDays(1), result.getExpectedWithdrawDate());
        verify(bookingRepository).save(any(NoticeDepositBooking.class));
    }

    @Test
    void book_shouldThrowException_whenAccountNotFound() {
        String accountNo = "ND_NOT_EXIST";
        NoticeWithdrawBookingCommand cmd = new NoticeWithdrawBookingCommand();
        cmd.setNoticeDepositAccountNo(accountNo);
        cmd.setBookingAmount(BigDecimal.valueOf(600000));

        when(accountRepository.findByNoticeDepositAccountNo(accountNo)).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> bookingService.book(cmd));
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void book_shouldThrowException_whenAmountExceedsPrincipal() {
        String accountNo = "ND2026001000000001";
        NoticeDepositAccount account = createActiveAccount(accountNo, "DAY7", BigDecimal.valueOf(500000));
        NoticeWithdrawBookingCommand cmd = new NoticeWithdrawBookingCommand();
        cmd.setNoticeDepositAccountNo(accountNo);
        cmd.setBookingAmount(BigDecimal.valueOf(600000));

        when(accountRepository.findByNoticeDepositAccountNo(accountNo)).thenReturn(Optional.of(account));

        assertThrows(BusinessException.class, () -> bookingService.book(cmd));
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void book_shouldThrowException_whenPendingBookingExists() {
        String accountNo = "ND2026001000000001";
        NoticeDepositAccount account = createActiveAccount(accountNo, "DAY7", BigDecimal.valueOf(600000));
        NoticeWithdrawBookingCommand cmd = new NoticeWithdrawBookingCommand();
        cmd.setNoticeDepositAccountNo(accountNo);
        cmd.setBookingAmount(BigDecimal.valueOf(600000));

        when(accountRepository.findByNoticeDepositAccountNo(accountNo)).thenReturn(Optional.of(account));
        when(bookingRepository.existsPendingByNoticeDepositAccountNo(accountNo)).thenReturn(true);

        assertThrows(BusinessException.class, () -> bookingService.book(cmd));
        verify(bookingRepository, never()).save(any());
    }
}