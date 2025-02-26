package RepositoryTests.Sport;

import RepositoryTests.DatabaseCleaner;
import cz.inspire.sport.entity.*;
import cz.inspire.sport.repository.SportKategorieRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.data.Limit;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SportKategorieRepositoryIT {

    @Inject
    SportKategorieRepository sportKategorieRepository;

    @Inject
    DatabaseCleaner databaseCleaner;

    @Inject
    EntityManager em;

    @BeforeAll
    @ActivateRequestContext
    public void clearDatabase() {
        databaseCleaner.clearTable(SportKategorieEntity.class, true);
        databaseCleaner.clearTable(SportKategorieLocEntity.class, true);
    }

    private SportKategorieEntity createSportKategorie(String id, String name, SportKategorieEntity parent) {
        SportKategorieEntity kategorie = new SportKategorieEntity(id, "Facility-001", "Service-001", parent, null, null, null);
        SportKategorieLocEntity locale = new SportKategorieLocEntity(id, "cs", name, "Description " + name);
        kategorie.setLocaleData(List.of(locale));
        return kategorie;
    }

    @Test
    @Order(1)
    void testFindAllOrdered() {
        SportKategorieEntity category1 = createSportKategorie(null, "Football", null);
        SportKategorieEntity category2 = createSportKategorie(null, "Basketball", null);
        em.persist(category1);
        em.persist(category2);
        em.flush();

        List<SportKategorieEntity> result = sportKategorieRepository.findAllOrdered();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Basketball", result.getFirst().getLocaleData().getFirst().getNazev());
    }

    @Test
    @Order(2)
    void testFindRoot() {
        SportKategorieEntity category = createSportKategorie(null, "Tennis", null);
        em.persist(category);
        em.flush();

        List<SportKategorieEntity> result = sportKategorieRepository.findRoot();

        assertNotNull(result);
        assertTrue(result.stream().anyMatch(c -> c.getId().equals(category.getId())));
    }

    @Test
    @Order(3)
    void testFindAllByNadrazenaKategorie() {
        SportKategorieEntity parent = createSportKategorie(null, "Main Category", null);
        SportKategorieEntity child = createSportKategorie(null, "Sub Category", parent);
        em.persist(parent);
        em.persist(child);
        em.flush();

        List<SportKategorieEntity> result = sportKategorieRepository.findAllByNadrazenaKategorie(parent.getId());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(child.getId(), result.getFirst().getId());
    }

    @Test
    @Order(4)
    void testFindAllWithLimit() {
        SportKategorieEntity category1 = createSportKategorie(null, "Swimming", null);
        SportKategorieEntity category2 = createSportKategorie(null, "Running", null);
        em.persist(category1);
        em.persist(category2);
        em.flush();

        List<SportKategorieEntity> result = sportKategorieRepository.findAll(Limit.of(1));

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @Order(5)
    void testFindAllByMultisportFacilityId() {
        int initialCount = sportKategorieRepository.findAllByMultisportFacilityId("Facility-001").size();
        SportKategorieEntity category = createSportKategorie(null, "Yoga", null);
        em.persist(category);
        em.flush();

        List<SportKategorieEntity> result = sportKategorieRepository.findAllByMultisportFacilityId("Facility-001");

        assertNotNull(result);
        assertEquals(initialCount + 1, result.size());
    }

    @Test
    @Order(6)
    void testCount() {
        Long initialCount = sportKategorieRepository.count();
        SportKategorieEntity category = createSportKategorie(null, "Golf", null);
        em.persist(category);
        em.flush();

        Long newCount = sportKategorieRepository.count();
        assertEquals(initialCount + 1, newCount);
    }

    @Test
    @Order(7)
    void testCountRoot() {
        Long initialCount = sportKategorieRepository.countRoot();
        SportKategorieEntity category = createSportKategorie(null, "Badminton", null);
        em.persist(category);
        em.flush();

        Long newCount = sportKategorieRepository.countRoot();
        assertEquals(initialCount + 1, newCount);
    }

    @Test
    @Order(8)
    void testCountByNadrazenaKategorie() {
        SportKategorieEntity parent = createSportKategorie(null, "Main Category 2", null);
        SportKategorieEntity child = createSportKategorie(null, "Sub Category 2", parent);
        em.persist(parent);
        em.persist(child);
        em.flush();

        Long count = sportKategorieRepository.countByNadrazenaKategorie(parent.getId());

        assertEquals(1, count);
    }
}
