package com.bank.liability.domain.product.repository;
import com.bank.liability.domain.product.entity.*;
import java.util.List;
import java.util.Optional;
public interface ProductRepository {
    void save(ProductDefinition d); void update(ProductDefinition d); void deleteByProductCode(String code);
    Optional<ProductDefinition> findByProductCode(String code);
    List<ProductDefinition> findAll(); List<ProductDefinition> findByDepositType(String t);
    // Phase 1
    void saveControl(ProductControl c); void deleteControlByProductCode(String code);
    List<ProductControl> findControlsByProductCode(String code);
    // Phase 2
    List<ProductTermControl> findTermControls(String code); void saveTermControl(ProductTermControl c); void deleteTermControl(String code);
    List<ProductOpenControl> findOpenControls(String code); void saveOpenControl(ProductOpenControl c); void deleteOpenControl(String code);
    List<ProductWithdrawControl> findWithdrawControls(String code); void saveWithdrawControl(ProductWithdrawControl c); void deleteWithdrawControl(String code);
    List<ProductMaturityControl> findMaturityControls(String code); void saveMaturityControl(ProductMaturityControl c); void deleteMaturityControl(String code);
    List<ProductFormTransfer> findFormTransfers(String code); void saveFormTransfer(ProductFormTransfer c); void deleteFormTransfer(String code);
    // Phase 3
    List<ProductChannelControl> findChannelControls(String code); void saveChannelControl(ProductChannelControl c); void deleteChannelControl(String code);
    List<ProductCurrencyControl> findCurrencyControls(String code); void saveCurrencyControl(ProductCurrencyControl c); void deleteCurrencyControl(String code);
    List<ProductInstitutionControl> findInstitutionControls(String code); void saveInstitutionControl(ProductInstitutionControl c); void deleteInstitutionControl(String code);
    List<ProductCustomerControl> findCustomerControls(String code); void saveCustomerControl(ProductCustomerControl c); void deleteCustomerControl(String code);
    List<ProductVoucherControl> findVoucherControls(String code); void saveVoucherControl(ProductVoucherControl c); void deleteVoucherControl(String code);
    List<ProductAccountingControl> findAccountingControls(String code); void saveAccountingControl(ProductAccountingControl c); void deleteAccountingControl(String code);
}