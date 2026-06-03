package com.bank.liability.infrastructure.persistence;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bank.liability.domain.liabilityaccount.entity.LiabilityAccount;
import com.bank.liability.domain.liabilityaccount.repository.LiabilityAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LiabilityAccountRepositoryImpl implements LiabilityAccountRepository {

    private final LiabilityAccountMapper liabilityAccountMapper;

    @Override
    public void save(LiabilityAccount liabilityAccount) {
        liabilityAccountMapper.insert(liabilityAccount);
    }

    @Override
    public void update(LiabilityAccount liabilityAccount) {
        LambdaUpdateWrapper<LiabilityAccount> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(LiabilityAccount::getLiabilityAccountNo, liabilityAccount.getLiabilityAccountNo())
               .set(LiabilityAccount::getBalance, liabilityAccount.getBalance())
               .set(LiabilityAccount::getStatus, liabilityAccount.getStatus())
               .set(LiabilityAccount::getCloseDate, liabilityAccount.getCloseDate())
               .set(LiabilityAccount::getUpdatedAt, liabilityAccount.getUpdatedAt())
               .set(LiabilityAccount::getVersion, liabilityAccount.getVersion() + 1);
        liabilityAccountMapper.update(null, wrapper);
    }

    @Override
    public Optional<LiabilityAccount> findById(Long id) {
        return Optional.ofNullable(liabilityAccountMapper.selectById(id));
    }

    @Override
    public Optional<LiabilityAccount> findByLiabilityAccountNo(String liabilityAccountNo) {
        LambdaQueryWrapper<LiabilityAccount> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LiabilityAccount::getLiabilityAccountNo, liabilityAccountNo);
        return Optional.ofNullable(liabilityAccountMapper.selectOne(wrapper));
    }

    @Override
    public List<LiabilityAccount> findByCustomerAccountNo(String customerAccountNo) {
        LambdaQueryWrapper<LiabilityAccount> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LiabilityAccount::getCustomerAccountNo, customerAccountNo);
        return liabilityAccountMapper.selectList(wrapper);
    }

    @Override
    public List<LiabilityAccount> findAll() {
        return liabilityAccountMapper.selectList(null);
    }

    @Override
    public boolean existsByLiabilityAccountNo(String liabilityAccountNo) {
        LambdaQueryWrapper<LiabilityAccount> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LiabilityAccount::getLiabilityAccountNo, liabilityAccountNo);
        return liabilityAccountMapper.exists(wrapper);
    }

    @Override
    public Long countByCustomerAccountNo(String customerAccountNo) {
        LambdaQueryWrapper<LiabilityAccount> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LiabilityAccount::getCustomerAccountNo, customerAccountNo);
        return liabilityAccountMapper.selectCount(wrapper);
    }

    @Override
    public Long countAll() {
        return liabilityAccountMapper.selectCount(null);
    }

    @Override
    public Optional<LiabilityAccount> findMaxSubAccountSeqByCustomerAccountNoAndAccountType(String customerAccountNo, String accountType) {
        return Optional.ofNullable(liabilityAccountMapper.findMaxSubAccountSeqByCustomerAccountNoAndAccountType(customerAccountNo, accountType));
    }
}
