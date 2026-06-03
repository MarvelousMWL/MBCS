package com.bank.teller.infrastructure.persistence;

import com.bank.teller.domain.institution.entity.Institution;
import com.bank.teller.domain.institution.repository.InstitutionRepository;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class InstitutionRepositoryImpl implements InstitutionRepository {

    private final InstitutionMapper institutionMapper;

    @Override
    public Optional<Institution> findById(Long id) {
        return Optional.ofNullable(institutionMapper.selectById(id));
    }

    @Override
    public Optional<Institution> findByInstitutionNo(String institutionNo) {
        LambdaQueryWrapper<Institution> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Institution::getInstitutionNo, institutionNo);
        return Optional.ofNullable(institutionMapper.selectOne(wrapper));
    }

    @Override
    public List<Institution> findAll() {
        return institutionMapper.selectList(null);
    }

    @Override
    public List<Institution> findByParentInstitutionNo(String parentInstitutionNo) {
        LambdaQueryWrapper<Institution> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Institution::getParentInstitutionNo, parentInstitutionNo);
        return institutionMapper.selectList(wrapper);
    }

    @Override
    public void save(Institution institution) {
        institutionMapper.insert(institution);
    }

    @Override
    public void update(Institution institution) {
        institutionMapper.updateById(institution);
    }
}
