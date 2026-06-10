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

    /** 会话超时时间：10分钟无活动自动过期 */
    private static final long SESSION_TIMEOUT_MS = 10 * 60 * 1000L;

    private static final Map<String, LoginResponse> TOKEN_STORE = new ConcurrentHashMap<>();
    private static final Map<String, String> ACTIVE_SESSIONS = new ConcurrentHashMap<>();

    private void purgeExpiredSessions() {
        long now = System.currentTimeMillis();
        TOKEN_STORE.entrySet().removeIf(entry -> {
            boolean expired = (now - entry.getValue().getLastAccessTime()) > SESSION_TIMEOUT_MS;
            if (expired) {
                ACTIVE_SESSIONS.remove(entry.getValue().getTellerNo());
            }
            return expired;
        });
    }

    public LoginResponse login(LoginCommand command) {
        purgeExpiredSessions();

        String existingToken = ACTIVE_SESSIONS.get(command.getTellerNo());
        if (existingToken != null && TOKEN_STORE.containsKey(existingToken)) {
            if (!command.isForce()) {
                throw new BusinessException(409, "柜员已登录，是否强制登录（挤掉之前的会话）？");
            }
            TOKEN_STORE.remove(existingToken);
            ACTIVE_SESSIONS.remove(command.getTellerNo());
        }

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
                .lastAccessTime(System.currentTimeMillis())
                .build();

        TOKEN_STORE.put(token, response);
        ACTIVE_SESSIONS.put(teller.getTellerNo(), token);
        return response;
    }

    public void logout(String token) {
        LoginResponse response = TOKEN_STORE.get(token);
        if (response != null) {
            ACTIVE_SESSIONS.remove(response.getTellerNo());
        }
        TOKEN_STORE.remove(token);
    }

    public LoginResponse validate(String token) {
        purgeExpiredSessions();

        LoginResponse response = TOKEN_STORE.get(token);
        if (response == null) {
            throw new BusinessException(401, "未登录或会话已过期");
        }

        response.setLastAccessTime(System.currentTimeMillis());
        return response;
    }

    public static LoginResponse getByToken(String token) {
        return TOKEN_STORE.get(token);
    }

    /**
     * 清除所有会话（用于测试清理）
     */
    public static void clearSessions() {
        TOKEN_STORE.clear();
        ACTIVE_SESSIONS.clear();
    }
}