package FacadeTests.SMSHistory;

import cz.inspire.sms.dto.SMSHistoryDto;
import cz.inspire.sms.entity.SMSHistoryEntity;
import cz.inspire.sms.facade.SMSHistoryFacade;
import cz.inspire.sms.mapper.SMSHistoryMapper;
import cz.inspire.sms.service.SMSHistoryService;
import jakarta.ejb.CreateException;
import jakarta.ejb.FinderException;
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

public class SMSHistoryFacadeTest {

    @Mock
    private SMSHistoryService smsHistoryService;

    @Mock
    private SMSHistoryMapper smsHistoryMapper;

    @InjectMocks
    private SMSHistoryFacade smsHistoryFacade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate_Success() throws CreateException {
        // Input DTO
        SMSHistoryDto dto = new SMSHistoryDto(
                null, new Date(), "Test Message",
                Arrays.asList("Group1", "Group2"),
                Arrays.asList("Recipient1", "Recipient2"),
                Arrays.asList("MoreRecipient1", "MoreRecipient2"),
                true
        );

        // Mapped Entity
        SMSHistoryEntity entity = new SMSHistoryEntity(
                null, new Date(), "Test Message",
                Arrays.asList("Group1", "Group2"),
                Arrays.asList("Recipient1", "Recipient2"),
                Arrays.asList("MoreRecipient1", "MoreRecipient2"),
                true
        );

        // Entity with ID generated
        SMSHistoryEntity savedEntity = new SMSHistoryEntity(
                "generated-id", new Date(), "Test Message",
                Arrays.asList("Group1", "Group2"),
                Arrays.asList("Recipient1", "Recipient2"),
                Arrays.asList("MoreRecipient1", "MoreRecipient2"),
                true
        );

        when(smsHistoryMapper.toEntity(dto)).thenReturn(entity);
        doAnswer(invocation -> savedEntity).when(smsHistoryService).create(entity);

        // Test
        String result = smsHistoryFacade.create(dto);

        assertNotNull(result);
        verify(smsHistoryMapper, times(1)).toEntity(dto);
        verify(smsHistoryService, times(1)).create(entity);
    }

    @Test
    void testCreate_Failure() throws CreateException {
        SMSHistoryDto dto = new SMSHistoryDto(
                null, new Date(), "Test Message",
                Arrays.asList("Group1", "Group2"),
                Arrays.asList("Recipient1", "Recipient2"),
                Arrays.asList("MoreRecipient1", "MoreRecipient2"),
                true
        );

        SMSHistoryEntity entity = new SMSHistoryEntity(
                null, new Date(), "Test Message",
                Arrays.asList("Group1", "Group2"),
                Arrays.asList("Recipient1", "Recipient2"),
                Arrays.asList("MoreRecipient1", "MoreRecipient2"),
                true
        );

        when(smsHistoryMapper.toEntity(dto)).thenReturn(entity);
        doThrow(new RuntimeException("Simulated failure")).when(smsHistoryService).create(entity);

        assertThrows(CreateException.class, () -> smsHistoryFacade.create(dto));
        verify(smsHistoryMapper, times(1)).toEntity(dto);
        verify(smsHistoryService, times(1)).create(entity);
    }

    @Test
    void testMapToDto() {
        SMSHistoryEntity entity = new SMSHistoryEntity(
                "1", new Date(), "Test Message",
                Arrays.asList("Group1", "Group2"),
                Arrays.asList("Recipient1", "Recipient2"),
                Arrays.asList("MoreRecipient1", "MoreRecipient2"),
                true
        );

        SMSHistoryDto dto = new SMSHistoryDto(
                "1", new Date(), "Test Message",
                Arrays.asList("Group1", "Group2"),
                Arrays.asList("Recipient1", "Recipient2"),
                Arrays.asList("MoreRecipient1", "MoreRecipient2"),
                true
        );

        when(smsHistoryMapper.toDto(entity)).thenReturn(dto);

        SMSHistoryDto result = smsHistoryFacade.mapToDto(entity);

        assertNotNull(result);
        assertEquals(entity.getId(), result.getId());
        verify(smsHistoryMapper, times(1)).toDto(entity);
    }

    @Test
    void testFindByDate() throws FinderException {
        Date from = new Date();
        Date to = new Date();
        List<SMSHistoryEntity> entities = Arrays.asList(
                new SMSHistoryEntity("1", from, "Message1", null, null, null, true),
                new SMSHistoryEntity("2", to, "Message2", null, null, null, false)
        );

        when(smsHistoryService.findByDate(from, to)).thenReturn(entities);

        List<SMSHistoryEntity> result = smsHistoryFacade.findByDate(from, to);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(smsHistoryService, times(1)).findByDate(from, to);
    }

    @Test
    void testFindByDateAutomatic() throws FinderException {
        Date from = new Date();
        Date to = new Date();
        boolean automatic = true;
        List<SMSHistoryEntity> entities = List.of(
                new SMSHistoryEntity("1", from, "Message1", null, null, null, true)
        );

        when(smsHistoryService.findByDateAutomatic(from, to, automatic)).thenReturn(entities);

        List<SMSHistoryEntity> result = smsHistoryFacade.findByDateAutomatic(from, to, automatic);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(smsHistoryService, times(1)).findByDateAutomatic(from, to, automatic);
    }
}
