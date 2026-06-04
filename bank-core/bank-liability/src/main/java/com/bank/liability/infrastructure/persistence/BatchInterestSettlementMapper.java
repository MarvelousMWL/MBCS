package com.bank.liability.infrastructure.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bank.liability.domain.batch.entity.InterestSettlementBatch;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BatchInterestSettlementMapper extends BaseMapper<InterestSettlementBatch> {
}
