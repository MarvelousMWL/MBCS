package com.bank.liability.infrastructure.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bank.liability.domain.batch.entity.OverdueProcessingBatch;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BatchOverdueProcessingMapper extends BaseMapper<OverdueProcessingBatch> {
}
