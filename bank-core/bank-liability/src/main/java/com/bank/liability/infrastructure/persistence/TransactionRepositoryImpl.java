package com.bank.liability.infrastructure.persistence;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bank.liability.domain.enums.TransactionType;
import com.bank.liability.domain.transaction.entity.LiabilityTransaction;
import com.bank.liability.domain.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TransactionRepositoryImpl implements TransactionRepository {

    private final TransactionMapper transactionMapper;

    @Override
    public void save(LiabilityTransaction transaction) {
        transactionMapper.insert(transaction);
    }

    @Override
    public void update(LiabilityTransaction transaction) {
        transactionMapper.updateById(transaction);
    }

    @Override
    public List<LiabilityTransaction> findAll() {
        LambdaQueryWrapper<LiabilityTransaction> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(LiabilityTransaction::getOperateTime);
        return transactionMapper.selectList(wrapper);
    }

    @Override
    public Optional<LiabilityTransaction> findById(Long id) {
        return Optional.ofNullable(transactionMapper.selectById(id));
    }

    @Override
    public Optional<LiabilityTransaction> findByTransactionNo(String transactionNo) {
        LambdaQueryWrapper<LiabilityTransaction> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LiabilityTransaction::getTransactionNo, transactionNo);
        return Optional.ofNullable(transactionMapper.selectOne(wrapper));
    }

    @Override
    public List<LiabilityTransaction> findByLiabilityAccountNo(String liabilityAccountNo) {
        LambdaQueryWrapper<LiabilityTransaction> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LiabilityTransaction::getLiabilityAccountNo, liabilityAccountNo)
               .orderByDesc(LiabilityTransaction::getOperateTime);
        return transactionMapper.selectList(wrapper);
    }

    @Override
    public List<LiabilityTransaction> findByRelatedTransactionNo(String relatedTransactionNo) {
        LambdaQueryWrapper<LiabilityTransaction> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LiabilityTransaction::getRelatedTransactionNo, relatedTransactionNo);
        return transactionMapper.selectList(wrapper);
    }

    @Override
    public IPage<LiabilityTransaction> findPage(IPage<LiabilityTransaction> page, String liabilityAccountNo, TransactionType transactionType, LocalDateTime startDate, LocalDateTime endDate) {
        LambdaQueryWrapper<LiabilityTransaction> wrapper = new LambdaQueryWrapper<>();
        if (liabilityAccountNo != null && !liabilityAccountNo.isEmpty()) {
            wrapper.eq(LiabilityTransaction::getLiabilityAccountNo, liabilityAccountNo);
        }
        if (transactionType != null) {
            wrapper.eq(LiabilityTransaction::getTransactionType, transactionType);
        }
        if (startDate != null) {
            wrapper.ge(LiabilityTransaction::getOperateTime, startDate);
        }
        if (endDate != null) {
            wrapper.le(LiabilityTransaction::getOperateTime, endDate);
        }
        wrapper.orderByDesc(LiabilityTransaction::getOperateTime);
        return transactionMapper.selectPage(page, wrapper);
    }
}
