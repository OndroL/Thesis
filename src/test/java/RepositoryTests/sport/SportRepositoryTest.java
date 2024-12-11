package RepositoryTests.sport;

import cz.inspire.thesis.data.EntityManagerProducer;
import cz.inspire.thesis.data.model.sport.activity.ActivityEntity;
import cz.inspire.thesis.data.model.sport.sport.*;
import cz.inspire.thesis.data.repository.sport.sport.SportRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class SportRepositoryTest {

    private SportRepository sportRepository;

    @Before
    public void setUp() {
        CdiContainer container = CdiContainerLoader.getCdiContainer();
        container.boot();

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");

        EntityManagerProducer producer = BeanProvider.getContextualReference(EntityManagerProducer.class);
        producer.setEntityManagerFactory(emf);

        sportRepository = BeanProvider.getContextualReference(SportRepository.class);

        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.createQuery("DELETE FROM SportInstructorEntity").executeUpdate();
        em.createQuery("DELETE FROM SportEntity").executeUpdate();
        em.createQuery("DELETE FROM ActivityEntity").executeUpdate();
        em.createQuery("DELETE FROM SportKategorieEntity").executeUpdate();
        em.createQuery("DELETE FROM SportLocEntity").executeUpdate();
        em.getTransaction().commit();
    }

    private SportKategorieEntity createAndPersistCategory(EntityManager em) {
        SportKategorieEntity category = new SportKategorieEntity(
                "category1", "facility1", "serviceUUID1", null, Collections.emptyList(), Collections.emptyList(), Collections.emptyList()
        );
        em.persist(category);
        return category;
    }

    private ActivityEntity createAndPersistActivity(EntityManager em) {
        ActivityEntity activity = new ActivityEntity("activity1", "Yoga", "Relaxing yoga session", 1, "icon1", null, null);
        em.persist(activity);
        return activity;
    }

    private SportLocEntity createAndPersistLocale(EntityManager em) {
        SportLocEntity locale = new SportLocEntity("locale1", "en", "Sport Name", "Sport Description");
        em.persist(locale);
        return locale;
    }

    private SportEntity createSportEntity(String id, SportKategorieEntity category, ActivityEntity activity, SportLocEntity locale, SportEntity parent) {
        return new SportEntity(
                id, 1, "zbozi1", "sklad1", 100, true, 60, true, 0,
                null, 30, 120, true, 15, null, null, true, true, 10,
                90, 1, 5, 20, List.of(locale), category, parent, null, null,
                activity, null, null
        );
    }

    private SportLocEntity createAndPersistLocale(EntityManager em, String localeId, String language, String name) {
        SportLocEntity locale = new SportLocEntity(localeId, language, name, "Sport Description");
        em.persist(locale);
        return locale;
    }

    @Test
    public void testFindAll() {
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();

        SportKategorieEntity category = createAndPersistCategory(em);
        ActivityEntity activity = createAndPersistActivity(em);
        SportLocEntity locale = createAndPersistLocale(em);

        SportEntity sport = createSportEntity("sport1", category, activity, locale, null);
        em.persist(sport);

        em.getTransaction().commit();

        List<SportEntity> sports = sportRepository.findAll();
        assertNotNull(sports);
        assertEquals(1, sports.size());
        assertEquals("sport1", sports.get(0).getId());
    }

    @Test
    public void testFindByParent() {
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();

        SportKategorieEntity category = createAndPersistCategory(em);
        ActivityEntity activity = createAndPersistActivity(em);
        SportLocEntity locale = createAndPersistLocale(em);

        SportEntity parentSport = createSportEntity("parentSport", category, activity, locale, null);
        em.persist(parentSport);

        SportEntity childSport = createSportEntity("childSport", category, activity, locale, parentSport);
        em.persist(childSport);

        em.getTransaction().commit();

        List<SportEntity> sports = sportRepository.findByParent("parentSport", "en");
        assertNotNull(sports);
        assertEquals(1, sports.size());
        assertEquals("childSport", sports.get(0).getId());
    }

    @Test
    public void testFindByCategory() {
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();

        SportKategorieEntity category = createAndPersistCategory(em);
        ActivityEntity activity = createAndPersistActivity(em);
        SportLocEntity locale = createAndPersistLocale(em);

        SportEntity sport = createSportEntity("sport1", category, activity, locale, null);
        em.persist(sport);

        em.getTransaction().commit();

        List<SportEntity> sports = sportRepository.findByCategory("category1", 0, 10);
        assertNotNull(sports);
        assertEquals(1, sports.size());
        assertEquals("category1", sports.get(0).getSportKategorie().getId());
    }

    @Test
    public void testFindByZbozi() {
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();

        SportKategorieEntity category = createAndPersistCategory(em);
        ActivityEntity activity = createAndPersistActivity(em);
        SportLocEntity locale = createAndPersistLocale(em);

        SportEntity sport = createSportEntity("sport1", category, activity, locale, null);
        em.persist(sport);

        em.getTransaction().commit();

        List<SportEntity> sports = sportRepository.findByZbozi("zbozi1", 0, 10);
        assertNotNull(sports);
        assertEquals(1, sports.size());
        assertEquals("zbozi1", sports.get(0).getZboziId());
    }

    @Test
    public void testFindRoot() {
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();

        SportKategorieEntity category = createAndPersistCategory(em);
        ActivityEntity activity = createAndPersistActivity(em);
        SportLocEntity locale = createAndPersistLocale(em);

        SportEntity rootSport = createSportEntity("rootSport", category, activity, locale, null);
        em.persist(rootSport);

        em.getTransaction().commit();

        List<SportEntity> sports = sportRepository.findRoot("en");
        assertNotNull(sports);
        assertEquals(1, sports.size());
        assertEquals("rootSport", sports.get(0).getId());
    }

    @Test
    public void testFindCategoryRoot() {
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();

        // Create locale data
        SportLocEntity localeForSport1 = new SportLocEntity("locale1", "en", "Test Sport 1", "Description 1");
        em.persist(localeForSport1);

        SportLocEntity localeForSport2 = new SportLocEntity("locale2", "en", "Test Sport 2", "Description 2");
        em.persist(localeForSport2);
        ActivityEntity activity = createAndPersistActivity(em);
        SportKategorieEntity category = createAndPersistCategory(em);


        // Create and persist a sport without a category, but with valid locale data
        SportEntity sportWithoutCategory = createSportEntity("sport1", null, activity, localeForSport1, null);
        em.persist(sportWithoutCategory);

        // Create a sport with a category (should not be returned)
        SportEntity sportWithCategory = createSportEntity("sport2", category, activity, localeForSport2, null);
        em.persist(sportWithCategory);

        em.getTransaction().commit();


        // Verify all sports are persisted
        List<SportEntity> allSports = sportRepository.findAll();
        assertNotNull(allSports);
        assertEquals(2, allSports.size());
        assertNotNull(allSports.getFirst().getLocaleData());
        assertEquals(1, allSports.get(1).getLocaleData().size());
        assertEquals(1, allSports.getFirst().getLocaleData().size());
        assertNull(allSports.getFirst().getSportKategorie());

        // Query for sports without categories
        List<SportEntity> sports = sportRepository.findCategoryRoot(0, 10);
        assertNotNull(sports);
        assertEquals(1, sports.size());
        assertEquals("sport1", sports.get(0).getId());
        assertNull(sports.get(0).getSportKategorie());
    }
    @Test
    public void testFindRootWithOffsetAndLimit() {
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();

        SportLocEntity locale1 = createAndPersistLocale(em, "locale1", "en", "Root Sport 1");
        SportLocEntity locale2 = createAndPersistLocale(em, "locale2", "en", "Root Sport 2");
        SportLocEntity locale3 = createAndPersistLocale(em, "locale3", "en", "Root Sport 3");
        SportKategorieEntity category = createAndPersistCategory(em);
        ActivityEntity activity = createAndPersistActivity(em);

        // Create and persist root sports
        SportEntity rootSport1 = createSportEntity("sport1", category, activity, locale1, null);
        em.persist(rootSport1);

        SportEntity rootSport2 = createSportEntity("sport2", category, activity, locale2, null);
        em.persist(rootSport2);

        SportEntity rootSport3 = createSportEntity("sport3", category, activity, locale3, null);
        em.persist(rootSport3);

        em.getTransaction().commit();

        // Query with offset and limit
        List<SportEntity> sports = sportRepository.findRoot("en", 1, 1);
        assertNotNull(sports);
        assertEquals(1, sports.size());
        assertEquals("sport2", sports.get(0).getId());
    }

    @Test
    public void testFindByParentWithOffsetAndLimit() {
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();

        SportLocEntity locale1 = createAndPersistLocale(em, "locale1", "en", "Child Sport 1");
        SportLocEntity locale2 = createAndPersistLocale(em, "locale2", "en", "Child Sport 2");
        SportLocEntity locale3 = createAndPersistLocale(em, "locale3", "en", "Child Sport 3");
        SportKategorieEntity category = createAndPersistCategory(em);
        ActivityEntity activity = createAndPersistActivity(em);

        // Create and persist parent sport
        SportEntity parentSport = createSportEntity("parentSport", category, activity, locale1, null);
        em.persist(parentSport);

        // Create and persist child sports
        SportEntity childSport1 = createSportEntity("sport2", category, activity, locale1, parentSport);
        em.persist(childSport1);

        SportEntity childSport2 = createSportEntity("sport3", category, activity, locale2, parentSport);
        em.persist(childSport2);

        SportEntity childSport3 = createSportEntity("sport4", category, activity, locale3, parentSport);
        em.persist(childSport3);

        em.getTransaction().commit();

        // Query with offset and limit
        List<SportEntity> sports = sportRepository.findByParent("parentSport", "en", 1, 1);
        assertNotNull(sports);
        assertEquals(1, sports.size());
        assertEquals("sport3", sports.get(0).getId());
    }
}
