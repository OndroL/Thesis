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
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import java.util.List;

@Transactional
@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
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
        // Create entity with null ID to allow automatic generation.
        ObjektLocEntity entity = new ObjektLocEntity(null, "cz", "Main building", "123 Street", "A");
        entity = objektLocRepository.create(entity);
        String generatedId = entity.getId();

        ObjektLocEntity retrieved = objektLocRepository.findByPrimaryKey(generatedId);
        Assertions.assertNotNull(retrieved, "Entity should be present in repository.");
        Assertions.assertEquals("Main building", retrieved.getNazev(), "Name should match.");
    }

    @Order(2)
    @Test
    public void testFindAll() {
        // Create two additional locations with null IDs.
        ObjektLocEntity loc1 = new ObjektLocEntity(null, "Building B", "Secondary building", "456 Street", "B");
        ObjektLocEntity loc2 = new ObjektLocEntity(null, "Building C", "Tertiary building", "789 Street", "C");

        objektLocRepository.create(loc1);
        objektLocRepository.create(loc2);

        List<ObjektLocEntity> results = objektLocRepository.findAll();
        // Expect 3 locations total (one from testSaveAndFindById plus these two).
        Assertions.assertEquals(3, results.size(), "Expected 3 locations in repository.");
    }

    @Order(3)
    @Test
    public void testDeleteById() {
        // Create an entity to be deleted with null ID.
        ObjektLocEntity entity = new ObjektLocEntity(null, "Building D", "To be deleted", "101 Street", "D");
        entity = objektLocRepository.create(entity);
        String generatedId = entity.getId();

        objektLocRepository.deleteByPrimaryKey(generatedId);

        ObjektLocEntity deleted = objektLocRepository.findByPrimaryKey(generatedId);
        Assertions.assertNull(deleted, "Location should be deleted from repository.");
    }
}
