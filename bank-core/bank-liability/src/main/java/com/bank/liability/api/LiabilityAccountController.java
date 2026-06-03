package com.bank.liability.api;

import com.bank.common.result.Result;
import com.bank.liability.application.liabilityaccount.open.OpenLiabilityAccountCommand;
import com.bank.liability.application.liabilityaccount.open.OpenLiabilityAccountService;
import com.bank.liability.application.liabilityaccount.close.CloseLiabilityAccountCommand;
import com.bank.liability.application.liabilityaccount.close.CloseLiabilityAccountService;
import com.bank.liability.application.liabilityaccount.query.LiabilityAccountQueryService;
import com.bank.liability.application.liabilityaccount.freeze.FreezeLiabilityAccountCommand;
import com.bank.liability.application.liabilityaccount.freeze.FreezeLiabilityAccountService;
import com.bank.liability.domain.liabilityaccount.entity.LiabilityAccount;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/liability/liability-account")
@RequiredArgsConstructor
public class LiabilityAccountController {

    private final OpenLiabilityAccountService openLiabilityAccountService;
    private final CloseLiabilityAccountService closeLiabilityAccountService;
    private final LiabilityAccountQueryService liabilityAccountQueryService;
    private final FreezeLiabilityAccountService freezeLiabilityAccountService;

    @PostMapping("/open")
    public Result<LiabilityAccount> open(@Valid @RequestBody OpenLiabilityAccountCommand command,
                                          @RequestHeader(value = "operator-no", defaultValue = "SYSTEM") String operatorNo) {
        return Result.success(openLiabilityAccountService.open(command, operatorNo));
    }

    @PostMapping("/close")
    public Result<Void> close(@Valid @RequestBody CloseLiabilityAccountCommand command,
                               @RequestHeader(value = "operator-no", defaultValue = "SYSTEM") String operatorNo) {
        closeLiabilityAccountService.close(command, operatorNo);
        return Result.success();
    }

    @PostMapping("/freeze")
    public Result<Void> freeze(@Valid @RequestBody FreezeLiabilityAccountCommand command,
                                @RequestHeader(value = "operator-no", defaultValue = "SYSTEM") String operatorNo) {
        freezeLiabilityAccountService.freeze(command, operatorNo);
        return Result.success();
    }

    @PostMapping("/unfreeze")
    public Result<Void> unfreeze(@Valid @RequestBody FreezeLiabilityAccountCommand command,
                                  @RequestHeader(value = "operator-no", defaultValue = "SYSTEM") String operatorNo) {
        freezeLiabilityAccountService.unfreeze(command, operatorNo);
        return Result.success();
    }

    @GetMapping("/{liabilityAccountNo}")
    public Result<LiabilityAccount> findByNo(@PathVariable String liabilityAccountNo) {
        return Result.success(liabilityAccountQueryService.findByLiabilityAccountNo(liabilityAccountNo).orElse(null));
    }

    @GetMapping("/customer-account/{customerAccountNo}")
    public Result<List<LiabilityAccount>> findByCustomerAccountNo(@PathVariable String customerAccountNo) {
        return Result.success(liabilityAccountQueryService.findByCustomerAccountNo(customerAccountNo));
    }

    @GetMapping
    public Result<List<LiabilityAccount>> findAll() {
        return Result.success(liabilityAccountQueryService.findAll());
    }
}
