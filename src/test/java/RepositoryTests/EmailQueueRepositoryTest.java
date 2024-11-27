package RepositoryTests;

import cz.inspire.thesis.data.EntityManagerProducer;
import cz.inspire.thesis.data.model.EmailQueueEntity;
import cz.inspire.thesis.data.repository.EmailQueueRepository;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class EmailQueueRepositoryTest {

    private EmailQueueRepository emailQueueRepository;

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

        // Access EmailQueueRepository from CDI
        emailQueueRepository = BeanProvider.getContextualReference(EmailQueueRepository.class);
    }

    @After
    public void tearDown() {
        // Clean up the database
        emailQueueRepository.findAll().forEach(emailQueueRepository::remove);
    }

    @Test
    public void testFindAll() {
        assertNotNull("EmailQueueRepository should be initialized!", emailQueueRepository);

        // Save entities
        Date now = new Date();
        emailQueueRepository.save(new EmailQueueEntity("1", now, "history1", "recipient1", 1, false, "dependent1"));
        emailQueueRepository.save(new EmailQueueEntity("2", now, "history2", "recipient2", 2, true, "dependent2"));

        // Test retrieving all entities
        List<EmailQueueEntity> allEmails = emailQueueRepository.findAll();
        assertNotNull("Result list should not be null", allEmails);
        assertEquals(10, allEmails.size());
    }

    @Test
    public void testFindFirstMail() {
        assertNotNull("EmailQueueRepository should be initialized!", emailQueueRepository);

        // Save entities
        Date now = new Date();
        emailQueueRepository.save(new EmailQueueEntity("1", now, "history1", "recipient1", 5, false, "dependent1"));
        emailQueueRepository.save(new EmailQueueEntity("2", now, "history2", "recipient2", 10, true, "dependent2"));

        // Test finding the first mail by priority
        Optional<EmailQueueEntity> firstMail = emailQueueRepository.findFirstMail();
        assertTrue("First mail should be present", firstMail.isPresent());
        assertEquals("2", firstMail.get().getId());
    }

    @Test
    public void testFindByHistory() {
        assertNotNull("EmailQueueRepository should be initialized!", emailQueueRepository);

        // Save entities
        emailQueueRepository.save(new EmailQueueEntity("1", new Date(), "history1", "recipient1", 1, false, "dependent1"));
        emailQueueRepository.save(new EmailQueueEntity("2", new Date(), "history2", "recipient2", 2, true, "dependent2"));

        // Test finding by email history
        List<EmailQueueEntity> result = emailQueueRepository.findByHistory("history1");
        assertNotNull("Result list should not be null", result);
        assertEquals(1, result.size());
        assertEquals("1", result.get(0).getId());
    }

    @Test
    public void testFindByDependentHistory() {
        assertNotNull("EmailQueueRepository should be initialized!", emailQueueRepository);

        // Save entities
        emailQueueRepository.save(new EmailQueueEntity("1", new Date(), "history1", "recipient1", 1, false, "dependent1"));
        emailQueueRepository.save(new EmailQueueEntity("2", new Date(), "history2", "recipient2", 2, true, "dependent2"));

        // Test finding by dependent email history
        List<EmailQueueEntity> result = emailQueueRepository.findByDependentHistory("dependent2");
        assertNotNull("Result list should not be null", result);
        assertEquals(1, result.size());
        assertEquals("2", result.get(0).getId());
    }

    @Test
    public void testPagination() {
        assertNotNull("EmailQueueRepository should be initialized!", emailQueueRepository);

        // Save multiple entities
        for (int i = 0; i < 10; i++) {
            emailQueueRepository.save(new EmailQueueEntity(
                    String.valueOf(i), new Date(), "history" + i, "recipient" + i, i, false, "dependent" + i));
        }

        // Test retrieving entities with pagination
        List<EmailQueueEntity> result = emailQueueRepository.findAll(0, 5); // First 5 entities
        assertNotNull("Result list should not be null", result);
        assertEquals(5, result.size());
    }
}
