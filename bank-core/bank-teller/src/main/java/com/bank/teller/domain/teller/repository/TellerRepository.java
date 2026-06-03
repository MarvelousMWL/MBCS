package com.bank.teller.domain.teller.repository;

import com.bank.teller.domain.teller.entity.Teller;
import java.util.List;
import java.util.Optional;

public interface TellerRepository {
    Optional<Teller> findById(Long id);
    Optional<Teller> findByTellerNo(String tellerNo);
    List<Teller> findByInstitutionNo(String institutionNo);
    List<Teller> findAll();
    long countByInstitutionNo(String institutionNo);
    void save(Teller teller);
    void update(Teller teller);
}
