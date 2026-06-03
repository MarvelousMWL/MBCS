package com.bank.liability.infrastructure.persistence;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bank.liability.domain.account.entity.AccountWhitelist;
import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface AccountWhitelistMapper extends BaseMapper<AccountWhitelist> {}