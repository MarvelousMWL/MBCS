package com.bank.teller.application.teller;

import com.bank.teller.application.teller.create.CreateTellerCommand;
import com.bank.teller.application.teller.update.UpdateTellerCommand;
import com.bank.teller.domain.enums.TellerStatus;
import com.bank.teller.domain.teller.entity.Teller;
import com.bank.teller.domain.teller.repository.TellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TellerApplicationService {

    private final TellerRepository tellerRepository;
    private final PasswordEncoder passwordEncoder;

    public Teller create(CreateTellerCommand command) {
        Teller teller = new Teller();
        teller.setTellerNo(command.getTellerNo());
        teller.setTellerName(command.getTellerName());
        teller.setInstitutionNo(command.getInstitutionNo());
        teller.setTellerType(command.getTellerType());
        teller.setPassword(passwordEncoder.encode(command.getPassword()));
        teller.setStatus(TellerStatus.NORMAL);
        tellerRepository.save(teller);
        return teller;
    }

    public Teller update(String tellerNo, UpdateTellerCommand command) {
        Teller teller = tellerRepository.findByTellerNo(tellerNo)
                .orElseThrow(() -> new RuntimeException("柜员不存在"));
        if (command.getTellerName() != null) {
            teller.setTellerName(command.getTellerName());
        }
        if (command.getTellerType() != null) {
            teller.setTellerType(command.getTellerType());
        }
        if (command.getStatus() != null) {
            teller.setStatus(command.getStatus());
        }
        if (command.getPassword() != null && !command.getPassword().isEmpty()) {
            teller.setPassword(passwordEncoder.encode(command.getPassword()));
        }
        tellerRepository.update(teller);
        return teller;
    }

    public void delete(String tellerNo) {
        Teller teller = tellerRepository.findByTellerNo(tellerNo)
                .orElseThrow(() -> new RuntimeException("柜员不存在"));
        teller.setStatus(TellerStatus.RESIGNED);
        tellerRepository.update(teller);
    }
}
