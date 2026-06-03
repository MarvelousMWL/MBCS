package com.bank.liability.application.liabilityaccount.freeze;

import com.bank.common.exception.BusinessException;
import com.bank.liability.domain.enums.LiabilityAccountStatus;
import com.bank.liability.domain.liabilityaccount.entity.LiabilityAccount;
import com.bank.liability.domain.liabilityaccount.repository.LiabilityAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FreezeLiabilityAccountService {

    private final LiabilityAccountRepository liabilityAccountRepository;

    @Transactional
    public void freeze(FreezeLiabilityAccountCommand command, String operatorNo) {
        LiabilityAccount account = liabilityAccountRepository
                .findByLiabilityAccountNo(command.getLiabilityAccountNo())
                .orElseThrow(() -> new BusinessException("负债账号不存在"));

        if (account.getStatus() != LiabilityAccountStatus.NORMAL) {
            throw new BusinessException("仅正常状态的账户可冻结");
        }

        account.setStatus(LiabilityAccountStatus.FROZEN);
        account.setUpdatedAt(LocalDateTime.now());
        liabilityAccountRepository.update(account);
    }

    @Transactional
    public void unfreeze(FreezeLiabilityAccountCommand command, String operatorNo) {
        LiabilityAccount account = liabilityAccountRepository
                .findByLiabilityAccountNo(command.getLiabilityAccountNo())
                .orElseThrow(() -> new BusinessException("负债账号不存在"));

        if (account.getStatus() != LiabilityAccountStatus.FROZEN) {
            throw new BusinessException("仅冻结状态的账户可解冻");
        }

        account.setStatus(LiabilityAccountStatus.NORMAL);
        account.setUpdatedAt(LocalDateTime.now());
        liabilityAccountRepository.update(account);
    }
}
