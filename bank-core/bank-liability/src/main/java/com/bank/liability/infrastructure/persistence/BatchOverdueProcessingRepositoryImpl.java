package com.bank.liability.infrastructure.persistence;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bank.liability.domain.batch.entity.OverdueProcessingBatch;
import com.bank.liability.domain.batch.repository.OverdueProcessingBatchRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BatchOverdueProcessingRepositoryImpl implements OverdueProcessingBatchRepository {

    private final BatchOverdueProcessingMapper mapper;

    @Override
    public void save(OverdueProcessingBatch batch) {
        mapper.insert(batch);
    }

    @Override
    public void update(OverdueProcessingBatch batch) {
        LambdaUpdateWrapper<OverdueProcessingBatch> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(OverdueProcessingBatch::getId, batch.getId())
               .set(OverdueProcessingBatch::getStatus, batch.getStatus())
               .set(OverdueProcessingBatch::getCalculatedInterest, batch.getCalculatedInterest())
               .set(OverdueProcessingBatch::getErrorMessage, batch.getErrorMessage())
               .set(OverdueProcessingBatch::getVersion, batch.getVersion() + 1);
        mapper.update(null, wrapper);
    }

    @Override
    public Optional<OverdueProcessingBatch> findById(Long id) {
        return Optional.ofNullable(mapper.selectById(id));
    }

    @Override
    public List<OverdueProcessingBatch> findByBatchNo(String batchNo) {
        LambdaQueryWrapper<OverdueProcessingBatch> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OverdueProcessingBatch::getBatchNo, batchNo);
        return mapper.selectList(wrapper);
    }

    @Override
    public List<OverdueProcessingBatch> findByStatus(String status) {
        LambdaQueryWrapper<OverdueProcessingBatch> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OverdueProcessingBatch::getStatus, status);
        return mapper.selectList(wrapper);
    }

    @Override
    public List<OverdueProcessingBatch> findByAccountNo(String accountNo) {
        LambdaQueryWrapper<OverdueProcessingBatch> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OverdueProcessingBatch::getAccountNo, accountNo);
        return mapper.selectList(wrapper);
    }

    @Override
    public List<OverdueProcessingBatch> findAll() {
        return mapper.selectList(null);
    }

    @Override
    public long countByStatus(String status) {
        LambdaQueryWrapper<OverdueProcessingBatch> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OverdueProcessingBatch::getStatus, status);
        return mapper.selectCount(wrapper);
    }
}
