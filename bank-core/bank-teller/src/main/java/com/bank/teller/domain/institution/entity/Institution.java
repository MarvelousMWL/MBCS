package com.bank.teller.domain.institution.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bank.teller.domain.enums.InstitutionStatus;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("sys_institution")
public class Institution {
    private Long id;
    private String institutionNo;
    private String institutionName;
    private String institutionLevel;
    private String parentInstitutionNo;
    private InstitutionStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public boolean isNormal() {
        return InstitutionStatus.NORMAL.equals(this.status);
    }
}
