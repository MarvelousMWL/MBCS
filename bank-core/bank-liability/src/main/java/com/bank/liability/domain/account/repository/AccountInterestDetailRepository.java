package com.bank.liability.domain.account.repository;

import com.bank.liability.domain.account.entity.AccountInterestDetail;
import java.time.LocalDate;
import java.util.List;

public interface AccountInterestDetailRepository {
    void save(AccountInterestDetail detail);
    void update(AccountInterestDetail detail);
    List<AccountInterestDetail> findByBatchNo(String batchNo);
    List<AccountInterestDetail> findByAccountNo(String accountNo);
    List<AccountInterestDetail> findByCalcDate(LocalDate calcDate);
    List<AccountInterestDetail> findByStatusAndBatchType(String status, String batchType);
    List<AccountInterestDetail> findByAccountNoAndCalcDate(String accountNo, LocalDate calcDate);
    long countByBatchNo(String batchNo);
    long sumDailyInterestByBatchNo(String batchNo);
}
