package com.bank.teller.api;

import com.bank.common.result.Result;
import com.bank.teller.application.institution.CreateInstitutionCommand;
import com.bank.teller.application.institution.UpdateInstitutionCommand;
import com.bank.teller.application.institution.InstitutionApplicationService;
import com.bank.teller.domain.enums.TellerType;
import com.bank.teller.domain.institution.entity.Institution;
import com.bank.teller.domain.institution.repository.InstitutionRepository;
import com.bank.teller.domain.teller.repository.TellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/teller/institution")
@RequiredArgsConstructor
public class InstitutionController {

    private final InstitutionRepository institutionRepository;
    private final TellerRepository tellerRepository;
    private final InstitutionApplicationService institutionApplicationService;

    @GetMapping
    public Result<List<Institution>> findAll() {
        return Result.success(institutionRepository.findAll());
    }

    @GetMapping("/{institutionNo}")
    public Result<Institution> findByNo(@PathVariable String institutionNo) {
        return Result.success(institutionRepository.findByInstitutionNo(institutionNo).orElse(null));
    }

    @PostMapping
    public Result<Institution> create(@Valid @RequestBody CreateInstitutionCommand command,
                                      @RequestHeader(value = "operator-no", defaultValue = "SYSTEM") String operatorNo) {
        var currentTeller = tellerRepository.findByTellerNo(operatorNo).orElse(null);
        if (currentTeller == null || !TellerType.VAULT.equals(currentTeller.getTellerType())) {
            return Result.error("只有库管柜员可以创建机构");
        }
        return Result.success(institutionApplicationService.create(command));
    }

    @PutMapping("/{institutionNo}")
    public Result<Institution> update(@PathVariable String institutionNo,
                                      @Valid @RequestBody UpdateInstitutionCommand command,
                                      @RequestHeader(value = "operator-no", defaultValue = "SYSTEM") String operatorNo) {
        var currentTeller = tellerRepository.findByTellerNo(operatorNo).orElse(null);
        if (currentTeller == null || !TellerType.VAULT.equals(currentTeller.getTellerType())) {
            return Result.error("只有库管柜员可以修改机构");
        }
        var targetInstitution = institutionRepository.findByInstitutionNo(institutionNo).orElse(null);
        if (targetInstitution == null) {
            return Result.error("机构不存在");
        }
        return Result.success(institutionApplicationService.update(institutionNo, command));
    }

    @DeleteMapping("/{institutionNo}")
    public Result<Void> delete(@PathVariable String institutionNo,
                               @RequestHeader(value = "operator-no", defaultValue = "SYSTEM") String operatorNo) {
        var currentTeller = tellerRepository.findByTellerNo(operatorNo).orElse(null);
        if (currentTeller == null || !TellerType.VAULT.equals(currentTeller.getTellerType())) {
            return Result.error("只有库管柜员可以删除机构");
        }
        var targetInstitution = institutionRepository.findByInstitutionNo(institutionNo).orElse(null);
        if (targetInstitution == null) {
            return Result.error("机构不存在");
        }
        institutionApplicationService.delete(institutionNo);
        return Result.success();
    }
}
