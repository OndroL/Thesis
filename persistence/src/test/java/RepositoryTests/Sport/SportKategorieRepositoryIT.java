package RepositoryTests.Sport;

import RepositoryTests.DatabaseCleaner;
import cz.inspire.sport.entity.SportKategorieEntity;
import cz.inspire.sport.entity.SportKategorieLocEntity;
import cz.inspire.sport.repository.SportKategorieRepository;
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
public class SportKategorieRepositoryIT {

    @Inject
    SportKategorieRepository sportKategorieRepository;

    @Inject
    DatabaseCleaner databaseCleaner;

    @BeforeAll
    @ActivateRequestContext
    public void clearDatabase() {
        databaseCleaner.clearTable(SportKategorieEntity.class, true);
        databaseCleaner.clearTable(SportKategorieLocEntity.class, true);
    }

    // Helper method: pass null for ID so that it is generated.
    private SportKategorieEntity createSportKategorie(String id, String name, SportKategorieEntity parent) {
        // Create a SportKategorieEntity with null ID to trigger automatic generation.
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
        category1 = sportKategorieRepository.create(category1);
        category2 = sportKategorieRepository.create(category2);

        List<SportKategorieEntity> result = sportKategorieRepository.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        // Assuming that ordering is based on locale data name ascending.
        assertEquals("Basketball", result.get(0).getLocaleData().get(0).getNazev());
    }

    @Test
    @Order(2)
    void testFindRoot() {
        SportKategorieEntity category = createSportKategorie(null, "Tennis", null);
        category = sportKategorieRepository.create(category);

        List<SportKategorieEntity> result = sportKategorieRepository.findRoot();

        assertNotNull(result);
        SportKategorieEntity finalCategory = category;
        assertTrue(result.stream().anyMatch(c -> c.getId().equals(finalCategory.getId())));
    }

    @Test
    @Order(3)
    void testFindAllByNadrazenaKategorie() {
        SportKategorieEntity parent = createSportKategorie(null, "Main Category", null);
        SportKategorieEntity child = createSportKategorie(null, "Sub Category", parent);
        parent = sportKategorieRepository.create(parent);
        child = sportKategorieRepository.create(child);

        List<SportKategorieEntity> result = sportKategorieRepository.findAllByNadrazenaKategorie(parent.getId());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(child.getId(), result.get(0).getId());
    }

    @Test
    @Order(4)
    void testFindAllWithLimit() {
        SportKategorieEntity category1 = createSportKategorie(null, "Swimming", null);
        SportKategorieEntity category2 = createSportKategorie(null, "Running", null);
        category1 = sportKategorieRepository.create(category1);
        category2 = sportKategorieRepository.create(category2);

        List<SportKategorieEntity> result = sportKategorieRepository.findAll(1, 0);
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @Order(5)
    void testFindAllByMultisportFacilityId() {
        int initialCount = sportKategorieRepository.findAllByMultisportFacilityId("Facility-001").size();
        SportKategorieEntity category = createSportKategorie(null, "Yoga", null);
        category = sportKategorieRepository.create(category);

        List<SportKategorieEntity> result = sportKategorieRepository.findAllByMultisportFacilityId("Facility-001");

        assertNotNull(result);
        assertEquals(initialCount + 1, result.size());
    }

    @Test
    @Order(6)
    void testCount() {
        Long initialCount = sportKategorieRepository.count();
        SportKategorieEntity category = createSportKategorie(null, "Golf", null);
        category = sportKategorieRepository.create(category);

        Long newCount = sportKategorieRepository.count();
        assertEquals(initialCount + 1, newCount);
    }

    @Test
    @Order(7)
    void testCountRoot() {
        Long initialCount = sportKategorieRepository.countRoot();
        SportKategorieEntity category = createSportKategorie(null, "Badminton", null);
        category = sportKategorieRepository.create(category);

        Long newCount = sportKategorieRepository.countRoot();
        assertEquals(initialCount + 1, newCount);
    }

    @Test
    @Order(8)
    void testCountByNadrazenaKategorie() {
        SportKategorieEntity parent = createSportKategorie(null, "Main Category 2", null);
        SportKategorieEntity child = createSportKategorie(null, "Sub Category 2", parent);
        parent = sportKategorieRepository.create(parent);
        child = sportKategorieRepository.create(child);

        Long count = sportKategorieRepository.countByNadrazenaKategorie(parent.getId());
        assertEquals(1, count);
    }
}
