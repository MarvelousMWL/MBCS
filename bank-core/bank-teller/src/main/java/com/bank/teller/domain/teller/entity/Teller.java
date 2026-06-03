package com.bank.teller.domain.teller.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bank.teller.domain.enums.TellerStatus;
import com.bank.teller.domain.enums.TellerType;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("sys_teller")
public class Teller {
    private Long id;
    private String tellerNo;
    private String tellerName;
    private String institutionNo;
    private TellerType tellerType;
    private String password;
    private TellerStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public boolean isNormal() {
        return TellerStatus.NORMAL.equals(this.status);
    }

    public boolean isVault() {
        return TellerType.VAULT.equals(this.tellerType);
    }
}
