package FacadeTests.Email;

import cz.inspire.EntityManagerProducer;
import cz.inspire.email.dto.EmailHistoryDto;
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

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

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
        // Ensure the attachments directory is cleaned up before the test
        Path testDir = Paths.get("FILE_SYSTEM/attachments");
        //deleteDirectoryRecursively(testDir);

        // Create DTO with attachments
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

        // Attachments as a Map<String, byte[]>
        Map<String, byte[]> attachments = new HashMap<>();
        String fileName = "testFile.txt";
        String fileContent = "Test file content";
        attachments.put(fileName, fileContent.getBytes());
        dto.setAttachments(attachments);

        // Create entity using the facade
        String createdId = emailHistoryFacade.create(dto);

        // Verify the entity in the database
        EmailHistoryEntity entity = entityManager.find(EmailHistoryEntity.class, createdId);
        assertNotNull(entity);
        assertEquals("Test email text", entity.getText());
        assertEquals("Test subject", entity.getSubject());
        assertNotNull(entity.getAttachments());

        // Verify JSONB structure
        List<Map<String, String>> dbAttachments = entity.getAttachments();
        assertEquals(1, dbAttachments.size());
        Map<String, String> attachment = dbAttachments.get(0);
        assertTrue(attachment.containsKey("FilePath"));
        assertTrue(attachment.containsKey("FileName"));

        // Verify the stored file
        String filePath = attachment.get("FilePath");
        Path normalizedPath = Paths.get(filePath).toAbsolutePath();
        assertTrue(Files.exists(normalizedPath), "File should exist: " + normalizedPath);

        // Validate the stored file content
        byte[] storedFileContent = Files.readAllBytes(normalizedPath);
        assertArrayEquals(fileContent.getBytes(), storedFileContent, "File content mismatch");

        // Verify file reconstruction in DTO
        EmailHistoryDto retrievedDto = emailHistoryFacade.findById(createdId).orElseThrow();
        assertNotNull(retrievedDto.getAttachments());
        assertEquals(1, retrievedDto.getAttachments().size());
        assertArrayEquals(fileContent.getBytes(), retrievedDto.getAttachments().get(fileName), "Reconstructed file content mismatch");
    }

    @Test
    public void testCreateEmailHistoryWithRealFile() throws Exception {
        // File path for the real file
        String filePath = "FILE_SYSTEM/file-example_PDF_1MB.pdf";
        Path testDir = Paths.get("FILE_SYSTEM/attachments");
        //deleteDirectoryRecursively(testDir);

        // Ensure the file exists before the test
        Path path = Paths.get(filePath);
        assertTrue(Files.exists(path), "File does not exist: " + filePath);

        // Convert the file to a byte array
        byte[] fileContent = Files.readAllBytes(path);

        // Create DTO with real file
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

        // Add the file as an attachment
        String fileName = "file-example_PDF_1MB.pdf";
        Map<String, byte[]> attachments = Map.of(fileName, fileContent);
        dto.setAttachments(attachments);

        // Create entity using the facade
        String createdId = emailHistoryFacade.create(dto);

        // Verify the entity in the database
        EmailHistoryEntity entity = entityManager.find(EmailHistoryEntity.class, createdId);
        assertNotNull(entity, "Entity should not be null");
        assertEquals("Email with a single file", entity.getText());
        assertEquals("Single File Test", entity.getSubject());

        // Verify JSONB structure
        List<Map<String, String>> dbAttachments = entity.getAttachments();
        assertEquals(1, dbAttachments.size());
        Map<String, String> attachment = dbAttachments.get(0);
        assertTrue(attachment.containsKey("FilePath"));
        assertTrue(attachment.containsKey("FileName"));

        // Verify the stored file
        String storedFilePath = attachment.get("FilePath");
        Path normalizedPath = Paths.get(storedFilePath).toAbsolutePath();
        assertTrue(Files.exists(normalizedPath), "File should exist: " + normalizedPath);

        // Validate the stored file content
        byte[] storedFileContent = Files.readAllBytes(normalizedPath);
        assertArrayEquals(fileContent, storedFileContent, "File content mismatch");

        // Verify file reconstruction in DTO
        EmailHistoryDto retrievedDto = emailHistoryFacade.findById(createdId).orElseThrow();
        assertNotNull(retrievedDto.getAttachments());
        assertEquals(1, retrievedDto.getAttachments().size());
        assertArrayEquals(fileContent, retrievedDto.getAttachments().get(fileName), "Reconstructed file content mismatch");
    }

}
