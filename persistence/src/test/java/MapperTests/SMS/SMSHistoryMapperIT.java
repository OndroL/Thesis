package MapperTests.SMS;

import RepositoryTests.DatabaseCleaner;
import cz.inspire.sms.dto.SMSHistoryDto;
import cz.inspire.sms.entity.SMSHistoryEntity;
import cz.inspire.sms.mapper.SMSHistoryMapper;
import cz.inspire.sms.service.SMSHistoryService;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ejb.CreateException;
import jakarta.ejb.FinderException;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.Arrays;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SMSHistoryMapperIT {

    @Inject
    SMSHistoryMapper smsHistoryMapper;

    @Inject
    SMSHistoryService smsHistoryService;

    @Inject
    DatabaseCleaner databaseCleaner;

    private String smsId;

    @BeforeAll
    @Transactional
    public void setup() {
        databaseCleaner.clearTable(SMSHistoryEntity.class, true);
    }

    @Order(1)
    @Test
    @Transactional
    public void testPersistSMSHistory() throws CreateException {
        SMSHistoryDto dto = new SMSHistoryDto();
        dto.setDate(new Date());
        dto.setMessage("Test message");
        dto.setGroups(Arrays.asList("group1", "group2"));
        dto.setRecipients(Arrays.asList("recipient1", "recipient2"));
        dto.setMoreRecipients(Arrays.asList("more1", "more2"));
        dto.setAutomatic(true);

        SMSHistoryEntity entity = smsHistoryMapper.toEntity(dto);
        smsHistoryService.create(entity);
        smsId = entity.getId();

        assertNotNull(smsId);
        assertEquals("Test message", entity.getMessage());
        assertTrue(entity.getAutomatic());
        assertEquals(2, entity.getGroups().size());
    }

    @Order(2)
    @Test
    @Transactional
    public void testRetrieveSMSHistory() throws FinderException {
        SMSHistoryEntity entity = smsHistoryService.findByPrimaryKey(smsId);
        assertNotNull(entity);

        SMSHistoryDto dto = smsHistoryMapper.toDto(entity);
        assertEquals(smsId, dto.getId());
        assertEquals("Test message", dto.getMessage());
        assertTrue(dto.getAutomatic());
        assertEquals(2, dto.getRecipients().size());
    }
}
