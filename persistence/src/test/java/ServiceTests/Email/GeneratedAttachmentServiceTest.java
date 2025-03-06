package ServiceTests.Email;

import cz.inspire.email.entity.GeneratedAttachmentEntity;
import cz.inspire.email.repository.GeneratedAttachmentRepository;
import cz.inspire.email.service.GeneratedAttachmentService;
import cz.inspire.exception.SystemException;
import jakarta.ejb.CreateException;
import jakarta.ejb.FinderException;
import jakarta.ejb.RemoveException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GeneratedAttachmentServiceTest {

    @Mock
    private GeneratedAttachmentRepository generatedAttachmentRepository;

    private GeneratedAttachmentService generatedAttachmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        generatedAttachmentService = new GeneratedAttachmentService(generatedAttachmentRepository);
    }

    @Test
    void testCreate_Success() throws CreateException {
        GeneratedAttachmentEntity entity = new GeneratedAttachmentEntity("1", "user@example.com", null, null, null);
        when(generatedAttachmentRepository.create(entity)).thenReturn(entity);

        GeneratedAttachmentEntity result = generatedAttachmentService.create(entity);

        assertEquals(entity, result);
        verify(generatedAttachmentRepository, times(1)).create(entity);
    }

    @Test
    void testCreate_Failure() {
        GeneratedAttachmentEntity entity = new GeneratedAttachmentEntity("1", "user@example.com", null, null, null);
        when(generatedAttachmentRepository.create(entity)).thenThrow(new RuntimeException("Database failure"));

        CreateException exception = assertThrows(CreateException.class, () -> generatedAttachmentService.create(entity));
        assertEquals("Failed to create GeneratedAttachmentEntity", exception.getMessage());
        verify(generatedAttachmentRepository, times(1)).create(entity);
    }

    @Test
    void testUpdate_Success() throws SystemException {
        GeneratedAttachmentEntity entity = new GeneratedAttachmentEntity("1", "user@example.com", null, null, null);
        when(generatedAttachmentRepository.update(entity)).thenReturn(entity);

        GeneratedAttachmentEntity result = generatedAttachmentService.update(entity);

        assertEquals(entity, result);
        verify(generatedAttachmentRepository, times(1)).update(entity);
    }

    @Test
    void testUpdate_Failure() {
        GeneratedAttachmentEntity entity = new GeneratedAttachmentEntity("1", "user@example.com", null, null, null);
        when(generatedAttachmentRepository.update(entity)).thenThrow(new RuntimeException("Database failure"));

        SystemException exception = assertThrows(SystemException.class, () -> generatedAttachmentService.update(entity));
        assertEquals("Failed to update GeneratedAttachmentEntity", exception.getMessage());
        verify(generatedAttachmentRepository, times(1)).update(entity);
    }

    @Test
    void testRemove_Success() throws RemoveException {
        GeneratedAttachmentEntity entity = new GeneratedAttachmentEntity("1", "user@example.com", null, null, null);
        doNothing().when(generatedAttachmentRepository).delete(entity);

        generatedAttachmentService.delete(entity);

        verify(generatedAttachmentRepository, times(1)).delete(entity);
    }

    @Test
    void testRemove_Failure() {
        GeneratedAttachmentEntity entity = new GeneratedAttachmentEntity("1", "user@example.com", null, null, null);
        doThrow(new RuntimeException("Database failure")).when(generatedAttachmentRepository).delete(entity);

        RemoveException exception = assertThrows(RemoveException.class, () -> generatedAttachmentService.delete(entity));
        assertEquals("Failed to remove GeneratedAttachmentEntity", exception.getMessage());
        verify(generatedAttachmentRepository, times(1)).delete(entity);
    }

    @Test
    void testFindByEmailAndHistory_Success() throws FinderException {
        String historyId = "history1";
        String email = "user@example.com";
        List<GeneratedAttachmentEntity> expectedAttachments = List.of(
                new GeneratedAttachmentEntity("1", email, null, null, null),
                new GeneratedAttachmentEntity("2", email, null, null, null)
        );
        when(generatedAttachmentRepository.findByEmailAndHistory(historyId, email)).thenReturn(expectedAttachments);

        List<GeneratedAttachmentEntity> result = generatedAttachmentService.findByEmailAndHistory(historyId, email);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(generatedAttachmentRepository, times(1)).findByEmailAndHistory(historyId, email);
    }

    @Test
    void testFindByHistory_Success() throws FinderException {
        String historyId = "history1";
        List<GeneratedAttachmentEntity> expectedAttachments = List.of(
                new GeneratedAttachmentEntity("1", "user1@example.com", null, null, null),
                new GeneratedAttachmentEntity("2", "user2@example.com", null, null, null)
        );
        when(generatedAttachmentRepository.findByHistory(historyId)).thenReturn(expectedAttachments);

        List<GeneratedAttachmentEntity> result = generatedAttachmentService.findByHistory(historyId);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(generatedAttachmentRepository, times(1)).findByHistory(historyId);
    }

    @Test
    void testFindByEmailAndHistoryAndTemplate_Success() throws FinderException {
        String historyId = "history1";
        String email = "user@example.com";
        String templateId = "template1";
        List<GeneratedAttachmentEntity> expectedAttachments = List.of(
                new GeneratedAttachmentEntity("1", email, null, null, null)
        );
        when(generatedAttachmentRepository.findByEmailAndHistoryAndTemplate(historyId, email, templateId))
                .thenReturn(expectedAttachments);

        List<GeneratedAttachmentEntity> result = generatedAttachmentService.findByEmailAndHistoryAndTemplate(historyId, email, templateId);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(generatedAttachmentRepository, times(1))
                .findByEmailAndHistoryAndTemplate(historyId, email, templateId);
    }

    @Test
    void testDelete_NullEntity() {
        RemoveException exception = assertThrows(RemoveException.class, () -> generatedAttachmentService.delete(null));
        assertEquals("Cannot delete null as GeneratedAttachmentEntity", exception.getMessage());
    }
}
