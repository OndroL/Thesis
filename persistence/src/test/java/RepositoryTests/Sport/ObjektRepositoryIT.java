package RepositoryTests.Sport;

import RepositoryTests.DatabaseCleaner;
import cz.inspire.sport.entity.ArealEntity;
import cz.inspire.sport.entity.ObjektEntity;
import cz.inspire.sport.entity.ObjektLocEntity;
import cz.inspire.sport.entity.ObjektSportEntity;
import cz.inspire.sport.entity.ObjektSportPK;
import cz.inspire.sport.entity.OvladacObjektuEntity;
import cz.inspire.sport.entity.PodminkaRezervaceEntity;
import cz.inspire.sport.entity.SportEntity;
import cz.inspire.sport.repository.ObjektRepository;
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
import java.util.Random;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ObjektRepositoryIT {

    @Inject
    ObjektRepository objektRepository;

    @Inject
    DatabaseCleaner databaseCleaner;

    @Inject
    EntityManager em;

    @BeforeAll
    @ActivateRequestContext
    public void clearDatabase() {
        databaseCleaner.clearTable(ObjektEntity.class, true);
        databaseCleaner.clearTable(ArealEntity.class, true);
        databaseCleaner.clearTable(ObjektLocEntity.class, true);
        databaseCleaner.clearTable(ObjektSportEntity.class, true);
        databaseCleaner.clearTable(OvladacObjektuEntity.class, true);
        databaseCleaner.clearTable(PodminkaRezervaceEntity.class, true);
        databaseCleaner.clearTable(SportEntity.class, true);
        em.flush();
    }

    private ObjektEntity createObjekt(ArealEntity areal, Boolean primyVstup, int typRezervace) {
        Random rand = new Random();
        ObjektEntity objekt = new ObjektEntity();
        objekt.setKapacita(rand.nextInt(200));
        objekt.setCasovaJednotka(rand.nextInt(100));
        objekt.setTypRezervace(typRezervace);
        objekt.setPrimyVstup(primyVstup);
        objekt.setAreal(areal);
        return objekt;
    }

    private ArealEntity createAreal() {
        Random rand = new Random();
        ArealEntity areal = new ArealEntity();
        areal.setPocetNavazujucichRez(rand.nextInt(10));
        return areal;
    }

    @Test
    @Order(1)
    void testFindAllOrdered() {
        ArealEntity areal = createAreal();
        em.persist(areal);

        ObjektEntity objekt1 = createObjekt(areal, false, 1);
        ObjektEntity objekt2 = createObjekt(areal, false, 1);
        ObjektEntity objekt3 = createObjekt(areal, false, 1);

        objekt1 = objektRepository.create(objekt1);
        objekt2 = objektRepository.create(objekt2);
        objekt3 = objektRepository.create(objekt3);

        List<ObjektEntity> result = objektRepository.findAllOrdered();
        assertNotNull(result);
        assertEquals(3, result.size());

        List<String> actualIds = result.stream().map(ObjektEntity::getId).collect(Collectors.toList());
        List<String> expectedSortedIds = List.of(objekt1.getId(), objekt2.getId(), objekt3.getId())
                .stream().sorted().collect(Collectors.toList());
        assertEquals(expectedSortedIds, actualIds, "Should be sorted by ID ascending");
    }

    @Test
    @Order(2)
    void testFindByAreal() {
        ArealEntity areal = createAreal();
        em.persist(areal);

        ObjektEntity objekt1 = createObjekt(areal, false, 1);
        ObjektEntity objekt2 = createObjekt(areal, false, 1);

        // Create locale data objects with null IDs for generation.
        ObjektLocEntity loc1 = new ObjektLocEntity(null, "cs", "Objekt 014", "Desc 014", "Short 014");
        ObjektLocEntity loc2 = new ObjektLocEntity(null, "cs", "Objekt 015", "Desc 015", "Short 015");

        objekt1.setLocaleData(List.of(loc1));
        objekt2.setLocaleData(List.of(loc2));

        objekt1 = objektRepository.create(objekt1);
        objekt2 = objektRepository.create(objekt2);

        List<ObjektEntity> result = objektRepository.findByAreal(areal.getId(), "cs");
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    @Order(3)
    void testFindByArealWithLimit() {
        ArealEntity areal = createAreal();
        em.persist(areal);

        ObjektEntity objekt1 = createObjekt(areal, false, 1);
        ObjektEntity objekt2 = createObjekt(areal, false, 1);
        ObjektEntity objekt3 = createObjekt(areal, false, 1);

        ObjektLocEntity loc1 = new ObjektLocEntity(null, "cs", "Objekt 014", "Desc 014", "Short 014");
        ObjektLocEntity loc2 = new ObjektLocEntity(null, "cs", "Objekt 015", "Desc 015", "Short 015");
        ObjektLocEntity loc3 = new ObjektLocEntity(null, "cs", "Objekt 016", "Desc 016", "Short 016");

        objekt1.setLocaleData(List.of(loc1));
        objekt2.setLocaleData(List.of(loc2));
        objekt3.setLocaleData(List.of(loc3));

        objekt1 = objektRepository.create(objekt1);
        objekt2 = objektRepository.create(objekt2);
        objekt3 = objektRepository.create(objekt3);

        List<ObjektEntity> result = objektRepository.findByArealWithLimit(areal.getId(), "cs", 2, 0);
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    @Order(4)
    void testFindBaseByAreal() {
        ArealEntity areal = createAreal();
        em.persist(areal);

        ObjektEntity objekt1 = createObjekt(areal, false, 1);
        ObjektEntity objekt2 = createObjekt(areal, false, 1);

        ObjektLocEntity loc1 = new ObjektLocEntity(null, "cs", "Objekt 014", "Desc 014", "Short 014");
        ObjektLocEntity loc2 = new ObjektLocEntity(null, "cs", "Objekt 015", "Desc 015", "Short 015");
        ObjektLocEntity loc3 = new ObjektLocEntity(null, "en", "Objekt 016", "Desc 016", "Short 016");

        objekt1.setLocaleData(List.of(loc1, loc3));
        objekt2.setLocaleData(List.of(loc2));

        objekt1 = objektRepository.create(objekt1);
        objekt2 = objektRepository.create(objekt2);

        List<ObjektEntity> result = objektRepository.findBaseByAreal(areal.getId(), "cs");
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    @Order(5)
    void testFindBaseByArealWithLimit() {
        ArealEntity areal = createAreal();
        em.persist(areal);

        ObjektEntity objekt1 = createObjekt(areal, false, 1);
        ObjektEntity objekt2 = createObjekt(areal, false, 1);
        ObjektEntity objekt3 = createObjekt(areal, false, 1);

        ObjektLocEntity loc1 = new ObjektLocEntity(null, "cs", "Objekt 014", "Desc 014", "Short 014");
        ObjektLocEntity loc2 = new ObjektLocEntity(null, "cs", "Objekt 015", "Desc 015", "Short 015");
        ObjektLocEntity loc3 = new ObjektLocEntity(null, "cs", "Objekt 016", "Desc 016", "Short 016");

        objekt1.setLocaleData(List.of(loc1));
        objekt2.setLocaleData(List.of(loc2));
        objekt3.setLocaleData(List.of(loc3));

        objekt1 = objektRepository.create(objekt1);
        objekt2 = objektRepository.create(objekt2);
        objekt3 = objektRepository.create(objekt3);

        List<ObjektEntity> result = objektRepository.findBaseByArealWithLimit(areal.getId(), "cs", 2, 0);
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    @Order(6)
    void testFindByTypRezervace() {
        ArealEntity areal = createAreal();
        em.persist(areal);

        ObjektEntity objekt1 = createObjekt(areal, false, 5);
        ObjektEntity objekt2 = createObjekt(areal, false, 2);
        ObjektEntity objekt3 = createObjekt(areal, false, 5);

        ObjektLocEntity loc1 = new ObjektLocEntity(null, "cs", "Objekt 014", "Desc 014", "Short 014");
        ObjektLocEntity loc2 = new ObjektLocEntity(null, "cs", "Objekt 015", "Desc 015", "Short 015");
        ObjektLocEntity loc3 = new ObjektLocEntity(null, "cs", "Objekt 016", "Desc 016", "Short 016");

        objekt1.setLocaleData(List.of(loc1));
        objekt2.setLocaleData(List.of(loc2));
        objekt3.setLocaleData(List.of(loc3));

        objekt1 = objektRepository.create(objekt1);
        objekt2 = objektRepository.create(objekt2);
        objekt3 = objektRepository.create(objekt3);

        List<ObjektEntity> result = objektRepository.findByTypRezervace(5, "cs");
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    @Order(8)
    void testFindBySport() {
        SportEntity sport = new SportEntity(null, 1, "ZB-001", "SK-001", 100, true, 60, true, 10, null, 30, 120, true, 15, null, null, true, true, 10, 90, 1, 5, 20, null, null, null, null, null, null, null, null, null);
        em.persist(sport);

        ObjektEntity objekt1 = new ObjektEntity();
        ObjektEntity objekt2 = new ObjektEntity();

        objekt1.setPrimyVstup(true);
        objekt2.setPrimyVstup(false);

        ObjektLocEntity loc1 = new ObjektLocEntity(null, "en", "Objekt One Name", "Objekt 1 Description", "Short Name 1");
        ObjektLocEntity loc2 = new ObjektLocEntity(null, "en", "Objekt Two Name", "Objekt 2 Description", "Short Name 2");
        objekt1.setLocaleData(List.of(loc1));
        objekt2.setLocaleData(List.of(loc2));

        objekt1 = objektRepository.create(objekt1);
        objekt2 = objektRepository.create(objekt2);

        ObjektSportPK key1 = new ObjektSportPK();
        key1.setIndex(1);

        // Persisting ObjektSportEntity using em.persist since it might not be managed by ObjektRepository.
        // (If you have a repository for it, you could use that instead.)
        ObjektSportEntity objektSport1 = new ObjektSportEntity(key1, sport, objekt1);
        em.persist(objektSport1);

        ObjektSportPK key2 = new ObjektSportPK();
        key2.setIndex(2);

        ObjektSportEntity objektSport2 = new ObjektSportEntity(key2, sport, objekt2);
        em.persist(objektSport2);

        objekt1.setObjektSports(List.of(objektSport1));
        objekt2.setObjektSports(List.of(objektSport2));

        em.flush();

        List<ObjektEntity> objects = objektRepository.findBySport(sport.getId(), "en");
        assertNotNull(objects);
        assertEquals(2, objects.size());
        ObjektEntity finalObjekt = objekt1;
        assertTrue(objects.stream().anyMatch(o -> o.getId().equals(finalObjekt.getId())));
        ObjektEntity finalObjekt1 = objekt2;
        assertTrue(objects.stream().anyMatch(o -> o.getId().equals(finalObjekt1.getId())));
    }

    @Test
    @Order(9)
    void testFindByPrimyVstup() {
        ArealEntity areal = createAreal();
        em.persist(areal);

        ObjektEntity objekt1 = createObjekt(areal, true, 2);
        ObjektEntity objekt2 = createObjekt(areal, false, 2);

        ObjektLocEntity loc1 = new ObjektLocEntity(null, "jp", "Objekt Three Name", "Objekt 3 Description", "Short Name 3");
        ObjektLocEntity loc2 = new ObjektLocEntity(null, "jp", "Objekt Four Name", "Objekt 4 Description", "Short Name 4");

        objekt1.setLocaleData(List.of(loc1));
        objekt2.setLocaleData(List.of(loc2));

        objekt1 = objektRepository.create(objekt1);
        objekt2 = objektRepository.create(objekt2);

        List<ObjektEntity> primyVstupObjects = objektRepository.findByPrimyVstup("jp", true);
        List<ObjektEntity> nonPrimyVstupObjects = objektRepository.findByPrimyVstup("jp", false);

        assertNotNull(primyVstupObjects);
        assertEquals(1, primyVstupObjects.size());
        assertEquals(objekt1.getId(), primyVstupObjects.get(0).getId());

        assertNotNull(nonPrimyVstupObjects);
        assertEquals(1, nonPrimyVstupObjects.size());
        assertEquals(objekt2.getId(), nonPrimyVstupObjects.get(0).getId());
    }

    @Test
    @Order(10)
    void testFindByPrimyVstupWithLimit() {
        ArealEntity areal = createAreal();
        em.persist(areal);

        ObjektEntity objekt1 = createObjekt(areal, true, 1);
        ObjektEntity objekt2 = createObjekt(areal, true, 2);
        ObjektEntity objekt3 = createObjekt(areal, false, 1);
        ObjektEntity objekt4 = createObjekt(areal, true, 1);

        ObjektLocEntity loc1 = new ObjektLocEntity(null, "ger", "Objekt 017", "Desc 017", "Short 017");
        ObjektLocEntity loc2 = new ObjektLocEntity(null, "ger", "Objekt 018", "Desc 018", "Short 018");
        ObjektLocEntity loc3 = new ObjektLocEntity(null, "ger", "Objekt 019", "Desc 019", "Short 019");
        ObjektLocEntity loc4 = new ObjektLocEntity(null, "ger", "Objekt 020", "Desc 020", "Short 020");

        objekt1.setLocaleData(List.of(loc1));
        objekt2.setLocaleData(List.of(loc2));
        objekt3.setLocaleData(List.of(loc3));
        objekt4.setLocaleData(List.of(loc4));

        objekt1 = objektRepository.create(objekt1);
        objekt2 = objektRepository.create(objekt2);
        objekt3 = objektRepository.create(objekt3);
        objekt4 = objektRepository.create(objekt4);

        List<ObjektEntity> result = objektRepository.findByPrimyVstupWithLimit("ger", 2, 0, true);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(ObjektEntity::isPrimyVstup));
    }

    @Test
    @Order(11)
    void testFindObjektIdsOfAreal() {
        ArealEntity areal = createAreal();
        em.persist(areal);

        ObjektEntity objekt1 = createObjekt(areal, true, 1);
        ObjektEntity objekt2 = createObjekt(areal, false, 2);
        ObjektEntity objekt3 = createObjekt(areal, true, 1);

        objekt1 = objektRepository.create(objekt1);
        objekt2 = objektRepository.create(objekt2);
        objekt3 = objektRepository.create(objekt3);

        List<String> result = objektRepository.findObjektIdsOfAreal(areal.getId());
        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.contains(objekt1.getId()));
        assertTrue(result.contains(objekt2.getId()));
        assertTrue(result.contains(objekt3.getId()));
    }

    @Test
    @Order(12)
    void testDeleteObjekt() {
        ArealEntity areal = createAreal();
        em.persist(areal);

        ObjektEntity objekt = createObjekt(areal, true, 9);
        objekt = objektRepository.create(objekt);

        ObjektEntity result = objektRepository.findById(objekt.getId());
        assertNotNull(result);

        // Instead of calling em.remove, use repository.deleteById.
        objektRepository.deleteById(objekt.getId());

        result = objektRepository.findById(objekt.getId());
        assertNull(result, "Objekt should be deleted");
    }
}
