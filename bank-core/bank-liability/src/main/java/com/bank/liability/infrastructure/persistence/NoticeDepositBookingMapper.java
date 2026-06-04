package com.bank.liability.infrastructure.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bank.liability.domain.noticedeposit.entity.NoticeDepositBooking;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NoticeDepositBookingMapper extends BaseMapper<NoticeDepositBooking> {
}