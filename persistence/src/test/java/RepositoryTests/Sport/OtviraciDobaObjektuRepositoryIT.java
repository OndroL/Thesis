package RepositoryTests.Sport;

import RepositoryTests.DatabaseCleaner;
import cz.inspire.sport.entity.OtviraciDobaObjektuEntity;
import cz.inspire.sport.entity.OtviraciDobaObjektuPK;
import cz.inspire.sport.repository.OtviraciDobaObjektuRepository;
import cz.inspire.sport.utils.OtviraciDoba;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OtviraciDobaObjektuRepositoryIT {

    @Inject
    OtviraciDobaObjektuRepository otviraciDobaObjektuRepository;

    @Inject
    DatabaseCleaner databaseCleaner;

    @BeforeAll
    @ActivateRequestContext
    public void clearDatabase() {
        databaseCleaner.clearTable(OtviraciDobaObjektuEntity.class, true);
    }

    private OtviraciDobaObjektuEntity createOtviraciDobaObjektu(String objektId, LocalDateTime platnostOd, OtviraciDoba otviraciDoba) {
        return new OtviraciDobaObjektuEntity(new OtviraciDobaObjektuPK(objektId, platnostOd), otviraciDoba);
    }

    @Order(1)
    @Test
    void testFindByObjekt() {
        LocalDateTime now = LocalDateTime.now();
        // Supply composite key parts (not null) as these are required
        OtviraciDobaObjektuEntity entity = createOtviraciDobaObjektu("OBJ-001", now, null);
        entity = otviraciDobaObjektuRepository.create(entity);

        List<OtviraciDobaObjektuEntity> result = otviraciDobaObjektuRepository.findByObjekt("OBJ-001");
        assertNotNull(result, "Result should not be null");
        assertEquals(1, result.size(), "Expected one entity");
    }

    @Order(2)
    @Test
    void testFindByObjektWithLimit() {
        LocalDateTime now = LocalDateTime.now();
        OtviraciDobaObjektuEntity entity1 = createOtviraciDobaObjektu("OBJ-002", now.minusDays(1), null);
        OtviraciDobaObjektuEntity entity2 = createOtviraciDobaObjektu("OBJ-002", now, null);

        entity1 = otviraciDobaObjektuRepository.create(entity1);
        entity2 = otviraciDobaObjektuRepository.create(entity2);

        List<OtviraciDobaObjektuEntity> result = otviraciDobaObjektuRepository.findByObjekt("OBJ-002", 1, 0);
        assertNotNull(result, "Result should not be null");
        assertEquals(1, result.size(), "Expected only one result due to limit");
    }

    @Order(3)
    @Test
    void testFindCurrent() {
        LocalDateTime now = LocalDateTime.now();
        OtviraciDobaObjektuEntity entity = createOtviraciDobaObjektu("OBJ-003", now.minusDays(1), null);
        entity = otviraciDobaObjektuRepository.create(entity);

        // Assume repository.findCurrent now returns the entity directly (or null)
        OtviraciDobaObjektuEntity result = otviraciDobaObjektuRepository.findCurrent("OBJ-003", now, 1);
        assertNotNull(result, "Expected to find current entity");
        assertEquals("OBJ-003", result.getEmbeddedId().getObjektId(), "Objekt ID should match");
    }

    @Order(4)
    @Test
    void testFindAfter() {
        LocalDateTime now = LocalDateTime.now();
        OtviraciDobaObjektuEntity entity = createOtviraciDobaObjektu("OBJ-004", now.plusDays(1), null);
        entity = otviraciDobaObjektuRepository.create(entity);

        List<OtviraciDobaObjektuEntity> result = otviraciDobaObjektuRepository.findAfter("OBJ-004", now);
        assertNotNull(result, "Result should not be null");
        assertEquals(1, result.size(), "Expected one entity");
    }

    @Order(5)
    @Test
    void testGetCurrentIdsByObjectAndDay() {
        LocalDateTime now = LocalDateTime.now();
        OtviraciDobaObjektuEntity entity = createOtviraciDobaObjektu("OBJ-005", now.minusDays(2), null);
        entity = otviraciDobaObjektuRepository.create(entity);

        List<LocalDateTime> result = otviraciDobaObjektuRepository.getCurrentIdsByObjectAndDay("OBJ-005", now);
        assertNotNull(result, "Result should not be null");
        assertEquals(1, result.size(), "Expected one ID");
    }

    @Order(6)
    @Test
    void testFindById() {
        LocalDateTime now = LocalDateTime.now();
        // Create composite key manually
        OtviraciDobaObjektuEntity entity = createOtviraciDobaObjektu("OBJ-006", now, null);
        entity = otviraciDobaObjektuRepository.create(entity);

        OtviraciDobaObjektuEntity result = otviraciDobaObjektuRepository.findByPrimaryKey(entity.getEmbeddedId());
        assertNotNull(result, "Entity should be found by composite key");
    }

    @Order(7)
    @Test
    void testDeleteById() {
        LocalDateTime now = LocalDateTime.now();
        OtviraciDobaObjektuEntity entity = createOtviraciDobaObjektu("OBJ-007", now, null);
        entity = otviraciDobaObjektuRepository.create(entity);

        OtviraciDobaObjektuEntity result = otviraciDobaObjektuRepository.findByPrimaryKey(entity.getEmbeddedId());
        assertNotNull(result, "Entity should exist before deletion");

        otviraciDobaObjektuRepository.deleteByPrimaryKey(entity.getEmbeddedId());

        result = otviraciDobaObjektuRepository.findByPrimaryKey(entity.getEmbeddedId());
        assertNull(result, "Entity should be deleted");
    }
}
