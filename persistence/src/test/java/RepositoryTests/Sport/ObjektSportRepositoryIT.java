package RepositoryTests.Sport;

import RepositoryTests.DatabaseCleaner;
import cz.inspire.sport.entity.ObjektEntity;
import cz.inspire.sport.entity.ObjektSportEntity;
import cz.inspire.sport.entity.ObjektSportPK;
import cz.inspire.sport.entity.SportEntity;
import cz.inspire.sport.repository.ObjektSportRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
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
        ObjektEntity objekt = createObjekt(null);
        SportEntity sport = createSport(null);
        ObjektSportEntity entity = createObjektSport(null, 1, objekt, sport);

        em.persist(objekt);
        em.persist(sport);
        em.persist(entity);
        em.flush();

        ObjektSportEntity result = objektSportRepository.findById(new ObjektSportPK(entity.getEmbeddedId().getId(), 1));

        assertNotNull(result);
        assertEquals(sport.getId(), result.getSport().getId());
    }

    @Test
    @Order(2)
    void testFindByObjekt() {
        ObjektEntity objekt = createObjekt(null);
        SportEntity sport1 = createSport(null);
        SportEntity sport2 = createSport(null);
        ObjektSportEntity entity1 = createObjektSport(null, 1, objekt, sport1);
        ObjektSportEntity entity2 = createObjektSport(null, 2, objekt, sport2);

        em.persist(objekt);
        em.persist(sport1);
        em.persist(sport2);
        em.persist(entity1);
        em.persist(entity2);
        em.flush();

        List<ObjektSportEntity> result = objektSportRepository.findByObjekt(objekt.getId());

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    @Order(3)
    void testDeleteById() {
        ObjektEntity objekt = createObjekt(null);
        SportEntity sport = createSport(null);
        ObjektSportEntity entity = createObjektSport(null, 1, objekt, sport);

        em.persist(objekt);
        em.persist(sport);
        em.persist(entity);
        em.flush();

        ObjektSportEntity result = objektSportRepository.findById(new ObjektSportPK(entity.getEmbeddedId().getId(), 1));
        assertNotNull(result);

        em.remove(entity);
        em.flush();

        result = objektSportRepository.findById(new ObjektSportPK(entity.getEmbeddedId().getId(), 1));
        assertNull(result);
    }
}
