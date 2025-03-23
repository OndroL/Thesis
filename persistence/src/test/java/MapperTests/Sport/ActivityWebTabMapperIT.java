package MapperTests.Sport;

import RepositoryTests.DatabaseCleaner;
import cz.inspire.sport.dto.ActivityWebTabDto;
import cz.inspire.sport.entity.ActivityWebTabEntity;
import cz.inspire.sport.mapper.ActivityWebTabMapper;
import cz.inspire.sport.service.ActivityWebTabService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ActivityWebTabMapperIT {

    @Inject
    ActivityWebTabMapper activityWebTabMapper;

    @Inject
    ActivityWebTabService activityWebTabService;

    @Inject
    DatabaseCleaner databaseCleaner;

    private String webTabId;

    @BeforeAll
    @Transactional
    public void setup() {
        databaseCleaner.clearTable(ActivityWebTabEntity.class, true);
    }

    @Order(1)
    @Test
    @Transactional
    public void testPersistActivityWebTab() throws CreateException {
        ActivityWebTabDto dto = DtoFactory.createActivityWebTabDto();
        ActivityWebTabEntity entity = activityWebTabMapper.toEntity(dto);
        activityWebTabService.create(entity);
        webTabId = entity.getId();
        assertNotNull(webTabId);
        assertEquals(dto.getSportId(), entity.getSportId());
        assertEquals(dto.getActivityId(), entity.getActivityId());
        assertEquals(dto.getObjectId(), entity.getObjectId());
        assertEquals(dto.getTabIndex(), entity.getTabIndex());
    }

    @Order(2)
    @Test
    @Transactional
    public void testRetrieveActivityWebTab() throws FinderException {
        ActivityWebTabEntity entity = activityWebTabService.findByPrimaryKey(webTabId);
        assertNotNull(entity);
        ActivityWebTabDto dto = activityWebTabMapper.toDto(entity);
        assertEquals(webTabId, dto.getId());
        assertEquals(entity.getSportId(), dto.getSportId());
        assertEquals(entity.getActivityId(), dto.getActivityId());
        assertEquals(entity.getObjectId(), dto.getObjectId());
        assertEquals(entity.getTabIndex(), dto.getTabIndex());
    }
}
