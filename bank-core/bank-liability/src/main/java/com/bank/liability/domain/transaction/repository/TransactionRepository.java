package com.bank.liability.domain.transaction.repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bank.liability.domain.enums.TransactionType;
import com.bank.liability.domain.transaction.entity.LiabilityTransaction;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository {
    void save(LiabilityTransaction transaction);
    void update(LiabilityTransaction transaction);
    List<LiabilityTransaction> findAll();
    Optional<LiabilityTransaction> findById(Long id);
    Optional<LiabilityTransaction> findByTransactionNo(String transactionNo);
    List<LiabilityTransaction> findByLiabilityAccountNo(String liabilityAccountNo);
    List<LiabilityTransaction> findByRelatedTransactionNo(String relatedTransactionNo);
    IPage<LiabilityTransaction> findPage(IPage<LiabilityTransaction> page, String liabilityAccountNo, TransactionType transactionType, LocalDateTime startDate, LocalDateTime endDate);
}
