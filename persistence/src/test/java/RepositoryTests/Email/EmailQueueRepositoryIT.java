package RepositoryTests.Email;

import cz.inspire.email.entity.EmailQueueEntity;
import cz.inspire.email.repository.EmailQueueRepository;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@QuarkusTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // Allows non-static @BeforeAll
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EmailQueueRepositoryIT {

    @Inject
    EmailQueueRepository emailQueueRepository;

    /**
     * Clears the database before tests to ensure isolation.
     */
    @BeforeAll
    @ActivateRequestContext
    public void clearDatabase() {
        List<EmailQueueEntity> allEntities = new ArrayList<>(emailQueueRepository.findAll());
        if (!allEntities.isEmpty()) {
            emailQueueRepository.deleteAll(allEntities);
        }
    }

    @Order(1)
    @Test
    public void testSaveAndFindById() {
        // Set first parameter to null to let the provider generate the ID
        EmailQueueEntity entity = new EmailQueueEntity(
                null, new Date(), "EMAIL-124", "user@example.com", 1, false, "EMAIL-456"
        );
        entity = emailQueueRepository.create(entity);
        String generatedId = entity.getId();

        EmailQueueEntity retrieved = emailQueueRepository.findById(generatedId);
        Assertions.assertNotNull(retrieved, "Entity should be present.");
        Assertions.assertEquals("EMAIL-124", retrieved.getEmailHistory(), "Email history should match.");
        Assertions.assertEquals("user@example.com", retrieved.getRecipient(), "Recipient should match.");
        Assertions.assertEquals(1, retrieved.getPriority(), "Priority should match.");
    }

    @Order(2)
    @Test
    public void testFindAllOrdered() {
        // Create two entities with different dates (ID passed as null)
        EmailQueueEntity oldEmail = new EmailQueueEntity(
                null, new Date(System.currentTimeMillis() - 10000), "EMAIL-231", "old@example.com", 2, false, null
        );
        EmailQueueEntity newEmail = new EmailQueueEntity(
                null, new Date(), "EMAIL-456", "new@example.com", 3, false, null
        );

        oldEmail = emailQueueRepository.create(oldEmail);
        newEmail = emailQueueRepository.create(newEmail);

        List<EmailQueueEntity> results = emailQueueRepository.findAllOrdered();
        Assertions.assertEquals(3, results.size(), "Expected 2 queued emails.");
        // Instead of comparing hard-coded IDs, check that the newest (by date) is first.
        Assertions.assertTrue(results.get(0).getCreated().after(results.get(1).getCreated()),
                "Newest email should be first.");
    }

    @Order(4)
    @Test
    public void testFindAllWithLimit() {
        for (int i = 1; i <= 5; i++) {
            emailQueueRepository.create(
                    new EmailQueueEntity(null, new Date(), "EMAIL-" + i, "user" + i + "@example.com", i, false, null)
            );
        }

        List<EmailQueueEntity> results = emailQueueRepository.findAll(3, 0);
        Assertions.assertEquals(3, results.size(), "Only 3 emails should be retrieved due to limit.");
    }

    @Order(3)
    @Test
    public void testFindFirstMail() {
        EmailQueueEntity lowPriority = new EmailQueueEntity(
                null, new Date(), "EMAIL-345", "low@example.com", 2, false, null
        );
        EmailQueueEntity highPriority = new EmailQueueEntity(
                null, new Date(), "EMAIL-456", "high@example.com", 5, false, null
        );

        lowPriority = emailQueueRepository.create(lowPriority);
        highPriority = emailQueueRepository.create(highPriority);

        Optional<EmailQueueEntity> firstMail = emailQueueRepository.findFirstMail(1);
        Assertions.assertTrue(firstMail.isPresent(), "There should be at least one email.");
        // Verify that the highest priority (5) email is retrieved.
        EmailQueueEntity retrieved = firstMail.get();
        Assertions.assertEquals("high@example.com", retrieved.getRecipient(), "Highest priority email should be retrieved.");
        Assertions.assertEquals(5, retrieved.getPriority(), "Highest priority value should be 5.");
    }

    @Order(5)
    @Test
    public void testFindByHistory() {
        EmailQueueEntity entity1 = new EmailQueueEntity(
                null, new Date(), "EMAIL-123", "user1@example.com", 1, false, null
        );
        EmailQueueEntity entity2 = new EmailQueueEntity(
                null, new Date(), "EMAIL-123", "user2@example.com", 1, false, null
        );
        emailQueueRepository.create(entity1);
        emailQueueRepository.create(entity2);

        List<EmailQueueEntity> results = emailQueueRepository.findByHistory("EMAIL-123");
        Assertions.assertEquals(2, results.size(), "Expected 2 emails linked to EMAIL-123.");
    }

    @Order(6)
    @Test
    public void testFindByDependentHistory() {
        EmailQueueEntity entity1 = new EmailQueueEntity(
                null, new Date(), "EMAIL-789", "user3@example.com", 1, false, "EMAIL-999"
        );
        EmailQueueEntity entity2 = new EmailQueueEntity(
                null, new Date(), "EMAIL-789", "user4@example.com", 1, false, "EMAIL-999"
        );
        emailQueueRepository.create(entity1);
        emailQueueRepository.create(entity2);

        List<EmailQueueEntity> results = emailQueueRepository.findByDependentHistory("EMAIL-999");
        Assertions.assertEquals(2, results.size(), "Expected 2 emails dependent on EMAIL-999.");
    }

    @Order(7)
    @Test
    public void testUpdateEntity() {
        EmailQueueEntity entity = new EmailQueueEntity(
                null, new Date(), "EMAIL-777", "update@example.com", 3, false, null
        );
        entity = emailQueueRepository.create(entity);
        String generatedId = entity.getId();

        // Update fields
        entity.setRecipient("updated@example.com");
        entity.setPriority(2);
        entity.setRemoveEmailHistory(true);
        emailQueueRepository.create(entity);

        EmailQueueEntity updated = emailQueueRepository.findById(generatedId);
        Assertions.assertNotNull(updated, "Entity should exist after update.");
        Assertions.assertEquals("updated@example.com", updated.getRecipient(), "Updated recipient should match.");
        Assertions.assertEquals(2, updated.getPriority(), "Updated priority should match.");
        Assertions.assertTrue(updated.isRemoveEmailHistory(), "RemoveEmailHistory should be true.");
    }

    @Order(8)
    @Test
    public void testDeleteEntity() {
        EmailQueueEntity entity = new EmailQueueEntity(
                null, new Date(), "EMAIL-888", "delete@example.com", 4, false, null
        );
        entity = emailQueueRepository.create(entity);
        String generatedId = entity.getId();

        emailQueueRepository.deleteById(generatedId);

        EmailQueueEntity deleted = emailQueueRepository.findById(generatedId);
        Assertions.assertNull(deleted, "Entity should be deleted.");
    }
}
