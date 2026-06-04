package com.bank.liability.api;

import com.bank.common.result.Result;
import com.bank.liability.application.batch.BatchResult;
import com.bank.liability.application.certificatedeposit.batch.CDAutoMaturityBatchCommand;
import com.bank.liability.application.certificatedeposit.batch.CDAutoMaturityBatchService;
import com.bank.liability.application.certificatedeposit.issue.IssueCDProductCommand;
import com.bank.liability.application.certificatedeposit.issue.IssueCDProductService;
import com.bank.liability.application.certificatedeposit.subscription.SubscribeCDCommand;
import com.bank.liability.application.certificatedeposit.subscription.SubscribeCDService;
import com.bank.liability.application.certificatedeposit.redemption.RedeemCDCommand;
import com.bank.liability.application.certificatedeposit.redemption.RedeemCDService;
import com.bank.liability.application.certificatedeposit.transfer.TransferCDCommand;
import com.bank.liability.application.certificatedeposit.transfer.TransferCDService;
import com.bank.liability.application.certificatedeposit.query.CDQueryService;
import com.bank.liability.domain.certificatedeposit.entity.CertificateDepositAccount;
import com.bank.liability.domain.certificatedeposit.entity.CertificateDepositProduct;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/liability/cd")
@RequiredArgsConstructor
public class CertificateDepositController {

    private final IssueCDProductService issueCDProductService;
    private final SubscribeCDService subscribeCDService;
    private final RedeemCDService redeemCDService;
    private final TransferCDService transferCDService;
    private final CDQueryService cdQueryService;
    private final CDAutoMaturityBatchService cdAutoMaturityBatchService;

    @PostMapping("/product")
    public Result<CertificateDepositProduct> issueProduct(@Valid @RequestBody IssueCDProductCommand command) {
        return Result.success(issueCDProductService.issue(command));
    }

    @PostMapping("/subscribe")
    public Result<CertificateDepositAccount> subscribe(@Valid @RequestBody SubscribeCDCommand command,
                                                        @RequestHeader(value = "operator-no", defaultValue = "SYSTEM") String operatorNo) {
        command.setOperatorNo(operatorNo);
        return Result.success(subscribeCDService.subscribe(command));
    }

    @PostMapping("/redeem")
    public Result<CertificateDepositAccount> redeem(@Valid @RequestBody RedeemCDCommand command,
                                                     @RequestHeader(value = "operator-no", defaultValue = "SYSTEM") String operatorNo) {
        command.setOperatorNo(operatorNo);
        return Result.success(redeemCDService.redeem(command));
    }

    @PostMapping("/transfer")
    public Result<CertificateDepositAccount> transfer(@Valid @RequestBody TransferCDCommand command,
                                                       @RequestHeader(value = "operator-no", defaultValue = "SYSTEM") String operatorNo) {
        command.setOperatorNo(operatorNo);
        return Result.success(transferCDService.transfer(command));
    }

    @GetMapping("/products")
    public Result<List<CertificateDepositProduct>> listProducts() {
        return Result.success(cdQueryService.listAllProducts());
    }

    @GetMapping("/product/{productCode}")
    public Result<CertificateDepositProduct> getProduct(@PathVariable String productCode) {
        return Result.success(cdQueryService.findProductByCode(productCode));
    }

    @GetMapping("/accounts")
    public Result<List<CertificateDepositAccount>> listAccounts(
            @RequestParam(required = false) String customerAccountNo,
            @RequestParam(required = false) String productCode) {
        if (customerAccountNo != null) {
            return Result.success(cdQueryService.listAccountsByCustomer(customerAccountNo));
        }
        if (productCode != null) {
            return Result.success(cdQueryService.listAccountsByProduct(productCode));
        }
        return Result.success(cdQueryService.listAllAccounts());
    }

    @GetMapping("/account/{cdAccountNo}")
    public Result<CertificateDepositAccount> getAccount(@PathVariable String cdAccountNo) {
        return Result.success(cdQueryService.findAccountByNo(cdAccountNo));
    }

    @PostMapping("/batch/maturity")
    public Result<BatchResult> executeBatchMaturity(@RequestBody(required = false) CDAutoMaturityBatchCommand command) {
        if (command == null) {
            command = new CDAutoMaturityBatchCommand();
        }
        return Result.success(cdAutoMaturityBatchService.executeBatch(command));
    }
}