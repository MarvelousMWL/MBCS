package com.bank.teller.api;

import com.bank.common.result.Result;
import com.bank.teller.application.teller.create.CreateTellerCommand;
import com.bank.teller.application.teller.update.UpdateTellerCommand;
import com.bank.teller.domain.teller.entity.Teller;
import com.bank.teller.domain.enums.TellerType;
import com.bank.teller.domain.teller.repository.TellerRepository;
import com.bank.teller.application.teller.TellerApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/teller/teller")
@RequiredArgsConstructor
public class TellerController {

    private final TellerRepository tellerRepository;
    private final TellerApplicationService tellerApplicationService;

    @GetMapping
    public Result<List<Teller>> findAll(@RequestHeader(value = "operator-no", defaultValue = "SYSTEM") String operatorNo) {
        Teller currentTeller = tellerRepository.findByTellerNo(operatorNo).orElse(null);
        if (currentTeller == null) {
            return Result.success(List.of());
        }

        if (TellerType.VAULT.equals(currentTeller.getTellerType())) {
            return Result.success(tellerRepository.findByInstitutionNo(currentTeller.getInstitutionNo()));
        } else {
            return Result.success(tellerRepository.findByInstitutionNo(currentTeller.getInstitutionNo()));
        }
    }

    @GetMapping("/{tellerNo}")
    public Result<Teller> findByNo(@PathVariable String tellerNo) {
        return Result.success(tellerRepository.findByTellerNo(tellerNo).orElse(null));
    }

    @GetMapping("/institution/{institutionNo}")
    public Result<List<Teller>> findByInstitutionNo(@PathVariable String institutionNo) {
        return Result.success(tellerRepository.findByInstitutionNo(institutionNo));
    }

    @PostMapping
    public Result<Teller> create(@Valid @RequestBody CreateTellerCommand command,
                                  @RequestHeader(value = "operator-no", defaultValue = "SYSTEM") String operatorNo) {
        Teller currentTeller = tellerRepository.findByTellerNo(operatorNo).orElse(null);
        if (currentTeller == null || !TellerType.VAULT.equals(currentTeller.getTellerType())) {
            return Result.error("只有库管柜员可以创建柜员");
        }
        if (!currentTeller.getInstitutionNo().equals(command.getInstitutionNo())) {
            return Result.error("只能创建本机构的柜员");
        }
        return Result.success(tellerApplicationService.create(command));
    }

    @PutMapping("/{tellerNo}")
    public Result<Teller> update(@PathVariable String tellerNo,
                                  @Valid @RequestBody UpdateTellerCommand command,
                                  @RequestHeader(value = "operator-no", defaultValue = "SYSTEM") String operatorNo) {
        Teller currentTeller = tellerRepository.findByTellerNo(operatorNo).orElse(null);
        if (currentTeller == null || !TellerType.VAULT.equals(currentTeller.getTellerType())) {
            return Result.error("只有库管柜员可以修改柜员");
        }
        Teller targetTeller = tellerRepository.findByTellerNo(tellerNo).orElse(null);
        if (targetTeller == null) {
            return Result.error("柜员不存在");
        }
        if (!currentTeller.getInstitutionNo().equals(targetTeller.getInstitutionNo())) {
            return Result.error("只能修改本机构的柜员");
        }
        return Result.success(tellerApplicationService.update(tellerNo, command));
    }

    @DeleteMapping("/{tellerNo}")
    public Result<Void> delete(@PathVariable String tellerNo,
                               @RequestHeader(value = "operator-no", defaultValue = "SYSTEM") String operatorNo) {
        Teller currentTeller = tellerRepository.findByTellerNo(operatorNo).orElse(null);
        if (currentTeller == null || !TellerType.VAULT.equals(currentTeller.getTellerType())) {
            return Result.error("只有库管柜员可以删除柜员");
        }
        Teller targetTeller = tellerRepository.findByTellerNo(tellerNo).orElse(null);
        if (targetTeller == null) {
            return Result.error("柜员不存在");
        }
        if (!currentTeller.getInstitutionNo().equals(targetTeller.getInstitutionNo())) {
            return Result.error("只能删除本机构的柜员");
        }
        if (currentTeller.getTellerNo().equals(tellerNo)) {
            return Result.error("不能删除自己");
        }
        tellerApplicationService.delete(tellerNo);
        return Result.success();
    }
}
