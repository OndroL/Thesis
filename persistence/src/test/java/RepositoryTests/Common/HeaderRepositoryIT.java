package RepositoryTests.Common;

import cz.inspire.common.entity.HeaderEntity;
import cz.inspire.common.repository.HeaderRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;
import java.util.List;

@Transactional
@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class HeaderRepositoryIT {

    @Inject
    HeaderRepository headerRepository;

    /**
     * Clears the database before tests to ensure isolation.
     */
    @BeforeAll
    @ActivateRequestContext
    public void clearDatabase() {
        List<HeaderEntity> allEntities = new ArrayList<>(headerRepository.findAll());
        if (!allEntities.isEmpty()) {
            headerRepository.deleteAll(allEntities);
        }
    }

    /**
     * Verifies that an entity can be saved and then retrieved by its ID.
     */
    @Test
    public void testSaveAndFindById() {
        HeaderEntity entity = new HeaderEntity(null, 456, 20);
        entity = headerRepository.create(entity);

        HeaderEntity retrieved = headerRepository.findById(entity.getId());
        Assertions.assertNotNull(retrieved, "Entity should be present in repository.");
        Assertions.assertEquals(456, retrieved.getField(), "Field value should match.");
        Assertions.assertEquals(20, retrieved.getLocation(), "Location value should match.");
    }

    /**
     * Verifies that updating an existing entity changes its values.
     */
    @Test
    public void testUpdateEntity() {
        HeaderEntity entity = new HeaderEntity(null, 789, 30);
        entity = headerRepository.create(entity);

        entity.setField(111);
        entity.setLocation(40);

        entity = headerRepository.create(entity);

        HeaderEntity updated = headerRepository.findById(entity.getId());
        Assertions.assertNotNull(updated, "Entity should be present after update.");
        Assertions.assertEquals(111, updated.getField(), "Updated field value should be 111.");
        Assertions.assertEquals(40, updated.getLocation(), "Updated location value should be 40.");
    }

    /**
     * Verifies that an entity can be deleted.
     */
    @Test
    public void testDeleteEntity() {
        HeaderEntity entity = new HeaderEntity(null, 222, 50);
        entity = headerRepository.create(entity);

        headerRepository.deleteById(entity.getId());
        HeaderEntity deleted = headerRepository.findById(entity.getId());
        Assertions.assertNull(deleted, "Entity should be deleted from repository.");
    }

    /**
     * Tests the custom query 'findValidAttributes()' for a simple entity.
     */
    @Test
    public void testSaveAndFindValidAttributes() {
        HeaderEntity entity = new HeaderEntity(null, 123, 10);
        entity = headerRepository.create(entity);
        Assertions.assertNotNull(entity, "Expected a non-null returned entity from save()");

        List<HeaderEntity> results = headerRepository.findValidAttributes();
        Assertions.assertFalse(results.isEmpty(), "Expected to find at least one entity");

        HeaderEntity finalEntity = entity;
        HeaderEntity found = results.stream()
                .filter(e -> e.getId().equals(finalEntity.getId()))
                .findFirst()
                .orElse(null);

        Assertions.assertNotNull(found, "Expected to find entity with id=" + entity.getId());
        Assertions.assertEquals(123, found.getField());
        Assertions.assertEquals(10, found.getLocation());
    }

    /**
     * Tests the custom query 'findValidAttributes()' for multiple entities.
     * Only entities with a non-negative location should be returned and ordered by location ascending.
     */
    @Test
    public void testFindValidAttributesOrdering() {
        int number_of_valid  = headerRepository.findValidAttributes().size();

        HeaderEntity e1 = new HeaderEntity(null, 10, 30);
        HeaderEntity e2 = new HeaderEntity(null, 20, -10);
        HeaderEntity e3 = new HeaderEntity(null, 30, 20);
        HeaderEntity e4 = new HeaderEntity(null, 40, 40);

        e1 = headerRepository.create(e1);
        e2 = headerRepository.create(e2);
        e3 = headerRepository.create(e3);
        e4 = headerRepository.create(e4);

        List<HeaderEntity> validEntities = headerRepository.findValidAttributes();

        Assertions.assertEquals(number_of_valid + 3, validEntities.size(), "Expected 3 valid entities.");

        Assertions.assertEquals(20, validEntities.get(0).getLocation(), "First entity should have location=20.");
        Assertions.assertEquals(30, validEntities.get(1).getLocation(), "Second entity should have location=30.");
        Assertions.assertEquals(40, validEntities.get(2).getLocation(), "Third entity should have location=40.");
    }

    /**
     * Verifies that the custom query returns an empty list if no entities meet the criteria.
     */
    @Test
    public void testFindValidAttributesWithNoValidRecords() {
        HeaderEntity e1 = new HeaderEntity(null, 10, -5);
        HeaderEntity e2 = new HeaderEntity(null, 20, -15);
        e1 = headerRepository.create(e1);
        e2 = headerRepository.create(e2);

        List<HeaderEntity> validEntities = headerRepository.findValidAttributes();
        HeaderEntity finalE1 = e1;
        HeaderEntity finalE2 = e2;
        Assertions.assertTrue(validEntities.stream().noneMatch(e ->
                        e.getId().equals(finalE1.getId()) || e.getId().equals(finalE2.getId())),
                "Expected no valid entities as all have negative locations.");
    }
}
