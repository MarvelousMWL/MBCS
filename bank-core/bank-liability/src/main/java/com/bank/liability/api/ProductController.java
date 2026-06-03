package com.bank.liability.api;
import com.bank.liability.application.product.ProductService;
import com.bank.liability.domain.product.repository.ProductRepository;
import com.bank.liability.domain.product.entity.*;
import com.bank.common.result.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
@RestController @RequestMapping("/api/liability/product") @RequiredArgsConstructor
public class ProductController {
    private final ProductService svc;
    private final ProductRepository r;
    private final ObjectMapper m = new ObjectMapper();

    @GetMapping public Result<List<ProductDefinition>> list() { return Result.success(svc.listAll()); }
    @GetMapping("/{c}") public Result<ProductDefinition> get(@PathVariable String c) { return Result.success(svc.getByProductCode(c)); }
    @PostMapping public Result<Void> create(@RequestBody Map<String,Object> b) { svc.save(m.convertValue(b, ProductDefinition.class)); return Result.success(); }
    @PutMapping("/{c}") public Result<Void> update(@PathVariable String c, @RequestBody Map<String,Object> b) {
        var d = m.convertValue(b, ProductDefinition.class); d.setProductCode(c); svc.update(d);
        cfg(c, b, "controls", ProductControl.class, r::deleteControlByProductCode, r::saveControl);
        cfg(c, b, "termControls", ProductTermControl.class, r::deleteTermControl, r::saveTermControl);
        cfg(c, b, "openControls", ProductOpenControl.class, r::deleteOpenControl, r::saveOpenControl);
        cfg(c, b, "withdrawControls", ProductWithdrawControl.class, r::deleteWithdrawControl, r::saveWithdrawControl);
        cfg(c, b, "maturityControls", ProductMaturityControl.class, r::deleteMaturityControl, r::saveMaturityControl);
        cfg(c, b, "formTransfers", ProductFormTransfer.class, r::deleteFormTransfer, r::saveFormTransfer);
        cfg(c, b, "channelControls", ProductChannelControl.class, r::deleteChannelControl, r::saveChannelControl);
        cfg(c, b, "currencyControls", ProductCurrencyControl.class, r::deleteCurrencyControl, r::saveCurrencyControl);
        cfg(c, b, "institutionControls", ProductInstitutionControl.class, r::deleteInstitutionControl, r::saveInstitutionControl);
        cfg(c, b, "customerControls", ProductCustomerControl.class, r::deleteCustomerControl, r::saveCustomerControl);
        cfg(c, b, "voucherControls", ProductVoucherControl.class, r::deleteVoucherControl, r::saveVoucherControl);
        cfg(c, b, "accountingControls", ProductAccountingControl.class, r::deleteAccountingControl, r::saveAccountingControl);
        // ADBC new tables
        cfg(c, b, "closeControls", ProductCloseControl.class, r::deleteCloseControl, r::saveCloseControl);
        cfg(c, b, "depositControls", ProductDepositControl.class, r::deleteDepositControl, r::saveDepositControl);
        cfg(c, b, "interestDefinitions", ProductInterestDefinition.class, r::deleteInterestDefinition, r::saveInterestDefinition);
        cfg(c, b, "rateDefinitions", ProductRateDefinition.class, r::deleteRateDefinition, r::saveRateDefinition);
        return Result.success();
    }
    private <T> void cfg(String code, Map<String,Object> b, String key, Class<T> cls, java.util.function.Consumer<String> del, java.util.function.Consumer<T> save) {
        var raw = b.get(key); del.accept(code);
        if (raw != null) for (var item : (List<T>)m.convertValue(raw, m.getTypeFactory().constructCollectionType(List.class, cls))) { setCode(item, code); save.accept(item); }
    }
    private void setCode(Object item, String code) {
        if (item instanceof ProductControl p) p.setProductCode(code); else if (item instanceof ProductTermControl p) p.setProductCode(code);
        else if (item instanceof ProductOpenControl p) p.setProductCode(code); else if (item instanceof ProductWithdrawControl p) p.setProductCode(code);
        else if (item instanceof ProductMaturityControl p) p.setProductCode(code); else if (item instanceof ProductFormTransfer p) p.setProductCode(code);
        else if (item instanceof ProductChannelControl p) p.setProductCode(code); else if (item instanceof ProductCurrencyControl p) p.setProductCode(code);
        else if (item instanceof ProductInstitutionControl p) p.setProductCode(code); else if (item instanceof ProductCustomerControl p) p.setProductCode(code);
        else if (item instanceof ProductVoucherControl p) p.setProductCode(code); else if (item instanceof ProductAccountingControl p) p.setProductCode(code);
        else if (item instanceof ProductCloseControl p) p.setProductCode(code); else if (item instanceof ProductDepositControl p) p.setProductCode(code);
        else if (item instanceof ProductInterestDefinition p) p.setProductCode(code); else if (item instanceof ProductRateDefinition p) p.setProductCode(code);
    }
    // GET endpoints
    @GetMapping("/{c}/controls") public Result<?> gc1(@PathVariable String c) { return Result.success(r.findControlsByProductCode(c)); }
    @GetMapping("/{c}/term-controls") public Result<?> gc2(@PathVariable String c) { return Result.success(r.findTermControls(c)); }
    @GetMapping("/{c}/open-controls") public Result<?> gc3(@PathVariable String c) { return Result.success(r.findOpenControls(c)); }
    @GetMapping("/{c}/withdraw-controls") public Result<?> gc4(@PathVariable String c) { return Result.success(r.findWithdrawControls(c)); }
    @GetMapping("/{c}/maturity-controls") public Result<?> gc5(@PathVariable String c) { return Result.success(r.findMaturityControls(c)); }
    @GetMapping("/{c}/form-transfers") public Result<?> gc6(@PathVariable String c) { return Result.success(r.findFormTransfers(c)); }
    @GetMapping("/{c}/channel-controls") public Result<?> gc7(@PathVariable String c) { return Result.success(r.findChannelControls(c)); }
    @GetMapping("/{c}/currency-controls") public Result<?> gc8(@PathVariable String c) { return Result.success(r.findCurrencyControls(c)); }
    @GetMapping("/{c}/institution-controls") public Result<?> gc9(@PathVariable String c) { return Result.success(r.findInstitutionControls(c)); }
    @GetMapping("/{c}/customer-controls") public Result<?> gc10(@PathVariable String c) { return Result.success(r.findCustomerControls(c)); }
    @GetMapping("/{c}/voucher-controls") public Result<?> gc11(@PathVariable String c) { return Result.success(r.findVoucherControls(c)); }
    @GetMapping("/{c}/accounting-controls") public Result<?> gc12(@PathVariable String c) { return Result.success(r.findAccountingControls(c)); }
    // ADBC new GET endpoints
    @GetMapping("/{c}/close-controls") public Result<?> gc13(@PathVariable String c) { return Result.success(r.findCloseControls(c)); }
    @GetMapping("/{c}/deposit-controls") public Result<?> gc14(@PathVariable String c) { return Result.success(r.findDepositControls(c)); }
    @GetMapping("/{c}/interest-definitions") public Result<?> gc15(@PathVariable String c) { return Result.success(r.findInterestDefinitions(c)); }
    @GetMapping("/{c}/rate-definitions") public Result<?> gc16(@PathVariable String c) { return Result.success(r.findRateDefinitions(c)); }
}
