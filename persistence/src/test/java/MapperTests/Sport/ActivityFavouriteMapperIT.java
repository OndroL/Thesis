package MapperTests.Sport;

import RepositoryTests.DatabaseCleaner;
import cz.inspire.sport.dto.ActivityFavouriteDto;
import cz.inspire.sport.entity.ActivityFavouriteEntity;
import cz.inspire.sport.mapper.ActivityFavouriteMapper;
import cz.inspire.sport.service.ActivityFavouriteService;
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
public class ActivityFavouriteMapperIT {

    @Inject
    ActivityFavouriteMapper activityFavouriteMapper;

    @Inject
    ActivityFavouriteService activityFavouriteService;

    @Inject
    DatabaseCleaner databaseCleaner;

    private String favId;

    @BeforeAll
    @Transactional
    public void setup() {
        databaseCleaner.clearTable(ActivityFavouriteEntity.class, true);
    }

    @Order(1)
    @Test
    @Transactional
    public void testPersistActivityFavourite() throws CreateException {
        ActivityFavouriteDto dto = DtoFactory.createActivityFavouriteDto();
        ActivityFavouriteEntity entity = activityFavouriteMapper.toEntity(dto);
        activityFavouriteService.create(entity);
        favId = entity.getId();
        assertNotNull(favId);
        assertEquals(dto.getZakaznikId(), entity.getZakaznikId());
        assertEquals(dto.getActivityId(), entity.getActivityId());
        assertEquals(dto.getPocet(), entity.getPocet());
    }

    @Order(2)
    @Test
    @Transactional
    public void testRetrieveActivityFavourite() throws FinderException {
        ActivityFavouriteEntity entity = activityFavouriteService.findByPrimaryKey(favId);
        assertNotNull(entity);
        ActivityFavouriteDto dto = activityFavouriteMapper.toDto(entity);
        assertEquals(favId, dto.getId());
        assertEquals(entity.getZakaznikId(), dto.getZakaznikId());
        assertEquals(entity.getActivityId(), dto.getActivityId());
        assertEquals(entity.getPocet(), dto.getPocet());
    }
}
