package com.bank.liability.infrastructure.persistence;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bank.liability.domain.batch.entity.AutoRedemptionBatch;
import com.bank.liability.domain.batch.repository.AutoRedemptionBatchRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BatchAutoRedemptionRepositoryImpl implements AutoRedemptionBatchRepository {

    private final BatchAutoRedemptionMapper mapper;

    @Override
    public void save(AutoRedemptionBatch batch) {
        mapper.insert(batch);
    }

    @Override
    public void update(AutoRedemptionBatch batch) {
        LambdaUpdateWrapper<AutoRedemptionBatch> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(AutoRedemptionBatch::getId, batch.getId())
               .set(AutoRedemptionBatch::getStatus, batch.getStatus())
               .set(AutoRedemptionBatch::getErrorMessage, batch.getErrorMessage())
               .set(AutoRedemptionBatch::getVersion, batch.getVersion() + 1);
        mapper.update(null, wrapper);
    }

    @Override
    public Optional<AutoRedemptionBatch> findById(Long id) {
        return Optional.ofNullable(mapper.selectById(id));
    }

    @Override
    public List<AutoRedemptionBatch> findByBatchNo(String batchNo) {
        LambdaQueryWrapper<AutoRedemptionBatch> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AutoRedemptionBatch::getBatchNo, batchNo);
        return mapper.selectList(wrapper);
    }

    @Override
    public List<AutoRedemptionBatch> findByStatus(String status) {
        LambdaQueryWrapper<AutoRedemptionBatch> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AutoRedemptionBatch::getStatus, status);
        return mapper.selectList(wrapper);
    }

    @Override
    public List<AutoRedemptionBatch> findByMaturityDateBefore(LocalDate date) {
        LambdaQueryWrapper<AutoRedemptionBatch> wrapper = new LambdaQueryWrapper<>();
        wrapper.le(AutoRedemptionBatch::getMaturityDate, date);
        return mapper.selectList(wrapper);
    }

    @Override
    public List<AutoRedemptionBatch> findAll() {
        return mapper.selectList(null);
    }

    @Override
    public long countByStatus(String status) {
        LambdaQueryWrapper<AutoRedemptionBatch> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AutoRedemptionBatch::getStatus, status);
        return mapper.selectCount(wrapper);
    }
}
