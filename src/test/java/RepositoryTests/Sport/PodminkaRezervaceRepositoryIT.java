package RepositoryTests.Sport;

import RepositoryTests.DatabaseCleaner;
import cz.inspire.sport.entity.PodminkaRezervaceEntity;
import cz.inspire.sport.entity.ObjektEntity;
import cz.inspire.sport.repository.PodminkaRezervaceRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.data.Limit;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PodminkaRezervaceRepositoryIT {

    @Inject
    PodminkaRezervaceRepository podminkaRezervaceRepository;

    @Inject
    DatabaseCleaner databaseCleaner;

    @Inject
    EntityManager em;

    @BeforeAll
    @ActivateRequestContext
    public void clearDatabase() {
        databaseCleaner.clearTable(PodminkaRezervaceEntity.class, true);
        databaseCleaner.clearTable(ObjektEntity.class, true);
    }

    private ObjektEntity createObjekt(String id) {
        ObjektEntity entity = new ObjektEntity();
        entity.setId(id);
        return entity;
    }

    private PodminkaRezervaceEntity createPodminkaRezervace(String id, long priorita, String objektRezervaceId, ObjektEntity objekt) {
        return new PodminkaRezervaceEntity(id, "Condition " + id, priorita, objektRezervaceId, true, objekt);
    }

    @Test
    @Order(1)
    void testFindAllOrdered() {
        ObjektEntity objekt = createObjekt("Objekt-001");
        em.persist(objekt);

        PodminkaRezervaceEntity entity1 = createPodminkaRezervace("PR-001", 3, "RZ-001", objekt);
        PodminkaRezervaceEntity entity2 = createPodminkaRezervace("PR-002", 1, "RZ-002", objekt);
        PodminkaRezervaceEntity entity3 = createPodminkaRezervace("PR-003", 2, "RZ-003", objekt);

        em.persist(entity1);
        em.persist(entity2);
        em.persist(entity3);
        em.flush();

        List<PodminkaRezervaceEntity> result = podminkaRezervaceRepository.findAllOrdered();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("PR-002", result.get(0).getId());
        assertEquals("PR-003", result.get(1).getId());
        assertEquals("PR-001", result.get(2).getId());
    }

    @Test
    @Order(2)
    void testFindAllWithLimit() {
        ObjektEntity objekt = createObjekt("Objekt-002");
        em.persist(objekt);

        PodminkaRezervaceEntity entity1 = createPodminkaRezervace("PR-004", 3, "RZ-004", objekt);
        PodminkaRezervaceEntity entity2 = createPodminkaRezervace("PR-005", 1, "RZ-005", objekt);
        PodminkaRezervaceEntity entity3 = createPodminkaRezervace("PR-006", 2, "RZ-006", objekt);

        em.persist(entity1);
        em.persist(entity2);
        em.persist(entity3);
        em.flush();

        List<PodminkaRezervaceEntity> result = podminkaRezervaceRepository.findAll(Limit.of(2));

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    @Order(3)
    void testFindByObjektWithLimit() {
        ObjektEntity objekt = createObjekt("Objekt-003");
        em.persist(objekt);

        PodminkaRezervaceEntity entity1 = createPodminkaRezervace("PR-007", 1, "RZ-007", objekt);
        PodminkaRezervaceEntity entity2 = createPodminkaRezervace("PR-008", 2, "RZ-008", objekt);
        em.persist(entity1);
        em.persist(entity2);
        em.flush();

        List<PodminkaRezervaceEntity> result = podminkaRezervaceRepository.findByObjekt("Objekt-003", Limit.of(1));

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @Order(4)
    void testCountAllByObject() {
        ObjektEntity objekt = createObjekt("Objekt-004");
        em.persist(objekt);

        PodminkaRezervaceEntity entity1 = createPodminkaRezervace("PR-009", 1, "RZ-009", objekt);
        PodminkaRezervaceEntity entity2 = createPodminkaRezervace("PR-010", 2, "RZ-010", objekt);
        em.persist(entity1);
        em.persist(entity2);
        em.flush();

        Long count = podminkaRezervaceRepository.countAllByObject("Objekt-004");

        assertEquals(2, count);
    }

    @Test
    @Order(5)
    void testCountAll() {
        Long initialCount = podminkaRezervaceRepository.countAll();

        ObjektEntity objekt = createObjekt("Objekt-005");
        PodminkaRezervaceEntity entity = createPodminkaRezervace("PR-011", 3, "RZ-011", objekt);

        em.persist(objekt);
        em.persist(entity);
        em.flush();

        Long newCount = podminkaRezervaceRepository.countAll();
        assertEquals(initialCount + 1, newCount);
    }

    @Test
    @Order(6)
    void testGetObjectIdsByReservationConditionObject() {
        ObjektEntity objekt = createObjekt("Objekt-006");
        em.persist(objekt);

        PodminkaRezervaceEntity entity = createPodminkaRezervace("PR-012", 2, "RZ-012", objekt);
        em.persist(entity);
        em.flush();

        List<String> result = podminkaRezervaceRepository.getObjectIdsByReservationConditionObject("RZ-012");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Objekt-006", result.getFirst());
    }

    @Test
    @Order(7)
    void testGetMaxPriority() {
        ObjektEntity objekt = createObjekt("Objekt-007");
        em.persist(objekt);

        PodminkaRezervaceEntity entity1 = createPodminkaRezervace("PR-013", 5, "RZ-013", objekt);
        PodminkaRezervaceEntity entity2 = createPodminkaRezervace("PR-014", 10, "RZ-014", objekt);
        em.persist(entity1);
        em.persist(entity2);
        em.flush();

        Long maxPriority = podminkaRezervaceRepository.getMaxPriority();

        assertEquals(10, maxPriority);
    }
}
