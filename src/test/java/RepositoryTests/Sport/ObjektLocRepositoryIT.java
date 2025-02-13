package RepositoryTests.Sport;

import RepositoryTests.DatabaseCleaner;
import cz.inspire.sport.entity.ObjektLocEntity;
import cz.inspire.sport.repository.ObjektLocRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Optional;

@Transactional
@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ObjektLocRepositoryIT {

    @Inject
    ObjektLocRepository objektLocRepository;

    @Inject
    DatabaseCleaner databaseCleaner;

    @BeforeAll
    @ActivateRequestContext
    public void clearDatabase() {
        databaseCleaner.clearTable(ObjektLocEntity.class, true);
    }

    @Order(1)
    @Test
    public void testSaveAndFindById() {
        ObjektLocEntity entity = new ObjektLocEntity("LOC-001", "cz", "Main building", "123 Street", "A");
        objektLocRepository.save(entity);

        Optional<ObjektLocEntity> retrieved = objektLocRepository.findById("LOC-001");
        Assertions.assertTrue(retrieved.isPresent(), "Entity should be present in repository.");
        Assertions.assertEquals("Main building", retrieved.get().getNazev(), "Name should match.");
    }

    @Order(2)
    @Test
    public void testFindAll() {
        ObjektLocEntity loc1 = new ObjektLocEntity("LOC-002", "Building B", "Secondary building", "456 Street", "B");
        ObjektLocEntity loc2 = new ObjektLocEntity("LOC-003", "Building C", "Tertiary building", "789 Street", "C");

        objektLocRepository.save(loc1);
        objektLocRepository.save(loc2);

        List<ObjektLocEntity> results = objektLocRepository.findAll().toList();
        Assertions.assertEquals(3, results.size(), "Expected 3 locations in repository.");
    }

    @Order(3)
    @Test
    public void testDeleteById() {
        ObjektLocEntity entity = new ObjektLocEntity("LOC-004", "Building D", "To be deleted", "101 Street","D");
        objektLocRepository.save(entity);
        objektLocRepository.deleteById("LOC-004");

        Optional<ObjektLocEntity> deleted = objektLocRepository.findById("LOC-004");
        Assertions.assertFalse(deleted.isPresent(), "Location should be deleted from repository.");
    }
}