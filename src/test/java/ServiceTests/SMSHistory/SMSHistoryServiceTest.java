package ServiceTests.SMSHistory;

import cz.inspire.enterprise.exception.SystemException;
import cz.inspire.sms.entity.SMSHistoryEntity;
import cz.inspire.sms.repository.SMSHistoryRepository;
import cz.inspire.sms.service.SMSHistoryService;
import jakarta.ejb.CreateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
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

    @Spy
    private SMSHistoryService smsHistoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        smsHistoryService = spy(new SMSHistoryService(smsHistoryRepository));
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

        smsHistoryService.create(entity);

        verify(smsHistoryService, times(1)).create(entity);
        verify(smsHistoryRepository, times(1)).save(entity);
    }

    @Test
    void testCreate_Failure() throws CreateException {
        SMSHistoryEntity entity = new SMSHistoryEntity(
                "1", new Date(), "Test Message",
                Arrays.asList("Group1", "Group2"),
                Arrays.asList("Recipient1", "Recipient2"),
                Arrays.asList("MoreRecipient1", "MoreRecipient2"),
                true
        );
        doThrow(new RuntimeException("Database failure")).when(smsHistoryRepository).save(entity);

        assertThrows(CreateException.class, () -> smsHistoryService.create(entity));

        verify(smsHistoryService, times(1)).create(entity);
    }

    @Test
    void testFindByDate_Success() {
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
    void testFindByDateAutomatic_Success() {
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
    void testUpdate_Success() throws SystemException {
        SMSHistoryEntity entity = new SMSHistoryEntity(
                "1", new Date(), "Test Message",
                Arrays.asList("Group1", "Group2"),
                Arrays.asList("Recipient1", "Recipient2"),
                Arrays.asList("MoreRecipient1", "MoreRecipient2"),
                true
        );

        smsHistoryService.update(entity);

        verify(smsHistoryService, times(1)).update(entity);
        verify(smsHistoryRepository, times(1)).save(entity);
    }

    @Test
    void testUpdate_Failure() throws SystemException {
        SMSHistoryEntity entity = new SMSHistoryEntity(
                "1", new Date(), "Test Message",
                Arrays.asList("Group1", "Group2"),
                Arrays.asList("Recipient1", "Recipient2"),
                Arrays.asList("MoreRecipient1", "MoreRecipient2"),
                true
        );
        doThrow(new RuntimeException("Database failure")).when(smsHistoryRepository).save(entity);

        assertThrows(SystemException.class, () -> smsHistoryService.update(entity));

        verify(smsHistoryService, times(1)).update(entity);
    }

    @Test
    void testRemove_Success() throws SystemException {
        SMSHistoryEntity entity = new SMSHistoryEntity(
                "1", new Date(), "Test Message",
                Arrays.asList("Group1", "Group2"),
                Arrays.asList("Recipient1", "Recipient2"),
                Arrays.asList("MoreRecipient1", "MoreRecipient2"),
                true
        );

        smsHistoryService.delete(entity);

        verify(smsHistoryService, times(1)).delete(entity);
        verify(smsHistoryRepository, times(1)).delete(entity);
    }

    @Test
    void testRemove_Failure() throws SystemException {
        SMSHistoryEntity entity = new SMSHistoryEntity(
                "1", new Date(), "Test Message",
                Arrays.asList("Group1", "Group2"),
                Arrays.asList("Recipient1", "Recipient2"),
                Arrays.asList("MoreRecipient1", "MoreRecipient2"),
                true
        );
        doThrow(new RuntimeException("Database failure")).when(smsHistoryRepository).delete(entity);

        assertThrows(SystemException.class, () -> smsHistoryService.delete(entity));

        verify(smsHistoryService, times(1)).delete(entity);
    }
}
