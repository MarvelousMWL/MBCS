package com.bank.liability.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bank.common.result.PageResult;
import com.bank.common.result.Result;
import com.bank.liability.application.transaction.deposit.DepositCommand;
import com.bank.liability.application.transaction.deposit.DepositService;
import com.bank.liability.application.transaction.withdraw.WithdrawCommand;
import com.bank.liability.application.transaction.withdraw.WithdrawService;
import com.bank.liability.application.transaction.depositcancel.DepositCancelCommand;
import com.bank.liability.application.transaction.depositcancel.DepositCancelService;
import com.bank.liability.application.transaction.withdrawcancel.WithdrawCancelCommand;
import com.bank.liability.application.transaction.withdrawcancel.WithdrawCancelService;
import com.bank.liability.domain.enums.TransactionType;
import com.bank.liability.domain.transaction.entity.LiabilityTransaction;
import com.bank.liability.domain.transaction.repository.TransactionRepository;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/liability/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final DepositService depositService;
    private final WithdrawService withdrawService;
    private final DepositCancelService depositCancelService;
    private final WithdrawCancelService withdrawCancelService;
    private final TransactionRepository transactionRepository;

    @PostMapping("/deposit")
    public Result<LiabilityTransaction> deposit(@Valid @RequestBody DepositCommand command,
                                                @RequestHeader(value = "operator-no", defaultValue = "SYSTEM") String operatorNo) {
        command.setOperatorNo(operatorNo);
        return Result.success(depositService.deposit(command));
    }

    @PostMapping("/withdraw")
    public Result<LiabilityTransaction> withdraw(@Valid @RequestBody WithdrawCommand command,
                                                  @RequestHeader(value = "operator-no", defaultValue = "SYSTEM") String operatorNo) {
        command.setOperatorNo(operatorNo);
        return Result.success(withdrawService.withdraw(command));
    }

    @PostMapping("/deposit-cancel")
    public Result<LiabilityTransaction> depositCancel(@Valid @RequestBody DepositCancelCommand command,
                                                       @RequestHeader(value = "operator-no", defaultValue = "SYSTEM") String operatorNo) {
        command.setOperatorNo(operatorNo);
        return Result.success(depositCancelService.cancel(command));
    }

    @PostMapping("/withdraw-cancel")
    public Result<LiabilityTransaction> withdrawCancel(@Valid @RequestBody WithdrawCancelCommand command,
                                                       @RequestHeader(value = "operator-no", defaultValue = "SYSTEM") String operatorNo) {
        command.setOperatorNo(operatorNo);
        return Result.success(withdrawCancelService.cancel(command));
    }

    @GetMapping
    public Result<PageResult<LiabilityTransaction>> list(
            @RequestParam(required = false) String liabilityAccountNo,
            @RequestParam(required = false) String transactionType,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {

        TransactionType type = null;
        if (transactionType != null && !transactionType.isEmpty()) {
            try {
                type = TransactionType.valueOf(transactionType);
            } catch (IllegalArgumentException ignored) {
            }
        }

        LocalDateTime startDateTime = null;
        if (startDate != null && !startDate.isEmpty()) {
            startDateTime = LocalDate.parse(startDate).atStartOfDay();
        }

        LocalDateTime endDateTime = null;
        if (endDate != null && !endDate.isEmpty()) {
            endDateTime = LocalDate.parse(endDate).atTime(LocalTime.MAX);
        }

        IPage<LiabilityTransaction> pageResult = transactionRepository.findPage(
                new Page<>(page, size),
                liabilityAccountNo,
                type,
                startDateTime,
                endDateTime
        );

        PageResult<LiabilityTransaction> result = PageResult.of(
                pageResult.getTotal(),
                (int) pageResult.getCurrent(),
                (int) pageResult.getSize(),
                (int) pageResult.getPages(),
                pageResult.getRecords()
        );

        return Result.success(result);
    }
}
