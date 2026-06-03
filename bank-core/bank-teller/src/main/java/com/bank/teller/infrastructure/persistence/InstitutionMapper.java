package com.bank.teller.infrastructure.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bank.teller.domain.institution.entity.Institution;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InstitutionMapper extends BaseMapper<Institution> {
}
