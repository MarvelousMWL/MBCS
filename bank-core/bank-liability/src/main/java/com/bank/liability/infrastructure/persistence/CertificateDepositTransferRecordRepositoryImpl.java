package com.bank.liability.infrastructure.persistence;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bank.liability.domain.certificatedeposit.entity.CertificateDepositTransferRecord;
import com.bank.liability.domain.certificatedeposit.repository.CertificateDepositTransferRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CertificateDepositTransferRecordRepositoryImpl implements CertificateDepositTransferRecordRepository {

    private final CertificateDepositTransferRecordMapper mapper;

    @Override
    public void save(CertificateDepositTransferRecord record) {
        mapper.insert(record);
    }

    @Override
    public void update(CertificateDepositTransferRecord record) {
        LambdaUpdateWrapper<CertificateDepositTransferRecord> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(CertificateDepositTransferRecord::getId, record.getId())
               .set(CertificateDepositTransferRecord::getStatus, record.getStatus())
               .set(CertificateDepositTransferRecord::getVersion, record.getVersion() + 1)
               .set(CertificateDepositTransferRecord::getUpdatedAt, record.getUpdatedAt());
        mapper.update(null, wrapper);
    }

    @Override
    public Optional<CertificateDepositTransferRecord> findById(Long id) {
        return Optional.ofNullable(mapper.selectById(id));
    }

    @Override
    public List<CertificateDepositTransferRecord> findByCdAccountNo(String cdAccountNo) {
        LambdaQueryWrapper<CertificateDepositTransferRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CertificateDepositTransferRecord::getCdAccountNo, cdAccountNo);
        return mapper.selectList(wrapper);
    }
}
