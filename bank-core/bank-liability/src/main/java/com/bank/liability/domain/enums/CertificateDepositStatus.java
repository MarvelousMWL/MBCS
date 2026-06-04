package com.bank.liability.domain.enums;

public enum CertificateDepositStatus {
    PENDING("待生效"),
    ACTIVE("正常"),
    FROZEN("冻结"),
    REDEEMED("已兑付"),
    CANCELLED("已撤销");

    private final String description;

    CertificateDepositStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
