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

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class EmailHistoryFacadeTest {

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
    public void testCreateEmailHistory() throws Exception {
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

}
