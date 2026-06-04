package com.bank.liability.application.transaction.transfer;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.bank.common.exception.BusinessException;
import com.bank.liability.domain.enums.TransactionType;
import com.bank.liability.domain.liabilityaccount.entity.LiabilityAccount;
import com.bank.liability.domain.liabilityaccount.repository.LiabilityAccountRepository;
import com.bank.liability.domain.liabilityaccount.service.LiabilityAccountDomainService;
import com.bank.liability.domain.transaction.service.TransactionDomainService;
import com.bank.liability.domain.transfer.entity.TransferRecord;
import com.bank.liability.domain.transfer.repository.TransferRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransferCancelService {

    private static final Snowflake SNOWFLAKE = IdUtil.getSnowflake(1, 1);

    private final TransferRecordRepository transferRecordRepository;
    private final LiabilityAccountRepository liabilityAccountRepository;
    private final LiabilityAccountDomainService liabilityAccountDomainService;
    private final TransactionDomainService transactionDomainService;

    @Transactional
    public TransferRecord cancel(TransferCancelCommand command) {
        TransferRecord record = transferRecordRepository.findByTransferNo(command.getOriginalTransferNo())
                .orElseThrow(() -> new BusinessException("\u8f6c\u8d26\u8bb0\u5f55\u4e0d\u5b58\u5728"));

        if (!record.isCancellable()) {
            throw new BusinessException("\u8f6c\u8d26\u8bb0\u5f55\u72b6\u6001\u4e0d\u5141\u8bb8\u51b2\u6b63");
        }

        String toAccountNo = record.getToAccountNo();
        String fromAccountNo = record.getFromAccountNo();
        BigDecimal amount = record.getAmount();

        LiabilityAccount toAccount = liabilityAccountRepository
                .findByLiabilityAccountNo(toAccountNo)
                .orElseThrow(() -> new BusinessException("\u8f6c\u5165\u8d26\u6237\u4e0d\u5b58\u5728"));

        if (!toAccount.isNormal()) {
            throw new BusinessException("\u8f6c\u5165\u8d26\u6237\u72b6\u6001\u4e0d\u5141\u8bb8\u51b2\u6b63");
        }

        liabilityAccountDomainService.validateWithdraw(toAccount, amount);

        LiabilityAccount fromAccount = liabilityAccountRepository
                .findByLiabilityAccountNo(fromAccountNo)
                .orElseThrow(() -> new BusinessException("\u8f6c\u51fa\u8d26\u6237\u4e0d\u5b58\u5728"));

        if (!fromAccount.isNormal()) {
            throw new BusinessException("\u8f6c\u51fa\u8d26\u6237\u72b6\u6001\u4e0d\u5141\u8bb8\u51b2\u6b63");
        }

        BigDecimal toBalanceBefore = toAccount.getBalance();
        liabilityAccountDomainService.withdraw(toAccount, amount);
        BigDecimal toBalanceAfter = toAccount.getBalance();
        toAccount.setUpdatedAt(LocalDateTime.now());
        liabilityAccountRepository.update(toAccount);

        BigDecimal fromBalanceBefore = fromAccount.getBalance();
        liabilityAccountDomainService.deposit(fromAccount, amount);
        BigDecimal fromBalanceAfter = fromAccount.getBalance();
        fromAccount.setUpdatedAt(LocalDateTime.now());
        liabilityAccountRepository.update(fromAccount);

        transactionDomainService.createTransaction(
                toAccountNo,
                TransactionType.TRANSFER_CANCEL,
                amount,
                toBalanceBefore,
                toBalanceAfter,
                command.getOperatorNo(),
                command.getRemark(),
                record.getTransferNo()
        );

        transactionDomainService.createTransaction(
                fromAccountNo,
                TransactionType.TRANSFER_CANCEL,
                amount,
                fromBalanceBefore,
                fromBalanceAfter,
                command.getOperatorNo(),
                command.getRemark(),
                record.getTransferNo()
        );

        record.setStatus("CANCELLED");
        record.setUpdatedAt(LocalDateTime.now());
        transferRecordRepository.update(record);

        return record;
    }
}
