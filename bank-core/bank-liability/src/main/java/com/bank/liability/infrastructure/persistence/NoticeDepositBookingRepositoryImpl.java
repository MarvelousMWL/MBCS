package com.bank.liability.infrastructure.persistence;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bank.liability.domain.noticedeposit.entity.NoticeDepositBooking;
import com.bank.liability.domain.noticedeposit.repository.NoticeDepositBookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class NoticeDepositBookingRepositoryImpl implements NoticeDepositBookingRepository {

    private final NoticeDepositBookingMapper mapper;

    @Override
    public void save(NoticeDepositBooking booking) {
        mapper.insert(booking);
    }

    @Override
    public void update(NoticeDepositBooking booking) {
        LambdaUpdateWrapper<NoticeDepositBooking> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(NoticeDepositBooking::getId, booking.getId())
               .set(NoticeDepositBooking::getStatus, booking.getStatus())
               .set(NoticeDepositBooking::getVersion, booking.getVersion() + 1);
        mapper.update(null, wrapper);
    }

    @Override
    public Optional<NoticeDepositBooking> findById(Long id) {
        return Optional.ofNullable(mapper.selectById(id));
    }

    @Override
    public List<NoticeDepositBooking> findByNoticeDepositAccountNo(String noticeDepositAccountNo) {
        LambdaQueryWrapper<NoticeDepositBooking> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(NoticeDepositBooking::getNoticeDepositAccountNo, noticeDepositAccountNo);
        return mapper.selectList(wrapper);
    }

    @Override
    public Optional<NoticeDepositBooking> findTopByNoticeDepositAccountNoOrderByCreatedAtDesc(String noticeDepositAccountNo) {
        LambdaQueryWrapper<NoticeDepositBooking> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(NoticeDepositBooking::getNoticeDepositAccountNo, noticeDepositAccountNo)
               .orderByDesc(NoticeDepositBooking::getCreatedAt)
               .last("LIMIT 1");
        return Optional.ofNullable(mapper.selectOne(wrapper));
    }

    @Override
    public boolean existsPendingByNoticeDepositAccountNo(String noticeDepositAccountNo) {
        LambdaQueryWrapper<NoticeDepositBooking> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(NoticeDepositBooking::getNoticeDepositAccountNo, noticeDepositAccountNo)
               .eq(NoticeDepositBooking::getStatus, "PENDING");
        return mapper.exists(wrapper);
    }
}