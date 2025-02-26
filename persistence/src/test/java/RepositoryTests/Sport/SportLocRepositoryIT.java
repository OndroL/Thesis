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
        SportLocEntity sportLoc = createSportLoc(null, "cs", "Fotbal", "Popis fotbalu");
        em.persist(sportLoc);
        em.flush();

        SportLocEntity result = em.find(SportLocEntity.class, sportLoc.getId());
        assertNotNull(result);
        assertEquals("Fotbal", result.getNazev());
    }

    @Test
    @Order(2)
    void testFindById() {
        SportLocEntity sportLoc = createSportLoc(null, "en", "Basketball", "Basketball description");
        em.persist(sportLoc);
        em.flush();

        SportLocEntity result = sportLocRepository.findById(sportLoc.getId()).orElse(null);
        assertNotNull(result);
        assertEquals("Basketball", result.getNazev());
    }

    @Test
    @Order(3)
    void testFindAll() {
        SportLocEntity sportLoc1 = createSportLoc(null, "cs", "Hokej", "Popis hokeje");
        SportLocEntity sportLoc2 = createSportLoc(null, "cs", "Tenis", "Popis tenisu");
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
        SportLocEntity sportLoc = createSportLoc(null, "cs", "Plavání", "Popis plavání");
        em.persist(sportLoc);
        em.flush();

        em.remove(sportLoc);
        em.flush();

        Optional<SportLocEntity> result = sportLocRepository.findById(sportLoc.getId());
        assertFalse(result.isPresent());
    }

    @Test
    @Order(5)
    void testUpdateSportLoc() {
        SportLocEntity sportLoc = createSportLoc(null, "cs", "Volejbal", "Popis volejbalu");
        em.persist(sportLoc);
        em.flush();

        SportLocEntity updated = em.find(SportLocEntity.class, sportLoc.getId());
        updated.setNazev("Beach Volejbal");
        em.merge(updated);
        em.flush();

        SportLocEntity result = em.find(SportLocEntity.class, sportLoc.getId());
        assertNotNull(result);
        assertEquals("Beach Volejbal", result.getNazev());
    }
}
