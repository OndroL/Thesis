package MapperTests.Email;

import RepositoryTests.DatabaseCleaner;
import cz.inspire.email.dto.EmailQueueDto;
import cz.inspire.email.entity.EmailQueueEntity;
import cz.inspire.email.mapper.EmailQueueMapper;
import cz.inspire.email.service.EmailQueueService;
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

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EmailQueueMapperIT {

    @Inject
    EmailQueueMapper emailQueueMapper;

    @Inject
    EmailQueueService emailQueueService;

    @Inject
    DatabaseCleaner databaseCleaner;

    private String queueId;

    @BeforeAll
    @Transactional
    public void setup() {
        databaseCleaner.clearTable(EmailQueueEntity.class, true);
    }

    @Order(1)
    @Test
    @Transactional
    public void testPersistEmailQueue() throws CreateException {
        EmailQueueDto dto = new EmailQueueDto();
        dto.setCreated(new Date());
        dto.setEmailHistory("Some email history");
        dto.setRecipient("test@example.com");
        dto.setPriority(5);
        dto.setRemoveEmailHistory(true);
        dto.setDependentEmailHistory("dep-123");

        EmailQueueEntity entity = emailQueueMapper.toEntity(dto);
        emailQueueService.create(entity);
        queueId = entity.getId();

        assertNotNull(queueId);
        assertNotNull(entity.getCreated());
        assertEquals("test@example.com", entity.getRecipient());
        assertTrue(entity.isRemoveEmailHistory());
    }

    @Order(2)
    @Test
    @Transactional
    public void testRetrieveEmailQueue() throws FinderException {
        EmailQueueEntity fromDb = emailQueueService.findByPrimaryKey(queueId);
        assertNotNull(fromDb);

        EmailQueueDto mappedDto = emailQueueMapper.toDto(fromDb);
        assertEquals(queueId, mappedDto.getId());
        assertEquals("test@example.com", mappedDto.getRecipient());
        assertEquals("Some email history", mappedDto.getEmailHistory());
        assertTrue(mappedDto.isRemoveEmailHistory());
    }
}
