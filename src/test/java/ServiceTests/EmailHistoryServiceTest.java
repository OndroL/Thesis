package ServiceTests;

import cz.inspire.thesis.data.EntityManagerProducer;
import cz.inspire.thesis.data.dto.EmailHistoryDetails;
import cz.inspire.thesis.data.dto.GeneratedAttachmentDetails;
import cz.inspire.thesis.data.model.EmailHistoryEntity;
import cz.inspire.thesis.data.model.GeneratedAttachmentEntity;
import cz.inspire.thesis.data.repository.EmailHistoryRepository;
import cz.inspire.thesis.data.repository.GeneratedAttachmentRepository;
import cz.inspire.thesis.data.service.EmailHistoryService;
import jakarta.persistence.EntityManager;
import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.junit.Before;
import org.junit.Test;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class EmailHistoryServiceTest {

    private EmailHistoryService emailHistoryService;
    private GeneratedAttachmentRepository generatedAttachmentRepository;
    private EmailHistoryRepository emailHistoryRepository;

    @Before
    public void setUp() {
        // Boot the CDI container
        CdiContainer container = CdiContainerLoader.getCdiContainer();
        container.boot();

        // Set up EntityManagerFactory
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        EntityManagerProducer producer = BeanProvider.getContextualReference(EntityManagerProducer.class);
        producer.setEntityManagerFactory(emf);

        // Access services and repositories
        emailHistoryService = BeanProvider.getContextualReference(EmailHistoryService.class);
        generatedAttachmentRepository = BeanProvider.getContextualReference(GeneratedAttachmentRepository.class);
        emailHistoryRepository = BeanProvider.getContextualReference(EmailHistoryRepository.class);

        // Clean up the database
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.createQuery("DELETE FROM GeneratedAttachmentEntity").executeUpdate();
        em.createQuery("DELETE FROM EmailHistoryEntity").executeUpdate();
        em.getTransaction().commit();
    }

    @Test
    public void testCreateEmailHistoryWithGeneratedAttachments() throws Exception {
        // Create a list of GeneratedAttachmentDetails
        List<GeneratedAttachmentDetails> attachmentDetailsList = new ArrayList<>();
        attachmentDetailsList.add(new GeneratedAttachmentDetails(null, "email1@example.com", null, null, null));
        attachmentDetailsList.add(new GeneratedAttachmentDetails(null, "email2@example.com", null, null, null));

        // Create EmailHistoryDetails with attachments
        EmailHistoryDetails emailDetails = new EmailHistoryDetails();
        emailDetails.setId(null); // Let the service generate the ID
        emailDetails.setDate(new Date());
        emailDetails.setText("This is a test email.");
        emailDetails.setSubject("Test Subject");
        emailDetails.setGroups(new ArrayList<>());
        emailDetails.setRecipients(new ArrayList<>());
        emailDetails.setMoreRecipients(new ArrayList<>());
        emailDetails.setAutomatic(false);
        emailDetails.setHtml(true);
        emailDetails.setSent(true);
        emailDetails.setGeneratedAttachments(attachmentDetailsList);

        // Save EmailHistoryEntity and its attachments
        String emailHistoryId = emailHistoryService.ejbCreate(emailDetails);

        // Verify EmailHistoryEntity is created
        Optional<EmailHistoryEntity> savedEmailHistory = emailHistoryRepository.findById(emailHistoryId);
        assertTrue("EmailHistoryEntity should exist in the database", savedEmailHistory.isPresent());

        EmailHistoryEntity emailHistoryEntity = savedEmailHistory.get();
        assertEquals("Email text should match", "This is a test email.", emailHistoryEntity.getText());
        assertEquals("Email subject should match", "Test Subject", emailHistoryEntity.getSubject());
        assertTrue("Email should be marked as sent", emailHistoryEntity.getSent());

        // Verify GeneratedAttachmentEntity records are created
        List<GeneratedAttachmentEntity> attachments = generatedAttachmentRepository.findByHistory(emailHistoryId);
        assertEquals("Two attachments should be created", 2, attachments.size());

        GeneratedAttachmentEntity attachment1 = attachments.get(0);
        assertEquals("First attachment email should match", "email1@example.com", attachment1.getEmail());

        GeneratedAttachmentEntity attachment2 = attachments.get(1);
        assertEquals("Second attachment email should match", "email2@example.com", attachment2.getEmail());

        // Verify the relationship between EmailHistoryEntity and GeneratedAttachmentEntity
        assertEquals("EmailHistoryEntity should reference two attachments", 2, emailHistoryEntity.getGeneratedAttachments().size());
    }
}

