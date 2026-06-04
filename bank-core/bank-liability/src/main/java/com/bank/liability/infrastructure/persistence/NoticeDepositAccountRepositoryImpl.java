package com.bank.liability.infrastructure.persistence;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bank.liability.domain.noticedeposit.entity.NoticeDepositAccount;
import com.bank.liability.domain.noticedeposit.repository.NoticeDepositAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class NoticeDepositAccountRepositoryImpl implements NoticeDepositAccountRepository {

    private final NoticeDepositAccountMapper mapper;

    @Override
    public void save(NoticeDepositAccount account) {
        mapper.insert(account);
    }

    @Override
    public void update(NoticeDepositAccount account) {
        LambdaUpdateWrapper<NoticeDepositAccount> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(NoticeDepositAccount::getNoticeDepositAccountNo, account.getNoticeDepositAccountNo())
               .set(NoticeDepositAccount::getStatus, account.getStatus())
               .set(NoticeDepositAccount::getInterest, account.getInterest())
               .set(NoticeDepositAccount::getTotalAmount, account.getTotalAmount())
               .set(NoticeDepositAccount::getVersion, account.getVersion() + 1);
        mapper.update(null, wrapper);
    }

    @Override
    public Optional<NoticeDepositAccount> findById(Long id) {
        return Optional.ofNullable(mapper.selectById(id));
    }

    @Override
    public Optional<NoticeDepositAccount> findByNoticeDepositAccountNo(String noticeDepositAccountNo) {
        LambdaQueryWrapper<NoticeDepositAccount> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(NoticeDepositAccount::getNoticeDepositAccountNo, noticeDepositAccountNo);
        return Optional.ofNullable(mapper.selectOne(wrapper));
    }

    @Override
    public List<NoticeDepositAccount> findByCustomerAccountNo(String customerAccountNo) {
        LambdaQueryWrapper<NoticeDepositAccount> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(NoticeDepositAccount::getCustomerAccountNo, customerAccountNo);
        return mapper.selectList(wrapper);
    }

    @Override
    public List<NoticeDepositAccount> findByCurrentAccountNo(String currentAccountNo) {
        LambdaQueryWrapper<NoticeDepositAccount> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(NoticeDepositAccount::getCurrentAccountNo, currentAccountNo);
        return mapper.selectList(wrapper);
    }

    @Override
    public boolean existsByNoticeDepositAccountNo(String noticeDepositAccountNo) {
        LambdaQueryWrapper<NoticeDepositAccount> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(NoticeDepositAccount::getNoticeDepositAccountNo, noticeDepositAccountNo);
        return mapper.exists(wrapper);
    }
}