package com.bank.teller.application.institution;

import com.bank.teller.domain.enums.InstitutionStatus;
import com.bank.teller.domain.institution.entity.Institution;
import com.bank.teller.domain.institution.repository.InstitutionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InstitutionApplicationServiceTest {

    @Mock private InstitutionRepository institutionRepository;

    @InjectMocks
    private InstitutionApplicationService institutionApplicationService;

    @Test
    void create_shouldSucceed_whenCommandIsValid() {
        CreateInstitutionCommand command = new CreateInstitutionCommand();
        command.setInstitutionNo("INST001");
        command.setInstitutionName("总行营业部");
        command.setInstitutionLevel("1");
        command.setParentInstitutionNo("ROOT");

        doNothing().when(institutionRepository).save(any());

        Institution result = institutionApplicationService.create(command);

        assertNotNull(result);
        assertEquals("INST001", result.getInstitutionNo());
        assertEquals("总行营业部", result.getInstitutionName());
        assertEquals("1", result.getInstitutionLevel());
        assertEquals("ROOT", result.getParentInstitutionNo());
        assertEquals(InstitutionStatus.NORMAL, result.getStatus());
        assertNotNull(result.getCreatedAt());

        ArgumentCaptor<Institution> captor = ArgumentCaptor.forClass(Institution.class);
        verify(institutionRepository).save(captor.capture());
        Institution saved = captor.getValue();
        assertEquals("INST001", saved.getInstitutionNo());
        assertEquals(InstitutionStatus.NORMAL, saved.getStatus());
    }

    @Test
    void update_shouldSucceed_whenPartialFieldsProvided() {
        Institution existing = new Institution();
        existing.setId(1L);
        existing.setInstitutionNo("INST001");
        existing.setInstitutionName("总行营业部");
        existing.setInstitutionLevel("1");
        existing.setStatus(InstitutionStatus.NORMAL);

        UpdateInstitutionCommand command = new UpdateInstitutionCommand();
        command.setInstitutionName("总行营业部（更新）");
        command.setInstitutionLevel("2");

        when(institutionRepository.findByInstitutionNo("INST001")).thenReturn(Optional.of(existing));
        doNothing().when(institutionRepository).update(existing);

        Institution result = institutionApplicationService.update("INST001", command);

        assertNotNull(result);
        assertEquals("总行营业部（更新）", result.getInstitutionName());
        assertEquals("2", result.getInstitutionLevel());
        assertEquals(InstitutionStatus.NORMAL, result.getStatus());

        verify(institutionRepository).findByInstitutionNo("INST001");
        verify(institutionRepository).update(existing);
    }

    @Test
    void update_shouldThrowException_whenInstitutionNotExist() {
        when(institutionRepository.findByInstitutionNo("NONEXIST")).thenReturn(Optional.empty());

        UpdateInstitutionCommand command = new UpdateInstitutionCommand();
        command.setInstitutionName("测试");

        assertThrows(RuntimeException.class, () -> institutionApplicationService.update("NONEXIST", command));
        verify(institutionRepository, never()).update(any());
    }

    @Test
    void delete_shouldSucceed_whenInstitutionExists() {
        Institution existing = new Institution();
        existing.setId(1L);
        existing.setInstitutionNo("INST001");
        existing.setStatus(InstitutionStatus.NORMAL);

        when(institutionRepository.findByInstitutionNo("INST001")).thenReturn(Optional.of(existing));
        doNothing().when(institutionRepository).update(existing);

        institutionApplicationService.delete("INST001");

        assertEquals(InstitutionStatus.STOPPED, existing.getStatus());
        assertNotNull(existing.getUpdatedAt());
        verify(institutionRepository).findByInstitutionNo("INST001");
        verify(institutionRepository).update(existing);
    }

    @Test
    void delete_shouldThrowException_whenInstitutionNotExist() {
        when(institutionRepository.findByInstitutionNo("NONEXIST")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> institutionApplicationService.delete("NONEXIST"));
        verify(institutionRepository, never()).update(any());
    }
}
