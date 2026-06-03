package com.bank.liability.application.transaction.deposit;

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

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DepositService {

    private final LiabilityAccountRepository liabilityAccountRepository;
    private final TransactionRepository transactionRepository;
    private final LiabilityAccountDomainService liabilityAccountDomainService;
    private final TransactionDomainService transactionDomainService;

    @Transactional
    public LiabilityTransaction deposit(DepositCommand command) {
        LiabilityAccount account = liabilityAccountRepository
                .findByLiabilityAccountNo(command.getLiabilityAccountNo())
                .orElseThrow(() -> new BusinessException("负债账号不存在"));

        if (!account.isNormal()) {
            throw new BusinessException("负债账号状态不允许存款");
        }

        liabilityAccountDomainService.validateDeposit(command.getAmount());

        BigDecimal balanceBefore = account.getBalance();
        liabilityAccountDomainService.deposit(account, command.getAmount());
        BigDecimal balanceAfter = account.getBalance();

        liabilityAccountRepository.update(account);

        return transactionDomainService.createTransaction(
                account.getLiabilityAccountNo(),
                TransactionType.DEPOSIT,
                command.getAmount(),
                balanceBefore,
                balanceAfter,
                command.getOperatorNo(),
                command.getRemark(),
                null
        );
    }

    public List<LiabilityTransaction> listAll() {
        return transactionRepository.findAll();
    }
}
