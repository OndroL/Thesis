package RepositoryTests.Common;

import cz.inspire.common.entity.HeaderEntity;
import cz.inspire.common.repository.HeaderRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional
@QuarkusTest
public class HeaderRepositoryIT {

    @Inject
    HeaderRepository headerRepository;

    /**
     * Clears all records before each test to ensure isolation.
     */
    @BeforeEach
    public void clearDatabase() {
        List<HeaderEntity> allEntities = new ArrayList<>();
        headerRepository.findAll().forEach(allEntities::add);
        if (!allEntities.isEmpty()) {
            headerRepository.deleteAll(allEntities);
        }
    }

    /**
     * Verifies that an entity can be saved and then retrieved by its ID.
     */
    @Test
    public void testSaveAndFindById() {
        HeaderEntity entity = new HeaderEntity("ID-002", 456, 20);
        headerRepository.save(entity);

        Optional<HeaderEntity> retrieved = headerRepository.findById("ID-002");
        Assertions.assertTrue(retrieved.isPresent(), "Entity should be present in repository.");
        Assertions.assertEquals(456, retrieved.get().getField(), "Field value should match.");
        Assertions.assertEquals(20, retrieved.get().getLocation(), "Location value should match.");
    }

    /**
     * Verifies that updating an existing entity changes its values.
     */
    @Test
    public void testUpdateEntity() {
        HeaderEntity entity = new HeaderEntity("ID-003", 789, 30);
        headerRepository.save(entity);

        entity.setField(111);
        entity.setLocation(40);
        headerRepository.save(entity);

        Optional<HeaderEntity> updated = headerRepository.findById("ID-003");
        Assertions.assertTrue(updated.isPresent(), "Entity should be present after update.");
        Assertions.assertEquals(111, updated.get().getField(), "Updated field value should be 111.");
        Assertions.assertEquals(40, updated.get().getLocation(), "Updated location value should be 40.");
    }

    /**
     * Verifies that an entity can be deleted.
     */
    @Test
    public void testDeleteEntity() {
        HeaderEntity entity = new HeaderEntity("ID-004", 222, 50);
        headerRepository.save(entity);

        headerRepository.deleteById("ID-004");
        Optional<HeaderEntity> deleted = headerRepository.findById("ID-004");
        Assertions.assertFalse(deleted.isPresent(), "Entity should be deleted from repository.");
    }

    /**
     * Tests the custom query 'findValidAttributes()' for simple entity.
     */
    @Test
    public void testSaveAndFindValidAttributes() {
        // Create a new entity
        HeaderEntity entity = new HeaderEntity("ID-001", 123, 10);

        HeaderEntity saved = headerRepository.save(entity);
        Assertions.assertNotNull(saved, "Expected a non-null returned entity from save()");

        List<HeaderEntity> results = headerRepository.findValidAttributes();
        Assertions.assertFalse(results.isEmpty(), "Expected to find at least one entity");

        HeaderEntity found = results.stream()
                .filter(e -> "ID-001".equals(e.getId()))
                .findFirst()
                .orElse(null);

        Assertions.assertNotNull(found, "Expected to find entity with ID=ID-001");
        Assertions.assertEquals(123, found.getField());
        Assertions.assertEquals(10, found.getLocation());
    }

    /**
     * Tests the custom query 'findValidAttributes()' for multiple entities.
     * Only entities with a non-negative location should be returned and ordered by location ascending.
     */
    @Test
    public void testFindValidAttributesOrdering() {
        HeaderEntity e1 = new HeaderEntity("ID-005", 10, 30);
        HeaderEntity e2 = new HeaderEntity("ID-006", 20, -10);
        HeaderEntity e3 = new HeaderEntity("ID-007", 30, 20);
        HeaderEntity e4 = new HeaderEntity("ID-008", 40, 40);

        headerRepository.save(e1);
        headerRepository.save(e2);
        headerRepository.save(e3);
        headerRepository.save(e4);

        List<HeaderEntity> validEntities = headerRepository.findValidAttributes();
        Assertions.assertEquals(3, validEntities.size(), "Expected 3 valid entities.");

        Assertions.assertEquals("ID-007", validEntities.get(0).getId(), "First entity should have ID=ID-007.");
        Assertions.assertEquals("ID-005", validEntities.get(1).getId(), "Second entity should have ID=ID-005.");
        Assertions.assertEquals("ID-008", validEntities.get(2).getId(), "Third entity should have ID=ID-008.");
    }

    /**
     * Verifies that the custom query returns an empty list if no entities meet the criteria.
     */
    @Test
    public void testFindValidAttributesWithNoValidRecords() {
        HeaderEntity e1 = new HeaderEntity("ID-009", 10, -5);
        HeaderEntity e2 = new HeaderEntity("ID-010", 20, -15);
        headerRepository.save(e1);
        headerRepository.save(e2);

        List<HeaderEntity> validEntities = headerRepository.findValidAttributes();
        Assertions.assertTrue(validEntities.isEmpty(), "Expected no valid entities as all have negative locations.");
    }

}