package com.bank.liability.infrastructure.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bank.liability.domain.noticedeposit.entity.NoticeDepositAccount;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NoticeDepositAccountMapper extends BaseMapper<NoticeDepositAccount> {
}