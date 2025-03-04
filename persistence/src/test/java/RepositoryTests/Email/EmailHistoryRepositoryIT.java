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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@QuarkusTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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
     * Tests saving and retrieving an EmailHistoryEntity by ID.
     */
    @Test
    public void testSaveAndFindById() {
        List<FileAttributes> attachments = List.of(
                new FileAttributes("attachment.pdf", "FILE_SYSTEM/attachments/68057fc4-91a4-46bd-8229-7f770530a644")
        );

        EmailHistoryEntity entity = new EmailHistoryEntity(
                "EMAIL-001", new Date(), "Test email content", "Test Subject",
                Arrays.asList("GroupA", "GroupB"), Arrays.asList("user1@example.com", "user2@example.com"),
                List.of("extra@example.com"), true, false,
                attachments, true, new ArrayList<>());

        emailHistoryRepository.create(entity);

        EmailHistoryEntity retrieved = emailHistoryRepository.findById("EMAIL-001");
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
    @Test
    public void testFindAllOrdered() {
        EmailHistoryEntity entity1 = new EmailHistoryEntity("EMAIL-001", new Date(System.currentTimeMillis() - 10000), "Old email", "Old Subject",
                List.of(), List.of(), List.of(), true, false, new ArrayList<>(), true, new ArrayList<>());
        EmailHistoryEntity entity2 = new EmailHistoryEntity("EMAIL-002", new Date(), "New email", "New Subject",
                List.of(), List.of(), List.of(), false, true, new ArrayList<>(), false, new ArrayList<>());

        emailHistoryRepository.create(entity1);
        emailHistoryRepository.create(entity2);

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

        EmailHistoryEntity entity1 = new EmailHistoryEntity("EMAIL-001", past, "Old email", "Subject1", List.of(), List.of(), List.of(), true, false, new ArrayList<>(), true, new ArrayList<>());
        EmailHistoryEntity entity2 = new EmailHistoryEntity("EMAIL-002", now, "Current email", "Subject2", List.of(), List.of(), List.of(), false, true, new ArrayList<>(), true, new ArrayList<>());
        EmailHistoryEntity entity3 = new EmailHistoryEntity("EMAIL-003", future, "Future email", "Subject3", List.of(), List.of(), List.of(), true, true, new ArrayList<>(), true, new ArrayList<>());

        emailHistoryRepository.create(entity1);
        emailHistoryRepository.create(entity2);
        emailHistoryRepository.create(entity3);

        List<EmailHistoryEntity> results = emailHistoryRepository.findByDate(
                new Timestamp(past.getTime()), new Timestamp(future.getTime()), 10, 0);

        Assertions.assertEquals(3, results.size(), "Expected 3 emails in the given date range.");
    }

    /**
     * Tests updating an existing EmailHistoryEntity.
     */
    @Test
    public void testUpdateEntity() {
        EmailHistoryEntity entity = new EmailHistoryEntity(
                "EMAIL-004", new Date(), "Initial content", "Initial Subject",
                List.of("InitialGroup"), List.of("initial@example.com"),
                List.of(), false, false, new ArrayList<>(), false, new ArrayList<>());
        emailHistoryRepository.create(entity);

        entity.setText("Updated content");
        entity.setSubject("Updated Subject");
        entity.setSent(true);
        entity.setAttachments(List.of(new FileAttributes("updated.pdf", "http://example.com/updated.pdf")));
        emailHistoryRepository.create(entity);

        EmailHistoryEntity updated = emailHistoryRepository.findById("EMAIL-004");
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
    @Test
    public void testDeleteEntity() {
        EmailHistoryEntity entity = new EmailHistoryEntity(
                "EMAIL-005", new Date(), "To be deleted", "Delete Subject",
                List.of("DeleteGroup"), List.of("delete@example.com"),
                List.of(), true, true, new ArrayList<>(), false, new ArrayList<>());
        emailHistoryRepository.create(entity);

        emailHistoryRepository.deleteById("EMAIL-005");
        EmailHistoryEntity deleted = emailHistoryRepository.findById("EMAIL-005");
        Assertions.assertNull(deleted, "Entity should be deleted from repository.");
    }

    /**
     * Tests saving and retrieving email history records with multiple files.
     */
    @Test
    public void testSaveAndRetrieveFiles() {
        List<FileAttributes> attachments = List.of(
                new FileAttributes("photo1.jpg", "FILE_SYSTEM/photos/1234-5678"),
                new FileAttributes("document.pdf", "FILE_SYSTEM/attachments/abcd-efgh")
        );

        EmailHistoryEntity entity = new EmailHistoryEntity();
        entity.setId("EMAIL-007");
        entity.setSubject("Test Subject");
        entity.setText("File test email");
        entity.setAttachments(attachments);

        emailHistoryRepository.create(entity);

        EmailHistoryEntity retrieved = emailHistoryRepository.findById("EMAIL-007");
        Assertions.assertNotNull(retrieved, "Entity should be present in repository.");
        Assertions.assertNotNull(retrieved.getAttachments(), "Files should not be null.");

        List<FileAttributes> retrievedFileAttributes = retrieved.getAttachments();
        Assertions.assertEquals(2, retrievedFileAttributes.size(), "Files count should match.");
        Assertions.assertEquals("photo1.jpg", retrievedFileAttributes.getFirst().getFileName(), "First file name should match.");
        Assertions.assertEquals("FILE_SYSTEM/photos/1234-5678", retrievedFileAttributes.getFirst().getFilePath(), "First file path should match.");
    }
}
