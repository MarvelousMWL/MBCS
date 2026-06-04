package com.bank.liability.domain.certificatedeposit.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("cd_transfer_record")
public class CertificateDepositTransferRecord {
    private Long id;
    private String cdAccountNo;
    private String transferorAccountNo;
    private String transfereeAccountNo;
    private BigDecimal transferPrice;
    private String pricingType;
    private BigDecimal handlingFee;
    private String status;
    private LocalDate transferDate;
    private String operatorNo;
    private Integer version;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
