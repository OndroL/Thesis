package RepositoryTests.Sport;

import RepositoryTests.DatabaseCleaner;
import cz.inspire.sport.entity.SportKategorieLocEntity;
import cz.inspire.sport.repository.SportKategorieLocRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SportKategorieLocRepositoryIT {

    @Inject
    SportKategorieLocRepository sportKategorieLocRepository;

    @Inject
    DatabaseCleaner databaseCleaner;

    @Inject
    EntityManager em;

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
        SportKategorieLocEntity entity = createSportKategorieLoc("SKL-001", "cs", "Fotbal", "Popis fotbalu");
        em.persist(entity);
        em.flush();

        Optional<SportKategorieLocEntity> result = sportKategorieLocRepository.findById("SKL-001");

        assertTrue(result.isPresent());
        assertEquals("Fotbal", result.get().getNazev());
    }

    @Test
    @Order(2)
    void testDeleteEntity() {
        SportKategorieLocEntity entity = createSportKategorieLoc("SKL-002", "en", "Basketball", "Description of basketball");
        em.persist(entity);
        em.flush();

        Optional<SportKategorieLocEntity> result = sportKategorieLocRepository.findById("SKL-002");
        assertTrue(result.isPresent());

        em.remove(entity);
        em.flush();

        result = sportKategorieLocRepository.findById("SKL-002");
        assertFalse(result.isPresent());
    }
}
