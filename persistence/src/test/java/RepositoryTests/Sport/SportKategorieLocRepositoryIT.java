package RepositoryTests.Sport;

import RepositoryTests.DatabaseCleaner;
import cz.inspire.sport.entity.SportKategorieLocEntity;
import cz.inspire.sport.repository.SportKategorieLocRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SportKategorieLocRepositoryIT {

    @Inject
    SportKategorieLocRepository sportKategorieLocRepository;

    @Inject
    DatabaseCleaner databaseCleaner;

    @BeforeAll
    @ActivateRequestContext
    public void clearDatabase() {
        databaseCleaner.clearTable(SportKategorieLocEntity.class, true);
    }

    private SportKategorieLocEntity createSportKategorieLoc(String id, String jazyk, String nazev, String popis) {
        return new SportKategorieLocEntity(id, jazyk, nazev, popis);
    }

    @Test
    @Order(1)
    void testSaveAndFindById() {
        // Create entity with null ID to allow automatic generation.
        SportKategorieLocEntity entity = createSportKategorieLoc(null, "cs", "Fotbal", "Popis fotbalu");
        entity = sportKategorieLocRepository.create(entity);

        SportKategorieLocEntity result = sportKategorieLocRepository.findById(entity.getId());
        assertNotNull(result, "Entity should be present in repository.");
        assertEquals("Fotbal", result.getNazev(), "Name should match.");
    }

    @Test
    @Order(2)
    void testDeleteEntity() {
        // Create entity with null ID so that it is generated.
        SportKategorieLocEntity entity = createSportKategorieLoc(null, "en", "Basketball", "Description of basketball");
        entity = sportKategorieLocRepository.create(entity);

        SportKategorieLocEntity result = sportKategorieLocRepository.findById(entity.getId());
        assertNotNull(result, "Entity should be present in repository.");

        sportKategorieLocRepository.deleteById(entity.getId());

        result = sportKategorieLocRepository.findById(entity.getId());
        assertNull(result, "Entity should be deleted from repository.");
    }
}
