package com.bank.customer.api;

import com.bank.common.result.Result;
import com.bank.customer.application.command.create.CreateCustomerCommand;
import com.bank.customer.application.command.create.CreateCustomerService;
import com.bank.customer.application.command.delete.DeleteCustomerService;
import com.bank.customer.application.command.update.UpdateCustomerCommand;
import com.bank.customer.application.command.update.UpdateCustomerService;
import com.bank.customer.application.query.CustomerQueryService;
import com.bank.customer.domain.entity.Customer;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CreateCustomerService createCustomerService;
    private final UpdateCustomerService updateCustomerService;
    private final DeleteCustomerService deleteCustomerService;
    private final CustomerQueryService customerQueryService;

    @PostMapping
    public Result<Customer> create(@Valid @RequestBody CreateCustomerCommand command) {
        return Result.success(createCustomerService.create(command));
    }

    @PutMapping
    public Result<Customer> update(@Valid @RequestBody UpdateCustomerCommand command) {
        return Result.success(updateCustomerService.update(command));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        deleteCustomerService.delete(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<Customer> findById(@PathVariable Long id) {
        return Result.success(customerQueryService.findById(id).orElse(null));
    }

    @GetMapping("/no/{customerNo}")
    public Result<Customer> findByCustomerNo(@PathVariable String customerNo) {
        return Result.success(customerQueryService.findByCustomerNo(customerNo).orElse(null));
    }

    @GetMapping
    public Result<List<Customer>> findAll() {
        return Result.success(customerQueryService.findAll());
    }
}
