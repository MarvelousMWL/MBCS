package com.bank.liability.domain.noticedeposit.repository;

import com.bank.liability.domain.noticedeposit.entity.NoticeDepositBooking;
import java.util.List;
import java.util.Optional;

public interface NoticeDepositBookingRepository {
    void save(NoticeDepositBooking booking);
    void update(NoticeDepositBooking booking);
    Optional<NoticeDepositBooking> findById(Long id);
    List<NoticeDepositBooking> findByNoticeDepositAccountNo(String noticeDepositAccountNo);
    Optional<NoticeDepositBooking> findTopByNoticeDepositAccountNoOrderByCreatedAtDesc(String noticeDepositAccountNo);
    boolean existsPendingByNoticeDepositAccountNo(String noticeDepositAccountNo);
}