package RepositoryTests.SMS;

import cz.inspire.sms.entity.SMSHistoryEntity;
import cz.inspire.sms.repository.SMSHistoryRepository;
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

import java.sql.Timestamp;
import java.util.*;

@QuarkusTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // Allows non-static @BeforeAll
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SMSHistoryRepositoryIT {

    @Inject
    SMSHistoryRepository smsHistoryRepository;

    /**
     * Clears the database before tests to ensure isolation.
     */
    @BeforeAll
    @ActivateRequestContext
    public void clearDatabase() {
        List<SMSHistoryEntity> allEntities = new ArrayList<>(smsHistoryRepository.findAll());
        if (!allEntities.isEmpty()) {
            smsHistoryRepository.deleteAll(allEntities);
        }
    }

    /**
     * Tests saving and retrieving an SMSHistoryEntity by generated ID.
     */
    @Order(2)
    @Test
    public void testSaveAndFindById() {
        // Pass null for the ID to let the provider generate it.
        SMSHistoryEntity entity = new SMSHistoryEntity(
                null,
                new Date(),
                "Test message",
                List.of("GroupA", "GroupB"),
                List.of("user1@example.com", "user2@example.com"),
                List.of("extra1@example.com"),
                true
        );
        entity = smsHistoryRepository.create(entity);
        String generatedId = entity.getId();

        SMSHistoryEntity retrieved = smsHistoryRepository.findByPrimaryKey(generatedId);
        Assertions.assertNotNull(retrieved, "Entity should be present in repository.");
        Assertions.assertEquals("Test message", retrieved.getMessage(), "Message should match.");
        Assertions.assertEquals(2, retrieved.getGroups().size(), "Groups count should match.");
        Assertions.assertEquals(2, retrieved.getRecipients().size(), "Recipients count should match.");
        Assertions.assertEquals(1, retrieved.getMoreRecipients().size(), "MoreRecipients count should match.");
        Assertions.assertTrue(retrieved.getAutomatic(), "Automatic flag should match.");
    }

    /**
     * Tests retrieving SMS history records by date range.
     */
    @Order(2)
    @Test
    public void testFindByDate() {
        Date now = new Date();
        Date past = new Date(System.currentTimeMillis() - 100000);
        Date future = new Date(System.currentTimeMillis() + 100000);

        // Create three entities with generated IDs.
        SMSHistoryEntity entity1 = new SMSHistoryEntity(
                null, past, "Old message", Arrays.asList(), Arrays.asList(), Arrays.asList(), true
        );
        SMSHistoryEntity entity2 = new SMSHistoryEntity(
                null, now, "Current message", Arrays.asList(), Arrays.asList(), Arrays.asList(), false
        );
        SMSHistoryEntity entity3 = new SMSHistoryEntity(
                null, future, "Future message", Arrays.asList(), Arrays.asList(), Arrays.asList(), true
        );

        smsHistoryRepository.create(entity1);
        smsHistoryRepository.create(entity2);
        smsHistoryRepository.create(entity3);

        List<SMSHistoryEntity> results = smsHistoryRepository.findByDate(
                new Timestamp(past.getTime()), new Timestamp(future.getTime())
        );

        Assertions.assertEquals(5, results.size(), "Expected 3 messages in the given date range.");
    }

    /**
     * Tests retrieving SMS history records by date range and automatic flag.
     */
    @Order(1)
    @Test
    public void testFindByDateAutomatic() {
        Date now = new Date();
        Date past = new Date(System.currentTimeMillis() - 100000);
        Date future = new Date(System.currentTimeMillis() + 100000);

        SMSHistoryEntity entity1 = new SMSHistoryEntity(
                null, past, "Old message", List.of(), List.of(), List.of(), true
        );
        SMSHistoryEntity entity2 = new SMSHistoryEntity(
                null, now, "Current message", List.of(), List.of(), List.of(), false
        );
        SMSHistoryEntity entity3 = new SMSHistoryEntity(
                null, future, "Future message", List.of(), List.of(), List.of(), true
        );

        smsHistoryRepository.create(entity1);
        smsHistoryRepository.create(entity2);
        smsHistoryRepository.create(entity3);

        List<SMSHistoryEntity> results = smsHistoryRepository.findByDateAutomatic(
                new Timestamp(past.getTime()), new Timestamp(future.getTime()), true
        );

        Assertions.assertEquals(2, results.size(), "Expected 2 messages that are automatic.");
    }

    /**
     * Tests updating an existing SMSHistoryEntity.
     */
    @Order(4)
    @Test
    public void testUpdateEntity() {
        // Pass null for the ID to let the provider generate it.
        SMSHistoryEntity entity = new SMSHistoryEntity(
                null, new Date(), "Old message",
                List.of("OldGroup"),
                List.of("old@example.com"),
                List.of("extra@example.com"),
                false
        );
        entity = smsHistoryRepository.create(entity);
        String generatedId = entity.getId();

        // Update message and recipients
        entity.setMessage("Updated message");
        entity.setRecipients(List.of("updated@example.com"));
        entity.setAutomatic(true);
        smsHistoryRepository.create(entity);

        SMSHistoryEntity updated = smsHistoryRepository.findByPrimaryKey(generatedId);
        Assertions.assertNotNull(updated, "Entity should still exist after update.");
        Assertions.assertEquals("Updated message", updated.getMessage(), "Updated message should match.");
        Assertions.assertEquals(1, updated.getRecipients().size(), "Updated recipients count should match.");
        Assertions.assertTrue(updated.getAutomatic(), "Updated automatic flag should be true.");
    }

    /**
     * Tests deleting an entity.
     */
    @Order(5)
    @Test
    public void testDeleteEntity() {
        // Pass null for the ID to let the provider generate it.
        SMSHistoryEntity entity = new SMSHistoryEntity(
                null, new Date(), "Test deletion",
                List.of("GroupX"),
                List.of("delete@example.com"),
                List.of("extra@example.com"),
                true
        );
        entity = smsHistoryRepository.create(entity);
        String generatedId = entity.getId();

        smsHistoryRepository.deleteByPrimaryKey(generatedId);
        SMSHistoryEntity deleted = smsHistoryRepository.findByPrimaryKey(generatedId);
        Assertions.assertNull(deleted, "Entity should be deleted from repository.");
    }

    /**
     * Tests that retrieving a non-existent entity returns empty.
     */
    @Order(6)
    @Test
    public void testFindNonExistentEntity() {
        SMSHistoryEntity retrieved = smsHistoryRepository.findByPrimaryKey("SMS-999");
        Assertions.assertNull(retrieved, "Should return empty for non-existent entity.");
    }
}
