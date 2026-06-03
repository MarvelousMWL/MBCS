package com.bank.teller.infrastructure.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bank.teller.domain.teller.entity.Teller;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TellerMapper extends BaseMapper<Teller> {
}
