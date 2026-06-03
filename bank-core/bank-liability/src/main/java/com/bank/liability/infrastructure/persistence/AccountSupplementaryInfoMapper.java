package com.bank.liability.infrastructure.persistence;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bank.liability.domain.account.entity.AccountSupplementaryInfo;
import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface AccountSupplementaryInfoMapper extends BaseMapper<AccountSupplementaryInfo> {}