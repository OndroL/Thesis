package RepositoryTests.License;

import cz.inspire.license.entity.LicenseEntity;
import cz.inspire.license.repository.LicenseRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@QuarkusTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // Allows non-static @BeforeAll
public class LicenseRepositoryIT {

    @Inject
    LicenseRepository licenseRepository;

    /**
     * Clears the database before tests to ensure isolation.
     */
    @BeforeAll
    @ActivateRequestContext
    public void clearDatabase() {
        List<LicenseEntity> allEntities = new ArrayList<>(licenseRepository.findAll());
        if (!allEntities.isEmpty()) {
            licenseRepository.deleteAll(allEntities);
        }
    }

    /**
     * Tests saving and retrieving a LicenseEntity by generated ID.
     */
    @Test
    @Transactional
    public void testSaveAndFindById() {
        // Pass null instead of "LIC-001" to let the provider generate the ID.
        LicenseEntity entity = new LicenseEntity(
                null, "Customer A", true, true, new Date(), true,
                new Date(), true, 100, 10, 50, 5, 3, 2, 1, 500, true, 1024L,
                "hashValue", new Date(), new Date(), 1
        );
        entity = licenseRepository.create(entity);
        String generatedId = entity.getId();
        Assertions.assertNotNull(generatedId, "Generated ID should not be null");

        LicenseEntity retrieved = licenseRepository.findByPrimaryKey(generatedId);
        Assertions.assertNotNull(retrieved, "Entity should be present in repository.");
        Assertions.assertEquals("Customer A", retrieved.getCustomer(), "Customer name should match.");
        Assertions.assertEquals(100, retrieved.getActivityLimit(), "Activity limit should match.");
        Assertions.assertEquals(1024L, retrieved.getModules(), "Modules should match.");
    }

    /**
     * Tests retrieving all licenses in order using findAllOrdered().
     * Instead of expecting fixed IDs, we verify that the ordering follows the date order.
     */
    @Test
    @Transactional
    public void testFindAllOrdered() throws InterruptedException {
        LicenseEntity entity1 = new LicenseEntity(
                null, "Customer A", true, true, new Date(), true,
                new Date(), true, 100, 10, 50, 5, 3, 2, 1, 500, true, 1024L,
                "hashValue", new Date(System.currentTimeMillis() - 10000), new Date(), 1
        );
        LicenseEntity entity2 = new LicenseEntity(
                null, "Customer B", true, true, new Date(), true,
                new Date(), true, 200, 20, 100, 10, 6, 4, 2, 1000, false, 2048L,
                "hashValue2", new Date(System.currentTimeMillis()), new Date(), 2
        );

        entity1 = licenseRepository.create(entity1);
        entity2 = licenseRepository.create(entity2);

        List<LicenseEntity> results = licenseRepository.findAllOrdered();
        Assertions.assertEquals(2, results.size(), "Expected 2 licenses in repository.");
        // Verify that the first returned license has an earlier (or equal) date than the second.
        Assertions.assertTrue(results.get(0).getValidFrom().compareTo(results.get(1).getValidFrom()) <= 0,
                "Licenses should be ordered by start date ascending.");
    }

    /**
     * Tests updating an existing LicenseEntity.
     */
    @Test
    @Transactional
    public void testUpdateEntity() {
        LicenseEntity entity = new LicenseEntity(
                null, "Customer C", true, true, new Date(), true,
                new Date(), true, 300, 30, 150, 15, 9, 6, 3, 1500, false, 4096L,
                "hashValue3", new Date(), new Date(), 3
        );
        entity = licenseRepository.create(entity);
        String generatedId = entity.getId();
        Assertions.assertNotNull(generatedId, "Generated ID should not be null");

        // Update customer name and limits
        entity.setCustomer("Customer C Updated");
        entity.setActivityLimit(350);
        entity.setModules(8192L);
        licenseRepository.create(entity);

        LicenseEntity updated = licenseRepository.findByPrimaryKey(generatedId);
        Assertions.assertNotNull(updated, "Entity should still exist after update.");
        Assertions.assertEquals("Customer C Updated", updated.getCustomer(), "Updated customer name should match.");
        Assertions.assertEquals(350, updated.getActivityLimit(), "Updated activity limit should match.");
        Assertions.assertEquals(8192L, updated.getModules(), "Updated modules should match.");
    }

    /**
     * Tests deleting an entity.
     */
    @Test
    @Transactional
    public void testDeleteEntity() {
        LicenseEntity entity = new LicenseEntity(
                null, "Customer D", true, true, new Date(), true,
                new Date(), true, 400, 40, 200, 20, 12, 8, 4, 2000, true, 16384L,
                "hashValue4", new Date(), new Date(), 4
        );
        entity = licenseRepository.create(entity);
        String generatedId = entity.getId();
        Assertions.assertNotNull(generatedId, "Generated ID should not be null");

        licenseRepository.deleteByPrimaryKey(generatedId);
        LicenseEntity deleted = licenseRepository.findByPrimaryKey(generatedId);
        Assertions.assertNull(deleted, "Entity should be deleted from repository.");
    }

    /**
     * Tests that retrieving a non-existent entity returns empty.
     */
    @Test
    @Transactional
    public void testFindNonExistentEntity() {
        LicenseEntity retrieved = licenseRepository.findByPrimaryKey("LIC-999");
        Assertions.assertNull(retrieved, "Should return empty for non-existent entity.");
    }
}
