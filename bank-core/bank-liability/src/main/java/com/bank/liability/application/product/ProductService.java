package com.bank.liability.application.product;
import com.bank.liability.domain.product.entity.*;
import com.bank.liability.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service @RequiredArgsConstructor
public class ProductService {
    private final ProductRepository r;
    public List<ProductDefinition> listAll() { return r.findAll(); }
    public ProductDefinition getByProductCode(String code) { return r.findByProductCode(code).orElseThrow(() -> new RuntimeException("产品不存在: " + code)); }
    @Transactional public void save(ProductDefinition def) { if (def.getProductCode() == null) throw new RuntimeException("产品代码不能为空"); r.save(def); }
    @Transactional public void update(ProductDefinition def) { var e = r.findByProductCode(def.getProductCode()).orElseThrow(); def.setId(e.getId()); r.update(def); }
    private void setCode(Object item, String code) {
        if (item instanceof ProductControl p) p.setProductCode(code);
        else if (item instanceof ProductTermControl p) p.setProductCode(code);
        else if (item instanceof ProductOpenControl p) p.setProductCode(code);
        else if (item instanceof ProductWithdrawControl p) p.setProductCode(code);
        else if (item instanceof ProductMaturityControl p) p.setProductCode(code);
        else if (item instanceof ProductFormTransfer p) p.setProductCode(code);
        else if (item instanceof ProductChannelControl p) p.setProductCode(code);
        else if (item instanceof ProductCurrencyControl p) p.setProductCode(code);
        else if (item instanceof ProductInstitutionControl p) p.setProductCode(code);
        else if (item instanceof ProductCustomerControl p) p.setProductCode(code);
        else if (item instanceof ProductVoucherControl p) p.setProductCode(code);
        else if (item instanceof ProductAccountingControl p) p.setProductCode(code);
        else if (item instanceof ProductCloseControl p) p.setProductCode(code);
        else if (item instanceof ProductDepositControl p) p.setProductCode(code);
        else if (item instanceof ProductInterestDefinition p) p.setProductCode(code);
        else if (item instanceof ProductRateDefinition p) p.setProductCode(code);
    }
}
