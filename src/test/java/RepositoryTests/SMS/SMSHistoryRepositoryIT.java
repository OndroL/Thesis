package RepositoryTests.SMS;

import cz.inspire.common.entity.NastaveniEntity;
import cz.inspire.sms.entity.SMSHistoryEntity;
import cz.inspire.sms.repository.SMSHistoryRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.*;

@QuarkusTest
@Transactional
public class SMSHistoryRepositoryIT {

    @Inject
    SMSHistoryRepository smsHistoryRepository;

    /**
     * Clears the database before each test to ensure isolation.
     */
    @BeforeEach
    public void clearDatabase() {
        List<SMSHistoryEntity> allEntities = new ArrayList<>();
        smsHistoryRepository.findAll().forEach(allEntities::add);
        if (!allEntities.isEmpty()) {
            smsHistoryRepository.deleteAll(allEntities);
        }
    }

    /**
     * Tests saving and retrieving an SMSHistoryEntity by ID.
     */
    @Test
    public void testSaveAndFindById() {
        SMSHistoryEntity entity = new SMSHistoryEntity(
                "SMS-001", new Date(), "Test message",
                Arrays.asList("GroupA", "GroupB"),
                Arrays.asList("user1@example.com", "user2@example.com"),
                Arrays.asList("extra1@example.com"),
                true
        );

        smsHistoryRepository.save(entity);

        Optional<SMSHistoryEntity> retrieved = smsHistoryRepository.findById("SMS-001");
        Assertions.assertTrue(retrieved.isPresent(), "Entity should be present in repository.");
        Assertions.assertEquals("Test message", retrieved.get().getMessage(), "Message should match.");
        Assertions.assertEquals(2, retrieved.get().getGroups().size(), "Groups count should match.");
        Assertions.assertEquals(2, retrieved.get().getRecipients().size(), "Recipients count should match.");
        Assertions.assertEquals(1, retrieved.get().getMoreRecipients().size(), "MoreRecipients count should match.");
        Assertions.assertTrue(retrieved.get().getAutomatic(), "Automatic flag should match.");
    }

    /**
     * Tests retrieving SMS history records by date range.
     */
    @Test
    public void testFindByDate() throws InterruptedException {
        Date now = new Date();
        Date past = new Date(System.currentTimeMillis() - 100000);
        Date future = new Date(System.currentTimeMillis() + 100000);

        SMSHistoryEntity entity1 = new SMSHistoryEntity("SMS-001", past, "Old message", Arrays.asList(), Arrays.asList(), Arrays.asList(), true);
        SMSHistoryEntity entity2 = new SMSHistoryEntity("SMS-002", now, "Current message", Arrays.asList(), Arrays.asList(), Arrays.asList(), false);
        SMSHistoryEntity entity3 = new SMSHistoryEntity("SMS-003", future, "Future message", Arrays.asList(), Arrays.asList(), Arrays.asList(), true);

        smsHistoryRepository.save(entity1);
        smsHistoryRepository.save(entity2);
        smsHistoryRepository.save(entity3);

        List<SMSHistoryEntity> results = smsHistoryRepository.findByDate(new Timestamp(past.getTime()), new Timestamp(future.getTime()));

        Assertions.assertEquals(3, results.size(), "Expected 3 messages in the given date range.");
    }

    /**
     * Tests retrieving SMS history records by date range and automatic flag.
     */
    @Test
    public void testFindByDateAutomatic() throws InterruptedException {
        Date now = new Date();
        Date past = new Date(System.currentTimeMillis() - 100000);
        Date future = new Date(System.currentTimeMillis() + 100000);

        SMSHistoryEntity entity1 = new SMSHistoryEntity("SMS-001", past, "Old message", Arrays.asList(), Arrays.asList(), Arrays.asList(), true);
        SMSHistoryEntity entity2 = new SMSHistoryEntity("SMS-002", now, "Current message", Arrays.asList(), Arrays.asList(), Arrays.asList(), false);
        SMSHistoryEntity entity3 = new SMSHistoryEntity("SMS-003", future, "Future message", Arrays.asList(), Arrays.asList(), Arrays.asList(), true);

        smsHistoryRepository.save(entity1);
        smsHistoryRepository.save(entity2);
        smsHistoryRepository.save(entity3);

        List<SMSHistoryEntity> results = smsHistoryRepository.findByDateAutomatic(
                new Timestamp(past.getTime()), new Timestamp(future.getTime()), true);

        Assertions.assertEquals(2, results.size(), "Expected 2 messages that are automatic.");
    }

    /**
     * Tests updating an existing SMSHistoryEntity.
     */
    @Test
    public void testUpdateEntity() {
        SMSHistoryEntity entity = new SMSHistoryEntity(
                "SMS-004", new Date(), "Old message",
                Arrays.asList("OldGroup"),
                Arrays.asList("old@example.com"),
                Arrays.asList("extra@example.com"),
                false
        );
        smsHistoryRepository.save(entity);

        // Update message and recipients
        entity.setMessage("Updated message");
        entity.setRecipients(Arrays.asList("updated@example.com"));
        entity.setAutomatic(true);
        smsHistoryRepository.save(entity);

        Optional<SMSHistoryEntity> updated = smsHistoryRepository.findById("SMS-004");
        Assertions.assertTrue(updated.isPresent(), "Entity should still exist after update.");
        Assertions.assertEquals("Updated message", updated.get().getMessage(), "Updated message should match.");
        Assertions.assertEquals(1, updated.get().getRecipients().size(), "Updated recipients count should match.");
        Assertions.assertTrue(updated.get().getAutomatic(), "Updated automatic flag should be true.");
    }

    /**
     * Tests deleting an entity.
     */
    @Test
    public void testDeleteEntity() {
        SMSHistoryEntity entity = new SMSHistoryEntity(
                "SMS-005", new Date(), "Test deletion",
                Arrays.asList("GroupX"),
                Arrays.asList("delete@example.com"),
                Arrays.asList("extra@example.com"),
                true
        );
        smsHistoryRepository.save(entity);

        smsHistoryRepository.deleteById("SMS-005");
        Optional<SMSHistoryEntity> deleted = smsHistoryRepository.findById("SMS-005");
        Assertions.assertFalse(deleted.isPresent(), "Entity should be deleted from repository.");
    }

    /**
     * Tests that retrieving a non-existent entity returns empty.
     */
    @Test
    public void testFindNonExistentEntity() {
        Optional<SMSHistoryEntity> retrieved = smsHistoryRepository.findById("SMS-999");
        Assertions.assertFalse(retrieved.isPresent(), "Should return empty for non-existent entity.");
    }
}
