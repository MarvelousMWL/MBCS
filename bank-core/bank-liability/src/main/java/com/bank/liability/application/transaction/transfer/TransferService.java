package com.bank.liability.application.transaction.transfer;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.bank.common.exception.BusinessException;
import com.bank.liability.domain.enums.TransactionType;
import com.bank.liability.domain.liabilityaccount.entity.LiabilityAccount;
import com.bank.liability.domain.liabilityaccount.repository.LiabilityAccountRepository;
import com.bank.liability.domain.liabilityaccount.service.LiabilityAccountDomainService;
import com.bank.liability.domain.transaction.entity.LiabilityTransaction;
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
public class TransferService {

    private static final Snowflake SNOWFLAKE = IdUtil.getSnowflake(1, 1);

    private final LiabilityAccountRepository liabilityAccountRepository;
    private final LiabilityAccountDomainService liabilityAccountDomainService;
    private final TransactionDomainService transactionDomainService;
    private final TransferRecordRepository transferRecordRepository;

    @Transactional
    public TransferRecord transfer(TransferCommand command) {
        String fromAccountNo = command.getFromAccountNo();
        String toAccountNo = command.getToAccountNo();
        BigDecimal amount = command.getAmount();

        if (fromAccountNo.equals(toAccountNo)) {
            throw new BusinessException("\u8f6c\u51fa\u8d26\u6237\u4e0e\u8f6c\u5165\u8d26\u6237\u4e0d\u80fd\u76f8\u540c");
        }

        LiabilityAccount fromAccount = liabilityAccountRepository
                .findByLiabilityAccountNo(fromAccountNo)
                .orElseThrow(() -> new BusinessException("\u8f6c\u51fa\u8d26\u6237\u4e0d\u5b58\u5728"));

        if (!fromAccount.isNormal()) {
            throw new BusinessException("\u8f6c\u51fa\u8d26\u6237\u72b6\u6001\u4e0d\u5141\u8bb8\u8f6c\u8d26");
        }

        liabilityAccountDomainService.validateWithdraw(fromAccount, amount);

        LiabilityAccount toAccount = liabilityAccountRepository
                .findByLiabilityAccountNo(toAccountNo)
                .orElseThrow(() -> new BusinessException("\u8f6c\u5165\u8d26\u6237\u4e0d\u5b58\u5728"));

        if (!toAccount.isNormal()) {
            throw new BusinessException("\u8f6c\u5165\u8d26\u6237\u72b6\u6001\u4e0d\u5141\u8bb8\u8f6c\u8d26");
        }

        BigDecimal fromBalanceBefore = fromAccount.getBalance();
        liabilityAccountDomainService.withdraw(fromAccount, amount);
        BigDecimal fromBalanceAfter = fromAccount.getBalance();
        fromAccount.setUpdatedAt(LocalDateTime.now());
        liabilityAccountRepository.update(fromAccount);

        BigDecimal toBalanceBefore = toAccount.getBalance();
        liabilityAccountDomainService.deposit(toAccount, amount);
        BigDecimal toBalanceAfter = toAccount.getBalance();
        toAccount.setUpdatedAt(LocalDateTime.now());
        liabilityAccountRepository.update(toAccount);

        String transferNo = "TF" + SNOWFLAKE.nextIdStr();

        LiabilityTransaction fromTx = transactionDomainService.createTransaction(
                fromAccountNo,
                TransactionType.TRANSFER,
                amount,
                fromBalanceBefore,
                fromBalanceAfter,
                command.getOperatorNo(),
                command.getRemark(),
                transferNo
        );

        LiabilityTransaction toTx = transactionDomainService.createTransaction(
                toAccountNo,
                TransactionType.TRANSFER,
                amount,
                toBalanceBefore,
                toBalanceAfter,
                command.getOperatorNo(),
                command.getRemark(),
                transferNo
        );

        TransferRecord record = new TransferRecord();
        record.setTransferNo(transferNo);
        record.setFromAccountNo(fromAccountNo);
        record.setToAccountNo(toAccountNo);
        record.setAmount(amount);
        record.setFromTransactionNo(fromTx.getTransactionNo());
        record.setToTransactionNo(toTx.getTransactionNo());
        record.setStatus("NORMAL");
        record.setOperatorNo(command.getOperatorNo());
        record.setRemark(command.getRemark());
        record.setCreatedAt(LocalDateTime.now());
        record.setUpdatedAt(LocalDateTime.now());

        transferRecordRepository.save(record);
        return record;
    }
}
