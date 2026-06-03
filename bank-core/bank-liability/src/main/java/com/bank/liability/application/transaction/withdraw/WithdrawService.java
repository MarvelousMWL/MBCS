package com.bank.liability.application.transaction.withdraw;

import com.bank.common.exception.BusinessException;
import com.bank.liability.domain.enums.TransactionType;
import com.bank.liability.domain.liabilityaccount.entity.LiabilityAccount;
import com.bank.liability.domain.liabilityaccount.repository.LiabilityAccountRepository;
import com.bank.liability.domain.liabilityaccount.service.LiabilityAccountDomainService;
import com.bank.liability.domain.transaction.entity.LiabilityTransaction;
import com.bank.liability.domain.transaction.service.TransactionDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class WithdrawService {

    private final LiabilityAccountRepository liabilityAccountRepository;
    private final LiabilityAccountDomainService liabilityAccountDomainService;
    private final TransactionDomainService transactionDomainService;

    @Transactional
    public LiabilityTransaction withdraw(WithdrawCommand command) {
        LiabilityAccount account = liabilityAccountRepository
                .findByLiabilityAccountNo(command.getLiabilityAccountNo())
                .orElseThrow(() -> new BusinessException("负债账号不存在"));

        if (!account.isNormal()) {
            throw new BusinessException("负债账号状态不允许取款");
        }

        liabilityAccountDomainService.validateWithdraw(account, command.getAmount());

        BigDecimal balanceBefore = account.getBalance();
        liabilityAccountDomainService.withdraw(account, command.getAmount());
        account.setUpdatedAt(java.time.LocalDateTime.now());
        liabilityAccountRepository.update(account);

        return transactionDomainService.createTransaction(
                account.getLiabilityAccountNo(),
                TransactionType.WITHDRAW,
                command.getAmount(),
                balanceBefore,
                account.getBalance(),
                command.getOperatorNo(),
                command.getRemark(),
                null
        );
    }
}
