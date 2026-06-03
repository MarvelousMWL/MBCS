package com.bank.liability.infrastructure.persistence;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bank.liability.domain.account.entity.*;
import com.bank.liability.domain.account.repository.AccountRepository;
import com.bank.liability.domain.customeraccount.entity.CustomerAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository @RequiredArgsConstructor
public class AccountRepositoryImpl implements AccountRepository {
    private final AccountInfoMapper a; private final AccountCloseControlMapper b;
    private final AccountDepositControlMapper c; private final AccountWithdrawControlMapper d;
    private final AccountInterestDefinitionMapper e; private final AccountRateDefinitionMapper f;
    private final AccountMaturityDefinitionMapper g; private final AccountRenewDefinitionMapper h;
    private final com.bank.liability.infrastructure.persistence.CustomerAccountMapper i;
    private final AccountSupplementaryInfoMapper j; private final AccountBalanceInfoMapper k;
    private final BalanceTransactionDetailMapper l; private final AccountWhitelistMapper m;
    private final AccountFreezeRegisterMapper n; private final AccountFreezeDetailMapper o;
    private final AccountUnfreezeRegisterMapper p; private final AccountDeductionRegisterMapper q;

    @Override public void saveAccount(AccountInfo a) { this.a.insert(a); }
    @Override public void updateAccount(AccountInfo a) { this.a.updateById(a); }
    @Override public Optional<AccountInfo> findAccountByNo(String accountNo) { return Optional.ofNullable(this.a.selectOne(wq("account_no", accountNo))); }
    @Override public List<AccountInfo> findAllAccounts() { return this.a.selectList(null); }

