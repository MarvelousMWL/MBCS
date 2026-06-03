package com.bank.liability.api;

import com.bank.common.result.Result;
import com.bank.liability.application.customeraccount.open.OpenCustomerAccountCommand;
import com.bank.liability.application.customeraccount.open.OpenCustomerAccountService;
import com.bank.liability.application.customeraccount.close.CloseCustomerAccountCommand;
import com.bank.liability.application.customeraccount.close.CloseCustomerAccountService;
import com.bank.liability.domain.customeraccount.entity.CustomerAccount;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/liability/customer-account")
@RequiredArgsConstructor
public class CustomerAccountController {

    private final OpenCustomerAccountService openCustomerAccountService;
    private final CloseCustomerAccountService closeCustomerAccountService;

    @PostMapping("/open")
    public Result<CustomerAccount> open(@Valid @RequestBody OpenCustomerAccountCommand command,
                                        @RequestHeader(value = "operator-no", defaultValue = "SYSTEM") String operatorNo) {
        return Result.success(openCustomerAccountService.open(command, operatorNo));
    }

    @PostMapping("/close")
    public Result<Void> close(@Valid @RequestBody CloseCustomerAccountCommand command,
                               @RequestHeader(value = "operator-no", defaultValue = "SYSTEM") String operatorNo) {
        closeCustomerAccountService.close(command, operatorNo);
        return Result.success();
    }
}
