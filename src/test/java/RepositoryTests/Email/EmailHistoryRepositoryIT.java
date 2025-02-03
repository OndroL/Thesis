package RepositoryTests.Email;

import cz.inspire.email.entity.EmailHistoryEntity;
import cz.inspire.email.repository.EmailHistoryRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.data.Limit;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.*;

@QuarkusTest
@Transactional
public class EmailHistoryRepositoryIT {

    @Inject
    EmailHistoryRepository emailHistoryRepository;

    /**
     * Clears the database before each test to ensure isolation.
     */
    @BeforeEach
    public void clearDatabase() {
        List<EmailHistoryEntity> allEntities = new ArrayList<>();
        emailHistoryRepository.findAll().forEach(allEntities::add);
        if (!allEntities.isEmpty()) {
            emailHistoryRepository.deleteAll(allEntities);
        }
    }

    /**
     * Tests saving and retrieving an EmailHistoryEntity by ID.
     */
    @Test
    public void testSaveAndFindById() {
        Map<String, String> attachments = Map.of(
                "attachment.pdf", "http://example.com/attachment.pdf"
        );

        EmailHistoryEntity entity = new EmailHistoryEntity(
                "EMAIL-001", new Date(), "Test email content", "Test Subject",
                Arrays.asList("GroupA", "GroupB"), Arrays.asList("user1@example.com", "user2@example.com"),
                Arrays.asList("extra@example.com"), true, false,
                attachments, true, new ArrayList<>());

        emailHistoryRepository.save(entity);

        Optional<EmailHistoryEntity> retrieved = emailHistoryRepository.findById("EMAIL-001");
        Assertions.assertTrue(retrieved.isPresent(), "Entity should be present in repository.");
        Assertions.assertEquals("Test Subject", retrieved.get().getSubject(), "Subject should match.");
        Assertions.assertEquals(2, retrieved.get().getGroups().size(), "Groups count should match.");
        Assertions.assertEquals(1, retrieved.get().getMoreRecipients().size(), "MoreRecipients count should match.");
        Assertions.assertTrue(retrieved.get().getSent(), "Sent status should match.");
        Assertions.assertEquals(1, retrieved.get().getAttachments().size(), "Attachments count should match.");
        Assertions.assertEquals("http://example.com/attachment.pdf", retrieved.get().getAttachments().get("attachment.pdf"), "Attachment URL should match.");
    }

    /**
     * Tests retrieving all email history records ordered by date.
     */
    @Test
    public void testFindAllOrdered() {
        EmailHistoryEntity entity1 = new EmailHistoryEntity("EMAIL-001", new Date(System.currentTimeMillis() - 10000), "Old email", "Old Subject",
                Arrays.asList(), Arrays.asList(), Arrays.asList(), true, false, new HashMap<>(), true, new ArrayList<>());
        EmailHistoryEntity entity2 = new EmailHistoryEntity("EMAIL-002", new Date(), "New email", "New Subject",
                Arrays.asList(), Arrays.asList(), Arrays.asList(), false, true, new HashMap<>(), false, new ArrayList<>());

        emailHistoryRepository.save(entity1);
        emailHistoryRepository.save(entity2);

        List<EmailHistoryEntity> results = emailHistoryRepository.findAllOrdered();
        Assertions.assertEquals(2, results.size(), "Expected 2 emails in repository.");
        Assertions.assertEquals("EMAIL-002", results.get(0).getId(), "Expected EMAIL-002 to be first.");
        Assertions.assertEquals("EMAIL-001", results.get(1).getId(), "Expected EMAIL-001 to be second.");
    }

    /**
     * Tests retrieving email history records by date range.
     */
    @Test
    public void testFindByDate() {
        Date now = new Date();
        Date past = new Date(System.currentTimeMillis() - 100000);
        Date future = new Date(System.currentTimeMillis() + 100000);

        EmailHistoryEntity entity1 = new EmailHistoryEntity("EMAIL-001", past, "Old email", "Subject1", Arrays.asList(), Arrays.asList(), Arrays.asList(), true, false, new HashMap<>(), true, new ArrayList<>());
        EmailHistoryEntity entity2 = new EmailHistoryEntity("EMAIL-002", now, "Current email", "Subject2", Arrays.asList(), Arrays.asList(), Arrays.asList(), false, true, new HashMap<>(), true, new ArrayList<>());
        EmailHistoryEntity entity3 = new EmailHistoryEntity("EMAIL-003", future, "Future email", "Subject3", Arrays.asList(), Arrays.asList(), Arrays.asList(), true, true, new HashMap<>(), true, new ArrayList<>());

        emailHistoryRepository.save(entity1);
        emailHistoryRepository.save(entity2);
        emailHistoryRepository.save(entity3);

        List<EmailHistoryEntity> results = emailHistoryRepository.findByDate(
                new Timestamp(past.getTime()), new Timestamp(future.getTime()), Limit.of(10));

        Assertions.assertEquals(3, results.size(), "Expected 3 emails in the given date range.");
    }

    /**
     * Tests updating an existing EmailHistoryEntity.
     */
    @Test
    public void testUpdateEntity() {
        EmailHistoryEntity entity = new EmailHistoryEntity(
                "EMAIL-004", new Date(), "Initial content", "Initial Subject",
                Arrays.asList("InitialGroup"), Arrays.asList("initial@example.com"),
                Arrays.asList(), false, false, new HashMap<>(), false, new ArrayList<>());
        emailHistoryRepository.save(entity);

        // Update email content and subject
        entity.setText("Updated content");
        entity.setSubject("Updated Subject");
        entity.setSent(true);
        entity.setAttachments(Map.of("updated.pdf", "http://example.com/updated.pdf"));
        emailHistoryRepository.save(entity);

        Optional<EmailHistoryEntity> updated = emailHistoryRepository.findById("EMAIL-004");
        Assertions.assertTrue(updated.isPresent(), "Entity should still exist after update.");
        Assertions.assertEquals("Updated content", updated.get().getText(), "Updated content should match.");
        Assertions.assertEquals("Updated Subject", updated.get().getSubject(), "Updated subject should match.");
        Assertions.assertTrue(updated.get().getSent(), "Updated sent status should be true.");
        Assertions.assertEquals(1, updated.get().getAttachments().size(), "Attachments count should match.");
        Assertions.assertEquals("http://example.com/updated.pdf", updated.get().getAttachments().get("updated.pdf"), "Updated attachment should match.");
    }

    /**
     * Tests deleting an entity.
     */
    @Test
    public void testDeleteEntity() {
        EmailHistoryEntity entity = new EmailHistoryEntity(
                "EMAIL-005", new Date(), "To be deleted", "Delete Subject",
                Arrays.asList("DeleteGroup"), Arrays.asList("delete@example.com"),
                Arrays.asList(), true, true, new HashMap<>(), false, new ArrayList<>());
        emailHistoryRepository.save(entity);

        emailHistoryRepository.deleteById("EMAIL-005");
        Optional<EmailHistoryEntity> deleted = emailHistoryRepository.findById("EMAIL-005");
        Assertions.assertFalse(deleted.isPresent(), "Entity should be deleted from repository.");
    }

    /**
     * Tests that retrieving a non-existent entity returns empty.
     */
    @Test
    public void testFindNonExistentEntity() {
        Optional<EmailHistoryEntity> retrieved = emailHistoryRepository.findById("NON_EXISTENT");
        Assertions.assertFalse(retrieved.isPresent(), "Should return empty for non-existent entity.");
    }
}
