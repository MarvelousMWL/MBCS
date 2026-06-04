package com.bank.liability.infrastructure.persistence;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bank.liability.domain.batch.entity.InterestSettlementBatch;
import com.bank.liability.domain.batch.repository.InterestSettlementBatchRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BatchInterestSettlementRepositoryImpl implements InterestSettlementBatchRepository {

    private final BatchInterestSettlementMapper mapper;

    @Override
    public void save(InterestSettlementBatch batch) {
        mapper.insert(batch);
    }

    @Override
    public void update(InterestSettlementBatch batch) {
        LambdaUpdateWrapper<InterestSettlementBatch> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(InterestSettlementBatch::getId, batch.getId())
               .set(InterestSettlementBatch::getStatus, batch.getStatus())
               .set(InterestSettlementBatch::getCalculatedInterest, batch.getCalculatedInterest())
               .set(InterestSettlementBatch::getErrorMessage, batch.getErrorMessage())
               .set(InterestSettlementBatch::getVersion, batch.getVersion() + 1);
        mapper.update(null, wrapper);
    }

    @Override
    public Optional<InterestSettlementBatch> findById(Long id) {
        return Optional.ofNullable(mapper.selectById(id));
    }

    @Override
    public List<InterestSettlementBatch> findByBatchNo(String batchNo) {
        LambdaQueryWrapper<InterestSettlementBatch> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InterestSettlementBatch::getBatchNo, batchNo);
        return mapper.selectList(wrapper);
    }

    @Override
    public List<InterestSettlementBatch> findByStatus(String status) {
        LambdaQueryWrapper<InterestSettlementBatch> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InterestSettlementBatch::getStatus, status);
        return mapper.selectList(wrapper);
    }

    @Override
    public List<InterestSettlementBatch> findBySettlementDate(LocalDate settlementDate) {
        LambdaQueryWrapper<InterestSettlementBatch> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InterestSettlementBatch::getSettlementDate, settlementDate);
        return mapper.selectList(wrapper);
    }

    @Override
    public List<InterestSettlementBatch> findAll() {
        return mapper.selectList(null);
    }

    @Override
    public long countByStatus(String status) {
        LambdaQueryWrapper<InterestSettlementBatch> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InterestSettlementBatch::getStatus, status);
        return mapper.selectCount(wrapper);
    }
}
