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

        PrintTemplateEntity printTemplate = new PrintTemplateEntity("TPL-001", "Template Content", 1, "Invoice Template", "invoice.pdf");
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
    public void testFindByEmailAndHistoryAndTemplate() {
        EmailHistoryEntity emailHistory = new EmailHistoryEntity("HIST-004", new Date(), "Email Content", "Subject", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), false, false, new ArrayList<>(), true, new ArrayList<>());
        emailHistoryRepository.save(emailHistory);

        PrintTemplateEntity printTemplate = new PrintTemplateEntity("TPL-002", "Report Content", 2, "Report Template", "report.pdf");
        printTemplateRepository.save(printTemplate);

        GeneratedAttachmentEntity entity = new GeneratedAttachmentEntity("ATT-006", "user@example.com", Map.of("key", "val"), emailHistory, printTemplate);
        generatedAttachmentRepository.save(entity);

        List<GeneratedAttachmentEntity> results = generatedAttachmentRepository.findByEmailAndHistoryAndTemplate("HIST-004", "user@example.com", "TPL-002");
        Assertions.assertEquals(1, results.size(), "Expected 1 matching attachment.");
        Assertions.assertEquals("ATT-006", results.getFirst().getId(), "Expected ATT-006.");
    }

    @Test
    public void testUpdateEntity() {
        EmailHistoryEntity emailHistory = new EmailHistoryEntity("HIST-005", new Date(), "Sample Content", "Some Subject", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), false, false, new ArrayList<>(), true, new ArrayList<>());
        emailHistoryRepository.save(emailHistory);

        PrintTemplateEntity printTemplate = new PrintTemplateEntity("TPL-003", "Contract Template", 3, "Contract", "contract.pdf");
        printTemplateRepository.save(printTemplate);

        GeneratedAttachmentEntity entity = new GeneratedAttachmentEntity("ATT-007", "test@example.com", Map.of("field", "old"), emailHistory, printTemplate);
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
        PrintTemplateEntity printTemplate = new PrintTemplateEntity("TPL-004", "Simple Template", 4, "Simple Report", "simple.pdf");
        printTemplateRepository.save(printTemplate);

        GeneratedAttachmentEntity entity = new GeneratedAttachmentEntity("ATT-008", "delete@example.com", Map.of("key", "val"), null, printTemplate);
        generatedAttachmentRepository.save(entity);

        generatedAttachmentRepository.deleteById("ATT-008");

        Optional<GeneratedAttachmentEntity> deleted = generatedAttachmentRepository.findById("ATT-008");
        Assertions.assertFalse(deleted.isPresent(), "Entity should be deleted.");
    }
}
