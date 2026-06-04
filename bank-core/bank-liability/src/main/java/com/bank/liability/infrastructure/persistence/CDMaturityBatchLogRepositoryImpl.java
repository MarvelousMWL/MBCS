package com.bank.liability.infrastructure.persistence;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bank.liability.domain.certificatedeposit.entity.CDMaturityBatchLog;
import com.bank.liability.domain.certificatedeposit.repository.CDMaturityBatchLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CDMaturityBatchLogRepositoryImpl implements CDMaturityBatchLogRepository {

    private final CDMaturityBatchLogMapper mapper;

    @Override
    public void save(CDMaturityBatchLog log) {
        mapper.insert(log);
    }

    @Override
    public void update(CDMaturityBatchLog log) {
        mapper.updateById(log);
    }

    @Override
    public Optional<CDMaturityBatchLog> findById(Long id) {
        return Optional.ofNullable(mapper.selectById(id));
    }

    @Override
    public List<CDMaturityBatchLog> findByBatchNo(String batchNo) {
        LambdaQueryWrapper<CDMaturityBatchLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CDMaturityBatchLog::getBatchNo, batchNo);
        return mapper.selectList(wrapper);
    }

    @Override
    public List<CDMaturityBatchLog> findAll() {
        return mapper.selectList(null);
    }
}