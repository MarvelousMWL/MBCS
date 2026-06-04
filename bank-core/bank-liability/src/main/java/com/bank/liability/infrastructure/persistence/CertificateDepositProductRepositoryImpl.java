package com.bank.liability.infrastructure.persistence;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bank.liability.domain.certificatedeposit.entity.CertificateDepositProduct;
import com.bank.liability.domain.certificatedeposit.repository.CertificateDepositProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CertificateDepositProductRepositoryImpl implements CertificateDepositProductRepository {

    private final CertificateDepositProductMapper mapper;

    @Override
    public void save(CertificateDepositProduct product) {
        mapper.insert(product);
    }

    @Override
    public void update(CertificateDepositProduct product) {
        LambdaUpdateWrapper<CertificateDepositProduct> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(CertificateDepositProduct::getProductCode, product.getProductCode())
               .set(CertificateDepositProduct::getRemainingQuota, product.getRemainingQuota())
               .set(CertificateDepositProduct::getStatus, product.getStatus());
        mapper.update(null, wrapper);
    }

    @Override
    public Optional<CertificateDepositProduct> findById(Long id) {
        return Optional.ofNullable(mapper.selectById(id));
    }

    @Override
    public Optional<CertificateDepositProduct> findByProductCode(String productCode) {
        LambdaQueryWrapper<CertificateDepositProduct> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CertificateDepositProduct::getProductCode, productCode);
        return Optional.ofNullable(mapper.selectOne(wrapper));
    }

    @Override
    public List<CertificateDepositProduct> findAll() {
        return mapper.selectList(null);
    }

    @Override
    public boolean existsByProductCode(String productCode) {
        LambdaQueryWrapper<CertificateDepositProduct> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CertificateDepositProduct::getProductCode, productCode);
        return mapper.exists(wrapper);
    }
}
