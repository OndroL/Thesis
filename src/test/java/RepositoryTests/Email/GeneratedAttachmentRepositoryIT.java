package RepositoryTests.Email;

import cz.inspire.email.entity.GeneratedAttachmentEntity;
import cz.inspire.email.entity.EmailHistoryEntity;
import cz.inspire.template.entity.PrintTemplateEntity;
import cz.inspire.email.repository.GeneratedAttachmentRepository;
import cz.inspire.email.repository.EmailHistoryRepository;
import cz.inspire.template.repository.PrintTemplateRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.*;

@QuarkusTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GeneratedAttachmentRepositoryIT {

    @Inject
    GeneratedAttachmentRepository generatedAttachmentRepository;

    @Inject
    EmailHistoryRepository emailHistoryRepository;

    @Inject
    PrintTemplateRepository printTemplateRepository;

    @BeforeAll
    @ActivateRequestContext
    public void clearDatabase() {
        List<GeneratedAttachmentEntity> allEntities = new ArrayList<>();
        generatedAttachmentRepository.findAll().forEach(allEntities::add);
        if (!allEntities.isEmpty()) {
            generatedAttachmentRepository.deleteAll(allEntities);
        }
    }

    @Test
    public void testSaveAndFindById() {
        EmailHistoryEntity emailHistory = new EmailHistoryEntity("HIST-001", new Date(), "Sample Text", "Subject", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), false, false, new ArrayList<>(), true, new ArrayList<>());
        emailHistoryRepository.save(emailHistory);

        PrintTemplateEntity printTemplate = new PrintTemplateEntity("TPL-001", "Template A");
        printTemplateRepository.save(printTemplate);

        Map<String, Object> attributes = Map.of("key1", "value1", "key2", 123);

        GeneratedAttachmentEntity entity = new GeneratedAttachmentEntity(
                "ATT-001", "user@example.com", attributes, emailHistory, printTemplate
        );

        generatedAttachmentRepository.save(entity);

        Optional<GeneratedAttachmentEntity> retrieved = generatedAttachmentRepository.findById("ATT-001");
        Assertions.assertTrue(retrieved.isPresent(), "Entity should be present.");
        Assertions.assertEquals("user@example.com", retrieved.get().getEmail(), "Email should match.");
        Assertions.assertEquals(emailHistory.getId(), retrieved.get().getEmailHistory().getId(), "Email history ID should match.");
        Assertions.assertEquals(printTemplate.getId(), retrieved.get().getPrintTemplate().getId(), "Print template ID should match.");
        Assertions.assertEquals(2, retrieved.get().getAttributes().size(), "Attributes should match.");
    }

    @Test
    public void testFindByEmailAndHistory() {
        EmailHistoryEntity emailHistory = new EmailHistoryEntity("HIST-002", new Date(), "Email Text", "Another Subject", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), false, false, new ArrayList<>(), true, new ArrayList<>());
        emailHistoryRepository.save(emailHistory);

        GeneratedAttachmentEntity entity1 = new GeneratedAttachmentEntity("ATT-002", "user@example.com", Map.of("data", "value"), emailHistory, null);
        GeneratedAttachmentEntity entity2 = new GeneratedAttachmentEntity("ATT-003", "other@example.com", Map.of("data", "value"), emailHistory, null);
        generatedAttachmentRepository.save(entity1);
        generatedAttachmentRepository.save(entity2);

        List<GeneratedAttachmentEntity> results = generatedAttachmentRepository.findByEmailAndHistory("HIST-002", "user@example.com");
        Assertions.assertEquals(1, results.size(), "Expected 1 attachment for this email and history.");
        Assertions.assertEquals("ATT-002", results.get(0).getId(), "Expected ATT-002.");
    }

    @Test
    public void testFindByHistory() {
        EmailHistoryEntity emailHistory = new EmailHistoryEntity("HIST-003", new Date(), "Email Text", "Another Subject", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), false, false, new ArrayList<>(), true, new ArrayList<>());
        emailHistoryRepository.save(emailHistory);

        GeneratedAttachmentEntity entity1 = new GeneratedAttachmentEntity("ATT-004", "user1@example.com", Map.of("key", "val1"), emailHistory, null);
        GeneratedAttachmentEntity entity2 = new GeneratedAttachmentEntity("ATT-005", "user2@example.com", Map.of("key", "val2"), emailHistory, null);
        generatedAttachmentRepository.save(entity1);
        generatedAttachmentRepository.save(entity2);

        List<GeneratedAttachmentEntity> results = generatedAttachmentRepository.findByHistory("HIST-003");
        Assertions.assertEquals(2, results.size(), "Expected 2 attachments for this history ID.");
    }

    @Test
    public void testFindByEmailAndHistoryAndTemplate() {
        EmailHistoryEntity emailHistory = new EmailHistoryEntity("HIST-004", new Date(), "Email Content", "Subject", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), false, false, new ArrayList<>(), true, new ArrayList<>());
        emailHistoryRepository.save(emailHistory);

        PrintTemplateEntity printTemplate = new PrintTemplateEntity("TPL-001", "Template Content", 1, "Invoice Template", "invoice.pdf");
        printTemplateRepository.save(printTemplate);

        GeneratedAttachmentEntity entity = new GeneratedAttachmentEntity("ATT-006", "user@example.com", Map.of("key", "val"), emailHistory, printTemplate);
        generatedAttachmentRepository.save(entity);

        List<GeneratedAttachmentEntity> results = generatedAttachmentRepository.findByEmailAndHistoryAndTemplate("HIST-004", "user@example.com", "TPL-002");
        Assertions.assertEquals(1, results.size(), "Expected 1 matching attachment.");
        Assertions.assertEquals("ATT-006", results.get(0).getId(), "Expected ATT-006.");
    }

    @Test
    public void testUpdateEntity() {
        EmailHistoryEntity emailHistory = new EmailHistoryEntity("HIST-005", new Date(), "Sample Content", "Some Subject", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), false, false, new ArrayList<>(), true, new ArrayList<>());
        emailHistoryRepository.save(emailHistory);

        GeneratedAttachmentEntity entity = new GeneratedAttachmentEntity("ATT-007", "test@example.com", Map.of("field", "old"), emailHistory, null);
        generatedAttachmentRepository.save(entity);

        entity.setEmail("updated@example.com");
        entity.setAttributes(Map.of("field", "new"));
        generatedAttachmentRepository.save(entity);

        Optional<GeneratedAttachmentEntity> updated = generatedAttachmentRepository.findById("ATT-007");
        Assertions.assertTrue(updated.isPresent(), "Entity should exist after update.");
        Assertions.assertEquals("updated@example.com", updated.get().getEmail(), "Updated email should match.");
        Assertions.assertEquals("new", updated.get().getAttributes().get("field"), "Updated attribute should match.");
    }

    @Test
    public void testDeleteEntity() {
        GeneratedAttachmentEntity entity = new GeneratedAttachmentEntity("ATT-008", "delete@example.com", Map.of("key", "val"), null, null);
        generatedAttachmentRepository.save(entity);

        generatedAttachmentRepository.deleteById("ATT-008");

        Optional<GeneratedAttachmentEntity> deleted = generatedAttachmentRepository.findById("ATT-008");
        Assertions.assertFalse(deleted.isPresent(), "Entity should be deleted.");
    }
}
