package com.bank.liability.api;

import com.bank.common.result.Result;
import com.bank.liability.application.noticedeposit.open.OpenNoticeDepositCommand;
import com.bank.liability.application.noticedeposit.open.OpenNoticeDepositService;
import com.bank.liability.application.noticedeposit.booking.NoticeWithdrawBookingCommand;
import com.bank.liability.application.noticedeposit.booking.NoticeWithdrawBookingService;
import com.bank.liability.application.noticedeposit.withdraw.NoticeWithdrawCommand;
import com.bank.liability.application.noticedeposit.withdraw.NoticeWithdrawService;
import com.bank.liability.domain.noticedeposit.entity.NoticeDepositAccount;
import com.bank.liability.domain.noticedeposit.entity.NoticeDepositBooking;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/liability/notice-deposit")
@RequiredArgsConstructor
public class NoticeDepositController {

    private final OpenNoticeDepositService openNoticeDepositService;
    private final NoticeWithdrawBookingService noticeWithdrawBookingService;
    private final NoticeWithdrawService noticeWithdrawService;

    @PostMapping("/open")
    public Result<NoticeDepositAccount> open(@Valid @RequestBody OpenNoticeDepositCommand command) {
        return Result.success(openNoticeDepositService.open(command));
    }

    @PostMapping("/book")
    public Result<NoticeDepositBooking> book(@Valid @RequestBody NoticeWithdrawBookingCommand command) {
        return Result.success(noticeWithdrawBookingService.book(command));
    }

    @PostMapping("/withdraw")
    public Result<NoticeDepositAccount> withdraw(@Valid @RequestBody NoticeWithdrawCommand command) {
        return Result.success(noticeWithdrawService.withdraw(command));
    }
}