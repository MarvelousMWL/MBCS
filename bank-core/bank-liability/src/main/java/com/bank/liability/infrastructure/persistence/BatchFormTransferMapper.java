package com.bank.liability.infrastructure.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bank.liability.domain.batch.entity.FormTransferBatch;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BatchFormTransferMapper extends BaseMapper<FormTransferBatch> {
}
