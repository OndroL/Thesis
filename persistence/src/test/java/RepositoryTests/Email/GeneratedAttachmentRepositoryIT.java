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
        List<GeneratedAttachmentEntity> allEntities = new ArrayList<>(generatedAttachmentRepository.findAll());
        if (!allEntities.isEmpty()) {
            generatedAttachmentRepository.deleteAll(allEntities);
        }
    }

    @Test
    public void testSaveAndFindById() {
        // Create EmailHistoryEntity and PrintTemplateEntity with null ID
        EmailHistoryEntity emailHistory = new EmailHistoryEntity(
                null, new Date(), "Sample Text", "Subject",
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                false, false, new ArrayList<>(), true, new ArrayList<>()
        );
        emailHistory = emailHistoryRepository.create(emailHistory);

        PrintTemplateEntity printTemplate = new PrintTemplateEntity(
                null, "Template Content", 1, "Invoice Template", "invoice.pdf"
        );
        printTemplate = printTemplateRepository.create(printTemplate);

        Map<String, Object> attributes = Map.of("key1", "value1", "key2", 123);

        // Pass null as the first parameter to GeneratedAttachmentEntity
        GeneratedAttachmentEntity entity = new GeneratedAttachmentEntity(
                null, "user@example.com", attributes, emailHistory, printTemplate
        );
        entity = generatedAttachmentRepository.create(entity);
        String generatedId = entity.getId();

        GeneratedAttachmentEntity retrieved = generatedAttachmentRepository.findById(generatedId);
        Assertions.assertNotNull(retrieved, "Entity should be present.");
        Assertions.assertEquals("user@example.com", retrieved.getEmail(), "Email should match.");
        Assertions.assertEquals(emailHistory.getId(), retrieved.getEmailHistory().getId(), "Email history ID should match.");
        Assertions.assertEquals(printTemplate.getId(), retrieved.getPrintTemplate().getId(), "Print template ID should match.");
        Assertions.assertEquals(2, retrieved.getAttributes().size(), "Attributes should match.");
    }

    @Test
    public void testFindByEmailAndHistoryAndTemplate() {
        EmailHistoryEntity emailHistory = new EmailHistoryEntity(
                null, new Date(), "Email Content", "Subject",
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                false, false, new ArrayList<>(), true, new ArrayList<>()
        );
        emailHistory = emailHistoryRepository.create(emailHistory);

        PrintTemplateEntity printTemplate = new PrintTemplateEntity(
                null, "Report Content", 2, "Report Template", "report.pdf"
        );
        printTemplate = printTemplateRepository.create(printTemplate);

        GeneratedAttachmentEntity entity = new GeneratedAttachmentEntity(
                null, "user@example.com", Map.of("key", "val"), emailHistory, printTemplate
        );
        entity = generatedAttachmentRepository.create(entity);

        List<GeneratedAttachmentEntity> results = generatedAttachmentRepository.findByEmailAndHistoryAndTemplate(
                emailHistory.getId(), "user@example.com", printTemplate.getId()
        );
        Assertions.assertEquals(1, results.size(), "Expected 1 matching attachment.");
        Assertions.assertEquals(entity.getId(), results.get(0).getId(), "Expected generated ID to match.");
    }

    @Test
    public void testUpdateEntity() {
        EmailHistoryEntity emailHistory = new EmailHistoryEntity(
                null, new Date(), "Sample Content", "Some Subject",
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                false, false, new ArrayList<>(), true, new ArrayList<>()
        );
        emailHistory = emailHistoryRepository.create(emailHistory);

        PrintTemplateEntity printTemplate = new PrintTemplateEntity(
                null, "Contract Template", 3, "Contract", "contract.pdf"
        );
        printTemplate = printTemplateRepository.create(printTemplate);

        GeneratedAttachmentEntity entity = new GeneratedAttachmentEntity(
                null, "test@example.com", Map.of("field", "old"), emailHistory, printTemplate
        );
        entity = generatedAttachmentRepository.create(entity);
        String generatedId = entity.getId();

        entity.setEmail("updated@example.com");
        entity.setAttributes(Map.of("field", "new"));
        generatedAttachmentRepository.create(entity);

        GeneratedAttachmentEntity updated = generatedAttachmentRepository.findById(generatedId);
        Assertions.assertNotNull(updated, "Entity should exist after update.");
        Assertions.assertEquals("updated@example.com", updated.getEmail(), "Updated email should match.");
        Assertions.assertEquals("new", updated.getAttributes().get("field"), "Updated attribute should match.");
    }

    @Test
    public void testDeleteEntity() {
        PrintTemplateEntity printTemplate = new PrintTemplateEntity(
                null, "Simple Template", 4, "Simple Report", "simple.pdf"
        );
        printTemplate = printTemplateRepository.create(printTemplate);

        GeneratedAttachmentEntity entity = new GeneratedAttachmentEntity(
                null, "delete@example.com", Map.of("key", "val"), null, printTemplate
        );
        entity = generatedAttachmentRepository.create(entity);
        String generatedId = entity.getId();

        generatedAttachmentRepository.deleteById(generatedId);

        GeneratedAttachmentEntity deleted = generatedAttachmentRepository.findById(generatedId);
        Assertions.assertNull(deleted, "Entity should be deleted.");
    }
}
