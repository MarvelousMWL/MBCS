package com.bank.teller.domain.institution.repository;

import com.bank.teller.domain.institution.entity.Institution;
import java.util.List;
import java.util.Optional;

public interface InstitutionRepository {
    Optional<Institution> findById(Long id);
    Optional<Institution> findByInstitutionNo(String institutionNo);
    List<Institution> findAll();
    List<Institution> findByParentInstitutionNo(String parentInstitutionNo);
    void save(Institution institution);
    void update(Institution institution);
}
