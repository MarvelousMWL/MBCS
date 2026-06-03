package com.bank.teller.application.teller.update;

import com.bank.teller.domain.enums.TellerStatus;
import com.bank.teller.domain.enums.TellerType;
import lombok.Data;

@Data
public class UpdateTellerCommand {
    private String tellerName;
    private TellerType tellerType;
    private TellerStatus status;
    private String password;
}
