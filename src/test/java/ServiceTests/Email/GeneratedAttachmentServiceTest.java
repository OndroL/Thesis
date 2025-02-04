package ServiceTests.Email;

import cz.inspire.email.entity.GeneratedAttachmentEntity;
import cz.inspire.email.repository.GeneratedAttachmentRepository;
import cz.inspire.email.service.GeneratedAttachmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
public class GeneratedAttachmentServiceTest {

    @Mock
    private GeneratedAttachmentRepository generatedAttachmentRepository;

    @Spy
    private GeneratedAttachmentService generatedAttachmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        generatedAttachmentService = spy(new GeneratedAttachmentService(generatedAttachmentRepository));
    }

    @Test
    void testFindByEmailAndHistory_Success() {
        String historyId = "history1";
        String email = "user@example.com";

        List<GeneratedAttachmentEntity> expectedAttachments = List.of(
                new GeneratedAttachmentEntity("1", email, null, null, null),
                new GeneratedAttachmentEntity("2", email, null, null, null)
        );

        when(generatedAttachmentRepository.findByEmailAndHistory(historyId, email))
                .thenReturn(expectedAttachments);

        List<GeneratedAttachmentEntity> result = generatedAttachmentService.findByEmailAndHistory(historyId, email);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(generatedAttachmentRepository, times(1)).findByEmailAndHistory(historyId, email);
    }

    @Test
    void testFindByHistory_Success() {
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
    void testFindByEmailAndHistoryAndTemplate_Success() {
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
}
