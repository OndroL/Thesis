package ServiceTests.Email;

import cz.inspire.email.entity.EmailQueueEntity;
import cz.inspire.email.repository.EmailQueueRepository;
import cz.inspire.email.service.EmailQueueService;
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

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmailQueueServiceTest {

    @Mock
    private EmailQueueRepository emailQueueRepository;

    private EmailQueueService emailQueueService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        emailQueueService = new EmailQueueService(emailQueueRepository);
    }

    @Test
    void testCreate_Success() throws CreateException {
        EmailQueueEntity entity = new EmailQueueEntity(
                "1", new Date(), "history1", "recipient@example.com", 1, false, "depHistory1"
        );

        when(emailQueueRepository.create(entity)).thenReturn(entity);

        EmailQueueEntity result = emailQueueService.create(entity);

        assertNotNull(result);
        verify(emailQueueRepository, times(1)).create(entity);
    }

    @Test
    void testCreate_Failure() {
        EmailQueueEntity entity = new EmailQueueEntity(
                "1", new Date(), "history1", "recipient@example.com", 1, false, "depHistory1"
        );

        doThrow(new RuntimeException("Database failure")).when(emailQueueRepository).create(entity);

        CreateException exception = assertThrows(CreateException.class, () -> emailQueueService.create(entity));
        assertEquals("Failed to create EmailQueueEntity", exception.getMessage());

        verify(emailQueueRepository, times(1)).create(entity);
    }

    @Test
    void testUpdate_Success() throws SystemException {
        EmailQueueEntity entity = new EmailQueueEntity(
                "1", new Date(), "history1", "recipient@example.com", 1, false, "depHistory1"
        );

        when(emailQueueRepository.update(entity)).thenReturn(entity);

        EmailQueueEntity result = emailQueueService.update(entity);

        assertNotNull(result);
        verify(emailQueueRepository, times(1)).update(entity);
    }

    @Test
    void testUpdate_Failure() {
        EmailQueueEntity entity = new EmailQueueEntity(
                "1", new Date(), "history1", "recipient@example.com", 1, false, "depHistory1"
        );

        doThrow(new RuntimeException("Database failure")).when(emailQueueRepository).update(entity);

        SystemException exception = assertThrows(SystemException.class, () -> emailQueueService.update(entity));
        assertEquals("Failed to update EmailQueueEntity", exception.getMessage());

        verify(emailQueueRepository, times(1)).update(entity);
    }

    @Test
    void testRemove_Success() throws RemoveException {
        EmailQueueEntity entity = new EmailQueueEntity(
                "1", new Date(), "history1", "recipient@example.com", 1, false, "depHistory1"
        );

        doNothing().when(emailQueueRepository).delete(entity);

        emailQueueService.delete(entity);

        verify(emailQueueRepository, times(1)).delete(entity);
    }

    @Test
    void testRemove_Failure() {
        EmailQueueEntity entity = new EmailQueueEntity(
                "1", new Date(), "history1", "recipient@example.com", 1, false, "depHistory1"
        );

        doThrow(new RuntimeException("Database failure")).when(emailQueueRepository).delete(entity);

        RemoveException exception = assertThrows(RemoveException.class, () -> emailQueueService.delete(entity));
        assertEquals("Failed to remove EmailQueueEntity", exception.getMessage());

        verify(emailQueueRepository, times(1)).delete(entity);
    }

    @Test
    void testFindAll_Success() throws FinderException {
        List<EmailQueueEntity> entities = List.of(
                new EmailQueueEntity("1", new Date(), "history1", "recipient1@example.com", 1, false, "depHistory1"),
                new EmailQueueEntity("2", new Date(), "history2", "recipient2@example.com", 2, true, "depHistory2")
        );

        when(emailQueueRepository.findAll()).thenReturn(entities);

        List<EmailQueueEntity> result = emailQueueService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(emailQueueRepository, times(1)).findAll();
    }

    @Test
    void testFindAll_WithLimit_Success() throws FinderException {
        List<EmailQueueEntity> entities = List.of(
                new EmailQueueEntity("1", new Date(), "history1", "recipient1@example.com", 1, false, "depHistory1")
        );

        when(emailQueueRepository.findAll(1,1)).thenReturn(entities);

        List<EmailQueueEntity> result = emailQueueService.findAll(1, 1);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(emailQueueRepository, times(1)).findAll(1,1);
    }

    @Test
    void testFindFirstMail_Success() throws FinderException {
        EmailQueueEntity entity = new EmailQueueEntity("1", new Date(), "history1", "recipient@example.com", 1, false, "depHistory1");
        when(emailQueueRepository.findFirstMail(1)).thenReturn(entity);

        EmailQueueEntity result = emailQueueService.findFirstMail();

        assertNotNull(result);
        assertEquals("1", result.getId());
        verify(emailQueueRepository, times(1)).findFirstMail(1);
    }

    @Test
    void testFindByHistory_Success() throws FinderException {
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
    void testFindByDependentHistory_Success() throws FinderException {
        List<EmailQueueEntity> entities = List.of(
                new EmailQueueEntity("1", new Date(), "history1", "recipient@example.com", 1, false, "depHistory1")
        );

        when(emailQueueRepository.findByDependentHistory("depHistory1")).thenReturn(entities);

        List<EmailQueueEntity> result = emailQueueService.findByDependentHistory("depHistory1");

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(emailQueueRepository, times(1)).findByDependentHistory("depHistory1");
    }

    @Test
    void testDelete_NullEntity() {
        RemoveException exception = assertThrows(RemoveException.class, () -> emailQueueService.delete(null));
        assertEquals("Cannot delete null as EmailQueueEntity", exception.getMessage());
    }
}
