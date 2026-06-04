package com.bank.liability.infrastructure.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bank.liability.domain.certificatedeposit.entity.CertificateDepositProduct;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CertificateDepositProductMapper extends BaseMapper<CertificateDepositProduct> {
}
