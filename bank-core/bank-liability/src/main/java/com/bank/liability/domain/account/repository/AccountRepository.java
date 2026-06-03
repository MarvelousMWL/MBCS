package com.bank.liability.domain.account.repository;
import com.bank.liability.domain.account.entity.*;
import com.bank.liability.domain.customeraccount.entity.CustomerAccount;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AccountRepository {
    void saveAccount(AccountInfo a); void updateAccount(AccountInfo a);
    Optional<AccountInfo> findAccountByNo(String accountNo);
    List<AccountInfo> findAllAccounts();
    List<AccountCloseControl> findCloseControls(String a); void saveCloseControl(AccountCloseControl c); void deleteCloseControl(String a);
    List<AccountDepositControl> findDepositControls(String a); void saveDepositControl(AccountDepositControl c); void deleteDepositControl(String a);
    List<AccountWithdrawControl> findWithdrawControls(String a); void saveWithdrawControl(AccountWithdrawControl c); void deleteWithdrawControl(String a);
    List<AccountInterestDefinition> findInterestDefinitions(String a); void saveInterestDefinition(AccountInterestDefinition c); void deleteInterestDefinition(String a);
    List<AccountRateDefinition> findRateDefinitions(String a); void saveRateDefinition(AccountRateDefinition c); void deleteRateDefinition(String a);
    List<AccountMaturityDefinition> findMaturityDefinitions(String a); void saveMaturityDefinition(AccountMaturityDefinition c); void deleteMaturityDefinition(String a);
    List<AccountRenewDefinition> findRenewDefinitions(String a); void saveRenewDefinition(AccountRenewDefinition c); void deleteRenewDefinition(String a);
    List<CustomerAccount> findCustomerAccounts(String n); void saveCustomerAccount(CustomerAccount c);
    List<AccountSupplementaryInfo> findSupplementaryInfo(String a); void saveSupplementaryInfo(AccountSupplementaryInfo c);
    AccountBalanceInfo findBalanceInfo(String a); void saveBalanceInfo(AccountBalanceInfo c); void updateBalanceInfo(AccountBalanceInfo c);
    List<BalanceTransactionDetail> queryTransactions(String a, LocalDate s, LocalDate e); void saveTransaction(BalanceTransactionDetail t);
    List<AccountWhitelist> findWhitelist(String a); void saveWhitelist(AccountWhitelist w); void deleteWhitelist(Long id);
    List<AccountFreezeRegister> findFreezeRegisters(String a); void saveFreezeRegister(AccountFreezeRegister r); void updateFreezeRegister(AccountFreezeRegister r);
    List<AccountFreezeDetail> findFreezeDetails(String f); void saveFreezeDetail(AccountFreezeDetail d);
    List<AccountUnfreezeRegister> findUnfreezeRegisters(String a); void saveUnfreezeRegister(AccountUnfreezeRegister r);
    List<AccountDeductionRegister> findDeductionRegisters(String a); void saveDeductionRegister(AccountDeductionRegister r);
}