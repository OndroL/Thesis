package RepositoryTests.Email;

import cz.inspire.EntityManagerProducer;
import cz.inspire.email.entity.EmailHistoryEntity;
import cz.inspire.email.repository.EmailHistoryRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class EmailHistoryRepositoryTest {

    private EmailHistoryRepository emailHistoryRepository;

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

        // Access EmailHistoryRepository from CDI
        emailHistoryRepository = BeanProvider.getContextualReference(EmailHistoryRepository.class);

        // Clear the database
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.createQuery("DELETE FROM EmailHistoryEntity").executeUpdate();
        em.getTransaction().commit();
    }

    @Test
    public void testSaveAndFindAll() {
        assertNotNull("EmailHistoryRepository should be initialized!", emailHistoryRepository);

        // Create and save an EmailHistoryEntity
        EmailHistoryEntity history = new EmailHistoryEntity();
        history.setId("history-100");
        history.setDate(new Date());
        history.setText("Test email text");
        history.setSubject("Test subject");
        history.setGroups(Arrays.asList("group1", "group2"));
        history.setRecipients(Arrays.asList("recipient1@example.com"));
        history.setMoreRecipients(Arrays.asList("more1@example.com"));
        history.setAutomatic(true);
        history.setHtml(true);
        history.setAttachments(null);
        history.setSent(true);

        emailHistoryRepository.save(history);

        // Retrieve all email history
        List<EmailHistoryEntity> histories = emailHistoryRepository.findAll();
        assertNotNull("Email history list should not be null", histories);
        assertEquals(1, histories.size());
        assertEquals("history-100", histories.get(0).getId());
    }

    @Test
    public void testFindById() {
        // Create and save an EmailHistoryEntity
        EmailHistoryEntity history = new EmailHistoryEntity();
        history.setId("history-10");
        history.setDate(new Date());
        history.setText("Test email text");
        history.setSubject("Test subject");
        history.setGroups(Arrays.asList("group1", "group2"));
        history.setRecipients(Arrays.asList("recipient1@example.com"));
        history.setMoreRecipients(Arrays.asList("more1@example.com"));
        history.setAutomatic(true);
        history.setHtml(true);
        history.setAttachments(null);
        history.setSent(true);

        emailHistoryRepository.save(history);

        // Retrieve by ID
        Optional<EmailHistoryEntity> result = emailHistoryRepository.findById("history-10");
        assertNotNull("Result should not be null", result);
        assertEquals("history-10", result.get().getId());
    }

    @Test
    public void testFindByDate() {
        // Create and save EmailHistoryEntities
        EmailHistoryEntity history1 = new EmailHistoryEntity();
        history1.setId("history-5");
        history1.setDate(new Date());
        history1.setText("Test email text 1");
        history1.setSubject("Test subject 1");
        history1.setGroups(Arrays.asList("group1"));
        history1.setRecipients(Arrays.asList("recipient1@example.com"));
        history1.setMoreRecipients(null);
        history1.setAutomatic(true);
        history1.setHtml(false);
        history1.setAttachments(null);
        history1.setSent(true);

        EmailHistoryEntity history2 = new EmailHistoryEntity();
        history2.setId("history-4");
        history2.setDate(new Date());
        history2.setText("Test email text 2");
        history2.setSubject("Test subject 2");
        history2.setGroups(Arrays.asList("group2"));
        history2.setRecipients(Arrays.asList("recipient2@example.com"));
        history2.setMoreRecipients(null);
        history2.setAutomatic(false);
        history2.setHtml(true);
        history2.setAttachments(null);
        history2.setSent(true);

        emailHistoryRepository.save(history1);
        emailHistoryRepository.save(history2);

        // Find by date range
        List<EmailHistoryEntity> results = emailHistoryRepository.findByDate(new Date(System.currentTimeMillis() - 10000), new Date(System.currentTimeMillis() + 10000), 0, 10);
        assertNotNull("Results should not be null", results);
        assertEquals(2, results.size());
    }
}

