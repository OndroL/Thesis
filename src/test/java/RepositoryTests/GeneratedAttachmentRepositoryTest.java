package RepositoryTests;

import cz.inspire.thesis.data.EntityManagerProducer;
import cz.inspire.thesis.data.model.EmailHistoryEntity;
import cz.inspire.thesis.data.model.GeneratedAttachmentEntity;
import cz.inspire.thesis.data.model.PrintTemplateEntity;
import cz.inspire.thesis.data.repository.GeneratedAttachmentRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class GeneratedAttachmentRepositoryTest {

    private GeneratedAttachmentRepository generatedAttachmentRepository;
    private EntityManager entityManager;

    @Before
    public void setUp() {
        // Boot CDI container
        CdiContainer container = CdiContainerLoader.getCdiContainer();
        container.boot();

        // Create the EntityManagerFactory
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");

        // Access EntityManagerProducer and set the EntityManagerFactory
        EntityManagerProducer producer = BeanProvider.getContextualReference(EntityManagerProducer.class);
        producer.setEntityManagerFactory(emf);

        // Access GeneratedAttachmentRepository from CDI
        generatedAttachmentRepository = BeanProvider.getContextualReference(GeneratedAttachmentRepository.class);
        entityManager = BeanProvider.getContextualReference(EntityManager.class);

        // Clear the database
        entityManager.getTransaction().begin();
        entityManager.createQuery("DELETE FROM GeneratedAttachmentEntity").executeUpdate();
        entityManager.createQuery("DELETE FROM EmailHistoryEntity").executeUpdate();
        entityManager.createQuery("DELETE FROM PrintTemplateEntity").executeUpdate();
        entityManager.getTransaction().commit();
    }

    private EmailHistoryEntity createEmailHistory(String id) {
        EmailHistoryEntity emailHistory = new EmailHistoryEntity(id, new java.util.Date(), "Body", "Subject", null, null, null, true, true, null, true, null);
        entityManager.getTransaction().begin();
        entityManager.persist(emailHistory);
        entityManager.getTransaction().commit();
        return emailHistory;
    }

    private PrintTemplateEntity createPrintTemplate(String id, String content, int type, String templateName, String fileName) {
        PrintTemplateEntity template = new PrintTemplateEntity(id, content, type, templateName, fileName);
        entityManager.getTransaction().begin();
        entityManager.persist(template);
        entityManager.getTransaction().commit();
        return template;
    }

    @Test
    public void testFindByEmailAndHistory() {
        assertNotNull("GeneratedAttachmentRepository should be initialized!", generatedAttachmentRepository);

        // Create dependencies
        EmailHistoryEntity history1 = createEmailHistory("history1");
        EmailHistoryEntity history2 = createEmailHistory("history2");

        // Save entities
        generatedAttachmentRepository.save(new GeneratedAttachmentEntity("1", "user1@example.com", null, history1, null));
        generatedAttachmentRepository.save(new GeneratedAttachmentEntity("2", "user2@example.com", null, history1, null));
        generatedAttachmentRepository.save(new GeneratedAttachmentEntity("3", "user1@example.com", null, history2, null));

        // Test findByEmailAndHistory
        List<GeneratedAttachmentEntity> results = generatedAttachmentRepository.findByEmailAndHistory("history1", "user1@example.com");
        assertNotNull("Result list should not be null", results);
        assertEquals(1, results.size());
        assertEquals("1", results.getFirst().getId());
    }

    @Test
    public void testFindByHistory() {
        assertNotNull("GeneratedAttachmentRepository should be initialized!", generatedAttachmentRepository);

        // Create dependencies
        EmailHistoryEntity history = createEmailHistory("history1");

        // Save entities
        generatedAttachmentRepository.save(new GeneratedAttachmentEntity("1", "user1@example.com", null, history, null));
        generatedAttachmentRepository.save(new GeneratedAttachmentEntity("2", "user2@example.com", null, history, null));

        // Test findByHistory
        List<GeneratedAttachmentEntity> results = generatedAttachmentRepository.findByHistory("history1");
        assertNotNull("Result list should not be null", results);
        assertEquals(2, results.size());
    }

    @Test
    public void testFindByEmailAndHistoryAndTemplate() {
        assertNotNull("GeneratedAttachmentRepository should be initialized!", generatedAttachmentRepository);

        // Create dependencies
        EmailHistoryEntity history = createEmailHistory("history1");
        PrintTemplateEntity template1 = createPrintTemplate("template1", "Content1", 1, "TemplateName1", "FileName1");
        PrintTemplateEntity template2 = createPrintTemplate("template2", "Content2", 2, "TemplateName2", "FileName2");

        // Save entities
        generatedAttachmentRepository.save(new GeneratedAttachmentEntity("1", "user1@example.com", null, history, template1));
        generatedAttachmentRepository.save(new GeneratedAttachmentEntity("2", "user1@example.com", null, history, template2));

        // Test findByEmailAndHistoryAndTemplate
        List<GeneratedAttachmentEntity> results = generatedAttachmentRepository.findByEmailAndHistoryAndTemplate("history1", "user1@example.com", "template1");
        assertNotNull("Result list should not be null", results);
        assertEquals(1, results.size());
        assertEquals("1", results.getFirst().getId());
    }
}
