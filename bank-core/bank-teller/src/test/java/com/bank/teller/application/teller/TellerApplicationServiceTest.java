package com.bank.teller.application.teller;

import com.bank.teller.application.teller.create.CreateTellerCommand;
import com.bank.teller.application.teller.update.UpdateTellerCommand;
import com.bank.teller.domain.enums.TellerStatus;
import com.bank.teller.domain.enums.TellerType;
import com.bank.teller.domain.teller.entity.Teller;
import com.bank.teller.domain.teller.repository.TellerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TellerApplicationServiceTest {

    @Mock
    private TellerRepository tellerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private TellerApplicationService tellerApplicationService;

    @Test
    void create_shouldSucceed_whenCommandIsValid() {
        CreateTellerCommand command = new CreateTellerCommand();
        command.setTellerNo( T001);
        command.setTellerName(王五);
        command.setInstitutionNo(INST001);
        command.setTellerType(TellerType.NORMAL);
        command.setPassword(plainPassword);

        when(passwordEncoder.encode(plainPassword)).thenReturn();

        doNothing().when(tellerRepository).save(any(Teller.class));

        Teller result = tellerApplicationService.create(command);

        assertNotNull(result);
        assertEquals(T001, result.getTellerNo());
        assertEquals(王五, result.getTellerName());
        assertEquals(INST001, result.getInstitutionNo());
        assertEquals(TellerType.NORMAL, result.getTellerType());
        assertEquals(TellerStatus.NORMAL, result.getStatus());

        ArgumentCaptor<Teller> tellerCaptor = ArgumentCaptor.forClass(Teller.class);
        verify(tellerRepository).save(tellerCaptor.capture());

        Teller savedTeller = tellerCaptor.getValue();
        assertEquals(T001, savedTeller.getTellerNo());
        assertEquals(王五, savedTeller.getTellerName());
        assertEquals(INST001, savedTeller.getInstitutionNo());
        assertEquals(TellerType.NORMAL, savedTeller.getTellerType());
        assertEquals(, savedTeller.getPassword());
        assertEquals(TellerStatus.NORMAL, savedTeller.getStatus());

        verify(passwordEncoder).encode(plainPassword);
    }

    @Test
    void update_shouldSucceed_whenPartialFieldsProvided() {
        Teller existingTeller = new Teller();
        existingTeller.setId(1L);
        existingTeller.setTellerNo(T001);
        existingTeller.setTellerName(王五);
        existingTeller.setInstitutionNo(INST001);
        existingTeller.setTellerType(TellerType.NORMAL);
        existingTeller.setPassword(oldPassword);
        existingTeller.setStatus(TellerStatus.NORMAL);

        UpdateTellerCommand command = new UpdateTellerCommand();
        command.setTellerName(王五更新);
        command.setTellerType(TellerType.VAULT);

        when(tellerRepository.findByTellerNo(T001)).thenReturn(Optional.of(existingTeller));
        doNothing().when(tellerRepository).update(any(Teller.class));

        Teller result = tellerApplicationService.update(T001, command);

        assertNotNull(result);
        assertEquals(王五更新, result.getTellerName());
        assertEquals(TellerType.VAULT, result.getTellerType());
        assertEquals(oldPassword, result.getPassword());
        assertEquals(TellerStatus.NORMAL, result.getStatus());

        verify(tellerRepository).findByTellerNo(T001);
        verify(tellerRepository).update(existingTeller);
    }

    @Test
    void update_shouldThrowException_whenTellerNotExist() {
        when(tellerRepository.findByTellerNo(NONEXIST)).thenReturn(Optional.empty());

        UpdateTellerCommand command = new UpdateTellerCommand();
        command.setTellerName(测试);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> tellerApplicationService.update(NONEXIST, command));
        assertNotNull(exception.getMessage());

        verify(tellerRepository).findByTellerNo(NONEXIST);
        verify(tellerRepository, never()).update(any());
    }

    @Test
    void delete_shouldSucceed_whenTellerExists() {
        Teller existingTeller = new Teller();
        existingTeller.setId(1L);
        existingTeller.setTellerNo(T001);
        existingTeller.setTellerName(王五);
        existingTeller.setStatus(TellerStatus.NORMAL);

        when(tellerRepository.findByTellerNo(T001)).thenReturn(Optional.of(existingTeller));
        doNothing().when(tellerRepository).update(any(Teller.class));

        tellerApplicationService.delete(T001);

        assertEquals(TellerStatus.RESIGNED, existingTeller.getStatus());

        verify(tellerRepository).findByTellerNo(T001);
        verify(tellerRepository).update(existingTeller);
    }

    @Test
    void delete_shouldThrowException_whenTellerNotExist() {
        when(tellerRepository.findByTellerNo(NONEXIST)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> tellerApplicationService.delete(NONEXIST));
        assertNotNull(exception.getMessage());

        verify(tellerRepository).findByTellerNo(NONEXIST);
        verify(tellerRepository, never()).update(any());
    }
}
