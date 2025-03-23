package RepositoryTests.Sport;

import RepositoryTests.DatabaseCleaner;
import cz.inspire.sport.entity.SportEntity;
import cz.inspire.sport.entity.SportKategorieEntity;
import cz.inspire.sport.entity.SportLocEntity;
import cz.inspire.sport.repository.SportKategorieRepository;
import cz.inspire.sport.repository.SportRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SportRepositoryIT {

    @Inject
    SportRepository sportRepository;

    @Inject
    SportKategorieRepository sportKategoryRepository;

    @Inject
    DatabaseCleaner databaseCleaner;

    @BeforeAll
    @ActivateRequestContext
    public void clearDatabase() {
        databaseCleaner.clearTable(SportEntity.class, true);
        databaseCleaner.clearTable(SportKategorieEntity.class, true);
    }

    // Helper method: pass null for id so that it's generated automatically.
    private SportEntity createSport(String id, String name, SportKategorieEntity category, SportEntity parent) {
        SportEntity sport = new SportEntity(id, 1, "ZB-001", "SK-001", 100, true, 60, true, 10, null, 30, 120, true, 15, null, null, true, true, 10, 90, 1, 5, 20, null, null, null, null, null, null, null, null, null);
        sport.setSportKategorie(category);
        sport.setNadrazenySport(parent);
        // Create locale data with null ID for auto-generation.
        sport.setLocaleData(List.of(new SportLocEntity(null, "cs", name, "Description " + name)));
        return sport;
    }

    @Test
    @Order(1)
    void testFindAllOrdered() {
        SportEntity sport1 = createSport(null, "Football", null, null);
        SportEntity sport2 = createSport(null, "Basketball", null, null);
        sport1 = sportRepository.create(sport1);
        sport2 = sportRepository.create(sport2);

        List<SportEntity> result = sportRepository.findAll();
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    @Order(2)
    void testFindByParent() {
        SportEntity parent = createSport(null, "Main Sport", null, null);
        SportEntity child = createSport(null, "Sub Sport", null, parent);
        parent = sportRepository.create(parent);
        child = sportRepository.create(child);

        List<SportEntity> result = sportRepository.findByParent(parent.getId(), "cs");
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @Order(3)
    void testFindByCategory() {
        SportKategorieEntity category = new SportKategorieEntity(null, "Facility-001", "Service-001", null, null, null, null);
        SportEntity sport = createSport(null, "Category Sport", category, null);
        category.setCinnosti(List.of(sport));
        sportKategoryRepository.create(category);
        sport = sportRepository.create(sport);

        List<SportEntity> result = sportRepository.findByCategory(category.getId(), 1, 0);
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @Order(4)
    void testFindByZbozi() {
        SportEntity sport = createSport(null, "Zbozi Sport", null, null);
        sport.setZboziId("ZB-123");
        sport = sportRepository.create(sport);

        List<SportEntity> result = sportRepository.findByZbozi("ZB-123", 1, 0);
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @Order(5)
    void testFindRoot() {
        SportEntity sport = createSport(null, "Root Sport", null, null);
        sport.setLocaleData(List.of(new SportLocEntity(null, "jp", "Root ID Sport", "Description Root ID Sport")));
        sport = sportRepository.create(sport);

        List<SportEntity> result = sportRepository.findRoot("jp");
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @Order(6)
    void testFindCategoryRoot() {
        SportEntity sport = createSport(null, "Category Root", null, null);
        sport = sportRepository.create(sport);

        List<SportEntity> result = sportRepository.findCategoryRoot(1, 0);
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @Order(7)
    void testCountCategoryRoot() {
        Long initialCount = sportRepository.countCategoryRoot();
        SportEntity sport = createSport(null, "Root Category Sport", null, null);
        sport = sportRepository.create(sport);
        Long newCount = sportRepository.countCategoryRoot();
        assertEquals(initialCount + 1, newCount);
    }

    @Test
    @Order(8)
    void testCountAllByCategory() {
        SportKategorieEntity category = new SportKategorieEntity(null, "Facility-002", "Service-002", null, null, null, null);
        SportEntity sport = createSport(null, "Count Category Sport", category, null);
        category.setCinnosti(List.of(sport));
        sportKategoryRepository.create(category);
        sport = sportRepository.create(sport);

        Long count = sportRepository.countAllByCategory(category.getId());
        assertEquals(1, count);
    }

    @Test
    @Order(9)
    void testGetAllIdsByParentAndLanguage() {
        SportEntity parent = createSport(null, "Parent Sport", null, null);
        SportEntity child = createSport(null, "Child Sport", null, parent);
        parent = sportRepository.create(parent);
        child = sportRepository.create(child);

        List<String> result = sportRepository.getAllIdsByParentAndLanguage(parent.getId(), "cs");
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @Order(10)
    void testCountRootByLanguage() {
        Long initialCount = sportRepository.countRootByLanguage("cs");
        SportEntity sport = createSport(null, "Root Language Sport", null, null);
        sport = sportRepository.create(sport);
        Long newCount = sportRepository.countRootByLanguage("cs");
        assertEquals(initialCount + 1, newCount);
    }

    @Test
    @Order(11)
    void testGetRootIdsByLanguage() {
        SportEntity sport = createSport(null, "Root ID Sport", null, null);
        sport.setLocaleData(List.of(new SportLocEntity(null, "en", "Root ID Sport", "Description Root ID Sport")));
        sport = sportRepository.create(sport);

        List<String> result = sportRepository.getRootIdsByLanguage("en");
        assertNotNull(result);
        assertEquals(1, result.size());
    }
}
