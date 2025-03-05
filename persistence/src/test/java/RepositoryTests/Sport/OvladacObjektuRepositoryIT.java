package RepositoryTests.Sport;

import RepositoryTests.DatabaseCleaner;
import cz.inspire.sport.entity.OvladacObjektuEntity;
import cz.inspire.sport.repository.OvladacObjektuRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@Transactional
@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OvladacObjektuRepositoryIT {

    @Inject
    OvladacObjektuRepository ovladacObjektuRepository;

    @Inject
    DatabaseCleaner databaseCleaner;

    @BeforeAll
    @ActivateRequestContext
    public void clearDatabase() {
        databaseCleaner.clearTable(OvladacObjektuEntity.class, true);
    }

    private OvladacObjektuEntity createOvladacObjektu(String id, String idOvladace, String objektId) {
        return new OvladacObjektuEntity(id, idOvladace, List.of(1, 2), true, false, 10, 5, objektId);
    }

    @Order(1)
    @Test
    void testSaveAndFindById() {
        // Pass null for the ID so that it is generated automatically.
        OvladacObjektuEntity entity = createOvladacObjektu(null, "OVL-001", "OBJ-001");
        entity = ovladacObjektuRepository.create(entity);

        OvladacObjektuEntity result = ovladacObjektuRepository.findById(entity.getId());
        assertNotNull(result, "Entity should be found.");
        assertEquals("OVL-001", result.getIdOvladace(), "idOvladace should match.");
    }

    @Order(2)
    @Test
    void testFindWithOvladacObjektu() {
        OvladacObjektuEntity entity1 = createOvladacObjektu(null, "OVL-002", "OBJ-002");
        OvladacObjektuEntity entity2 = createOvladacObjektu(null, "OVL-002", "OBJ-003");
        entity1 = ovladacObjektuRepository.create(entity1);
        entity2 = ovladacObjektuRepository.create(entity2);

        List<OvladacObjektuEntity> result = ovladacObjektuRepository.findWithOvladacObjektu("OVL-002");
        assertNotNull(result, "Result should not be null.");
        assertEquals(2, result.size(), "Expected 2 results.");
    }

    @Order(3)
    @Test
    void testFindByObjekt() {
        OvladacObjektuEntity entity = createOvladacObjektu(null, "OVL-003", "OBJ-004");
        entity = ovladacObjektuRepository.create(entity);

        List<OvladacObjektuEntity> result = ovladacObjektuRepository.findByObjekt("OBJ-004");
        assertNotNull(result, "Result should not be null.");
        assertEquals(1, result.size(), "Expected 1 result.");
        assertEquals(entity.getId(), result.get(0).getId(), "IDs should match.");
    }

    @Order(4)
    @Test
    void testDeleteEntity() {
        OvladacObjektuEntity entity = createOvladacObjektu(null, "OVL-004", "OBJ-005");
        entity = ovladacObjektuRepository.create(entity);

        OvladacObjektuEntity result = ovladacObjektuRepository.findById(entity.getId());
        assertNotNull(result, "Entity should exist before deletion.");

        ovladacObjektuRepository.deleteById(entity.getId());

        result = ovladacObjektuRepository.findById(entity.getId());
        assertNull(result, "Entity should be deleted.");
    }
}
