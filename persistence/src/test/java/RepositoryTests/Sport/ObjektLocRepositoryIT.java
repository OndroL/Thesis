package RepositoryTests.Sport;

import RepositoryTests.DatabaseCleaner;
import cz.inspire.sport.entity.ObjektLocEntity;
import cz.inspire.sport.repository.ObjektLocRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.List;

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
        objektLocRepository.create(entity);

        ObjektLocEntity retrieved = objektLocRepository.findById("LOC-001");
        Assertions.assertNotNull(retrieved, "Entity should be present in repository.");
        Assertions.assertEquals("Main building", retrieved.getNazev(), "Name should match.");
    }

    @Order(2)
    @Test
    public void testFindAll() {
        ObjektLocEntity loc1 = new ObjektLocEntity("LOC-002", "Building B", "Secondary building", "456 Street", "B");
        ObjektLocEntity loc2 = new ObjektLocEntity("LOC-003", "Building C", "Tertiary building", "789 Street", "C");

        objektLocRepository.create(loc1);
        objektLocRepository.create(loc2);

        List<ObjektLocEntity> results = objektLocRepository.findAll();
        Assertions.assertEquals(3, results.size(), "Expected 3 locations in repository.");
    }

    @Order(3)
    @Test
    public void testDeleteById() {
        ObjektLocEntity entity = new ObjektLocEntity("LOC-004", "Building D", "To be deleted", "101 Street","D");
        objektLocRepository.create(entity);
        objektLocRepository.deleteById("LOC-004");

        ObjektLocEntity deleted = objektLocRepository.findById("LOC-004");
        Assertions.assertNull(deleted, "Location should be deleted from repository.");
    }
}