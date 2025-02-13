package RepositoryTests.Sport;

import RepositoryTests.DatabaseCleaner;
import cz.inspire.sport.entity.OtviraciDobaObjektuEntity;
import cz.inspire.sport.entity.OtviraciDobaObjektuPK;
import cz.inspire.sport.repository.OtviraciDobaObjektuRepository;
import cz.inspire.sport.utils.OtviraciDoba;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.data.Limit;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OtviraciDobaObjektuRepositoryIT {

    @Inject
    OtviraciDobaObjektuRepository otviraciDobaObjektuRepository;

    @Inject
    DatabaseCleaner databaseCleaner;

    @Inject
    EntityManager em;

    @BeforeAll
    @ActivateRequestContext
    public void clearDatabase() {
        databaseCleaner.clearTable(OtviraciDobaObjektuEntity.class, true);
    }

    private OtviraciDobaObjektuEntity createOtviraciDobaObjektu(String objektId, LocalDateTime platnostOd, OtviraciDoba otviraciDoba) {
        return new OtviraciDobaObjektuEntity(new OtviraciDobaObjektuPK(objektId, platnostOd), otviraciDoba);
    }

    @Test
    @Order(1)
    void testFindByObjekt() {
        LocalDateTime now = LocalDateTime.now();
        OtviraciDobaObjektuEntity entity = createOtviraciDobaObjektu("OBJ-001", now, null);
        em.persist(entity);
        em.flush();

        List<OtviraciDobaObjektuEntity> result = otviraciDobaObjektuRepository.findByObjekt("OBJ-001");

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @Order(2)
    void testFindByObjektWithLimit() {
        LocalDateTime now = LocalDateTime.now();
        OtviraciDobaObjektuEntity entity1 = createOtviraciDobaObjektu("OBJ-002", now.minusDays(1), null);
        OtviraciDobaObjektuEntity entity2 = createOtviraciDobaObjektu("OBJ-002", now, null);

        em.persist(entity1);
        em.persist(entity2);
        em.flush();

        List<OtviraciDobaObjektuEntity> result = otviraciDobaObjektuRepository.findByObjektWithLimit("OBJ-002", Limit.of(1));

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @Order(3)
    void testFindCurrent() {
        LocalDateTime now = LocalDateTime.now();
        OtviraciDobaObjektuEntity entity = createOtviraciDobaObjektu("OBJ-003", now.minusDays(1), null);
        em.persist(entity);
        em.flush();

        OtviraciDobaObjektuEntity result = otviraciDobaObjektuRepository.findCurrent("OBJ-003", now);

        assertNotNull(result);
        assertEquals("OBJ-003", result.getId().getObjektId());
    }

    @Test
    @Order(4)
    void testFindAfter() {
        LocalDateTime now = LocalDateTime.now();
        OtviraciDobaObjektuEntity entity = createOtviraciDobaObjektu("OBJ-004", now.plusDays(1), null);
        em.persist(entity);
        em.flush();

        List<OtviraciDobaObjektuEntity> result = otviraciDobaObjektuRepository.findAfter("OBJ-004", now);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @Order(5)
    void testGetCurrentIdsByObjectAndDay() {
        LocalDateTime now = LocalDateTime.now();
        OtviraciDobaObjektuEntity entity = createOtviraciDobaObjektu("OBJ-005", now.minusDays(2), null);
        em.persist(entity);
        em.flush();

        List<LocalDateTime> result = otviraciDobaObjektuRepository.getCurrentIdsByObjectAndDay("OBJ-005", now);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @Order(6)
    void testFindById() {
        LocalDateTime now = LocalDateTime.now();
        OtviraciDobaObjektuPK pk = new OtviraciDobaObjektuPK("OBJ-006", now);
        OtviraciDobaObjektuEntity entity = new OtviraciDobaObjektuEntity(pk, null);
        em.persist(entity);
        em.flush();

        Optional<OtviraciDobaObjektuEntity> result = otviraciDobaObjektuRepository.findById(pk);

        assertTrue(result.isPresent());
    }

    @Test
    @Order(7)
    void testDeleteById() {
        LocalDateTime now = LocalDateTime.now();
        OtviraciDobaObjektuPK pk = new OtviraciDobaObjektuPK("OBJ-007", now);
        OtviraciDobaObjektuEntity entity = new OtviraciDobaObjektuEntity(pk, null);
        em.persist(entity);
        em.flush();

        Optional<OtviraciDobaObjektuEntity> result = otviraciDobaObjektuRepository.findById(pk);
        assertTrue(result.isPresent());

        otviraciDobaObjektuRepository.deleteById(pk);
        em.flush();

        result = otviraciDobaObjektuRepository.findById(pk);
        assertFalse(result.isPresent());
    }
}
