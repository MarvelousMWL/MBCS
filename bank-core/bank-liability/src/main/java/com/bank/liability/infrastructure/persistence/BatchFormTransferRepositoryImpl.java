package com.bank.liability.infrastructure.persistence;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bank.liability.domain.batch.entity.FormTransferBatch;
import com.bank.liability.domain.batch.repository.FormTransferBatchRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BatchFormTransferRepositoryImpl implements FormTransferBatchRepository {

    private final BatchFormTransferMapper mapper;

    @Override
    public void save(FormTransferBatch batch) {
        mapper.insert(batch);
    }

    @Override
    public void update(FormTransferBatch batch) {
        LambdaUpdateWrapper<FormTransferBatch> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(FormTransferBatch::getId, batch.getId())
               .set(FormTransferBatch::getStatus, batch.getStatus())
               .set(FormTransferBatch::getTransferAmount, batch.getTransferAmount())
               .set(FormTransferBatch::getErrorMessage, batch.getErrorMessage())
               .set(FormTransferBatch::getVersion, batch.getVersion() + 1);
        mapper.update(null, wrapper);
    }

    @Override
    public Optional<FormTransferBatch> findById(Long id) {
        return Optional.ofNullable(mapper.selectById(id));
    }

    @Override
    public List<FormTransferBatch> findByBatchNo(String batchNo) {
        LambdaQueryWrapper<FormTransferBatch> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FormTransferBatch::getBatchNo, batchNo);
        return mapper.selectList(wrapper);
    }

    @Override
    public List<FormTransferBatch> findByStatus(String status) {
        LambdaQueryWrapper<FormTransferBatch> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FormTransferBatch::getStatus, status);
        return mapper.selectList(wrapper);
    }

    @Override
    public List<FormTransferBatch> findByAccountNo(String accountNo) {
        LambdaQueryWrapper<FormTransferBatch> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FormTransferBatch::getAccountNo, accountNo);
        return mapper.selectList(wrapper);
    }

    @Override
    public List<FormTransferBatch> findAll() {
        return mapper.selectList(null);
    }

    @Override
    public long countByStatus(String status) {
        LambdaQueryWrapper<FormTransferBatch> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FormTransferBatch::getStatus, status);
        return mapper.selectCount(wrapper);
    }
}
