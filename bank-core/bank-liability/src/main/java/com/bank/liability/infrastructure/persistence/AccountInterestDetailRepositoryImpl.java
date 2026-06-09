package com.bank.liability.infrastructure.persistence;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bank.liability.domain.account.entity.AccountInterestDetail;
import com.bank.liability.domain.account.repository.AccountInterestDetailRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AccountInterestDetailRepositoryImpl implements AccountInterestDetailRepository {

    private final AccountInterestDetailMapper mapper;

    @Override
    public void save(AccountInterestDetail detail) {
        mapper.insert(detail);
    }

    @Override
    public void update(AccountInterestDetail detail) {
        LambdaUpdateWrapper<AccountInterestDetail> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(AccountInterestDetail::getId, detail.getId())
               .set(AccountInterestDetail::getStatus, detail.getStatus())
               .set(AccountInterestDetail::getAccruedInterest, detail.getAccruedInterest())
               .set(AccountInterestDetail::getErrorMessage, detail.getErrorMessage())
               .set(AccountInterestDetail::getVersion, detail.getVersion() + 1);
        mapper.update(null, wrapper);
    }

    @Override
    public List<AccountInterestDetail> findByBatchNo(String batchNo) {
        LambdaQueryWrapper<AccountInterestDetail> w = new LambdaQueryWrapper<>();
        w.eq(AccountInterestDetail::getBatchNo, batchNo);
        return mapper.selectList(w);
    }

    @Override
    public List<AccountInterestDetail> findByAccountNo(String accountNo) {
        LambdaQueryWrapper<AccountInterestDetail> w = new LambdaQueryWrapper<>();
        w.eq(AccountInterestDetail::getAccountNo, accountNo);
        return mapper.selectList(w);
    }

    @Override
    public List<AccountInterestDetail> findByCalcDate(LocalDate calcDate) {
        LambdaQueryWrapper<AccountInterestDetail> w = new LambdaQueryWrapper<>();
        w.eq(AccountInterestDetail::getCalcDate, calcDate);
        return mapper.selectList(w);
    }

    @Override
    public List<AccountInterestDetail> findByStatusAndBatchType(String status, String batchType) {
        LambdaQueryWrapper<AccountInterestDetail> w = new LambdaQueryWrapper<>();
        w.eq(AccountInterestDetail::getStatus, status)
         .eq(AccountInterestDetail::getBatchType, batchType);
        return mapper.selectList(w);
    }

    @Override
    public List<AccountInterestDetail> findByAccountNoAndCalcDate(String accountNo, LocalDate calcDate) {
        LambdaQueryWrapper<AccountInterestDetail> w = new LambdaQueryWrapper<>();
        w.eq(AccountInterestDetail::getAccountNo, accountNo)
         .eq(AccountInterestDetail::getCalcDate, calcDate);
        return mapper.selectList(w);
    }

    @Override
    public long countByBatchNo(String batchNo) {
        LambdaQueryWrapper<AccountInterestDetail> w = new LambdaQueryWrapper<>();
        w.eq(AccountInterestDetail::getBatchNo, batchNo);
        return mapper.selectCount(w);
    }

    @Override
    public long sumDailyInterestByBatchNo(String batchNo) {
        return 0; // placeholder - aggregation queries via SQL
    }
}
