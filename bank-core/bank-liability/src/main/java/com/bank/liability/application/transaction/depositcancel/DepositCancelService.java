package com.bank.liability.application.transaction.depositcancel;

import com.bank.common.exception.BusinessException;
import com.bank.liability.domain.enums.TransactionType;
import com.bank.liability.domain.liabilityaccount.entity.LiabilityAccount;
import com.bank.liability.domain.liabilityaccount.repository.LiabilityAccountRepository;
import com.bank.liability.domain.liabilityaccount.service.LiabilityAccountDomainService;
import com.bank.liability.domain.transaction.entity.LiabilityTransaction;
import com.bank.liability.domain.transaction.repository.TransactionRepository;
import com.bank.liability.domain.transaction.service.TransactionDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DepositCancelService {

    private final TransactionRepository transactionRepository;
    private final LiabilityAccountRepository liabilityAccountRepository;
    private final LiabilityAccountDomainService liabilityAccountDomainService;
    private final TransactionDomainService transactionDomainService;

    @Transactional
    public LiabilityTransaction cancel(DepositCancelCommand command) {
        LiabilityTransaction original = transactionRepository.findByTransactionNo(command.getOriginalTransactionNo())
                .orElseThrow(() -> new BusinessException("原交易不存在"));

        if (original.getTransactionType() != TransactionType.DEPOSIT) {
            throw new BusinessException("只有存款交易可以撤销");
        }

        transactionDomainService.validateCancel(command.getOriginalTransactionNo());

        LiabilityAccount account = liabilityAccountRepository
                .findByLiabilityAccountNo(original.getLiabilityAccountNo())
                .orElseThrow(() -> new BusinessException("负债账号不存在"));

        if (!account.isNormal()) {
            throw new BusinessException("负债账号状态不允许撤销");
        }

        if (account.getBalance().compareTo(original.getAmount()) < 0) {
            throw new BusinessException("账户余额不足，无法撤销存款");
        }

        liabilityAccountDomainService.withdraw(account, original.getAmount());
        account.setUpdatedAt(java.time.LocalDateTime.now());
        liabilityAccountRepository.update(account);

        return transactionDomainService.cancelTransaction(original, command.getOperatorNo());
    }
}
