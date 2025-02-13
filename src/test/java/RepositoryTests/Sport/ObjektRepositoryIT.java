package RepositoryTests.Sport;

import RepositoryTests.DatabaseCleaner;
import cz.inspire.sport.entity.*;
import cz.inspire.sport.repository.ObjektRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.data.Limit;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;

import java.util.Random;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

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
    }

    private ObjektEntity createObjekt(ArealEntity areal, String id, boolean primyVstup, int typRezervace) {
        Random rand = new Random();

        ObjektEntity objekt = new ObjektEntity();
        objekt.setId(id);
        objekt.setKapacita(rand.nextInt(200));
        objekt.setCasovaJednotka(rand.nextInt(100));
        objekt.setTypRezervace(typRezervace);
        objekt.setPrimyVstup(primyVstup);
        objekt.setAreal(areal);
        return objekt;
    }

    private ArealEntity createAreal(String id) {
        Random rand = new Random();

        ArealEntity areal = new ArealEntity();
        areal.setId(id);
        areal.setPocetNavazujucichRez(rand.nextInt(10));
        return areal;
    }

    @Test
    @Order(1)
    void testFindAllOrdered() {
        ArealEntity areal = createAreal("Areal-001");

        ObjektEntity objekt1 = createObjekt(areal, "Objekt-002", false, 1);
        ObjektEntity objekt2 = createObjekt(areal, "Objekt-001", false, 1);
        ObjektEntity objekt3 = createObjekt(areal, "Objekt-003", false, 1);

        em.persist(areal);
        em.persist(objekt1);
        em.persist(objekt2);
        em.persist(objekt3);
        em.flush();

        List<ObjektEntity> result = objektRepository.findAllOrdered();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("Objekt-001", result.get(0).getId());
        assertEquals("Objekt-002", result.get(1).getId());
        assertEquals("Objekt-003", result.get(2).getId());
    }

    @Test
    @Order(2)
    void testFindByAreal() {
        ArealEntity areal = createAreal("Areal-002");

        ObjektEntity objekt1 = createObjekt(areal, "Objekt-004", false, 1);
        ObjektEntity objekt2 = createObjekt(areal, "Objekt-005", false, 1);

        ObjektLocEntity loc1 = new ObjektLocEntity("ObjektLoc-004", "cs", "Objekt 014", "Desc 014", "Short 014");
        ObjektLocEntity loc2 = new ObjektLocEntity("ObjektLoc-005", "cs", "Objekt 015", "Desc 015", "Short 015");

        objekt1.setLocaleData(List.of(loc1));
        objekt2.setLocaleData(List.of(loc2));

        em.persist(areal);
        em.persist(objekt1);
        em.persist(objekt2);
        em.flush();

        List<ObjektEntity> result = objektRepository.findByAreal("Areal-002", "cs");

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    @Order(3)
    void testFindByArealWithLimit() {
        ArealEntity areal = createAreal("Areal-003");

        ObjektEntity objekt1 = createObjekt(areal, "Objekt-006", false, 1);
        ObjektEntity objekt2 = createObjekt(areal, "Objekt-007", false, 1);
        ObjektEntity objekt3 = createObjekt(areal, "Objekt-008", false, 1);

        ObjektLocEntity loc1 = new ObjektLocEntity("ObjektLoc-006", "cs", "Objekt 014", "Desc 014", "Short 014");
        ObjektLocEntity loc2 = new ObjektLocEntity("ObjektLoc-007", "cs", "Objekt 015", "Desc 015", "Short 015");
        ObjektLocEntity loc3 = new ObjektLocEntity("ObjektLoc-008", "cs", "Objekt 016", "Desc 016", "Short 016");

        // Associate locale data with ObjektEntities
        objekt1.setLocaleData(List.of(loc1));
        objekt2.setLocaleData(List.of(loc2));
        objekt3.setLocaleData(List.of(loc3));

        em.persist(objekt1);
        em.persist(objekt2);
        em.persist(objekt3);
        em.persist(areal);
        em.flush();

        List<ObjektEntity> result = objektRepository.findByArealWithLimit("Areal-003", "cs", Limit.of(2));

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    @Order(4)
    void testFindBaseByAreal() {
        ArealEntity areal = createAreal("Areal-004");

        ObjektEntity objekt1 = createObjekt(areal, "Objekt-009", false, 1);
        ObjektEntity objekt2 =createObjekt(areal, "Objekt-010", false, 1);

        ObjektLocEntity loc1 = new ObjektLocEntity("ObjektLoc-009", "cs", "Objekt 014", "Desc 014", "Short 014");
        ObjektLocEntity loc2 = new ObjektLocEntity("ObjektLoc-0091", "cs", "Objekt 015", "Desc 015", "Short 015");
        ObjektLocEntity loc3 = new ObjektLocEntity("ObjektLoc-010", "en", "Objekt 016", "Desc 016", "Short 016");

        // Associate locale data with ObjektEntities
        objekt1.setLocaleData(List.of(loc1,loc3));
        objekt2.setLocaleData(List.of(loc2));

        em.persist(objekt1);
        em.persist(objekt2);
        em.persist(areal);
        em.flush();

        List<ObjektEntity> result = objektRepository.findBaseByAreal("Areal-004", "cs");

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    @Order(5)
    void testFindBaseByArealWithLimit() {
        ArealEntity areal = createAreal("Areal-005");

        ObjektEntity objekt1 = createObjekt(areal, "Objekt-011", false, 1);
        ObjektEntity objekt2 = createObjekt(areal, "Objekt-012", false, 1);
        ObjektEntity objekt3 = createObjekt(areal, "Objekt-013", false, 1);

        ObjektLocEntity loc1 = new ObjektLocEntity("ObjektLoc-011", "cs", "Objekt 014", "Desc 014", "Short 014");
        ObjektLocEntity loc2 = new ObjektLocEntity("ObjektLoc-012", "cs", "Objekt 015", "Desc 015", "Short 015");
        ObjektLocEntity loc3 = new ObjektLocEntity("ObjektLoc-013", "cs", "Objekt 016", "Desc 016", "Short 016");

        objekt1.setLocaleData(List.of(loc1));
        objekt2.setLocaleData(List.of(loc2));
        objekt3.setLocaleData(List.of(loc3));

        em.persist(objekt1);
        em.persist(objekt2);
        em.persist(objekt3);
        em.persist(areal);
        em.flush();

        List<ObjektEntity> result = objektRepository.findBaseByArealWithLimit("Areal-005", "cs", Limit.of(2));

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    @Order(6)
    void testFindByTypRezervace() {
        ArealEntity areal = createAreal("Areal-006");

        ObjektEntity objekt1 = createObjekt(areal, "Objekt-014", false, 5);
        ObjektEntity objekt2 = createObjekt(areal, "Objekt-015", false, 2);
        ObjektEntity objekt3 = createObjekt(areal, "Objekt-016", false, 5);

        ObjektLocEntity loc1 = new ObjektLocEntity("ObjektLoc-014", "cs", "Objekt 014", "Desc 014", "Short 014");
        ObjektLocEntity loc2 = new ObjektLocEntity("ObjektLoc-015", "cs", "Objekt 015", "Desc 015", "Short 015");
        ObjektLocEntity loc3 = new ObjektLocEntity("ObjektLoc-016", "cs", "Objekt 016", "Desc 016", "Short 016");

        objekt1.setLocaleData(List.of(loc1));
        objekt2.setLocaleData(List.of(loc2));
        objekt3.setLocaleData(List.of(loc3));

        em.persist(objekt1);
        em.persist(objekt2);
        em.persist(objekt3);
        em.persist(areal);

        em.flush();

        List<ObjektEntity> result = objektRepository.findByTypRezervace(5, "cs");

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    @Order(8)
    void testFindBySport() {
        SportEntity sport = new SportEntity(
                "sport1", 1, "zbozi1", "sklad1", 100, true, 60, true, 0,
                null, 30, 120, true, 15, null, null, true, true, 10,
                90, 1, 5, 20, null, null, null, null, null, null, null, null, null
        );
        em.persist(sport);

        ObjektEntity objekt1 = new ObjektEntity(
                "Objekt-021", 101, 15, 1, true, 30, 120, 10, 20, 5, 15, true, 2,
                null, null, true, false, false, true, false, true, true, true,
                false, true, 5, 10, 15, "calendarId1", true, 30, null,
                null, null, null, null, null, null
        );

        ObjektEntity objekt2 = new ObjektEntity(
                "Objekt-022", 102, 15, 1, false, 30, 120, 10, 20, 5, 15, true, 2,
                null, null, true, false, false, true, false, true, true, true,
                false, true, 5, 10, 15, "calendarId2", true, 30, null,
                null, null, null, null, null, null
        );

        ObjektLocEntity loc1 = new ObjektLocEntity(
                "ObjektLoc-021", "en", "Objekt One Name", "Objekt 1 Description", "Short Name 1"
        );
        ObjektLocEntity loc2 = new ObjektLocEntity(
                "ObjektLoc-022", "en", "Objekt Two Name", "Objekt 2 Description", "Short Name 2"
        );

        objekt1.setLocaleData(List.of(loc1));
        objekt2.setLocaleData(List.of(loc2));

        em.persist(objekt1);
        em.persist(objekt2);

        ObjektSportPK key1 = new ObjektSportPK("ObjektSport-021", 1);
        ObjektSportEntity objektSport1 = new ObjektSportEntity(key1, sport, objekt1);
        em.persist(objektSport1);

        ObjektSportPK key2 = new ObjektSportPK("ObjektSport-022", 2);
        ObjektSportEntity objektSport2 = new ObjektSportEntity(key2, sport, objekt2);
        em.persist(objektSport2);

        objekt1.setObjektSports(List.of(objektSport1));
        objekt2.setObjektSports(List.of(objektSport2));

        em.flush();

        List<ObjektEntity> objects = objektRepository.findBySport("sport1", "en");

        assertNotNull(objects);
        assertEquals(2, objects.size());
        assertTrue(objects.stream().anyMatch(o -> o.getId().equals("Objekt-021")));
        assertTrue(objects.stream().anyMatch(o -> o.getId().equals("Objekt-022")));
    }


    @Test
    @Order(9)
    void testFindByPrimyVstup() {
        ArealEntity areal = createAreal("Areal-031");
        em.persist(areal);

        ObjektEntity objekt1 = new ObjektEntity(
                "Objekt-031", 150, 30, 2, true, 25, 120, 5, 15, 3, 10, false, 1,
                null, null, true, false, false, true, false, true, true, true,
                false, true, 10, 15, 20, "calendarId3", true, 40, null,
                null, null, null, null, null, null
        );
        objekt1.setAreal(areal);

        ObjektEntity objekt2 = new ObjektEntity(
                "Objekt-032", 160, 30, 2, false, 25, 120, 5, 15, 3, 10, false, 1,
                null, null, true, false, false, true, false, true, true, true,
                false, true, 10, 15, 20, "calendarId4", true, 40, null,
                null, null, null, null, null, null
        );
        objekt2.setAreal(areal);

        ObjektLocEntity loc1 = new ObjektLocEntity(
                "ObjektLoc-031", "jp", "Objekt Three Name", "Objekt 3 Description", "Short Name 3"
        );
        ObjektLocEntity loc2 = new ObjektLocEntity(
                "ObjektLoc-032", "jp", "Objekt Four Name", "Objekt 4 Description", "Short Name 4"
        );

        objekt1.setLocaleData(List.of(loc1));
        objekt2.setLocaleData(List.of(loc2));

        em.persist(objekt1);
        em.persist(objekt2);

        em.flush();

        List<ObjektEntity> primyVstupObjects = objektRepository.findByPrimyVstup("jp", true);
        List<ObjektEntity> nonPrimyVstupObjects = objektRepository.findByPrimyVstup("jp", false);

        assertNotNull(primyVstupObjects);
        assertEquals(1, primyVstupObjects.size());
        assertEquals("Objekt-031", primyVstupObjects.getFirst().getId());

        assertNotNull(nonPrimyVstupObjects);
        assertEquals(1, nonPrimyVstupObjects.size());
        assertEquals("Objekt-032", nonPrimyVstupObjects.getFirst().getId());
    }


    @Test
    @Order(10)
    void testFindByPrimyVstupWithLimit() {
        ArealEntity areal = createAreal("Areal-007");
        em.persist(areal);

        ObjektEntity objekt1 = createObjekt(areal, "Objekt-017", true, 1);
        ObjektEntity objekt2 = createObjekt(areal, "Objekt-018", true, 2);
        ObjektEntity objekt3 = createObjekt(areal, "Objekt-019", false, 1);
        ObjektEntity objekt4 = createObjekt(areal, "Objekt-020", true, 1);

        ObjektLocEntity loc1 = new ObjektLocEntity("ObjektLoc-017", "ger", "Objekt 017", "Desc 017", "Short 017");
        ObjektLocEntity loc2 = new ObjektLocEntity("ObjektLoc-018", "ger", "Objekt 018", "Desc 018", "Short 018");
        ObjektLocEntity loc3 = new ObjektLocEntity("ObjektLoc-019", "ger", "Objekt 019", "Desc 019", "Short 019");
        ObjektLocEntity loc4 = new ObjektLocEntity("ObjektLoc-020", "ger", "Objekt 020", "Desc 020", "Short 020");

        objekt1.setLocaleData(List.of(loc1));
        objekt2.setLocaleData(List.of(loc2));
        objekt3.setLocaleData(List.of(loc3));
        objekt4.setLocaleData(List.of(loc4));

        em.persist(objekt1);
        em.persist(objekt2);
        em.persist(objekt3);
        em.persist(objekt4);

        em.flush();

        Limit limit = Limit.of(2);
        List<ObjektEntity> result = objektRepository.findByPrimyVstupWithLimit("ger", limit, true);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(ObjektEntity::isPrimyVstup));
    }

    @Test
    @Order(11)
    void testFindObjektIdsOfAreal() {
        ArealEntity areal = createAreal("Areal-008");
        em.persist(areal);

        ObjektEntity objekt1 = createObjekt(areal, "Objekt-041", true, 1);
        ObjektEntity objekt2 = createObjekt(areal, "Objekt-042", false, 2);
        ObjektEntity objekt3 = createObjekt(areal, "Objekt-043", true, 1);

        em.persist(objekt1);
        em.persist(objekt2);
        em.persist(objekt3);

        em.flush();

        List<String> result = objektRepository.findObjektIdsOfAreal("Areal-008");

        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.contains("Objekt-041"));
        assertTrue(result.contains("Objekt-042"));
        assertTrue(result.contains("Objekt-043"));
    }



    @Test
    @Order(12)
    public void testDeleteObjekt() {
        ArealEntity areal = createAreal("Areal-999");
        ObjektEntity objekt = createObjekt(areal, "objDel", true, 9);
        em.persist(areal);
        em.persist(objekt);
        em.flush();
        Optional<ObjektEntity> result = objektRepository.findById("objDel");
        assertTrue(result.isPresent());
        em.remove(objekt);
        em.flush();

        result = objektRepository.findById("objDel");
        assertFalse(result.isPresent());
    }
}
