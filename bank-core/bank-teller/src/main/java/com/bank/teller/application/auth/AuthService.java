package com.bank.teller.application.auth;

import com.bank.common.exception.BusinessException;
import com.bank.teller.domain.enums.TellerStatus;
import com.bank.teller.domain.teller.entity.Teller;
import com.bank.teller.domain.teller.repository.TellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final TellerRepository tellerRepository;
    private final PasswordEncoder passwordEncoder;

    private static final Map<String, LoginResponse> TOKEN_STORE = new ConcurrentHashMap<>();

    public LoginResponse login(LoginCommand command) {
        Teller teller = tellerRepository.findByTellerNo(command.getTellerNo())
                .orElseThrow(() -> new BusinessException(400, "柜员不存在"));

        if (!teller.getInstitutionNo().equals(command.getInstitutionNo())) {
            throw new BusinessException(400, "机构编号不匹配");
        }

        if (teller.getStatus() != TellerStatus.NORMAL) {
            throw new BusinessException(400, "柜员状态异常");
        }

        if (!passwordEncoder.matches(command.getPassword(), teller.getPassword())) {
            throw new BusinessException(400, "密码错误");
        }

        String token = UUID.randomUUID().toString().replace("-", "");
        LoginResponse response = LoginResponse.builder()
                .token(token)
                .tellerNo(teller.getTellerNo())
                .tellerName(teller.getTellerName())
                .institutionNo(teller.getInstitutionNo())
                .tellerType(teller.getTellerType().getCode())
                .status(teller.getStatus().getCode())
                .build();

        TOKEN_STORE.put(token, response);
        return response;
    }

    public void logout(String token) {
        TOKEN_STORE.remove(token);
    }

    public LoginResponse validate(String token) {
        LoginResponse response = TOKEN_STORE.get(token);
        if (response == null) {
            throw new BusinessException(401, "未登录或会话已过期");
        }
        return response;
    }

    public static LoginResponse getByToken(String token) {
        return TOKEN_STORE.get(token);
    }
}
