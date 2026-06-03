package com.bank.liability.domain.liabilityaccount.repository;

import com.bank.liability.domain.liabilityaccount.entity.LiabilityAccount;
import java.util.List;
import java.util.Optional;

public interface LiabilityAccountRepository {
    void save(LiabilityAccount liabilityAccount);
    void update(LiabilityAccount liabilityAccount);
    Optional<LiabilityAccount> findById(Long id);
    Optional<LiabilityAccount> findByLiabilityAccountNo(String liabilityAccountNo);
    List<LiabilityAccount> findByCustomerAccountNo(String customerAccountNo);
    List<LiabilityAccount> findAll();
    boolean existsByLiabilityAccountNo(String liabilityAccountNo);
    Long countByCustomerAccountNo(String customerAccountNo);
    Long countAll();
    Optional<LiabilityAccount> findMaxSubAccountSeqByCustomerAccountNoAndAccountType(String customerAccountNo, String accountType);
}
