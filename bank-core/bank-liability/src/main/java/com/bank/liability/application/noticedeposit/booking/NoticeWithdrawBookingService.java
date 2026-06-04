package com.bank.liability.application.noticedeposit.booking;

import com.bank.common.exception.BusinessException;
import com.bank.liability.domain.noticedeposit.entity.NoticeDepositAccount;
import com.bank.liability.domain.noticedeposit.entity.NoticeDepositBooking;
import com.bank.liability.domain.noticedeposit.repository.NoticeDepositAccountRepository;
import com.bank.liability.domain.noticedeposit.repository.NoticeDepositBookingRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NoticeWithdrawBookingService {

    private final NoticeDepositAccountRepository accountRepository;
    private final NoticeDepositBookingRepository bookingRepository;

    @Transactional
    public NoticeDepositBooking book(NoticeWithdrawBookingCommand command) {
        NoticeDepositAccount account = accountRepository
                .findByNoticeDepositAccountNo(command.getNoticeDepositAccountNo())
                .orElseThrow(() -> new BusinessException("通知存款账号不存在"));

        if (!account.isActive()) {
            throw new BusinessException("通知存款账户状态不允许预约支取");
        }
        if (command.getBookingAmount().compareTo(account.getPrincipal()) > 0) {
            throw new BusinessException("预约金额不能超过本金");
        }
        if (bookingRepository.existsPendingByNoticeDepositAccountNo(command.getNoticeDepositAccountNo())) {
            throw new BusinessException("该账户已有未执行的预约记录");
        }

        LocalDate bookingDate = command.getBookingDate() != null ? command.getBookingDate() : LocalDate.now();
        int noticeDays = account.getNoticeDays();
        LocalDate expectedWithdrawDate = bookingDate.plusDays(noticeDays);

        NoticeDepositBooking booking = new NoticeDepositBooking();
        booking.setNoticeDepositAccountNo(command.getNoticeDepositAccountNo());
        booking.setBookingDate(bookingDate);
        booking.setBookingAmount(command.getBookingAmount());
        booking.setExpectedWithdrawDate(expectedWithdrawDate);
        booking.setStatus("PENDING");
        booking.setVersion(0);
        booking.setCreatedAt(LocalDateTime.now());
        booking.setUpdatedAt(LocalDateTime.now());

        bookingRepository.save(booking);
        return booking;
    }
}