    private <T> QueryWrapper<T> wq(String col, Object val) { return new QueryWrapper<T>().eq(col, val); }
    private <T> List<T> lst(Class<T> cls, String col, Object val) { return ((com.baomidou.mybatisplus.core.mapper.BaseMapper<T>)mp(cls)).selectList(wq(col, val)); }
    private <T> void del(Class<T> cls, String col, Object val) { ((com.baomidou.mybatisplus.core.mapper.BaseMapper<T>)mp(cls)).delete(wq(col, val)); }
    @SuppressWarnings("unchecked")
    private <T> Object mp(Class<T> cls) {
        if (cls==AccountInfo.class) return a; if (cls==AccountCloseControl.class) return b;
        if (cls==AccountDepositControl.class) return c; if (cls==AccountWithdrawControl.class) return d;
        if (cls==AccountInterestDefinition.class) return e; if (cls==AccountRateDefinition.class) return f;
        if (cls==AccountMaturityDefinition.class) return g; if (cls==AccountRenewDefinition.class) return h;
        if (cls==CustomerAccount.class) return i; if (cls==AccountSupplementaryInfo.class) return j;
        if (cls==AccountBalanceInfo.class) return k; if (cls==BalanceTransactionDetail.class) return l;
        if (cls==AccountWhitelist.class) return m; if (cls==AccountFreezeRegister.class) return n;
        if (cls==AccountFreezeDetail.class) return o; if (cls==AccountUnfreezeRegister.class) return p;
        if (cls==AccountDeductionRegister.class) return q; return null;
    }
    // Controls
    @Override public List<AccountCloseControl> findCloseControls(String a) { return lst(AccountCloseControl.class,"account_no",a); }
    @Override public void saveCloseControl(AccountCloseControl x) { b.insert(x); }
    @Override public void deleteCloseControl(String a) { del(AccountCloseControl.class,"account_no",a); }
    @Override public List<AccountDepositControl> findDepositControls(String a) { return lst(AccountDepositControl.class,"account_no",a); }
    @Override public void saveDepositControl(AccountDepositControl x) { c.insert(x); }
    @Override public void deleteDepositControl(String a) { del(AccountDepositControl.class,"account_no",a); }
    @Override public List<AccountWithdrawControl> findWithdrawControls(String a) { return lst(AccountWithdrawControl.class,"account_no",a); }
    @Override public void saveWithdrawControl(AccountWithdrawControl x) { d.insert(x); }
    @Override public void deleteWithdrawControl(String a) { del(AccountWithdrawControl.class,"account_no",a); }
    @Override public List<AccountInterestDefinition> findInterestDefinitions(String a) { return lst(AccountInterestDefinition.class,"account_no",a); }
    @Override public void saveInterestDefinition(AccountInterestDefinition x) { e.insert(x); }
    @Override public void deleteInterestDefinition(String a) { del(AccountInterestDefinition.class,"account_no",a); }
    @Override public List<AccountRateDefinition> findRateDefinitions(String a) { return lst(AccountRateDefinition.class,"account_no",a); }
    @Override public void saveRateDefinition(AccountRateDefinition x) { f.insert(x); }
    @Override public void deleteRateDefinition(String a) { del(AccountRateDefinition.class,"account_no",a); }
    @Override public List<AccountMaturityDefinition> findMaturityDefinitions(String a) { return lst(AccountMaturityDefinition.class,"account_no",a); }
    @Override public void saveMaturityDefinition(AccountMaturityDefinition x) { g.insert(x); }
    @Override public void deleteMaturityDefinition(String a) { del(AccountMaturityDefinition.class,"account_no",a); }
    @Override public List<AccountRenewDefinition> findRenewDefinitions(String a) { return lst(AccountRenewDefinition.class,"account_no",a); }
    @Override public void saveRenewDefinition(AccountRenewDefinition x) { h.insert(x); }
    @Override public void deleteRenewDefinition(String a) { del(AccountRenewDefinition.class,"account_no",a); }
    // Supporting
    @Override public List<CustomerAccount> findCustomerAccounts(String n) { return lst(CustomerAccount.class,"customer_no",n); }
    @Override public void saveCustomerAccount(CustomerAccount x) { i.insert(x); }
    @Override public List<AccountSupplementaryInfo> findSupplementaryInfo(String a) { return lst(AccountSupplementaryInfo.class,"account_no",a); }
    @Override public void saveSupplementaryInfo(AccountSupplementaryInfo x) { j.insert(x); }
    @Override public AccountBalanceInfo findBalanceInfo(String a) { return k.selectOne(wq("account_no",a)); }
    @Override public void saveBalanceInfo(AccountBalanceInfo x) { k.insert(x); }
    @Override public void updateBalanceInfo(AccountBalanceInfo x) { k.updateById(x); }
    // Transaction
    @Override public List<BalanceTransactionDetail> queryTransactions(String a, LocalDate s, LocalDate e) { return l.selectList(new QueryWrapper<BalanceTransactionDetail>().eq("account_no",a).between("txn_date",s,e).orderByAsc("txn_date")); }
    @Override public void saveTransaction(BalanceTransactionDetail t) { l.insert(t); }
    // Registers
    @Override public List<AccountWhitelist> findWhitelist(String a) { return lst(AccountWhitelist.class,"account_no",a); }
    @Override public void saveWhitelist(AccountWhitelist w) { m.insert(w); }
    @Override public void deleteWhitelist(Long id) { m.deleteById(id); }
    @Override public List<AccountFreezeRegister> findFreezeRegisters(String a) { return lst(AccountFreezeRegister.class,"account_no",a); }
    @Override public void saveFreezeRegister(AccountFreezeRegister r) { n.insert(r); }
    @Override public void updateFreezeRegister(AccountFreezeRegister r) { n.updateById(r); }
    @Override public List<AccountFreezeDetail> findFreezeDetails(String f) { return lst(AccountFreezeDetail.class,"freeze_no",f); }
    @Override public void saveFreezeDetail(AccountFreezeDetail d) { o.insert(d); }
    @Override public List<AccountUnfreezeRegister> findUnfreezeRegisters(String a) { return lst(AccountUnfreezeRegister.class,"account_no",a); }
    @Override public void saveUnfreezeRegister(AccountUnfreezeRegister r) { p.insert(r); }
    @Override public List<AccountDeductionRegister> findDeductionRegisters(String a) { return lst(AccountDeductionRegister.class,"account_no",a); }
    @Override public void saveDeductionRegister(AccountDeductionRegister r) { q.insert(r); }
}