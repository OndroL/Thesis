package ServiceTests.SMSHistory;

import cz.inspire.exception.SystemException;
import cz.inspire.sms.entity.SMSHistoryEntity;
import cz.inspire.sms.repository.SMSHistoryRepository;
import cz.inspire.sms.service.SMSHistoryService;
import jakarta.ejb.CreateException;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SMSHistoryServiceTest {

    @Mock
    private SMSHistoryRepository smsHistoryRepository;

    @Mock
    private Logger logger;

    @InjectMocks
    private SMSHistoryService smsHistoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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

        verify(smsHistoryRepository, times(1)).save(entity);
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
        doThrow(RuntimeException.class).when(smsHistoryRepository).save(entity);

        assertThrows(CreateException.class, () -> smsHistoryService.create(entity));
        verify(logger, times(1)).error(eq("Failed to create SMSHistoryEntity"), any(RuntimeException.class));
    }

    @Test
    void testFindByDate_Success() {
        Date from = new Date();
        Date to = new Date();
        List<SMSHistoryEntity> entities = Arrays.asList(
                new SMSHistoryEntity("1", from, "Message1", null, null, null, true),
                new SMSHistoryEntity("2", to, "Message2", null, null, null, false)
        );

        when(smsHistoryRepository.findByDate(from, to)).thenReturn(entities);

        List<SMSHistoryEntity> result = smsHistoryService.findByDate(from, to);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(smsHistoryRepository, times(1)).findByDate(from, to);
    }

    @Test
    void testFindByDateAutomatic_Success() {
        Date from = new Date();
        Date to = new Date();
        boolean automatic = true;
        List<SMSHistoryEntity> entities = List.of(
                new SMSHistoryEntity("1", from, "Message1", null, null, null, true)
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

        verify(smsHistoryRepository, times(1)).save(entity);
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
        doThrow(RuntimeException.class).when(smsHistoryRepository).save(entity);

        SystemException exception = assertThrows(SystemException.class, () -> smsHistoryService.update(entity));
        assertEquals("Failed to update SMSHistoryEntity", exception.getMessage());
        verify(logger, times(1)).error(eq("Failed to update SMSHistoryEntity"), any(RuntimeException.class));
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

        smsHistoryService.remove(entity);

        verify(smsHistoryRepository, times(1)).remove(entity);
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
        doThrow(RuntimeException.class).when(smsHistoryRepository).remove(entity);

        SystemException exception = assertThrows(SystemException.class, () -> smsHistoryService.remove(entity));
        assertEquals("Failed to remove SMSHistoryEntity", exception.getMessage());
        verify(logger, times(1)).error(eq("Failed to remove SMSHistoryEntity"), any(RuntimeException.class));
    }
}
