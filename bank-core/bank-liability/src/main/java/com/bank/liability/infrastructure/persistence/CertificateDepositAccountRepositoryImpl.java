package com.bank.liability.infrastructure.persistence;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bank.liability.domain.certificatedeposit.entity.CertificateDepositAccount;
import com.bank.liability.domain.certificatedeposit.repository.CertificateDepositAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CertificateDepositAccountRepositoryImpl implements CertificateDepositAccountRepository {

    private final CertificateDepositAccountMapper mapper;

    @Override
    public void save(CertificateDepositAccount account) {
        mapper.insert(account);
    }

    @Override
    public void update(CertificateDepositAccount account) {
        LambdaUpdateWrapper<CertificateDepositAccount> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(CertificateDepositAccount::getCdAccountNo, account.getCdAccountNo())
               .set(CertificateDepositAccount::getStatus, account.getStatus())
               .set(CertificateDepositAccount::getCustomerAccountNo, account.getCustomerAccountNo())
               .set(CertificateDepositAccount::getInterestTransferAccount, account.getInterestTransferAccount())
               .set(CertificateDepositAccount::getInterest, account.getInterest())
               .set(CertificateDepositAccount::getTotalAmount, account.getTotalAmount())
               .set(CertificateDepositAccount::getRedeemDate, account.getRedeemDate())
               .set(CertificateDepositAccount::getUpdatedAt, account.getUpdatedAt())
               .set(CertificateDepositAccount::getVersion, account.getVersion() + 1);
        mapper.update(null, wrapper);
    }

    @Override
    public Optional<CertificateDepositAccount> findById(Long id) {
        return Optional.ofNullable(mapper.selectById(id));
    }

    @Override
    public Optional<CertificateDepositAccount> findByCdAccountNo(String cdAccountNo) {
        LambdaQueryWrapper<CertificateDepositAccount> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CertificateDepositAccount::getCdAccountNo, cdAccountNo);
        return Optional.ofNullable(mapper.selectOne(wrapper));
    }

    @Override
    public List<CertificateDepositAccount> findByCustomerAccountNo(String customerAccountNo) {
        LambdaQueryWrapper<CertificateDepositAccount> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CertificateDepositAccount::getCustomerAccountNo, customerAccountNo);
        return mapper.selectList(wrapper);
    }

    @Override
    public List<CertificateDepositAccount> findByProductCode(String productCode) {
        LambdaQueryWrapper<CertificateDepositAccount> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CertificateDepositAccount::getProductCode, productCode);
        return mapper.selectList(wrapper);
    }

    @Override
    public List<CertificateDepositAccount> findAll() {
        return mapper.selectList(null);
    }

    @Override
    public boolean existsByCdAccountNo(String cdAccountNo) {
        LambdaQueryWrapper<CertificateDepositAccount> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CertificateDepositAccount::getCdAccountNo, cdAccountNo);
        return mapper.exists(wrapper);
    }

    @Override
    public Long countByProductCode(String productCode) {
        LambdaQueryWrapper<CertificateDepositAccount> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CertificateDepositAccount::getProductCode, productCode);
        return mapper.selectCount(wrapper);
    }
}
