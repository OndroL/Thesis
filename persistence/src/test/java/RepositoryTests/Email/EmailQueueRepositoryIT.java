package RepositoryTests.Email;

import cz.inspire.email.entity.EmailQueueEntity;
import cz.inspire.email.repository.EmailQueueRepository;
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
public class EmailQueueRepositoryIT {

    @Inject
    EmailQueueRepository emailQueueRepository;

    /**
     * Clears the database before tests to ensure isolation.
     */
    @BeforeAll
    @ActivateRequestContext
    public void clearDatabase() {
        List<EmailQueueEntity> allEntities = new ArrayList<>();
        emailQueueRepository.findAll().forEach(allEntities::add);
        if (!allEntities.isEmpty()) {
            emailQueueRepository.deleteAll(allEntities);
        }
    }

    @Test
    public void testSaveAndFindById() {
        EmailQueueEntity entity = new EmailQueueEntity(
                "QUEUE-001", new Date(), "EMAIL-123", "user@example.com", 1, false, "EMAIL-456"
        );

        emailQueueRepository.create(entity);

        EmailQueueEntity retrieved = emailQueueRepository.findById("QUEUE-001");
        Assertions.assertNotNull(retrieved, "Entity should be present.");
        Assertions.assertEquals("EMAIL-123", retrieved.getEmailHistory(), "Email history should match.");
        Assertions.assertEquals("user@example.com", retrieved.getRecipient(), "Recipient should match.");
        Assertions.assertEquals(1, retrieved.getPriority(), "Priority should match.");
    }

    @Test
    public void testFindAllOrdered() {
        EmailQueueEntity oldEmail = new EmailQueueEntity("QUEUE-001", new Date(System.currentTimeMillis() - 10000), "EMAIL-123", "old@example.com", 2, false, null);
        EmailQueueEntity newEmail = new EmailQueueEntity("QUEUE-002", new Date(), "EMAIL-456", "new@example.com", 3, false, null);

        emailQueueRepository.create(oldEmail);
        emailQueueRepository.create(newEmail);

        List<EmailQueueEntity> results = emailQueueRepository.findAllOrdered();
        Assertions.assertEquals(2, results.size(), "Expected 2 queued emails.");
        Assertions.assertEquals("QUEUE-002", results.get(0).getId(), "Newest email should be first.");
        Assertions.assertEquals("QUEUE-001", results.get(1).getId(), "Oldest email should be second.");
    }

    @Test
    public void testFindAllWithLimit() {
        for (int i = 1; i <= 5; i++) {
            emailQueueRepository.create(new EmailQueueEntity("QUEUE-00" + i, new Date(), "EMAIL-" + i, "user" + i + "@example.com", i, false, null));
        }

        List<EmailQueueEntity> results = emailQueueRepository.findAll(3, 0);
        Assertions.assertEquals(3, results.size(), "Only 3 emails should be retrieved due to limit.");
    }

    @Test
    public void testFindFirstMail() {
        EmailQueueEntity lowPriority = new EmailQueueEntity("QUEUE-001", new Date(), "EMAIL-123", "low@example.com", 2, false, null);
        EmailQueueEntity highPriority = new EmailQueueEntity("QUEUE-002", new Date(), "EMAIL-456", "high@example.com", 5, false, null);

        emailQueueRepository.create(lowPriority);
        emailQueueRepository.create(highPriority);

        Optional<EmailQueueEntity> firstMail = emailQueueRepository.findFirstMail(1);
        Assertions.assertTrue(firstMail.isPresent(), "There should be at least one email.");
        Assertions.assertEquals("QUEUE-002", firstMail.get().getId(), "Highest priority email should be retrieved.");
    }

    @Test
    public void testFindByHistory() {
        EmailQueueEntity entity1 = new EmailQueueEntity("QUEUE-001", new Date(), "EMAIL-123", "user1@example.com", 1, false, null);
        EmailQueueEntity entity2 = new EmailQueueEntity("QUEUE-002", new Date(), "EMAIL-123", "user2@example.com", 1, false, null);
        emailQueueRepository.create(entity1);
        emailQueueRepository.create(entity2);

        List<EmailQueueEntity> results = emailQueueRepository.findByHistory("EMAIL-123");
        Assertions.assertEquals(2, results.size(), "Expected 2 emails linked to EMAIL-123.");
    }

    @Test
    public void testFindByDependentHistory() {
        EmailQueueEntity entity1 = new EmailQueueEntity("QUEUE-003", new Date(), "EMAIL-789", "user3@example.com", 1, false, "EMAIL-456");
        EmailQueueEntity entity2 = new EmailQueueEntity("QUEUE-004", new Date(), "EMAIL-789", "user4@example.com", 1, false, "EMAIL-456");
        emailQueueRepository.create(entity1);
        emailQueueRepository.create(entity2);

        List<EmailQueueEntity> results = emailQueueRepository.findByDependentHistory("EMAIL-456");
        Assertions.assertEquals(2, results.size(), "Expected 2 emails dependent on EMAIL-456.");
    }

    @Test
    public void testUpdateEntity() {
        EmailQueueEntity entity = new EmailQueueEntity("QUEUE-005", new Date(), "EMAIL-777", "update@example.com", 3, false, null);
        emailQueueRepository.create(entity);

        entity.setRecipient("updated@example.com");
        entity.setPriority(2);
        entity.setRemoveEmailHistory(true);
        emailQueueRepository.create(entity);

        EmailQueueEntity updated = emailQueueRepository.findById("QUEUE-005");
        Assertions.assertNotNull(updated, "Entity should exist after update.");
        Assertions.assertEquals("updated@example.com", updated.getRecipient(), "Updated recipient should match.");
        Assertions.assertEquals(2, updated.getPriority(), "Updated priority should match.");
        Assertions.assertTrue(updated.isRemoveEmailHistory(), "RemoveEmailHistory should be true.");
    }

    @Test
    public void testDeleteEntity() {
        EmailQueueEntity entity = new EmailQueueEntity("QUEUE-006", new Date(), "EMAIL-888", "delete@example.com", 4, false, null);
        emailQueueRepository.create(entity);

        emailQueueRepository.deleteById("QUEUE-006");

        EmailQueueEntity deleted = emailQueueRepository.findById("QUEUE-006");
        Assertions.assertNull(deleted, "Entity should be deleted.");
    }
}
