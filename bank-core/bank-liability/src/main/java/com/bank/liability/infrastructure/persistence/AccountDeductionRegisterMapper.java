package com.bank.liability.infrastructure.persistence;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bank.liability.domain.account.entity.AccountDeductionRegister;
import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface AccountDeductionRegisterMapper extends BaseMapper<AccountDeductionRegister> {}