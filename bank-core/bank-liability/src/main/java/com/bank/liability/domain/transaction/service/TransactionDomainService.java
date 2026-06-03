package com.bank.liability.domain.transaction.service;

import com.bank.common.exception.BusinessException;
import com.bank.common.util.Assert;
import com.bank.common.util.NoGenerator;
import com.bank.liability.domain.transaction.entity.LiabilityTransaction;
import com.bank.liability.domain.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransactionDomainService {

    private final TransactionRepository transactionRepository;

    public LiabilityTransaction createTransaction(
            String liabilityAccountNo,
            com.bank.liability.domain.enums.TransactionType transactionType,
            BigDecimal amount,
            BigDecimal balanceBefore,
            BigDecimal balanceAfter,
            String operatorNo,
            String remark,
            String relatedTransactionNo) {

        Assert.notBlank(liabilityAccountNo, "负债账号不能为空");
        Assert.notNull(amount, "交易金额不能为空");
        Assert.notBlank(operatorNo, "操作员编号不能为空");

        LiabilityTransaction transaction = new LiabilityTransaction();
        transaction.setTransactionNo(NoGenerator.generateTransactionNo());
        transaction.setLiabilityAccountNo(liabilityAccountNo);
        transaction.setTransactionType(transactionType);
        transaction.setAmount(amount);
        transaction.setBalanceBefore(balanceBefore);
        transaction.setBalanceAfter(balanceAfter);
        transaction.setOperateTime(LocalDateTime.now());
        transaction.setOperatorNo(operatorNo);
        transaction.setRemark(remark);
        transaction.setRelatedTransactionNo(relatedTransactionNo);
        transaction.setStatus(com.bank.liability.domain.enums.TransactionStatus.NORMAL);
        transaction.setCreatedAt(LocalDateTime.now());

        transactionRepository.save(transaction);
        return transaction;
    }

    public void validateCancel(String transactionNo) {
        LiabilityTransaction original = transactionRepository.findByTransactionNo(transactionNo)
                .orElseThrow(() -> new BusinessException("原交易不存在"));

        if (!original.isCancellable()) {
            throw new BusinessException("原交易已撤销或状态不允许撤销");
        }
    }

    public LiabilityTransaction cancelTransaction(LiabilityTransaction original, String operatorNo) {
        return createTransaction(
                original.getLiabilityAccountNo(),
                getCancelTransactionType(original.getTransactionType()),
                original.getAmount(),
                original.getBalanceAfter(),
                original.getBalanceBefore(),
                operatorNo,
                "撤销交易：" + original.getTransactionNo(),
                original.getTransactionNo()
        );
    }

    private com.bank.liability.domain.enums.TransactionType getCancelTransactionType(
            com.bank.liability.domain.enums.TransactionType originalType) {
        return switch (originalType) {
            case DEPOSIT -> com.bank.liability.domain.enums.TransactionType.DEPOSIT_CANCEL;
            case WITHDRAW -> com.bank.liability.domain.enums.TransactionType.WITHDRAW_CANCEL;
            default -> throw new BusinessException("该交易类型不支持撤销");
        };
    }
}
