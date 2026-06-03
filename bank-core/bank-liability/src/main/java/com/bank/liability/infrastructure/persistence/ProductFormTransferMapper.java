package com.bank.liability.infrastructure.persistence;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bank.liability.domain.product.entity.ProductFormTransfer;
import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface ProductFormTransferMapper extends BaseMapper<ProductFormTransfer> {}