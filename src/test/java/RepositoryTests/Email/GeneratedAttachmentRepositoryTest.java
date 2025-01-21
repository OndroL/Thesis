package RepositoryTests.Email;

import cz.inspire.EntityManagerProducer;
import cz.inspire.email.entity.EmailHistoryEntity;
import cz.inspire.email.entity.GeneratedAttachmentEntity;
import cz.inspire.email.repository.GeneratedAttachmentRepository;
import cz.inspire.template.entity.PrintTemplateEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GeneratedAttachmentRepositoryTest {

    private GeneratedAttachmentRepository generatedAttachmentRepository;

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

        // Access GeneratedAttachmentRepository from CDI
        generatedAttachmentRepository = BeanProvider.getContextualReference(GeneratedAttachmentRepository.class);

        // Clear the database
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.createQuery("DELETE FROM GeneratedAttachmentEntity").executeUpdate();
        em.createQuery("DELETE FROM EmailHistoryEntity").executeUpdate();
        em.createQuery("DELETE FROM PrintTemplateEntity").executeUpdate();
        em.getTransaction().commit();
    }

    @Test
    public void testSaveAndFindAll() {
        assertNotNull("GeneratedAttachmentRepository should be initialized!", generatedAttachmentRepository);

        // Create and save related entities
        EmailHistoryEntity emailHistory = new EmailHistoryEntity("history-1", null, null, null, null, null, null, false, false, null, false, null);
        PrintTemplateEntity printTemplate = new PrintTemplateEntity("template-1", "Content", 1, "Template Name", "filename.pdf");

        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.persist(emailHistory);
        em.persist(printTemplate);
        em.getTransaction().commit();

        // Create and save a GeneratedAttachmentEntity
        GeneratedAttachmentEntity attachment = new GeneratedAttachmentEntity();
        attachment.setId("attachment-5");
        attachment.setEmail("test@example.com");
        attachment.setAttributes(Map.of("key1", "value1"));
        attachment.setEmailHistory(emailHistory);
        attachment.setPrintTemplate(printTemplate);

        generatedAttachmentRepository.save(attachment);

        // Retrieve all attachments
        List<GeneratedAttachmentEntity> attachments = generatedAttachmentRepository.findAll().toList();
        assertNotNull("Attachment list should not be null", attachments);
        assertEquals(1, attachments.size());
        assertEquals("attachment-5", attachments.get(0).getId());
    }

    @Test
    public void testFindByEmailAndHistory() {
        // Create and save related entities
        EmailHistoryEntity emailHistory = new EmailHistoryEntity("history-2", null, null, null, null, null, null, false, false, null, false, null);

        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.persist(emailHistory);
        em.getTransaction().commit();

        // Create and save a GeneratedAttachmentEntity
        GeneratedAttachmentEntity attachment = new GeneratedAttachmentEntity();
        attachment.setId("attachment-1");
        attachment.setEmail("test@example.com");
        attachment.setAttributes(Map.of("key1", "value1"));
        attachment.setEmailHistory(emailHistory);

        generatedAttachmentRepository.save(attachment);

        // Find by email and history
        List<GeneratedAttachmentEntity> result = generatedAttachmentRepository.findByEmailAndHistory("history-2", "test@example.com");
        assertNotNull("Result should not be null", result);
        assertEquals(1, result.size());
        assertEquals("attachment-1", result.get(0).getId());
    }

    @Test
    public void testFindByHistoryAndTemplate() {
        // Create and save related entities
        EmailHistoryEntity emailHistory = new EmailHistoryEntity("history-3", null, null, null, null, null, null, false, false, null, false, null);
        PrintTemplateEntity printTemplate = new PrintTemplateEntity("template-2", "Content", 1, "Template Name", "filename.pdf");

        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.persist(emailHistory);
        em.persist(printTemplate);
        em.getTransaction().commit();

        // Create and save a GeneratedAttachmentEntity
        GeneratedAttachmentEntity attachment = new GeneratedAttachmentEntity();
        attachment.setId("attachment-2");
        attachment.setEmail("test@example.com");
        attachment.setAttributes(Map.of("key1", "value1"));
        attachment.setEmailHistory(emailHistory);
        attachment.setPrintTemplate(printTemplate);

        generatedAttachmentRepository.save(attachment);

        // Find by email, history, and template
        List<GeneratedAttachmentEntity> result = generatedAttachmentRepository.findByEmailAndHistoryAndTemplate("history-3", "test@example.com", "template-2");
        assertNotNull("Result should not be null", result);
        assertEquals(1, result.size());
        assertEquals("attachment-2", result.get(0).getId());
    }
}

