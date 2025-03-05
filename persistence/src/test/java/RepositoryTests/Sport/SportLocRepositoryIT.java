package RepositoryTests.Sport;

import RepositoryTests.DatabaseCleaner;
import cz.inspire.sport.entity.SportLocEntity;
import cz.inspire.sport.repository.SportLocRepository;
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
public class SportLocRepositoryIT {

    @Inject
    SportLocRepository sportLocRepository;

    @Inject
    DatabaseCleaner databaseCleaner;

    @BeforeAll
    @ActivateRequestContext
    public void clearDatabase() {
        databaseCleaner.clearTable(SportLocEntity.class, true);
    }

    private SportLocEntity createSportLoc(String id, String jazyk, String nazev, String popis) {
        SportLocEntity sportLoc = new SportLocEntity();
        sportLoc.setId(id); // We'll pass null to have it generated.
        sportLoc.setJazyk(jazyk);
        sportLoc.setNazev(nazev);
        sportLoc.setPopis(popis);
        return sportLoc;
    }

    @Order(1)
    @Test
    void testCreateSportLoc() {
        // Use repository.create() instead of em.persist()/flush()
        SportLocEntity sportLoc = createSportLoc(null, "cs", "Fotbal", "Popis fotbalu");
        sportLoc = sportLocRepository.create(sportLoc);

        SportLocEntity result = sportLocRepository.findById(sportLoc.getId());
        assertNotNull(result, "Entity should be present.");
        assertEquals("Fotbal", result.getNazev(), "Name should match.");
    }

    @Order(2)
    @Test
    void testFindById() {
        SportLocEntity sportLoc = createSportLoc(null, "en", "Basketball", "Basketball description");
        sportLoc = sportLocRepository.create(sportLoc);

        SportLocEntity result = sportLocRepository.findById(sportLoc.getId());
        assertNotNull(result, "Entity should be found.");
        assertEquals("Basketball", result.getNazev(), "Name should match.");
    }

    @Order(3)
    @Test
    void testFindAll() {
        SportLocEntity sportLoc1 = createSportLoc(null, "cs", "Hokej", "Popis hokeje");
        SportLocEntity sportLoc2 = createSportLoc(null, "cs", "Tenis", "Popis tenisu");

        sportLoc1 = sportLocRepository.create(sportLoc1);
        sportLoc2 = sportLocRepository.create(sportLoc2);

        List<SportLocEntity> result = sportLocRepository.findAll();
        assertNotNull(result, "Result should not be null.");
        assertEquals(2, result.size(), "Expected 2 entries.");
    }

    @Order(4)
    @Test
    void testDeleteById() {
        SportLocEntity sportLoc = createSportLoc(null, "cs", "Plavání", "Popis plavání");
        sportLoc = sportLocRepository.create(sportLoc);

        sportLocRepository.deleteById(sportLoc.getId());

        SportLocEntity result = sportLocRepository.findById(sportLoc.getId());
        assertNull(result, "Entity should be deleted.");
    }

    @Order(5)
    @Test
    void testUpdateSportLoc() {
        SportLocEntity sportLoc = createSportLoc(null, "cs", "Volejbal", "Popis volejbalu");
        sportLoc = sportLocRepository.create(sportLoc);

        // Retrieve, update, and persist again via repository.create()
        SportLocEntity updated = sportLocRepository.findById(sportLoc.getId());
        updated.setNazev("Beach Volejbal");
        updated = sportLocRepository.create(updated);

        SportLocEntity result = sportLocRepository.findById(sportLoc.getId());
        assertNotNull(result, "Updated entity should be present.");
        assertEquals("Beach Volejbal", result.getNazev(), "Updated name should match.");
    }
}
