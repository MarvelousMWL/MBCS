package com.bank.liability.infrastructure.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bank.liability.domain.product.entity.ProductDefinition;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductDefinitionMapper extends BaseMapper<ProductDefinition> {}
