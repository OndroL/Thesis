package ServiceTests.SMSHistory;

import cz.inspire.enterprise.exception.SystemException;
import cz.inspire.sms.entity.SMSHistoryEntity;
import cz.inspire.sms.repository.SMSHistoryRepository;
import cz.inspire.sms.service.SMSHistoryService;
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

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SMSHistoryServiceTest {

    @Mock
    private SMSHistoryRepository smsHistoryRepository;

    @Mock
    private EntityManager em;

    private SMSHistoryService smsHistoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        smsHistoryService = new SMSHistoryService(smsHistoryRepository);
        smsHistoryService.setEntityManager(em);
    }

    @Test
    void testCreate_Success() throws CreateException {
        SMSHistoryEntity entity = new SMSHistoryEntity(
                "1", new Date(), "Test Message",
                Arrays.asList("Group1", "Group2"),
                Arrays.asList("Recipient1", "Recipient2"),
                Arrays.asList("MoreRecipient1", "MoreRecipient2"),
                true
        );

        doNothing().when(em).persist(entity);
        doNothing().when(em).flush();

        smsHistoryService.create(entity);

        verify(em, times(1)).persist(entity);
        verify(em, times(1)).flush();
    }

    @Test
    void testCreate_Failure() {
        SMSHistoryEntity entity = new SMSHistoryEntity(
                "1", new Date(), "Test Message",
                Arrays.asList("Group1", "Group2"),
                Arrays.asList("Recipient1", "Recipient2"),
                Arrays.asList("MoreRecipient1", "MoreRecipient2"),
                true
        );

        doThrow(new RuntimeException("Database failure")).when(em).persist(entity);

        CreateException exception = assertThrows(CreateException.class, () -> smsHistoryService.create(entity));
        assertEquals("Failed to create SMSHistoryEntity", exception.getMessage());

        verify(em, times(1)).persist(entity);
    }

    @Test
    void testUpdate_Success() throws SystemException {
        SMSHistoryEntity entity = new SMSHistoryEntity(
                "1", new Date(), "Test Message",
                Arrays.asList("Group1", "Group2"),
                Arrays.asList("Recipient1", "Recipient2"),
                Arrays.asList("MoreRecipient1", "MoreRecipient2"),
                true
        );

        when(em.merge(entity)).thenReturn(entity);
        doNothing().when(em).flush();

        smsHistoryService.update(entity);

        verify(em, times(1)).merge(entity);
        verify(em, times(1)).flush();
    }

    @Test
    void testUpdate_Failure() {
        SMSHistoryEntity entity = new SMSHistoryEntity(
                "1", new Date(), "Test Message",
                Arrays.asList("Group1", "Group2"),
                Arrays.asList("Recipient1", "Recipient2"),
                Arrays.asList("MoreRecipient1", "MoreRecipient2"),
                true
        );

        doThrow(new RuntimeException("Database failure")).when(em).merge(entity);

        SystemException exception = assertThrows(SystemException.class, () -> smsHistoryService.update(entity));
        assertEquals("Failed to update SMSHistoryEntity", exception.getMessage());

        verify(em, times(1)).merge(entity);
    }

    @Test
    void testRemove_Success() throws RemoveException {
        SMSHistoryEntity entity = new SMSHistoryEntity(
                "1", new Date(), "Test Message",
                Arrays.asList("Group1", "Group2"),
                Arrays.asList("Recipient1", "Recipient2"),
                Arrays.asList("MoreRecipient1", "MoreRecipient2"),
                true
        );

        when(em.merge(entity)).thenReturn(entity);
        doNothing().when(em).remove(entity);
        doNothing().when(em).flush();

        smsHistoryService.delete(entity);

        verify(em, times(1)).merge(entity);
        verify(em, times(1)).remove(entity);
        verify(em, times(1)).flush();
    }

    @Test
    void testRemove_Failure() {
        SMSHistoryEntity entity = new SMSHistoryEntity(
                "1", new Date(), "Test Message",
                Arrays.asList("Group1", "Group2"),
                Arrays.asList("Recipient1", "Recipient2"),
                Arrays.asList("MoreRecipient1", "MoreRecipient2"),
                true
        );

        when(em.merge(entity)).thenReturn(entity);
        doThrow(new RuntimeException("Database failure")).when(em).remove(entity);

        RemoveException exception = assertThrows(RemoveException.class, () -> smsHistoryService.delete(entity));
        assertEquals("Failed to remove SMSHistoryEntity", exception.getMessage());

        verify(em, times(1)).merge(entity);
        verify(em, times(1)).remove(entity);
    }

    @Test
    void testFindByDate_Success() throws FinderException {
        Timestamp from = new Timestamp(System.currentTimeMillis());
        Timestamp to = new Timestamp(System.currentTimeMillis() + 10000);

        List<SMSHistoryEntity> entities = Arrays.asList(
                new SMSHistoryEntity("1", new Date(from.getTime()), "Message1", null, null, null, true),
                new SMSHistoryEntity("2", new Date(to.getTime()), "Message2", null, null, null, false)
        );

        when(smsHistoryRepository.findByDate(from, to)).thenReturn(entities);

        List<SMSHistoryEntity> result = smsHistoryService.findByDate(from, to);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(smsHistoryRepository, times(1)).findByDate(from, to);
    }

    @Test
    void testFindByDateAutomatic_Success() throws FinderException {
        Timestamp from = new Timestamp(System.currentTimeMillis());
        Timestamp to = new Timestamp(System.currentTimeMillis() + 10000);
        boolean automatic = true;

        List<SMSHistoryEntity> entities = List.of(
                new SMSHistoryEntity("1", new Date(from.getTime()), "Message1", null, null, null, true)
        );

        when(smsHistoryRepository.findByDateAutomatic(from, to, automatic)).thenReturn(entities);

        List<SMSHistoryEntity> result = smsHistoryService.findByDateAutomatic(from, to, automatic);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(smsHistoryRepository, times(1)).findByDateAutomatic(from, to, automatic);
    }

    @Test
    void testDelete_NullEntity() {
        RemoveException exception = assertThrows(RemoveException.class, () -> smsHistoryService.delete(null));
        assertEquals("Cannot delete null entity in SMSHistoryEntity", exception.getMessage());
    }
}
