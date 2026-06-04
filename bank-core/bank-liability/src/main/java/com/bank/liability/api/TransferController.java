package com.bank.liability.api;

import com.bank.common.result.Result;
import com.bank.liability.application.transaction.transfer.TransferCancelCommand;
import com.bank.liability.application.transaction.transfer.TransferCancelService;
import com.bank.liability.application.transaction.transfer.TransferCommand;
import com.bank.liability.application.transaction.transfer.TransferService;
import com.bank.liability.domain.transfer.entity.TransferRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/liability/transfer")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;
    private final TransferCancelService transferCancelService;

    @PostMapping("/execute")
    public Result<TransferRecord> execute(@Valid @RequestBody TransferCommand command,
                                           @RequestHeader(value = "operator-no", defaultValue = "SYSTEM") String operatorNo) {
        command.setOperatorNo(operatorNo);
        return Result.success(transferService.transfer(command));
    }

    @PostMapping("/cancel")
    public Result<TransferRecord> cancel(@Valid @RequestBody TransferCancelCommand command,
                                          @RequestHeader(value = "operator-no", defaultValue = "SYSTEM") String operatorNo) {
        command.setOperatorNo(operatorNo);
        return Result.success(transferCancelService.cancel(command));
    }
}
