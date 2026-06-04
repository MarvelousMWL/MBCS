package com.bank.liability.application.noticedeposit.withdraw;

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
public class NoticeWithdrawService {

    private final NoticeDepositAccountRepository accountRepository;
    private final NoticeDepositBookingRepository bookingRepository;

    @Transactional
    public NoticeDepositAccount withdraw(NoticeWithdrawCommand command) {
        NoticeDepositAccount account = accountRepository
                .findByNoticeDepositAccountNo(command.getNoticeDepositAccountNo())
                .orElseThrow(() -> new BusinessException("通知存款账号不存在"));

        if (!account.isActive()) {
            throw new BusinessException("通知存款账户状态不允许支取");
        }

        NoticeDepositBooking booking = bookingRepository
                .findTopByNoticeDepositAccountNoOrderByCreatedAtDesc(command.getNoticeDepositAccountNo())
                .orElse(null);

        BigDecimal interestAmount;
        if (booking != null && booking.isPending() && !booking.getExpectedWithdrawDate().isAfter(LocalDate.now())) {
            interestAmount = account.calculateInterest();
            booking.setStatus("EXECUTED");
            booking.setUpdatedAt(LocalDateTime.now());
            bookingRepository.update(booking);
        } else {
            interestAmount = account.getPrincipal()
                    .multiply(account.getCurrentInterestRate())
                    .multiply(BigDecimal.valueOf(account.getNoticeDays()))
                    .divide(BigDecimal.valueOf(36500), 2, BigDecimal.ROUND_HALF_UP);
        }

        account.setInterest(interestAmount);
        account.setTotalAmount(account.getPrincipal().add(interestAmount));
        account.setStatus("WITHDRAWN");
        account.setUpdatedAt(LocalDateTime.now());
        accountRepository.update(account);

        return account;
    }
}