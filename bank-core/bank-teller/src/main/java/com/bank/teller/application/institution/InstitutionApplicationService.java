package com.bank.teller.application.institution;

import com.bank.teller.domain.enums.InstitutionStatus;
import com.bank.teller.domain.institution.entity.Institution;
import com.bank.teller.domain.institution.repository.InstitutionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class InstitutionApplicationService {

    private final InstitutionRepository institutionRepository;

    public Institution create(CreateInstitutionCommand command) {
        Institution institution = new Institution();
        institution.setInstitutionNo(command.getInstitutionNo());
        institution.setInstitutionName(command.getInstitutionName());
        institution.setInstitutionLevel(command.getInstitutionLevel());
        institution.setParentInstitutionNo(command.getParentInstitutionNo());
        institution.setStatus(InstitutionStatus.NORMAL);
        institution.setCreatedAt(LocalDateTime.now());
        institutionRepository.save(institution);
        return institution;
    }

    public Institution update(String institutionNo, UpdateInstitutionCommand command) {
        Institution institution = institutionRepository.findByInstitutionNo(institutionNo)
                .orElseThrow(() -> new RuntimeException("机构不存在"));
        if (command.getInstitutionName() != null) {
            institution.setInstitutionName(command.getInstitutionName());
        }
        if (command.getInstitutionLevel() != null) {
            institution.setInstitutionLevel(command.getInstitutionLevel());
        }
        if (command.getParentInstitutionNo() != null) {
            institution.setParentInstitutionNo(command.getParentInstitutionNo());
        }
        if (command.getStatus() != null) {
            institution.setStatus(command.getStatus());
        }
        institution.setUpdatedAt(LocalDateTime.now());
        institutionRepository.update(institution);
        return institution;
    }

    public void delete(String institutionNo) {
        Institution institution = institutionRepository.findByInstitutionNo(institutionNo)
                .orElseThrow(() -> new RuntimeException("机构不存在"));
        institution.setStatus(InstitutionStatus.STOPPED);
        institution.setUpdatedAt(LocalDateTime.now());
        institutionRepository.update(institution);
    }
}
