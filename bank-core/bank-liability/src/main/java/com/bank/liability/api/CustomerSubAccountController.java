package com.bank.liability.api;

import com.bank.common.result.Result;
import com.bank.liability.application.customersubaccount.open.OpenSubAccountCommand;
import com.bank.liability.application.customersubaccount.open.OpenSubAccountService;
import com.bank.liability.application.customersubaccount.query.CustomerSubAccountQueryService;
import com.bank.liability.domain.customersubaccount.entity.CustomerSubAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/liability/customer-sub-account")
@RequiredArgsConstructor
public class CustomerSubAccountController {

    private final CustomerSubAccountQueryService customerSubAccountQueryService;
    private final OpenSubAccountService openSubAccountService;

    @PostMapping("/open")
    public Result<CustomerSubAccount> open(@RequestBody @Valid OpenSubAccountCommand command) {
        return Result.success(openSubAccountService.open(command, null));
    }

    @GetMapping("/customer-account/{customerAccountNo}")
    public Result<List<CustomerSubAccount>> findByCustomerAccountNo(@PathVariable String customerAccountNo) {
        return Result.success(customerSubAccountQueryService.findByCustomerAccountNo(customerAccountNo));
    }

    @GetMapping("/customer-account/{customerAccountNo}/account-type/{accountType}")
    public Result<List<CustomerSubAccount>> findByCustomerAccountNoAndAccountType(
            @PathVariable String customerAccountNo, @PathVariable String accountType) {
        return Result.success(customerSubAccountQueryService.findByCustomerAccountNoAndAccountType(customerAccountNo, accountType));
    }
}
