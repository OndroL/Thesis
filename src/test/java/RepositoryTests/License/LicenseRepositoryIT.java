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
import java.util.Optional;

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
        List<LicenseEntity> allEntities = new ArrayList<>();
        licenseRepository.findAll().forEach(allEntities::add);
        if (!allEntities.isEmpty()) {
            licenseRepository.deleteAll(allEntities);
        }
    }

    /**
     * Tests saving and retrieving a LicenseEntity by ID.
     */
    @Test
    public void testSaveAndFindById() {
        LicenseEntity entity = new LicenseEntity("LIC-001", "Customer A", true, true, new Date(), true,
                new Date(), true, 100, 10, 50, 5, 3, 2, 1, 500, true, 1024L,
                "hashValue", new Date(), new Date(), 1);

        licenseRepository.save(entity);

        Optional<LicenseEntity> retrieved = licenseRepository.findById("LIC-001");
        Assertions.assertTrue(retrieved.isPresent(), "Entity should be present in repository.");
        Assertions.assertEquals("Customer A", retrieved.get().getCustomer(), "Customer name should match.");
        Assertions.assertEquals(100, retrieved.get().getActivityLimit(), "Activity limit should match.");
        Assertions.assertEquals(1024L, retrieved.get().getModules(), "Modules should match.");
    }

    /**
     * Tests retrieving all licenses in order using findAllOrdered().
     */
    @Test
    public void testFindAllOrdered() throws InterruptedException {
        LicenseEntity entity1 = new LicenseEntity("LIC-001", "Customer A", true, true, new Date(), true,
                new Date(), true, 100, 10, 50, 5, 3, 2, 1, 500, true, 1024L,
                "hashValue", new Date(System.currentTimeMillis() - 10000), new Date(), 1);

        LicenseEntity entity2 = new LicenseEntity("LIC-002", "Customer B", true, true, new Date(), true,
                new Date(), true, 200, 20, 100, 10, 6, 4, 2, 1000, false, 2048L,
                "hashValue2", new Date(System.currentTimeMillis()), new Date(), 2);

        licenseRepository.save(entity1);
        licenseRepository.save(entity2);

        List<LicenseEntity> results = licenseRepository.findAllOrdered();
        Assertions.assertEquals(2, results.size(), "Expected 2 licenses in repository.");
        Assertions.assertEquals("LIC-001", results.get(0).getId(), "Expected LIC-001 to be first.");
        Assertions.assertEquals("LIC-002", results.get(1).getId(), "Expected LIC-002 to be second.");
    }

    /**
     * Tests updating an existing LicenseEntity.
     */
    @Test
    public void testUpdateEntity() {
        LicenseEntity entity = new LicenseEntity("LIC-003", "Customer C", true, true, new Date(), true,
                new Date(), true, 300, 30, 150, 15, 9, 6, 3, 1500, false, 4096L,
                "hashValue3", new Date(), new Date(), 3);
        licenseRepository.save(entity);

        // Update customer name and limits
        entity.setCustomer("Customer C Updated");
        entity.setActivityLimit(350);
        entity.setModules(8192L);
        licenseRepository.save(entity);

        Optional<LicenseEntity> updated = licenseRepository.findById("LIC-003");
        Assertions.assertTrue(updated.isPresent(), "Entity should still exist after update.");
        Assertions.assertEquals("Customer C Updated", updated.get().getCustomer(), "Updated customer name should match.");
        Assertions.assertEquals(350, updated.get().getActivityLimit(), "Updated activity limit should match.");
        Assertions.assertEquals(8192L, updated.get().getModules(), "Updated modules should match.");
    }

    /**
     * Tests deleting an entity.
     */
    @Test
    public void testDeleteEntity() {
        LicenseEntity entity = new LicenseEntity("LIC-004", "Customer D", true, true, new Date(), true,
                new Date(), true, 400, 40, 200, 20, 12, 8, 4, 2000, true, 16384L,
                "hashValue4", new Date(), new Date(), 4);
        licenseRepository.save(entity);

        licenseRepository.deleteById("LIC-004");
        Optional<LicenseEntity> deleted = licenseRepository.findById("LIC-004");
        Assertions.assertFalse(deleted.isPresent(), "Entity should be deleted from repository.");
    }

    /**
     * Tests that retrieving a non-existent entity returns empty.
     */
    @Test
    public void testFindNonExistentEntity() {
        Optional<LicenseEntity> retrieved = licenseRepository.findById("LIC-999");
        Assertions.assertFalse(retrieved.isPresent(), "Should return empty for non-existent entity.");
    }
}
