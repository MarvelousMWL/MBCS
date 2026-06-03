package com.bank.liability.infrastructure.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bank.liability.domain.customersubaccount.entity.CustomerSubAccount;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CustomerSubAccountMapper extends BaseMapper<CustomerSubAccount> {
}
