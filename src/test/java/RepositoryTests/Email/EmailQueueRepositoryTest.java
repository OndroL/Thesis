package RepositoryTests.Email;

import cz.inspire.EntityManagerProducer;
import cz.inspire.email.entity.EmailQueueEntity;
import cz.inspire.email.repository.EmailQueueRepository;
import jakarta.data.Limit;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class EmailQueueRepositoryTest {

    private EmailQueueRepository emailQueueRepository;

    @Before
    public void setUp() {
        CdiContainer container = CdiContainerLoader.getCdiContainer();
        container.boot();
        System.out.println("CDI Container initialized.");

        // Create the EntityManagerFactory
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");

        // Access EntityManagerProducer and set the EntityManagerFactory
        EntityManagerProducer producer = BeanProvider.getContextualReference(EntityManagerProducer.class);
        producer.setEntityManagerFactory(emf);

        // Access EmailQueueRepository from CDI
        emailQueueRepository = BeanProvider.getContextualReference(EmailQueueRepository.class);

        // Clear the database
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.createQuery("DELETE FROM EmailQueueEntity").executeUpdate();
        em.getTransaction().commit();
    }

    @Test
    public void testSaveAndFindAll() {
        assertNotNull("EmailQueueRepository should be initialized!", emailQueueRepository);

        // Create and save an EmailQueueEntity
        EmailQueueEntity emailQueue = new EmailQueueEntity();
        emailQueue.setId("queue-10");
        emailQueue.setCreated(new Date());
        emailQueue.setEmailHistory("history-10");
        emailQueue.setRecipient("recipient1@example.com");
        emailQueue.setPriority(1);
        emailQueue.setRemoveEmailHistory(false);
        emailQueue.setDependentEmailHistory("dependent-history-1");

        emailQueueRepository.save(emailQueue);

        // Retrieve all entries
        List<EmailQueueEntity> queues = emailQueueRepository.findAllOrdered();
        assertNotNull("Queue list should not be null", queues);
        assertEquals(1, queues.size());
        assertEquals("queue-10", queues.get(0).getId());
    }

    @Test
    public void testFindByHistory() {
        // Create and save EmailQueueEntities
        EmailQueueEntity queue1 = new EmailQueueEntity();
        queue1.setId("queue-1");
        queue1.setCreated(new Date());
        queue1.setEmailHistory("history-1");
        queue1.setRecipient("recipient1@example.com");
        queue1.setPriority(1);
        queue1.setRemoveEmailHistory(false);
        queue1.setDependentEmailHistory("dependent-history-1");

        EmailQueueEntity queue2 = new EmailQueueEntity();
        queue2.setId("queue-2");
        queue2.setCreated(new Date());
        queue2.setEmailHistory("history-2");
        queue2.setRecipient("recipient2@example.com");
        queue2.setPriority(2);
        queue2.setRemoveEmailHistory(true);
        queue2.setDependentEmailHistory("dependent-history-2");

        emailQueueRepository.save(queue1);
        emailQueueRepository.save(queue2);


        // Find by emailHistory
        List<EmailQueueEntity> result = emailQueueRepository.findByHistory("history-1");
        assertNotNull("Result should not be null", result);
        assertEquals(1, result.size());
        assertEquals("queue-1", result.get(0).getId());
    }

    @Test
    public void testFindFirstMail() {
        // Create and save EmailQueueEntities with different priorities
        EmailQueueEntity queue1 = new EmailQueueEntity();
        queue1.setId("queue-3");
        queue1.setCreated(new Date());
        queue1.setEmailHistory("history-1");
        queue1.setRecipient("recipient1@example.com");
        queue1.setPriority(1);
        queue1.setRemoveEmailHistory(false);
        queue1.setDependentEmailHistory("dependent-history-1");

        EmailQueueEntity queue2 = new EmailQueueEntity();
        queue2.setId("queue-4");
        queue2.setCreated(new Date());
        queue2.setEmailHistory("history-2");
        queue2.setRecipient("recipient2@example.com");
        queue2.setPriority(2);
        queue2.setRemoveEmailHistory(true);
        queue2.setDependentEmailHistory("dependent-history-2");

        emailQueueRepository.save(queue1);
        emailQueueRepository.save(queue2);

        // Find the first mail
        Optional<EmailQueueEntity> firstMail = emailQueueRepository.findFirstMail(Limit.of(1));
        assertNotNull("First mail should not be null", firstMail);
        assertEquals("queue-4", firstMail.get().getId());
    }
}
