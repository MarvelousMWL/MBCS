package com.bank.liability.application.liabilityaccount.close;

import com.bank.common.exception.BusinessException;
import com.bank.liability.domain.customersubaccount.entity.CustomerSubAccount;
import com.bank.liability.domain.customersubaccount.repository.CustomerSubAccountRepository;
import com.bank.liability.domain.enums.SubAccountStatus;
import com.bank.liability.domain.liabilityaccount.entity.LiabilityAccount;
import com.bank.liability.domain.liabilityaccount.repository.LiabilityAccountRepository;
import com.bank.liability.domain.liabilityaccount.service.LiabilityAccountDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CloseLiabilityAccountService {

    private final LiabilityAccountRepository liabilityAccountRepository;
    private final LiabilityAccountDomainService liabilityAccountDomainService;
    private final CustomerSubAccountRepository customerSubAccountRepository;

    @Transactional
    public void close(CloseLiabilityAccountCommand command, String operatorNo) {
        LiabilityAccount liabilityAccount = liabilityAccountRepository
                .findByLiabilityAccountNo(command.getLiabilityAccountNo())
                .orElseThrow(() -> new BusinessException("负债账号不存在"));

        liabilityAccountDomainService.validateClose(liabilityAccount);

        liabilityAccount.setStatus(com.bank.liability.domain.enums.LiabilityAccountStatus.CLOSED);
        liabilityAccount.setCloseDate(LocalDateTime.now());
        liabilityAccount.setUpdatedAt(LocalDateTime.now());

        liabilityAccountRepository.update(liabilityAccount);

        customerSubAccountRepository.findByLiabilityAccountNo(liabilityAccount.getLiabilityAccountNo())
                .ifPresent(subAccount -> {
                    subAccount.setStatus(SubAccountStatus.CLOSED);
                    subAccount.setUpdatedAt(LocalDateTime.now());
                    customerSubAccountRepository.update(subAccount);
                });
    }
}
