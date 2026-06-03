package com.bank.liability.infrastructure.persistence;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bank.liability.domain.product.entity.*;
import com.bank.liability.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository @RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {
    private final ProductDefinitionMapper a;
    private final ProductControlMapper b;
    private final ProductTermControlMapper c;
    private final ProductOpenControlMapper d;
    private final ProductWithdrawControlMapper e;
    private final ProductMaturityControlMapper f;
    private final ProductFormTransferMapper g;
    private final ProductChannelControlMapper h;
    private final ProductCurrencyControlMapper i;
    private final ProductInstitutionControlMapper j;
    private final ProductCustomerControlMapper k;
    private final ProductVoucherControlMapper l;
    private final ProductAccountingControlMapper m;

    // Helper
    private <T> void del(Class<T> cls, String code) { ((com.baomidou.mybatisplus.core.mapper.BaseMapper<T>)getMapper(cls)).delete(wq("product_code", code)); }
    private <T> List<T> lst(Class<T> cls, String code) { return ((com.baomidou.mybatisplus.core.mapper.BaseMapper<T>)getMapper(cls)).selectList(wq("product_code", code)); }
    @SuppressWarnings("unchecked")
    private <T> Object getMapper(Class<T> cls) {
        if (cls == ProductDefinition.class) return a; if (cls == ProductControl.class) return b;
        if (cls == ProductTermControl.class) return c; if (cls == ProductOpenControl.class) return d;
        if (cls == ProductWithdrawControl.class) return e; if (cls == ProductMaturityControl.class) return f;
        if (cls == ProductFormTransfer.class) return g; if (cls == ProductChannelControl.class) return h;
        if (cls == ProductCurrencyControl.class) return i; if (cls == ProductInstitutionControl.class) return j;
        if (cls == ProductCustomerControl.class) return k; if (cls == ProductVoucherControl.class) return l;
        if (cls == ProductAccountingControl.class) return m; return null;
    }

    @Override public void save(ProductDefinition d) { a.insert(d); }
    @Override public void update(ProductDefinition d) { a.updateById(d); }
    @Override public void deleteByProductCode(String code) { for (var cls : List.of(ProductDefinition.class,ProductControl.class,ProductTermControl.class,ProductOpenControl.class,ProductWithdrawControl.class,ProductMaturityControl.class,ProductFormTransfer.class,ProductChannelControl.class,ProductCurrencyControl.class,ProductInstitutionControl.class,ProductCustomerControl.class,ProductVoucherControl.class,ProductAccountingControl.class)) del(cls, code); }
    @Override public Optional<ProductDefinition> findByProductCode(String code) { return Optional.ofNullable(a.selectOne(wq("product_code", code))); }
    @Override public List<ProductDefinition> findAll() { return a.selectList(null); }
    @Override public List<ProductDefinition> findByDepositType(String t) { return a.selectList(wq("deposit_type", t)); }
    // Phase 1
    @Override public void saveControl(ProductControl x) { b.insert(x); }
    @Override public void deleteControlByProductCode(String code) { del(ProductControl.class, code); }
    @Override public List<ProductControl> findControlsByProductCode(String code) { return lst(ProductControl.class, code); }
    // Phase 2
    @Override public void saveTermControl(ProductTermControl x) { c.insert(x); }
    @Override public void deleteTermControl(String code) { del(ProductTermControl.class, code); }
    @Override public List<ProductTermControl> findTermControls(String code) { return lst(ProductTermControl.class, code); }
    @Override public void saveOpenControl(ProductOpenControl x) { d.insert(x); }
    @Override public void deleteOpenControl(String code) { del(ProductOpenControl.class, code); }
    @Override public List<ProductOpenControl> findOpenControls(String code) { return lst(ProductOpenControl.class, code); }
    @Override public void saveWithdrawControl(ProductWithdrawControl x) { e.insert(x); }
    @Override public void deleteWithdrawControl(String code) { del(ProductWithdrawControl.class, code); }
    @Override public List<ProductWithdrawControl> findWithdrawControls(String code) { return lst(ProductWithdrawControl.class, code); }
    @Override public void saveMaturityControl(ProductMaturityControl x) { f.insert(x); }
    @Override public void deleteMaturityControl(String code) { del(ProductMaturityControl.class, code); }
    @Override public List<ProductMaturityControl> findMaturityControls(String code) { return lst(ProductMaturityControl.class, code); }
    @Override public void saveFormTransfer(ProductFormTransfer x) { g.insert(x); }
    @Override public void deleteFormTransfer(String code) { del(ProductFormTransfer.class, code); }
    @Override public List<ProductFormTransfer> findFormTransfers(String code) { return lst(ProductFormTransfer.class, code); }
    // Phase 3
    @Override public void saveChannelControl(ProductChannelControl x) { h.insert(x); }
    @Override public void deleteChannelControl(String code) { del(ProductChannelControl.class, code); }
    @Override public List<ProductChannelControl> findChannelControls(String code) { return lst(ProductChannelControl.class, code); }
    @Override public void saveCurrencyControl(ProductCurrencyControl x) { i.insert(x); }
    @Override public void deleteCurrencyControl(String code) { del(ProductCurrencyControl.class, code); }
    @Override public List<ProductCurrencyControl> findCurrencyControls(String code) { return lst(ProductCurrencyControl.class, code); }
    @Override public void saveInstitutionControl(ProductInstitutionControl x) { j.insert(x); }
    @Override public void deleteInstitutionControl(String code) { del(ProductInstitutionControl.class, code); }
    @Override public List<ProductInstitutionControl> findInstitutionControls(String code) { return lst(ProductInstitutionControl.class, code); }
    @Override public void saveCustomerControl(ProductCustomerControl x) { k.insert(x); }
    @Override public void deleteCustomerControl(String code) { del(ProductCustomerControl.class, code); }
    @Override public List<ProductCustomerControl> findCustomerControls(String code) { return lst(ProductCustomerControl.class, code); }
    @Override public void saveVoucherControl(ProductVoucherControl x) { l.insert(x); }
    @Override public void deleteVoucherControl(String code) { del(ProductVoucherControl.class, code); }
    @Override public List<ProductVoucherControl> findVoucherControls(String code) { return lst(ProductVoucherControl.class, code); }
    @Override public void saveAccountingControl(ProductAccountingControl x) { m.insert(x); }
    @Override public void deleteAccountingControl(String code) { del(ProductAccountingControl.class, code); }
    @Override public List<ProductAccountingControl> findAccountingControls(String code) { return lst(ProductAccountingControl.class, code); }
    private <T> QueryWrapper<T> wq(String col, Object val) { return new QueryWrapper<T>().eq(col, val); }
}