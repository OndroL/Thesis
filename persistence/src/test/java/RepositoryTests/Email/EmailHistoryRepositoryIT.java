package RepositoryTests.Email;

import RepositoryTests.DatabaseCleaner;
import cz.inspire.email.entity.EmailHistoryEntity;
import cz.inspire.email.entity.GeneratedAttachmentEntity;
import cz.inspire.email.repository.EmailHistoryRepository;
import cz.inspire.utils.FileAttributes;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@QuarkusTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EmailHistoryRepositoryIT {

    @Inject
    EmailHistoryRepository emailHistoryRepository;

    @Inject
    DatabaseCleaner databaseCleaner;

    /**
     * Clears the database before tests to ensure isolation.
     */
    @BeforeAll
    @ActivateRequestContext
    public void clearDatabase() {
        databaseCleaner.clearTable(EmailHistoryEntity.class, true);
        databaseCleaner.clearTable(GeneratedAttachmentEntity.class, true);
    }

    /**
     * Tests saving and retrieving an EmailHistoryEntity by generated ID.
     */
    @Order(2)
    @Test
    public void testSaveAndFindById() {
        List<FileAttributes> attachments = List.of(
                new FileAttributes("attachment.pdf", "FILE_SYSTEM/attachments/68057fc4-91a4-46bd-8229-7f770530a644")
        );

        // Create entity with null ID so that it is generated.
        EmailHistoryEntity entity = new EmailHistoryEntity(
                null,
                new Date(),
                "Test email content",
                "Test Subject",
                Arrays.asList("GroupA", "GroupB"),
                Arrays.asList("user1@example.com", "user2@example.com"),
                List.of("extra@example.com"),
                true,
                false,
                attachments,
                true,
                new ArrayList<>()
        );
        entity = emailHistoryRepository.create(entity);
        String generatedId = entity.getId();

        EmailHistoryEntity retrieved = emailHistoryRepository.findByPrimaryKey(generatedId);
        Assertions.assertNotNull(retrieved, "Entity should be present in repository.");
        Assertions.assertEquals("Test Subject", retrieved.getSubject(), "Subject should match.");
        Assertions.assertEquals(2, retrieved.getGroups().size(), "Groups count should match.");
        Assertions.assertEquals(1, retrieved.getMoreRecipients().size(), "MoreRecipients count should match.");
        Assertions.assertTrue(retrieved.getSent(), "Sent status should match.");
        Assertions.assertEquals(1, retrieved.getAttachments().size(), "Files count should match.");
        Assertions.assertEquals("attachment.pdf", retrieved.getAttachments().getFirst().getFileName(), "First file name should match.");
        Assertions.assertEquals("FILE_SYSTEM/attachments/68057fc4-91a4-46bd-8229-7f770530a644", retrieved.getAttachments().getFirst().getFilePath(), "First file path should match.");
    }

    /**
     * Tests retrieving all email history records ordered by date.
     */
    @Order(3)
    @Test
    public void testFindAllOrdered() {
        int number_of_valid = emailHistoryRepository.findAllOrdered().size();
        EmailHistoryEntity entity1 = new EmailHistoryEntity(
                null,
                new Date(System.currentTimeMillis() - 10000),
                "Old email",
                "Old Subject",
                List.of(),
                List.of(),
                List.of(),
                true,
                false,
                new ArrayList<>(),
                true,
                new ArrayList<>()
        );
        EmailHistoryEntity entity2 = new EmailHistoryEntity(
                null,
                new Date(),
                "New email",
                "New Subject",
                List.of(),
                List.of(),
                List.of(),
                false,
                true,
                new ArrayList<>(),
                false,
                new ArrayList<>()
        );

        entity1 = emailHistoryRepository.create(entity1);
        entity2 = emailHistoryRepository.create(entity2);

        List<EmailHistoryEntity> results = emailHistoryRepository.findAllOrdered();
        Assertions.assertEquals(number_of_valid + 2, results.size(), "Expected 2 emails in repository.");
        // Assuming ordering is descending by date (newest first)
        Assertions.assertTrue(results.get(0).getDate().after(results.get(1).getDate()),
                "First email should be more recent than the second.");
    }

    /**
     * Tests retrieving email history records by date range.
     */
    @Order(1)
    @Test
    public void testFindByDate() {
        Date now = new Date();
        Date past = new Date(System.currentTimeMillis() - 100000);
        Date future = new Date(System.currentTimeMillis() + 100000);

        EmailHistoryEntity entity1 = new EmailHistoryEntity(
                null,
                past,
                "Old email",
                "Subject1",
                List.of(),
                List.of(),
                List.of(),
                true,
                false,
                new ArrayList<>(),
                true,
                new ArrayList<>()
        );
        EmailHistoryEntity entity2 = new EmailHistoryEntity(
                null,
                now,
                "Current email",
                "Subject2",
                List.of(),
                List.of(),
                List.of(),
                false,
                true,
                new ArrayList<>(),
                true,
                new ArrayList<>()
        );
        EmailHistoryEntity entity3 = new EmailHistoryEntity(
                null,
                future,
                "Future email",
                "Subject3",
                List.of(),
                List.of(),
                List.of(),
                true,
                true,
                new ArrayList<>(),
                true,
                new ArrayList<>()
        );

        emailHistoryRepository.create(entity1);
        emailHistoryRepository.create(entity2);
        emailHistoryRepository.create(entity3);

        List<EmailHistoryEntity> results = emailHistoryRepository.findByDate(
                new Timestamp(past.getTime()),
                new Timestamp(future.getTime()),
                10,
                0
        );

        Assertions.assertEquals(3, results.size(), "Expected 3 emails in the given date range.");
    }

    /**
     * Tests updating an existing EmailHistoryEntity.
     */
    @Order(4)
    @Test
    public void testUpdateEntity() {
        EmailHistoryEntity entity = new EmailHistoryEntity(
                null,
                new Date(),
                "Initial content",
                "Initial Subject",
                List.of("InitialGroup"),
                List.of("initial@example.com"),
                List.of(),
                false,
                false,
                new ArrayList<>(),
                false,
                new ArrayList<>()
        );
        entity = emailHistoryRepository.create(entity);
        String generatedId = entity.getId();

        // Update fields
        entity.setText("Updated content");
        entity.setSubject("Updated Subject");
        entity.setSent(true);
        entity.setAttachments(List.of(new FileAttributes("updated.pdf", "http://example.com/updated.pdf")));
        // Update the entity (using create() as update mechanism in your repository)
        entity = emailHistoryRepository.create(entity);

        EmailHistoryEntity updated = emailHistoryRepository.findByPrimaryKey(generatedId);
        Assertions.assertNotNull(updated, "Entity should still exist after update.");
        Assertions.assertEquals("Updated content", updated.getText(), "Updated content should match.");
        Assertions.assertEquals("Updated Subject", updated.getSubject(), "Updated subject should match.");
        Assertions.assertTrue(updated.getSent(), "Updated sent status should be true.");
        Assertions.assertEquals(1, updated.getAttachments().size(), "Files count should match.");
        Assertions.assertEquals("updated.pdf", updated.getAttachments().getFirst().getFileName(), "Updated file name should match.");
        Assertions.assertEquals("http://example.com/updated.pdf", updated.getAttachments().getFirst().getFilePath(), "Updated file path should match.");
    }

    /**
     * Tests deleting an entity.
     */
    @Order(5)
    @Test
    public void testDeleteEntity() {
        EmailHistoryEntity entity = new EmailHistoryEntity(
                null,
                new Date(),
                "To be deleted",
                "Delete Subject",
                List.of("DeleteGroup"),
                List.of("delete@example.com"),
                List.of(),
                true,
                true,
                new ArrayList<>(),
                false,
                new ArrayList<>()
        );
        entity = emailHistoryRepository.create(entity);
        String generatedId = entity.getId();

        emailHistoryRepository.deleteByPrimaryKey(generatedId);
        EmailHistoryEntity deleted = emailHistoryRepository.findByPrimaryKey(generatedId);
        Assertions.assertNull(deleted, "Entity should be deleted from repository.");
    }

    /**
     * Tests saving and retrieving email history records with multiple files.
     */
    @Order(6)
    @Test
    public void testSaveAndRetrieveFiles() {
        List<FileAttributes> attachments = List.of(
                new FileAttributes("photo1.jpg", "FILE_SYSTEM/photos/1234-5678"),
                new FileAttributes("document.pdf", "FILE_SYSTEM/attachments/abcd-efgh")
        );

        EmailHistoryEntity entity = new EmailHistoryEntity();
        // Set other required fields
        entity.setId(null);
        entity.setSubject("Test Subject");
        entity.setText("File test email");
        entity.setAttachments(attachments);
        // Set a date for ordering if needed
        entity.setDate(new Date());

        entity = emailHistoryRepository.create(entity);
        String generatedId = entity.getId();

        EmailHistoryEntity retrieved = emailHistoryRepository.findByPrimaryKey(generatedId);
        Assertions.assertNotNull(retrieved, "Entity should be present in repository.");
        Assertions.assertNotNull(retrieved.getAttachments(), "Files should not be null.");

        List<FileAttributes> retrievedFileAttributes = retrieved.getAttachments();
        Assertions.assertEquals(2, retrievedFileAttributes.size(), "Files count should match.");
        Assertions.assertEquals("photo1.jpg", retrievedFileAttributes.getFirst().getFileName(), "First file name should match.");
        Assertions.assertEquals("FILE_SYSTEM/photos/1234-5678", retrievedFileAttributes.getFirst().getFilePath(), "First file path should match.");
    }
}
