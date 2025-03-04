package ServiceTests.Email;

import cz.inspire.email.entity.EmailQueueEntity;
import cz.inspire.email.repository.EmailQueueRepository;
import cz.inspire.email.service.EmailQueueService;
import cz.inspire.exception.SystemException;
import jakarta.ejb.CreateException;
import jakarta.ejb.FinderException;
import jakarta.ejb.RemoveException;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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

    @Mock
    private EntityManager em;

    private EmailQueueService emailQueueService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        emailQueueService = new EmailQueueService(emailQueueRepository);
        emailQueueService.setEntityManager(em);
    }

    @Test
    void testCreate_Success() throws CreateException {
        EmailQueueEntity entity = new EmailQueueEntity(
                "1", new Date(), "history1", "recipient@example.com", 1, false, "depHistory1"
        );

        doNothing().when(em).persist(entity);
        doNothing().when(em).flush();

        emailQueueService.create(entity);

        verify(em, times(1)).persist(entity);
        verify(em, times(1)).flush();
    }

    @Test
    void testCreate_Failure() {
        EmailQueueEntity entity = new EmailQueueEntity(
                "1", new Date(), "history1", "recipient@example.com", 1, false, "depHistory1"
        );

        doThrow(new RuntimeException("Database failure")).when(em).persist(entity);

        CreateException exception = assertThrows(CreateException.class, () -> emailQueueService.create(entity));
        assertEquals("Failed to create EmailQueueEntity", exception.getMessage());

        verify(em, times(1)).persist(entity);
    }

    @Test
    void testUpdate_Success() throws SystemException {
        EmailQueueEntity entity = new EmailQueueEntity(
                "1", new Date(), "history1", "recipient@example.com", 1, false, "depHistory1"
        );

        when(em.merge(entity)).thenReturn(entity);
        doNothing().when(em).flush();

        emailQueueService.update(entity);

        verify(em, times(1)).merge(entity);
        verify(em, times(1)).flush();
    }

    @Test
    void testUpdate_Failure() {
        EmailQueueEntity entity = new EmailQueueEntity(
                "1", new Date(), "history1", "recipient@example.com", 1, false, "depHistory1"
        );

        doThrow(new RuntimeException("Database failure")).when(em).merge(entity);

        SystemException exception = assertThrows(SystemException.class, () -> emailQueueService.update(entity));
        assertEquals("Failed to update EmailQueueEntity", exception.getMessage());

        verify(em, times(1)).merge(entity);
    }

    @Test
    void testRemove_Success() throws RemoveException {
        EmailQueueEntity entity = new EmailQueueEntity(
                "1", new Date(), "history1", "recipient@example.com", 1, false, "depHistory1"
        );

        when(em.merge(entity)).thenReturn(entity);
        doNothing().when(em).remove(entity);
        doNothing().when(em).flush();

        emailQueueService.delete(entity);

        verify(em, times(1)).merge(entity);
        verify(em, times(1)).remove(entity);
        verify(em, times(1)).flush();
    }

    @Test
    void testRemove_Failure() {
        EmailQueueEntity entity = new EmailQueueEntity(
                "1", new Date(), "history1", "recipient@example.com", 1, false, "depHistory1"
        );

        when(em.merge(entity)).thenReturn(entity);
        doThrow(new RuntimeException("Database failure")).when(em).remove(entity);

        RemoveException exception = assertThrows(RemoveException.class, () -> emailQueueService.delete(entity));
        assertEquals("Failed to remove EmailQueueEntity", exception.getMessage());

        verify(em, times(1)).merge(entity);
        verify(em, times(1)).remove(entity);
    }

    @Test
    void testFindAll_Success() throws FinderException {
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
    void testFindAll_WithLimit_Success() throws FinderException {
        List<EmailQueueEntity> entities = List.of(
                new EmailQueueEntity("1", new Date(), "history1", "recipient1@example.com", 1, false, "depHistory1")
        );

        when(emailQueueRepository.findAll(1,1)).thenReturn(entities);

        List<EmailQueueEntity> result = emailQueueService.findAll(0, 1);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(emailQueueRepository, times(1)).findAll(1,1);
    }

    @Test
    void testFindFirstMail_Success() throws FinderException {
        EmailQueueEntity entity = new EmailQueueEntity("1", new Date(), "history1", "recipient@example.com", 1, false, "depHistory1");
        when(emailQueueRepository.findFirstMail(1)).thenReturn(Optional.of(entity));

        Optional<EmailQueueEntity> result = emailQueueService.findFirstMail();

        assertTrue(result.isPresent());
        assertEquals("1", result.get().getId());
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
        assertEquals("Cannot delete null entity in EmailQueueEntity", exception.getMessage());
    }
}
