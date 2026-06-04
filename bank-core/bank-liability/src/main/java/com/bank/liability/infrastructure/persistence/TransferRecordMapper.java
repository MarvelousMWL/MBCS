package com.bank.liability.infrastructure.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bank.liability.domain.transfer.entity.TransferRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TransferRecordMapper extends BaseMapper<TransferRecord> {
}
