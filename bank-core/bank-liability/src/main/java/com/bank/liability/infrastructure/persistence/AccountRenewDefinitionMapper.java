package com.bank.liability.infrastructure.persistence;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bank.liability.domain.account.entity.AccountRenewDefinition;
import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface AccountRenewDefinitionMapper extends BaseMapper<AccountRenewDefinition> {}