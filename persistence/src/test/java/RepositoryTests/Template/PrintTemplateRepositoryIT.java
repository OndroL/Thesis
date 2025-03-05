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
        List<PrintTemplateEntity> allEntities = new ArrayList<>(printTemplateRepository.findAll());
        if (!allEntities.isEmpty()) {
            printTemplateRepository.deleteAll(allEntities);
        }
    }

    /**
     * Tests saving and retrieving a PrintTemplateEntity by generated ID.
     */
    @Test
    public void testSaveAndFindById() {
        // Pass null for the ID so that it is generated automatically.
        PrintTemplateEntity entity = new PrintTemplateEntity(
                null, "<html>Sample Template</html>", 1, "Invoice", "invoice.html"
        );
        entity = printTemplateRepository.create(entity);
        String generatedId = entity.getId();
        Assertions.assertNotNull(generatedId, "Generated ID should not be null");

        PrintTemplateEntity retrieved = printTemplateRepository.findById(generatedId);
        Assertions.assertNotNull(retrieved, "Entity should be present in repository.");
        Assertions.assertEquals("Invoice", retrieved.getTemplateName(), "Template name should match.");
        Assertions.assertEquals("invoice.html", retrieved.getFileName(), "File name should match.");
        Assertions.assertEquals(1, retrieved.getType(), "Type should match.");
        Assertions.assertEquals("<html>Sample Template</html>", retrieved.getContent(), "Content should match.");
    }

    /**
     * Tests updating an existing PrintTemplateEntity.
     */
    @Test
    public void testUpdateEntity() {
        // Pass null for the ID
        PrintTemplateEntity entity = new PrintTemplateEntity(
                null, "<html>Old Content</html>", 2, "Report", "report.html"
        );
        entity = printTemplateRepository.create(entity);
        String generatedId = entity.getId();
        Assertions.assertNotNull(generatedId, "Generated ID should not be null");

        // Update template content and name
        entity.setContent("<html>Updated Content</html>");
        entity.setTemplateName("Updated Report");
        printTemplateRepository.create(entity);

        PrintTemplateEntity updated = printTemplateRepository.findById(generatedId);
        Assertions.assertNotNull(updated, "Entity should still exist after update.");
        Assertions.assertEquals("<html>Updated Content</html>", updated.getContent(), "Updated content should match.");
        Assertions.assertEquals("Updated Report", updated.getTemplateName(), "Updated template name should match.");
    }

    /**
     * Tests deleting an entity.
     */
    @Test
    public void testDeleteEntity() {
        // Pass null for the ID
        PrintTemplateEntity entity = new PrintTemplateEntity(
                null, "<html>Template to Delete</html>", 3, "ToDelete", "delete.html"
        );
        entity = printTemplateRepository.create(entity);
        String generatedId = entity.getId();
        Assertions.assertNotNull(generatedId, "Generated ID should not be null");

        printTemplateRepository.deleteById(generatedId);
        PrintTemplateEntity deleted = printTemplateRepository.findById(generatedId);
        Assertions.assertNull(deleted, "Entity should be deleted from repository.");
    }

    /**
     * Tests that retrieving a non-existent entity returns empty.
     */
    @Test
    public void testFindNonExistentEntity() {
        PrintTemplateEntity retrieved = printTemplateRepository.findById("NON_EXISTENT");
        Assertions.assertNull(retrieved, "Should return empty for non-existent entity.");
    }
}
