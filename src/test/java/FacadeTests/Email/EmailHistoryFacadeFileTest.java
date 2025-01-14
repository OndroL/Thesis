package FacadeTests.Email;

import cz.inspire.EntityManagerProducer;
import cz.inspire.email.dto.EmailHistoryDto;
import cz.inspire.email.dto.GeneratedAttachmentDto;
import cz.inspire.email.entity.EmailHistoryEntity;
import cz.inspire.email.facade.EmailHistoryFacade;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class EmailHistoryFacadeFileTest {

    private EmailHistoryFacade emailHistoryFacade;

    private EntityManager entityManager;

    @Before
    public void setUp() {
        // Boot the CDI container
        CdiContainer container = CdiContainerLoader.getCdiContainer();
        container.boot();

        // Create the EntityManagerFactory
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");

        // Access EntityManagerProducer and set the EntityManagerFactory
        EntityManagerProducer producer = BeanProvider.getContextualReference(EntityManagerProducer.class);
        producer.setEntityManagerFactory(emf);

        // Manually create the EntityManager if not injected
        emailHistoryFacade = BeanProvider.getContextualReference(EmailHistoryFacade.class);
        entityManager = emf.createEntityManager();

        // Clear the database
        entityManager.getTransaction().begin();
        entityManager.createQuery("DELETE FROM GeneratedAttachmentEntity").executeUpdate();
        entityManager.createQuery("DELETE FROM EmailHistoryEntity").executeUpdate();
        entityManager.createQuery("DELETE FROM PrintTemplateEntity").executeUpdate();
        entityManager.getTransaction().commit();
    }

    @Test
    public void testCreateEmailHistoryWithFile() throws Exception {

        Path testDir = Paths.get("FILE_SYSTEM/Email_History/test-id");
        deleteDirectoryRecursively(testDir);


        // Create DTO
        EmailHistoryDto dto = new EmailHistoryDto();
        dto.setId("test-id");
        dto.setDate(new Date());
        dto.setText("Test email text");
        dto.setSubject("Test subject");
        dto.setGroups(List.of("group1", "group2"));
        dto.setRecipients(List.of("recipient1@example.com"));
        dto.setMoreRecipients(List.of("more1@example.com"));
        dto.setAutomatic(true);
        dto.setHtml(false);
        dto.setSent(false);

        // Attachments as byte arrays
        List<byte[]> attachments = new ArrayList<>();
        attachments.add("Test file content".getBytes());
        dto.setAttachments(attachments);

        // Generated Attachments
        List<GeneratedAttachmentDto> generatedAttachments = new ArrayList<>();
        GeneratedAttachmentDto attachmentDto = new GeneratedAttachmentDto();
        attachmentDto.setId("gen-attach-id");
        attachmentDto.setEmail("recipient1@example.com");
        attachmentDto.setAttributes(Map.of("key1", "value1"));
        generatedAttachments.add(attachmentDto);
        dto.setGeneratedAttachments(generatedAttachments);

        // Create entity using the facade
        String createdId = emailHistoryFacade.create(dto);

        // Verify the entity in the database
        EmailHistoryEntity entity = entityManager.find(EmailHistoryEntity.class, createdId);
        assertNotNull(entity);
        assertEquals("Test email text", entity.getText());
        assertEquals("Test subject", entity.getSubject());
        assertTrue(Files.exists(Paths.get("FILE_SYSTEM/Email_History/" + createdId)));

        // Verify generated attachments
        assertEquals(1, entity.getGeneratedAttachments().size());
        assertEquals("gen-attach-id", entity.getGeneratedAttachments().get(0).getId());
    }


    @Test
    public void testCreateEmailHistoryWithRealFile() throws Exception {
        // File path for the real file
        String filePath = "FILE_SYSTEM/file-example_PDF_1MB.pdf";
        Path testDir = Paths.get("FILE_SYSTEM/Email_History/test-id-single-file");
        deleteDirectoryRecursively(testDir);

        // Ensure the file exists before the test
        assertTrue(Files.exists(Paths.get(filePath)), "File does not exist: " + filePath);

        // Convert the file to a byte array
        byte[] fileContent = Files.readAllBytes(Paths.get(filePath));

        // Create DTO
        EmailHistoryDto dto = new EmailHistoryDto();
        dto.setId("test-id-single-file");
        dto.setDate(new Date());
        dto.setText("Email with a single file");
        dto.setSubject("Single File Test");
        dto.setGroups(List.of("group1", "group2"));
        dto.setRecipients(List.of("recipient1@example.com"));
        dto.setMoreRecipients(List.of("more1@example.com"));
        dto.setAutomatic(true);
        dto.setHtml(true);
        dto.setSent(true);
        dto.setAttachments(List.of(fileContent)); // Attach the single file

        // Create entity using the facade
        String createdId = emailHistoryFacade.create(dto);

        // Verify the entity in the database
        EmailHistoryEntity entity = entityManager.find(EmailHistoryEntity.class, createdId);
        assertNotNull(entity, "Entity should not be null");
        assertEquals("Email with a single file", entity.getText());
        assertEquals("Single File Test", entity.getSubject());

        // Verify the stored file
        String storedFilePath = entity.getAttachments().trim();
        System.out.println("Stored path from entity: [" + storedFilePath + "]");
        Path normalizedStoredPath = Paths.get(storedFilePath).toAbsolutePath().normalize();
        System.out.println("Normalized stored path: [" + normalizedStoredPath + "]");

        assertTrue(Files.exists(normalizedStoredPath), "Stored file does not exist: " + normalizedStoredPath);

        // Validate the stored file content
        byte[] storedFileContent = Files.readAllBytes(normalizedStoredPath);
        assertArrayEquals(fileContent, storedFileContent, "File content mismatch for: " + normalizedStoredPath);

        // Verify file reconstruction in DTO
        EmailHistoryDto retrievedDto = emailHistoryFacade.findById(createdId).orElseThrow();
        assertNotNull(retrievedDto.getAttachments(), "Attachments in DTO should not be null");
        assertEquals(1, retrievedDto.getAttachments().size(), "Number of reconstructed files mismatch");
        assertArrayEquals(fileContent, retrievedDto.getAttachments().getFirst(), "Reconstructed file content mismatch");
    }


    private void deleteDirectoryRecursively(Path directory) {
        if (Files.exists(directory)) {
            try {
                Files.walk(directory)
                        .sorted((a, b) -> b.compareTo(a)) // Delete children before parent
                        .forEach(path -> {
                            try {
                                Files.delete(path);
                            } catch (IOException e) {
                                System.err.println("Failed to delete: " + path);
                            }
                        });
            } catch (IOException e) {
                System.err.println("Failed to clear test directory: " + directory);
            }
        }
    }
}
