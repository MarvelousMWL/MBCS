package com.bank.teller.infrastructure.persistence;

import com.bank.teller.domain.teller.entity.Teller;
import com.bank.teller.domain.teller.repository.TellerRepository;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TellerRepositoryImpl implements TellerRepository {

    private final TellerMapper tellerMapper;

    @Override
    public Optional<Teller> findById(Long id) {
        return Optional.ofNullable(tellerMapper.selectById(id));
    }

    @Override
    public Optional<Teller> findByTellerNo(String tellerNo) {
        LambdaQueryWrapper<Teller> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Teller::getTellerNo, tellerNo);
        return Optional.ofNullable(tellerMapper.selectOne(wrapper));
    }

    @Override
    public List<Teller> findByInstitutionNo(String institutionNo) {
        LambdaQueryWrapper<Teller> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Teller::getInstitutionNo, institutionNo);
        return tellerMapper.selectList(wrapper);
    }

    @Override
    public List<Teller> findAll() {
        return tellerMapper.selectList(null);
    }

    @Override
    public long countByInstitutionNo(String institutionNo) {
        LambdaQueryWrapper<Teller> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Teller::getInstitutionNo, institutionNo);
        return tellerMapper.selectCount(wrapper);
    }

    @Override
    public void save(Teller teller) {
        tellerMapper.insert(teller);
    }

    @Override
    public void update(Teller teller) {
        tellerMapper.updateById(teller);
    }
}
