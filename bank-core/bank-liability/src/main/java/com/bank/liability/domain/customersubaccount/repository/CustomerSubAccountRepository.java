package com.bank.liability.domain.customersubaccount.repository;

import com.bank.liability.domain.customersubaccount.entity.CustomerSubAccount;
import java.util.List;
import java.util.Optional;

public interface CustomerSubAccountRepository {
    void save(CustomerSubAccount customerSubAccount);
    void update(CustomerSubAccount customerSubAccount);
    Optional<CustomerSubAccount> findById(Long id);
    Optional<CustomerSubAccount> findByLiabilityAccountNo(String liabilityAccountNo);
    Optional<CustomerSubAccount> findByCustomerAccountNoAndSubAccountSeq(String customerAccountNo, String subAccountSeq);
    List<CustomerSubAccount> findByCustomerAccountNo(String customerAccountNo);
    List<CustomerSubAccount> findByCustomerAccountNoAndAccountType(String customerAccountNo, String accountType);
    boolean existsByCustomerAccountNoAndSubAccountSeq(String customerAccountNo, String subAccountSeq);
    Long countByCustomerAccountNoAndAccountType(String customerAccountNo, String accountType);
}
