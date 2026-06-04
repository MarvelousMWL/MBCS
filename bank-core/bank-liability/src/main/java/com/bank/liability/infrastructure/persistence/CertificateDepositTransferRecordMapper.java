package com.bank.liability.infrastructure.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bank.liability.domain.certificatedeposit.entity.CertificateDepositTransferRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CertificateDepositTransferRecordMapper extends BaseMapper<CertificateDepositTransferRecord> {
}
