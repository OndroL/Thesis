package RepositoryTests.Sport;

import RepositoryTests.DatabaseCleaner;
import cz.inspire.sport.entity.OvladacObjektuEntity;
import cz.inspire.sport.repository.OvladacObjektuRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OvladacObjektuRepositoryIT {

    @Inject
    OvladacObjektuRepository ovladacObjektuRepository;

    @Inject
    DatabaseCleaner databaseCleaner;

    @Inject
    EntityManager em;

    @BeforeAll
    @ActivateRequestContext
    public void clearDatabase() {
        databaseCleaner.clearTable(OvladacObjektuEntity.class, true);
    }

    private OvladacObjektuEntity createOvladacObjektu(String id, String idOvladace, String objektId) {
        return new OvladacObjektuEntity(id, idOvladace, List.of(1, 2), true, false, 10, 5, objektId);
    }

    @Test
    @Order(1)
    void testSaveAndFindById() {
        OvladacObjektuEntity entity = createOvladacObjektu(null, "OVL-001", "OBJ-001");
        em.persist(entity);
        em.flush();

        Optional<OvladacObjektuEntity> result = ovladacObjektuRepository.findById(entity.getId());

        assertTrue(result.isPresent());
        assertEquals("OVL-001", result.get().getIdOvladace());
    }

    @Test
    @Order(2)
    void testFindWithOvladacObjektu() {
        OvladacObjektuEntity entity1 = createOvladacObjektu(null, "OVL-002", "OBJ-002");
        OvladacObjektuEntity entity2 = createOvladacObjektu(null, "OVL-002", "OBJ-003");
        em.persist(entity1);
        em.persist(entity2);
        em.flush();

        List<OvladacObjektuEntity> result = ovladacObjektuRepository.findWithOvladacObjektu("OVL-002");

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    @Order(3)
    void testFindByObjekt() {
        OvladacObjektuEntity entity = createOvladacObjektu(null, "OVL-003", "OBJ-004");
        em.persist(entity);
        em.flush();

        List<OvladacObjektuEntity> result = ovladacObjektuRepository.findByObjekt("OBJ-004");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(entity.getId(), result.getFirst().getId());
    }

    @Test
    @Order(4)
    void testDeleteEntity() {
        OvladacObjektuEntity entity = createOvladacObjektu(null, "OVL-004", "OBJ-005");
        em.persist(entity);
        em.flush();

        Optional<OvladacObjektuEntity> result = ovladacObjektuRepository.findById(entity.getId());
        assertTrue(result.isPresent());

        em.remove(entity);
        em.flush();

        result = ovladacObjektuRepository.findById(entity.getId());
        assertFalse(result.isPresent());
    }
}
