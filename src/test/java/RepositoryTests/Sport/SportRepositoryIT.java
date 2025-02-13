package RepositoryTests.Sport;

import RepositoryTests.DatabaseCleaner;
import cz.inspire.sport.entity.SportEntity;
import cz.inspire.sport.entity.SportKategorieEntity;
import cz.inspire.sport.entity.SportLocEntity;
import cz.inspire.sport.repository.SportRepository;
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
public class SportRepositoryIT {

    @Inject
    SportRepository sportRepository;

    @Inject
    DatabaseCleaner databaseCleaner;

    @Inject
    EntityManager em;

    @BeforeAll
    @ActivateRequestContext
    public void clearDatabase() {
        databaseCleaner.clearTable(SportEntity.class, true);
        databaseCleaner.clearTable(SportKategorieEntity.class, true);
    }

    private SportEntity createSport(String id, String name, SportKategorieEntity category, SportEntity parent) {
        SportEntity sport = new SportEntity(id, 1, "ZB-001", "SK-001", 100, true, 60, true, 10, null, 30, 120, true, 15, null, null, true, true, 10, 90, 1, 5, 20, null, null, null, null, null, null, null, null, null);
        sport.setSportKategorie(category);
        sport.setNadrazenySport(parent);
        sport.setLocaleData(List.of(new SportLocEntity(id + "-loc", "cs", name, "Description " + name)));
        return sport;
    }

    @Test
    @Order(1)
    void testFindAllOrdered() {
        SportEntity sport1 = createSport("SP-001", "Football", null, null);
        SportEntity sport2 = createSport("SP-002", "Basketball", null, null);
        em.persist(sport1);
        em.persist(sport2);
        em.flush();

        List<SportEntity> result = sportRepository.findAllOrdered();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    @Order(2)
    void testFindByParent() {
        SportEntity parent = createSport("SP-003", "Main Sport", null, null);
        SportEntity child = createSport("SP-004", "Sub Sport", null, parent);
        em.persist(parent);
        em.persist(child);
        em.flush();

        List<SportEntity> result = sportRepository.findByParent("SP-003", "cs");

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @Order(3)
    void testFindByCategory() {
        SportKategorieEntity category = new SportKategorieEntity("SK-001", "Facility-001", "Service-001", null, null, null, null);
        SportEntity sport = createSport("SP-005", "Category Sport", category, null);
        category.setCinnosti(List.of(sport));
        em.persist(category);
        em.persist(sport);
        em.flush();

        List<SportEntity> result = sportRepository.findByCategory("SK-001", Limit.of(1));

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @Order(4)
    void testFindByZbozi() {
        SportEntity sport = createSport("SP-006", "Zbozi Sport", null, null);
        sport.setZboziId("ZB-123");
        em.persist(sport);
        em.flush();

        List<SportEntity> result = sportRepository.findByZbozi("ZB-123", Limit.of(1));

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @Order(5)
    void testFindRoot() {
        SportEntity sport = createSport("SP-007", "Root Sport", null, null);
        sport.setLocaleData(List.of(new SportLocEntity("SP-007" + "-loc", "jp", "Root ID Sport", "Description " + "Root ID Sport")));
        em.persist(sport);
        em.flush();

        List<SportEntity> result = sportRepository.findRoot("jp");

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @Order(6)
    void testFindCategoryRoot() {
        SportEntity sport = createSport("SP-008", "Category Root", null, null);
        em.persist(sport);
        em.flush();

        List<SportEntity> result = sportRepository.findCategoryRoot(Limit.of(1));

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @Order(7)
    void testCountCategoryRoot() {
        Long initialCount = sportRepository.countCategoryRoot();
        SportEntity sport = createSport("SP-009", "Root Category Sport", null, null);
        em.persist(sport);
        em.flush();

        Long newCount = sportRepository.countCategoryRoot();
        assertEquals(initialCount + 1, newCount);
    }

    @Test
    @Order(8)
    void testCountAllByCategory() {
        SportKategorieEntity category = new SportKategorieEntity("SK-002", "Facility-002", "Service-002", null, null, null,null);
        SportEntity sport = createSport("SP-010", "Count Category Sport", category, null);
        category.setCinnosti(List.of(sport));
        em.persist(category);
        em.persist(sport);
        em.flush();

        Long count = sportRepository.countAllByCategory("SK-002");

        assertEquals(1, count);
    }

    @Test
    @Order(9)
    void testGetAllIdsByParentAndLanguage() {
        SportEntity parent = createSport("SP-011", "Parent Sport", null, null);
        SportEntity child = createSport("SP-012", "Child Sport", null, parent);
        em.persist(parent);
        em.persist(child);
        em.flush();

        List<String> result = sportRepository.getAllIdsByParentAndLanguage("SP-011", "cs");

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @Order(10)
    void testCountRootByLanguage() {
        Long initialCount = sportRepository.countRootByLanguage("cs");
        SportEntity sport = createSport("SP-013", "Root Language Sport", null, null);
        em.persist(sport);
        em.flush();

        Long newCount = sportRepository.countRootByLanguage("cs");
        assertEquals(initialCount + 1, newCount);
    }

    @Test
    @Order(11)
    void testGetRootIdsByLanguage() {
        SportEntity sport = createSport("SP-014", "Root ID Sport", null, null);
        sport.setLocaleData(List.of(new SportLocEntity("SP-014" + "-loc", "en", "Root ID Sport", "Description " + "Root ID Sport")));
        em.persist(sport);
        em.flush();

        List<String> result = sportRepository.getRootIdsByLanguage("en");

        assertNotNull(result);
        assertEquals(1, result.size());
    }
}
