package com.bank.teller.application.auth;

import com.bank.common.exception.BusinessException;
import com.bank.teller.domain.enums.TellerStatus;
import com.bank.teller.domain.enums.TellerType;
import com.bank.teller.domain.teller.entity.Teller;
import com.bank.teller.domain.teller.repository.TellerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private TellerRepository tellerRepository;
    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @AfterEach
    void cleanup() {
        // 清除token存储，避免测试间的相互影响
        AuthService.getByToken("any");
    }

    private Teller createNormalTeller() {
        Teller teller = new Teller();
        teller.setTellerNo("T001");
        teller.setTellerName("张三");
        teller.setInstitutionNo("INST001");
        teller.setTellerType(TellerType.NORMAL);
        teller.setStatus(TellerStatus.NORMAL);
        teller.setPassword("encodedPassword");
        return teller;
    }

    private LoginCommand createLoginCommand() {
        LoginCommand command = new LoginCommand();
        command.setTellerNo("T001");
        command.setInstitutionNo("INST001");
        command.setPassword("plainPassword");
        return command;
    }

    @Test
    void login_shouldSucceed_whenCredentialsValid() {
        Teller teller = createNormalTeller();
        LoginCommand command = createLoginCommand();

        when(tellerRepository.findByTellerNo("T001")).thenReturn(Optional.of(teller));
        when(passwordEncoder.matches("plainPassword", "encodedPassword")).thenReturn(true);

        LoginResponse response = authService.login(command);

        assertNotNull(response);
        assertNotNull(response.getToken());
        assertEquals("T001", response.getTellerNo());
        assertEquals("张三", response.getTellerName());
        assertEquals("INST001", response.getInstitutionNo());

        verify(tellerRepository).findByTellerNo("T001");
        verify(passwordEncoder).matches("plainPassword", "encodedPassword");
    }

    @Test
    void login_shouldThrowException_whenTellerNotExist() {
        LoginCommand command = createLoginCommand();
        when(tellerRepository.findByTellerNo("T001")).thenReturn(Optional.empty());

        BusinessException ex = assertThrows(BusinessException.class, () -> authService.login(command));
        assertEquals(400, ex.getCode());
    }

    @Test
    void login_shouldThrowException_whenInstitutionNotMatch() {
        Teller teller = createNormalTeller();
        teller.setInstitutionNo("INST002");

        LoginCommand command = createLoginCommand();

        when(tellerRepository.findByTellerNo("T001")).thenReturn(Optional.of(teller));

        BusinessException ex = assertThrows(BusinessException.class, () -> authService.login(command));
        assertEquals(400, ex.getCode());
    }

    @Test
    void login_shouldThrowException_whenTellerStatusNotNormal() {
        Teller teller = createNormalTeller();
        teller.setStatus(TellerStatus.RESIGNED);

        LoginCommand command = createLoginCommand();

        when(tellerRepository.findByTellerNo("T001")).thenReturn(Optional.of(teller));

        BusinessException ex = assertThrows(BusinessException.class, () -> authService.login(command));
        assertEquals(400, ex.getCode());
    }

    @Test
    void login_shouldThrowException_whenPasswordWrong() {
        Teller teller = createNormalTeller();
        LoginCommand command = createLoginCommand();

        when(tellerRepository.findByTellerNo("T001")).thenReturn(Optional.of(teller));
        when(passwordEncoder.matches("plainPassword", "encodedPassword")).thenReturn(false);

        BusinessException ex = assertThrows(BusinessException.class, () -> authService.login(command));
        assertEquals(400, ex.getCode());
    }

    @Test
    void validate_shouldSucceed_whenTokenExists() {
        // 先登录获取token
        Teller teller = createNormalTeller();
        LoginCommand command = createLoginCommand();
        when(tellerRepository.findByTellerNo("T001")).thenReturn(Optional.of(teller));
        when(passwordEncoder.matches("plainPassword", "encodedPassword")).thenReturn(true);

        LoginResponse loginResp = authService.login(command);
        String token = loginResp.getToken();

        // 验证token
        LoginResponse validated = authService.validate(token);
        assertNotNull(validated);
        assertEquals("T001", validated.getTellerNo());
    }

    @Test
    void validate_shouldThrowException_whenTokenInvalid() {
        assertThrows(BusinessException.class, () -> authService.validate("invalid_token"));
    }

    @Test
    void logout_shouldRemoveToken() {
        // 先登录
        Teller teller = createNormalTeller();
        LoginCommand command = createLoginCommand();
        when(tellerRepository.findByTellerNo("T001")).thenReturn(Optional.of(teller));
        when(passwordEncoder.matches("plainPassword", "encodedPassword")).thenReturn(true);

        LoginResponse loginResp = authService.login(command);
        String token = loginResp.getToken();

        // 登出
        authService.logout(token);
        assertThrows(BusinessException.class, () -> authService.validate(token));
    }
}
