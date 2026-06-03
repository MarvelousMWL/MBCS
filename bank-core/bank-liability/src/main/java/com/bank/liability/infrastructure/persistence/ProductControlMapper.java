package com.bank.liability.infrastructure.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bank.liability.domain.product.entity.ProductControl;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductControlMapper extends BaseMapper<ProductControl> {}
