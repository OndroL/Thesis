package RepositoryTests.Sport;

import RepositoryTests.DatabaseCleaner;
import cz.inspire.sport.entity.*;
import cz.inspire.sport.repository.ObjektSportRepository;
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
public class ObjektSportRepositoryIT {

    @Inject
    ObjektSportRepository objektSportRepository;

    @Inject
    DatabaseCleaner databaseCleaner;

    @Inject
    EntityManager em;

    @BeforeAll
    @ActivateRequestContext
    public void clearDatabase() {
        databaseCleaner.clearTable(ObjektSportEntity.class, true);
        databaseCleaner.clearTable(SportEntity.class, true);
        databaseCleaner.clearTable(ObjektEntity.class, true);
    }

    private ObjektEntity createObjekt(String id) {
        ObjektEntity entity = new ObjektEntity();
        entity.setId(id);
        return entity;
    }

    private SportEntity createSport(String id) {
        return new SportEntity(id, 1, "ZB-001", "SK-001", 100, true, 60, true, 10, null, 30, 120, true, 15, null, null, true, true, 10, 90, 1, 5, 20, null, null, null, null, null, null, null, null, null);
    }

    private ObjektSportEntity createObjektSport(String id, int index, ObjektEntity objekt, SportEntity sport) {
        return new ObjektSportEntity(new ObjektSportPK(id, index), sport, objekt);
    }

    @Test
    @Order(1)
    void testSaveAndFindById() {
        ObjektEntity objekt = createObjekt("Objekt-001");
        SportEntity sport = createSport("Sport-001");
        ObjektSportEntity entity = createObjektSport("OS-001", 1, objekt, sport);

        em.persist(objekt);
        em.persist(sport);
        em.persist(entity);
        em.flush();

        Optional<ObjektSportEntity> result = objektSportRepository.findById(new ObjektSportPK("OS-001", 1));

        assertTrue(result.isPresent());
        assertEquals("Sport-001", result.get().getSport().getId());
    }

    @Test
    @Order(2)
    void testFindByObjekt() {
        ObjektEntity objekt = createObjekt("Objekt-002");
        SportEntity sport1 = createSport("Sport-002");
        SportEntity sport2 = createSport("Sport-003");
        ObjektSportEntity entity1 = createObjektSport("OS-002", 1, objekt, sport1);
        ObjektSportEntity entity2 = createObjektSport("OS-003", 2, objekt, sport2);

        em.persist(objekt);
        em.persist(sport1);
        em.persist(sport2);
        em.persist(entity1);
        em.persist(entity2);
        em.flush();

        List<ObjektSportEntity> result = objektSportRepository.findByObjekt("Objekt-002");

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    @Order(3)
    void testDeleteById() {
        ObjektEntity objekt = createObjekt("Objekt-003");
        SportEntity sport = createSport("Sport-004");
        ObjektSportEntity entity = createObjektSport("OS-004", 1, objekt, sport);

        em.persist(objekt);
        em.persist(sport);
        em.persist(entity);
        em.flush();

        Optional<ObjektSportEntity> result = objektSportRepository.findById(new ObjektSportPK("OS-004", 1));
        assertTrue(result.isPresent());

        em.remove(entity);
        em.flush();

        result = objektSportRepository.findById(new ObjektSportPK("OS-004", 1));
        assertFalse(result.isPresent());
    }
}
