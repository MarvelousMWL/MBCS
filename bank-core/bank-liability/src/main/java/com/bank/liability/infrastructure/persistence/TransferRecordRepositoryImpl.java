package com.bank.liability.infrastructure.persistence;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bank.liability.domain.transfer.entity.TransferRecord;
import com.bank.liability.domain.transfer.repository.TransferRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TransferRecordRepositoryImpl implements TransferRecordRepository {

    private final TransferRecordMapper transferRecordMapper;

    @Override
    public void save(TransferRecord record) {
        transferRecordMapper.insert(record);
    }

    @Override
    public void update(TransferRecord record) {
        transferRecordMapper.updateById(record);
    }

    @Override
    public Optional<TransferRecord> findByTransferNo(String transferNo) {
        LambdaQueryWrapper<TransferRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TransferRecord::getTransferNo, transferNo);
        return Optional.ofNullable(transferRecordMapper.selectOne(wrapper));
    }
}
