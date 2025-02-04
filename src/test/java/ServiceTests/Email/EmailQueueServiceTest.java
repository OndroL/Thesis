package ServiceTests.Email;

import cz.inspire.email.entity.EmailQueueEntity;
import cz.inspire.email.repository.EmailQueueRepository;
import cz.inspire.email.service.EmailQueueService;
import cz.inspire.enterprise.exception.SystemException;
import jakarta.ejb.CreateException;
import jakarta.data.Limit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailQueueServiceTest {

    @Mock
    private EmailQueueRepository emailQueueRepository;

    @Spy
    private EmailQueueService emailQueueService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        emailQueueService = spy(new EmailQueueService(emailQueueRepository));
    }

    @Test
    void testCreate_Success() throws CreateException {
        EmailQueueEntity entity = new EmailQueueEntity(
                "1", new Date(), "history1", "recipient@example.com", 1, false, "depHistory1"
        );

        emailQueueService.create(entity);

        verify(emailQueueService, times(1)).create(entity);
        verify(emailQueueRepository, times(1)).save(entity);
    }

    @Test
    void testCreate_Failure() throws CreateException {
        EmailQueueEntity entity = new EmailQueueEntity(
                "1", new Date(), "history1", "recipient@example.com", 1, false, "depHistory1"
        );
        doThrow(new RuntimeException("Database failure")).when(emailQueueRepository).save(entity);

        assertThrows(CreateException.class, () -> emailQueueService.create(entity));

        verify(emailQueueService, times(1)).create(entity);
    }

    @Test
    void testUpdate_Success() throws SystemException {
        EmailQueueEntity entity = new EmailQueueEntity(
                "1", new Date(), "history1", "recipient@example.com", 1, false, "depHistory1"
        );

        emailQueueService.update(entity);

        verify(emailQueueService, times(1)).update(entity);
        verify(emailQueueRepository, times(1)).save(entity);
    }

    @Test
    void testUpdate_Failure() throws SystemException {
        EmailQueueEntity entity = new EmailQueueEntity(
                "1", new Date(), "history1", "recipient@example.com", 1, false, "depHistory1"
        );
        doThrow(new RuntimeException("Database failure")).when(emailQueueRepository).save(entity);

        assertThrows(SystemException.class, () -> emailQueueService.update(entity));

        verify(emailQueueService, times(1)).update(entity);
    }

    @Test
    void testRemove_Success() throws SystemException {
        EmailQueueEntity entity = new EmailQueueEntity(
                "1", new Date(), "history1", "recipient@example.com", 1, false, "depHistory1"
        );

        emailQueueService.delete(entity);

        verify(emailQueueService, times(1)).delete(entity);
        verify(emailQueueRepository, times(1)).delete(entity);
    }

    @Test
    void testRemove_Failure() throws SystemException {
        EmailQueueEntity entity = new EmailQueueEntity(
                "1", new Date(), "history1", "recipient@example.com", 1, false, "depHistory1"
        );
        doThrow(new RuntimeException("Database failure")).when(emailQueueRepository).delete(entity);

        assertThrows(SystemException.class, () -> emailQueueService.delete(entity));

        verify(emailQueueService, times(1)).delete(entity);
    }

    @Test
    void testFindAll_Success() {
        List<EmailQueueEntity> entities = List.of(
                new EmailQueueEntity("1", new Date(), "history1", "recipient1@example.com", 1, false, "depHistory1"),
                new EmailQueueEntity("2", new Date(), "history2", "recipient2@example.com", 2, true, "depHistory2")
        );

        when(emailQueueRepository.findAllOrdered()).thenReturn(entities);

        List<EmailQueueEntity> result = emailQueueService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(emailQueueRepository, times(1)).findAllOrdered();
    }

    @Test
    void testFindAll_WithLimit_Success() {
        List<EmailQueueEntity> entities = List.of(
                new EmailQueueEntity("1", new Date(), "history1", "recipient1@example.com", 1, false, "depHistory1")
        );

        when(emailQueueRepository.findAll(new Limit(1, 1))).thenReturn(entities);

        List<EmailQueueEntity> result = emailQueueService.findAll(1, 1);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(emailQueueRepository, times(1)).findAll(new Limit(1, 1));
    }

    @Test
    void testFindFirstMail_Success() {
        EmailQueueEntity entity = new EmailQueueEntity("1", new Date(), "history1", "recipient@example.com", 1, false, "depHistory1");
        when(emailQueueRepository.findFirstMail(Limit.of(1))).thenReturn(Optional.of(entity));

        Optional<EmailQueueEntity> result = emailQueueService.findFirstMail();

        assertTrue(result.isPresent());
        assertEquals("1", result.get().getId());
        verify(emailQueueRepository, times(1)).findFirstMail(Limit.of(1));
    }

    @Test
    void testFindByHistory_Success() {
        List<EmailQueueEntity> entities = List.of(
                new EmailQueueEntity("1", new Date(), "history1", "recipient@example.com", 1, false, "depHistory1")
        );

        when(emailQueueRepository.findByHistory("history1")).thenReturn(entities);

        List<EmailQueueEntity> result = emailQueueService.findByHistory("history1");

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(emailQueueRepository, times(1)).findByHistory("history1");
    }

    @Test
    void testFindByDependentHistory_Success() {
        List<EmailQueueEntity> entities = List.of(
                new EmailQueueEntity("1", new Date(), "history1", "recipient@example.com", 1, false, "depHistory1")
        );

        when(emailQueueRepository.findByDependentHistory("depHistory1")).thenReturn(entities);

        List<EmailQueueEntity> result = emailQueueService.findByDependentHistory("depHistory1");

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(emailQueueRepository, times(1)).findByDependentHistory("depHistory1");
    }
}
