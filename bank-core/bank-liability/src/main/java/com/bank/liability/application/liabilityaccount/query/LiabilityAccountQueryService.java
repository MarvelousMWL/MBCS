package com.bank.liability.application.liabilityaccount.query;

import com.bank.common.exception.BusinessException;
import com.bank.liability.domain.liabilityaccount.entity.LiabilityAccount;
import com.bank.liability.domain.liabilityaccount.repository.LiabilityAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LiabilityAccountQueryService {

    private final LiabilityAccountRepository liabilityAccountRepository;

    public Optional<LiabilityAccount> findByLiabilityAccountNo(String liabilityAccountNo) {
        return liabilityAccountRepository.findByLiabilityAccountNo(liabilityAccountNo);
    }

    public List<LiabilityAccount> findByCustomerAccountNo(String customerAccountNo) {
        return liabilityAccountRepository.findByCustomerAccountNo(customerAccountNo);
    }

    public List<LiabilityAccount> findAll() {
        return liabilityAccountRepository.findAll();
    }
}
