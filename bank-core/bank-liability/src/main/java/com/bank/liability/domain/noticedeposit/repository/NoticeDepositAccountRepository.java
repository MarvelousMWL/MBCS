package com.bank.liability.domain.noticedeposit.repository;

import com.bank.liability.domain.noticedeposit.entity.NoticeDepositAccount;
import java.util.List;
import java.util.Optional;

public interface NoticeDepositAccountRepository {
    void save(NoticeDepositAccount account);
    void update(NoticeDepositAccount account);
    Optional<NoticeDepositAccount> findById(Long id);
    Optional<NoticeDepositAccount> findByNoticeDepositAccountNo(String noticeDepositAccountNo);
    List<NoticeDepositAccount> findByCustomerAccountNo(String customerAccountNo);
    List<NoticeDepositAccount> findByCurrentAccountNo(String currentAccountNo);
    boolean existsByNoticeDepositAccountNo(String noticeDepositAccountNo);
}