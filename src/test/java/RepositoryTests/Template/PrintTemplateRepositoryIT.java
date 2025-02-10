package RepositoryTests.Template;

import cz.inspire.template.entity.PrintTemplateEntity;
import cz.inspire.template.repository.PrintTemplateRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@QuarkusTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // Allows non-static @BeforeAll
public class PrintTemplateRepositoryIT {

    @Inject
    PrintTemplateRepository printTemplateRepository;

    /**
     * Clears the database before tests to ensure isolation.
     */
    @BeforeAll
    @ActivateRequestContext
    public  void clearDatabase() {
        List<PrintTemplateEntity> allEntities = new ArrayList<>();
        printTemplateRepository.findAll().forEach(allEntities::add);
        if (!allEntities.isEmpty()) {
            printTemplateRepository.deleteAll(allEntities);
        }
    }

    /**
     * Tests saving and retrieving a PrintTemplateEntity by ID.
     */
    @Test
    public void testSaveAndFindById() {
        PrintTemplateEntity entity = new PrintTemplateEntity(
                "TEMPLATE-001", "<html>Sample Template</html>", 1, "Invoice", "invoice.html");

        printTemplateRepository.save(entity);

        Optional<PrintTemplateEntity> retrieved = printTemplateRepository.findById("TEMPLATE-001");
        Assertions.assertTrue(retrieved.isPresent(), "Entity should be present in repository.");
        Assertions.assertEquals("Invoice", retrieved.get().getTemplateName(), "Template name should match.");
        Assertions.assertEquals("invoice.html", retrieved.get().getFileName(), "File name should match.");
        Assertions.assertEquals(1, retrieved.get().getType(), "Type should match.");
        Assertions.assertEquals("<html>Sample Template</html>", retrieved.get().getContent(), "Content should match.");
    }

    /**
     * Tests updating an existing PrintTemplateEntity.
     */
    @Test
    public void testUpdateEntity() {
        PrintTemplateEntity entity = new PrintTemplateEntity(
                "TEMPLATE-002", "<html>Old Content</html>", 2, "Report", "report.html");
        printTemplateRepository.save(entity);

        // Update template content and name
        entity.setContent("<html>Updated Content</html>");
        entity.setTemplateName("Updated Report");
        printTemplateRepository.save(entity);

        Optional<PrintTemplateEntity> updated = printTemplateRepository.findById("TEMPLATE-002");
        Assertions.assertTrue(updated.isPresent(), "Entity should still exist after update.");
        Assertions.assertEquals("<html>Updated Content</html>", updated.get().getContent(), "Updated content should match.");
        Assertions.assertEquals("Updated Report", updated.get().getTemplateName(), "Updated template name should match.");
    }

    /**
     * Tests deleting an entity.
     */
    @Test
    public void testDeleteEntity() {
        PrintTemplateEntity entity = new PrintTemplateEntity(
                "TEMPLATE-003", "<html>Template to Delete</html>", 3, "ToDelete", "delete.html");
        printTemplateRepository.save(entity);

        printTemplateRepository.deleteById("TEMPLATE-003");
        Optional<PrintTemplateEntity> deleted = printTemplateRepository.findById("TEMPLATE-003");
        Assertions.assertFalse(deleted.isPresent(), "Entity should be deleted from repository.");
    }

    /**
     * Tests that retrieving a non-existent entity returns empty.
     */
    @Test
    public void testFindNonExistentEntity() {
        Optional<PrintTemplateEntity> retrieved = printTemplateRepository.findById("NON_EXISTENT");
        Assertions.assertFalse(retrieved.isPresent(), "Should return empty for non-existent entity.");
    }
}
