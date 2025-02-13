package RepositoryTests.Sport;

import RepositoryTests.DatabaseCleaner;
import cz.inspire.sport.entity.SportLocEntity;
import cz.inspire.sport.repository.SportLocRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SportLocRepositoryIT {

    @Inject
    SportLocRepository sportLocRepository;

    @Inject
    EntityManager em;

    @Inject
    DatabaseCleaner databaseCleaner;

    @BeforeAll
    @ActivateRequestContext
    public void clearDatabase() {
        databaseCleaner.clearTable(SportLocEntity.class, true);
    }

    private SportLocEntity createSportLoc(String id, String jazyk, String nazev, String popis) {
        SportLocEntity sportLoc = new SportLocEntity();
        sportLoc.setId(id);
        sportLoc.setJazyk(jazyk);
        sportLoc.setNazev(nazev);
        sportLoc.setPopis(popis);
        return sportLoc;
    }

    @Test
    @Order(1)
    void testCreateSportLoc() {
        SportLocEntity sportLoc = createSportLoc("sport-001", "cs", "Fotbal", "Popis fotbalu");
        em.persist(sportLoc);
        em.flush();

        SportLocEntity result = em.find(SportLocEntity.class, "sport-001");
        assertNotNull(result);
        assertEquals("Fotbal", result.getNazev());
    }

    @Test
    @Order(2)
    void testFindById() {
        SportLocEntity sportLoc = createSportLoc("sport-002", "en", "Basketball", "Basketball description");
        em.persist(sportLoc);
        em.flush();

        SportLocEntity result = sportLocRepository.findById("sport-002").orElse(null);
        assertNotNull(result);
        assertEquals("Basketball", result.getNazev());
    }

    @Test
    @Order(3)
    void testFindAll() {
        SportLocEntity sportLoc1 = createSportLoc("sport-003", "cs", "Hokej", "Popis hokeje");
        SportLocEntity sportLoc2 = createSportLoc("sport-004", "cs", "Tenis", "Popis tenisu");
        em.persist(sportLoc1);
        em.persist(sportLoc2);
        em.flush();

        List<SportLocEntity> result = sportLocRepository.findAll().toList();
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    @Order(4)
    void testDeleteById() {
        SportLocEntity sportLoc = createSportLoc("sport-005", "cs", "Plavání", "Popis plavání");
        em.persist(sportLoc);
        em.flush();

        em.remove(sportLoc);
        em.flush();

        Optional<SportLocEntity> result = sportLocRepository.findById("sport-005");
        assertFalse(result.isPresent());
    }

    @Test
    @Order(5)
    void testUpdateSportLoc() {
        SportLocEntity sportLoc = createSportLoc("sport-006", "cs", "Volejbal", "Popis volejbalu");
        em.persist(sportLoc);
        em.flush();

        SportLocEntity updated = em.find(SportLocEntity.class, "sport-006");
        updated.setNazev("Beach Volejbal");
        em.merge(updated);
        em.flush();

        SportLocEntity result = em.find(SportLocEntity.class, "sport-006");
        assertNotNull(result);
        assertEquals("Beach Volejbal", result.getNazev());
    }
}
