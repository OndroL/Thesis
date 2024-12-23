package RepositoryTests.sport;

import cz.inspire.thesis.data.EntityManagerProducer;
import cz.inspire.thesis.data.model.sport.objekt.*;
import cz.inspire.thesis.data.model.sport.sport.SportEntity;
import cz.inspire.thesis.data.repository.sport.objekt.ArealRepository;
import cz.inspire.thesis.data.repository.sport.objekt.ObjektLocRepository;
import cz.inspire.thesis.data.repository.sport.objekt.ObjektRepository;
import cz.inspire.thesis.data.repository.sport.objekt.ObjektSportRepository;
import cz.inspire.thesis.data.repository.sport.sport.SportRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ObjektRepositoryTest {

    private ObjektRepository repository;
    private ObjektLocRepository objektLocRepository;
    private ArealRepository arealRepository;
    private SportRepository sportRepository;
    private ObjektSportRepository objektSportRepository;

    @Before
    public void setUp() {
        CdiContainer container = CdiContainerLoader.getCdiContainer();
        container.boot();

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        EntityManagerProducer producer = BeanProvider.getContextualReference(EntityManagerProducer.class);
        producer.setEntityManagerFactory(emf);

        repository = BeanProvider.getContextualReference(ObjektRepository.class);
        arealRepository = BeanProvider.getContextualReference(ArealRepository.class);
        objektLocRepository = BeanProvider.getContextualReference(ObjektLocRepository.class);
        sportRepository = BeanProvider.getContextualReference(SportRepository.class);
        objektSportRepository = BeanProvider.getContextualReference(ObjektSportRepository.class);

        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.createQuery("DELETE FROM ObjektEntity").executeUpdate();
        em.createQuery("DELETE FROM ArealEntity ").executeUpdate();
        em.createQuery("DELETE FROM ObjektLocEntity ").executeUpdate();
        em.createQuery("DELETE FROM ArealLocEntity ").executeUpdate();
        em.createQuery("DELETE FROM SportInstructorEntity ").executeUpdate();
        em.createQuery("DELETE FROM SportEntity ").executeUpdate();
        em.createQuery("DELETE FROM ObjektSportEntity ").executeUpdate();
        em.getTransaction().commit();
    }

    @Test
    public void testFindAll() {
        ArealEntity areal = new ArealEntity("areal1", 5, null, null, null, null);
        arealRepository.save(areal);
        ObjektEntity obj1 = new ObjektEntity(
                "obj1", 100, 15, 1, true, 30, 120, 10, 20, 5,
                15, true, 2, new Integer[]{8, 9, 10}, new Integer[]{18, 19, 20},
                true, false, false, true, false, true, true, true, false,
                true, 5, 10, 15, "calendarId123", true, 30, areal, null, null,
                null, null, null, null
        );

        ObjektEntity obj2 = new ObjektEntity(
                "obj2", 200, 20, 2, false, 40, 140, 15, 25, 10,
                20, false, 3, new Integer[]{1, 2, 3}, new Integer[]{4, 5, 6},
                false, true, true, false, true, false, false, false, true,
                false, 10, 20, 30, "calendarId456", false, 60, areal, null, null,
                null, null, null, null
        );

        repository.save(obj1);
        repository.save(obj2);

        List<ObjektEntity> allObjects = repository.findAll();
        assertNotNull(allObjects);
        assertEquals(2, allObjects.size());
    }

    @Test
    public void testFindByAreal() {
        // Create and save ArealEntity first
        ArealEntity areal = new ArealEntity("areal1", 5, null, null, null, null);
        arealRepository.save(areal);

        // Create ObjektEntity obj1
        ObjektEntity obj1 = new ObjektEntity(
                "obj1", 100, 15, 1, true, 30, 120, 10, 20, 5, 15, true, 2,
                null, null, true, false, false, true, false, true, true, true,
                false, true, 5, 10, 15, "calendarId123", true, 30, areal,
                null, null, null, null, null, null
        );

        // Create ObjektEntity obj2
        ObjektEntity obj2 = new ObjektEntity(
                "obj2", 200, 20, 2, false, 40, 140, 15, 25, 10, 20, false, 3,
                null, null, false, true, true, false, true, false, false, false,
                true, false, 10, 20, 30, "calendarId456", false, 60, areal,
                null, null, null, null, null, null
        );


        // Create ObjektLocEntity for obj1 and obj2
        ObjektLocEntity loc1 = new ObjektLocEntity("loc1", "en", "Objekt 1 Name", "Objekt 1 Description", "Short Name 1");
        ObjektLocEntity loc2 = new ObjektLocEntity("loc2", "en", "Objekt 2 Name", "Objekt 2 Description", "Short Name 2");

        obj1.setLocaleData(List.of(loc1));
        repository.save(obj1);

        obj2.setLocaleData(List.of(loc2));
        repository.save(obj2);


        // Find objects by Areal and language 'en'
        List<ObjektEntity> objects = repository.findByAreal("areal1", "en");

        // Verify the results
        assertNotNull(objects);
        assertEquals(2, objects.size());
        assertTrue(objects.stream().anyMatch(obj -> obj.getId().equals("obj1")));
        assertTrue(objects.stream().anyMatch(obj -> obj.getId().equals("obj2")));
    }


    @Test
    public void testFindByPrimyVstup() {
        // Create and save ArealEntity first
        ArealEntity areal = new ArealEntity("areal1", 5, null, null, null, null);
        arealRepository.save(areal);

        // Create ObjektEntity obj1
        ObjektEntity obj1 = new ObjektEntity(
                "obj1", 100, 15, 1, true, 30, 120, 10, 20, 5, 15, true, 2,
                null, null, true, false, false, true, false, true, true, true,
                false, true, 5, 10, 15, "calendarId123", true, 30, areal,
                null, null, null, null, null, null
        );

        // Create ObjektEntity obj2
        ObjektEntity obj2 = new ObjektEntity(
                "obj2", 200, 20, 2, false, 40, 140, 15, 25, 10, 20, false, 3,
                null, null, false, true, true, false, true, false, false, false,
                true, false, 10, 20, 30, "calendarId456", false, 60, areal,
                null, null, null, null, null, null
        );

        // Create ObjektLocEntity for obj1 and obj2
        ObjektLocEntity loc1 = new ObjektLocEntity("loc1", "en", "Objekt 1 Name", "Objekt 1 Description", "Short Name 1");
        ObjektLocEntity loc2 = new ObjektLocEntity("loc2", "en", "Objekt 2 Name", "Objekt 2 Description", "Short Name 2");

        // Save ObjektLocEntities
        obj1.setLocaleData(List.of(loc1));
        repository.save(obj1);

        obj2.setLocaleData(List.of(loc2));
        repository.save(obj2);


        // Find objects with primyVstup set to true and language 'en'
        List<ObjektEntity> objects = repository.findByPrimyVstup("en", true);

        // Verify the results
        assertNotNull(objects);
        assertEquals(1, objects.size());
        assertEquals("obj1", objects.get(0).getId());
    }

    @Test
    public void testFindByTypRezervace() {
        // Create and save ArealEntity first
        ArealEntity areal = new ArealEntity("areal1", 5, null, null, null, null);
        arealRepository.save(areal);

        // Create ObjektEntity obj1
        ObjektEntity obj1 = new ObjektEntity(
                "obj1", 100, 15, 1, true, 30, 120, 10, 20, 5, 15, true, 2,
                null, null, true, false, false, true, false, true, true, true,
                false, true, 5, 10, 15, "calendarId123", true, 30, areal,
                null, null, null, null, null, null
        );


        // Create ObjektLocEntity for obj1 with language "en"
        ObjektLocEntity loc1 = new ObjektLocEntity("loc1", "en", "Objekt 1 Name", "Objekt 1 Description", "Short Name 1");

        obj1.setLocaleData(List.of(loc1));
        repository.save(obj1);

        // Find objects by typRezervace and language 'en'
        List<ObjektEntity> objects = repository.findByTypRezervace(1, "en");

        // Verify the results
        assertNotNull(objects);
        assertEquals(1, objects.size());
        assertEquals("obj1", objects.get(0).getId());
    }


    @Test
    public void testFindBaseByAreal() {
        // Create and save ArealEntity first
        ArealEntity areal = new ArealEntity("areal1", 5, null, null, null, null);
        ArealLocEntity loc1 = new ArealLocEntity("loc1", "en", "Name1", "Description1");
        areal.setLocaleData(List.of(loc1));
        arealRepository.save(areal);

        // Create ObjektEntity
        ObjektEntity baseObj = new ObjektEntity(
                "base1", 100, 15, 1, false, 30, 120, 10, 20, 5, 15, true, 2,
                null, null, true, false, false, true, false, true, true, true,
                false, true, 5, 10, 15, "calendarIdBase", true, 30, areal,
                null, null, null, null, null, null
        );

        // Create ObjektLocEntity and set it as localeData in ObjektEntity
        ObjektLocEntity loc2 = new ObjektLocEntity("loc2", "en", "Objekt Name", "Objekt Description", "Short Name");
        baseObj.setLocaleData(List.of(loc2));

        // Save ObjektEntity and ObjektLocEntity
        repository.save(baseObj);
        objektLocRepository.save(loc2);  // Save ObjektLocEntity to ObjektLocRepository

        // Verify the results
        List<ObjektEntity> baseObjects = repository.findBaseByAreal("areal1", "en");
        assertNotNull(baseObjects);
        assertEquals(1, baseObjects.size());
        assertEquals("base1", baseObjects.get(0).getId());
    }

    @Test
    public void testFindByArealWithPagination() {
        // Create and save ArealEntity first
        ArealEntity areal = new ArealEntity("areal1", 5, null, null, null, null);
        arealRepository.save(areal);

        // Create and save multiple ObjektEntity instances using a loop
        for (int i = 1; i <= 10; i++) {
            ObjektEntity obj = new ObjektEntity(
                    "objP" + i, 100 + i, 15, 1, true, 30, 120, 10, 20, 5, 15, true, 2,
                    null, null, true, false, false, true, false, true, true, true,
                    false, true, 5, 10, 15, "calendarId" + i, true, 30, areal,
                    null, null, null, null, null, null
            );

            String lexicographicalName = getLexicographicalName(i);

            // Create and save the corresponding ObjektLocEntity with language 'en'
            ObjektLocEntity loc = new ObjektLocEntity(
                    "loc" + i, "en", lexicographicalName + " Name", "Objekt " + i + " Description", "Short Name " + i
            );
            obj.setLocaleData(List.of(loc));
            repository.save(obj);

        }

        // Perform pagination queries
        List<ObjektEntity> page1 = repository.findByAreal("areal1", "en", 0, 5);  // Offset 0, MaxResults 5
        List<ObjektEntity> page2 = repository.findByAreal("areal1", "en", 5, 5);  // Offset 5, MaxResults 5
        List<ObjektEntity> page3 = repository.findByAreal("areal1", "en", 10, 5); // Offset 10, MaxResults 5

        // Verify the results for page 1
        assertNotNull(page1);
        assertEquals(5, page1.size());  // Should return 5 objects
        assertEquals("objP1", page1.get(0).getId());  // The first object should be obj1
        assertEquals("objP2", page1.get(1).getId());  // The first object should be obj1
        assertEquals("objP3", page1.get(2).getId());  // The first object should be obj1
        assertEquals("objP4", page1.get(3).getId());  // The first object should be obj1
        assertEquals("objP5", page1.get(4).getId());  // The last object in this page should be obj5

        // Verify the results for page 2
        assertNotNull(page2);
        assertEquals(5, page2.size());  // Should return 5 objects
        assertEquals("objP6", page2.get(0).getId());  // The first object should be obj6
        assertEquals("objP10", page2.get(4).getId());  // The last object in this page should be obj10

        // Verify the results for page 3
        assertNotNull(page3);
        assertTrue(page3.isEmpty());  // Should return 0 objects because there are only 10 objects total
    }
    private String getLexicographicalName(int index) {
        StringBuilder name = new StringBuilder();
        while (index > 0) {
            index--;
            name.insert(0, (char)('A' + (index % 26)));
            index /= 26;
        }
        return name.toString();
    }

    @Test
    public void testFindBaseByArealWithPagination() {
        // Create and save ArealEntity first
        ArealEntity areal = new ArealEntity("areal1", 5, null, null, null, null);
        arealRepository.save(areal);

        // Create and save multiple ObjektEntities with linked ObjektLocEntities
        for (int i = 1; i <= 10; i++) {
            ObjektEntity obj = new ObjektEntity(
                    "obj" + i, 100 + i, 15, 1, i % 2 == 0, 30, 120, 10, 20, 5, 15, true, 2,
                    null, null, true, false, false, true, false, true, true, true,
                    false, true, 5, 10, 15, "calendarId" + i, true, 30, areal,
                    null, null, null, null, null, null
            );

            // Create ObjektLocEntity and associate with ObjektEntity
            String lexicographicalName = getLexicographicalName(i);
            ObjektLocEntity loc = new ObjektLocEntity(
                    "loc" + i, "en", "Objekt " + lexicographicalName + " Name", "Objekt " + i + " Description", "Short Name " + i
            );

            obj.setLocaleData(List.of(loc));

            // Save ObjektEntity, which cascades and saves ObjektLocEntity
            repository.save(obj);
        }

        // Perform pagination queries
        List<ObjektEntity> page1 = repository.findBaseByAreal("areal1", "en", 0, 5);  // Offset 0, MaxResults 5
        List<ObjektEntity> page2 = repository.findBaseByAreal("areal1", "en", 5, 5);  // Offset 5, MaxResults 5
        List<ObjektEntity> page3 = repository.findBaseByAreal("areal1", "en", 10, 5); // Offset 10, MaxResults 5

        assertNotNull(page1);
        assertEquals(5, page1.size());
        assertEquals("obj1", page1.get(0).getId());
        assertEquals("obj3", page1.get(1).getId());
        assertEquals("obj5", page1.get(2).getId());
        assertEquals("obj7", page1.get(3).getId());
        assertEquals("obj9", page1.get(4).getId());

        assertNotNull(page2);
        assertTrue(page2.isEmpty());  // Should return no objects because there are only 5 objects with primyVstup = FALSE

        assertNotNull(page3);
        assertTrue(page3.isEmpty());  // Should return 0 objects because there are only 5 objects with primyVstup = FALSE
    }


    @Test
    public void testFindByPrimyVstupPagination() {
        // Create and save ArealEntity first
        ArealEntity areal = new ArealEntity("areal1", 5, null, null, null, null);
        arealRepository.save(areal);

        // Create and save multiple ObjektEntities with alternating primyVstup values
        for (int i = 1; i <= 10; i++) {
            ObjektEntity obj = new ObjektEntity(
                    "obj" + i, 100 + i, 15, 1, i % 2 == 0, 30, 120, 10, 20, 5, 15, true, 2,
                    null, null, true, false, false, true, false, true, true, true,
                    false, true, 5, 10, 15, "calendarId" + i, true, 30, areal,
                    null, null, null, null, null, null
            );

            // Save the ObjektEntity



            String lexicographicalName = getLexicographicalName(i);

            // Create and save ObjektLocEntity for each ObjektEntity
            String jazyk = (i % 2 == 0) ? "en" : "cz";  // Alternate between "en" and "cz"
            ObjektLocEntity loc = new ObjektLocEntity(
                    "loc" + i, jazyk, "Objekt " + lexicographicalName + " Name", "Objekt " + i + " Description", "Short Name " + i
            );
            obj.setLocaleData(List.of(loc));
            repository.save(obj);
        }

        // Perform pagination queries for primyVstup = true
        List<ObjektEntity> page1True = repository.findByPrimyVstup("en", 0, 3, true);  // Offset 0, MaxResults 5, primyVstup = true
        List<ObjektEntity> page2True = repository.findByPrimyVstup("en", 3, 5, true);  // Offset 5, MaxResults 5, primyVstup = true

        // Perform pagination queries for primyVstup = false
        List<ObjektEntity> page1False = repository.findByPrimyVstup("cz", 0, 3, false);  // Offset 0, MaxResults 5, primyVstup = false
        List<ObjektEntity> page2False = repository.findByPrimyVstup("cz", 3, 5, false);  // Offset 5, MaxResults 5, primyVstup = false

        // Verify the results for primyVstup = true
        assertNotNull(page1True);
        assertEquals(3, page1True.size());  // Should return 3 objects with primyVstup = true (obj2, obj4, obj6)
        assertEquals("obj2", page1True.get(0).getId());  // The first object should be obj2
        assertEquals("obj6", page1True.get(2).getId());  // The last object in this page should be obj6

        // Verify the results for primyVstup = true on page 2
        assertNotNull(page2True);
        assertEquals(2, page2True.size());  // Should return 2 objects with primyVstup = true (obj8, obj10)
        assertEquals("obj8", page2True.get(0).getId());  // The first object should be obj8
        assertEquals("obj10", page2True.get(1).getId());  // The last object should be obj10

        // Verify the results for primyVstup = false
        assertNotNull(page1False);
        assertEquals(3, page1False.size());  // Should return 2 objects with primyVstup = false (obj1, obj3)
        assertEquals("obj1", page1False.get(0).getId());  // The first object should be obj1
        assertEquals("obj3", page1False.get(1).getId());  // The last object in this page should be obj3

        // Verify the results for primyVstup = false on page 2
        assertNotNull(page2False);
        assertEquals(2, page2False.size());  // Should return 2 objects with primyVstup = false (obj5, obj7)
        assertEquals("obj7", page2False.get(0).getId());  // The first object should be obj5
        assertEquals("obj9", page2False.get(1).getId());  // The last object should be obj7
    }

    @Test
    public void testFindBySport() {
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();

        // Create SportEntity
        SportEntity sport = new SportEntity(
                "sport1", 1, "zbozi1", "sklad1", 100, true, 60, true, 0,
                null, 30, 120, true, 15, null, null, true, true, 10,
                90, 1, 5, 20, null, null, null, null, null, null, null, null, null
        );
        em.persist(sport);

        // Create ObjektEntity instances
        ObjektEntity objekt1 = new ObjektEntity(
                "obj1", 101, 15, 1, true, 30, 120, 10, 20, 5, 15, true, 2,
                null, null, true, false, false, true, false, true, true, true,
                false, true, 5, 10, 15, "calendarId1", true, 30, null,
                null, null, null, null, null, null
        );

        ObjektEntity objekt2 = new ObjektEntity(
                "obj2", 102, 15, 1, false, 30, 120, 10, 20, 5, 15, true, 2,
                null, null, true, false, false, true, false, true, true, true,
                false, true, 5, 10, 15, "calendarId2", true, 30, null,
                null, null, null, null, null, null
        );

        // Create and persist ObjektLocEntity for each ObjektEntity
        ObjektLocEntity loc1 = new ObjektLocEntity(
                "loc1", "en", "Objekt One Name", "Objekt 1 Description", "Short Name 1"
        );
        ObjektLocEntity loc2 = new ObjektLocEntity(
                "loc2", "en", "Objekt Two Name", "Objekt 2 Description", "Short Name 2"
        );

        // Associate ObjektLocEntity with ObjektEntity
        objekt1.setLocaleData(List.of(loc1));
        objekt2.setLocaleData(List.of(loc2));

        // Persist ObjektEntity (cascade will save ObjektLocEntity)
        em.persist(objekt1);
        em.persist(objekt2);

        // Create and persist ObjektSportEntity
        ObjektSportPK key1 = new ObjektSportPK("objSport1", 1);
        ObjektSportEntity objektSport1 = new ObjektSportEntity(key1, sport, objekt1);
        em.persist(objektSport1);

        ObjektSportPK key2 = new ObjektSportPK("objSport2", 2);
        ObjektSportEntity objektSport2 = new ObjektSportEntity(key2, sport, objekt2);
        em.persist(objektSport2);

        // Link ObjektSportEntity back to ObjektEntity
        objekt1.setObjektSports(List.of(objektSport1));
        objekt2.setObjektSports(List.of(objektSport2));

        em.getTransaction().commit();

        // Query by sport
        List<ObjektEntity> objects = repository.findBySport("sport1", "en");
        assertNotNull(objects);
        assertEquals(2, objects.size());
        assertTrue(objects.stream().anyMatch(o -> o.getId().equals("obj1")));
        assertTrue(objects.stream().anyMatch(o -> o.getId().equals("obj2")));
    }
}
