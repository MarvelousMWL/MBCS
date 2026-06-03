package com.bank.liability.infrastructure.persistence;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bank.liability.domain.product.entity.ProductInterestDefinition;
import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface ProductInterestDefinitionMapper extends BaseMapper<ProductInterestDefinition> {}
