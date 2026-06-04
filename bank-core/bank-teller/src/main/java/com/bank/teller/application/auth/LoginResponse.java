package com.bank.teller.application.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String tellerNo;
    private String tellerName;
    private String institutionNo;
    private Integer tellerType;
    private Integer status;
}