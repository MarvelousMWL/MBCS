package com.bank.liability.infrastructure.persistence;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bank.liability.domain.batch.entity.BatchRunLog;
import com.bank.liability.domain.batch.repository.BatchRunLogRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BatchRunLogRepositoryImpl implements BatchRunLogRepository {

    private final BatchRunLogMapper mapper;

    @Override
    public void save(BatchRunLog log) {
        mapper.insert(log);
    }

    @Override
    public void update(BatchRunLog log) {
        LambdaUpdateWrapper<BatchRunLog> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(BatchRunLog::getId, log.getId())
               .set(BatchRunLog::getTotalAccounts, log.getTotalAccounts())
               .set(BatchRunLog::getSuccessCount, log.getSuccessCount())
               .set(BatchRunLog::getFailCount, log.getFailCount())
               .set(BatchRunLog::getEndTime, log.getEndTime())
               .set(BatchRunLog::getStatus, log.getStatus())
               .set(BatchRunLog::getErrorMessage, log.getErrorMessage());
        mapper.update(null, wrapper);
    }

    @Override
    public Optional<BatchRunLog> findById(Long id) {
        return Optional.ofNullable(mapper.selectById(id));
    }

    @Override
    public List<BatchRunLog> findByBatchTypeAndCalcDate(String batchType, LocalDate calcDate) {
        LambdaQueryWrapper<BatchRunLog> w = new LambdaQueryWrapper<>();
        w.eq(BatchRunLog::getBatchType, batchType)
         .eq(BatchRunLog::getCalcDate, calcDate)
         .orderByDesc(BatchRunLog::getId);
        return mapper.selectList(w);
    }

    @Override
    public List<BatchRunLog> findTopN(int n) {
        LambdaQueryWrapper<BatchRunLog> w = new LambdaQueryWrapper<>();
        w.orderByDesc(BatchRunLog::getId)
         .last("LIMIT " + n);
        return mapper.selectList(w);
    }
}
