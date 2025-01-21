package RepositoryTests.SMSHistory;

import cz.inspire.sms.entity.SMSHistoryEntity;
import cz.inspire.sms.repository.SMSHistoryRepository;
import cz.inspire.EntityManagerProducer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.*;

public class SMSHistoryRepositoryTest {

    private SMSHistoryRepository smsHistoryRepository;

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

        // Access SMSHistoryRepository from CDI
        smsHistoryRepository = BeanProvider.getContextualReference(SMSHistoryRepository.class);

        // Clear the database
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.createQuery("DELETE FROM SMSHistoryEntity").executeUpdate();
        em.getTransaction().commit();
    }

    @Test
    public void testSaveAndFindById() {
        assertNotNull("SMSHistoryRepository should be initialized!", smsHistoryRepository);

        // Create and save an SMSHistoryEntity
        SMSHistoryEntity history = new SMSHistoryEntity(
                "1",
                new Date(),
                "Test Message",
                Arrays.asList("Group1", "Group2"),
                Arrays.asList("Recipient1", "Recipient2"),
                Arrays.asList("MoreRecipient1", "MoreRecipient2"),
                true
        );
        smsHistoryRepository.save(history);

        // Test findById
        Optional<SMSHistoryEntity> retrievedHistory = Optional.of(smsHistoryRepository.findById("1").get());
        assertNotNull("Retrieved history should not be null", retrievedHistory);
        assertEquals("Test Message", retrievedHistory.get().getMessage());
        assertEquals(Arrays.asList("Group1", "Group2"), retrievedHistory.get().getGroups());
    }

    @Test
    public void testFindByDate() {
        assertNotNull("SMSHistoryRepository should be initialized!", smsHistoryRepository);

        // Add some data
        Date now = new Date();
        SMSHistoryEntity history1 = new SMSHistoryEntity(
                "2",
                new Date(now.getTime() - 3600 * 1000), // 1 hour ago
                "Message 1",
                List.of("GroupA"),
                List.of("RecipientA"),
                List.of("MoreRecipientA"),
                false
        );
        SMSHistoryEntity history2 = new SMSHistoryEntity(
                "3",
                new Date(now.getTime() + 3600 * 1000), // 1 hour ahead
                "Message 2",
                List.of("GroupB"),
                List.of("RecipientB"),
                List.of("MoreRecipientB"),
                true
        );
        smsHistoryRepository.save(history1);
        smsHistoryRepository.save(history2);

        // Test findByDate
        List<SMSHistoryEntity> results = smsHistoryRepository.findByDate(
                new Date(now.getTime() - 7200 * 1000), // 2 hours ago
                new Date(now.getTime() + 7200 * 1000)  // 2 hours ahead
        );
        assertNotNull("Results should not be null", results);
        assertEquals(2, results.size());
    }

    @Test
    public void testFindByDateAutomatic() {
        assertNotNull("SMSHistoryRepository should be initialized!", smsHistoryRepository);

        // Add some data
        Date now = new Date();
        SMSHistoryEntity history1 = new SMSHistoryEntity(
                "4",
                new Date(now.getTime() - 3600 * 1000), // 1 hour ago
                "Auto Message",
                List.of("AutoGroup"),
                List.of("AutoRecipient"),
                List.of("MoreAutoRecipient"),
                true
        );
        smsHistoryRepository.save(history1);

        // Test findByDateAutomatic
        List<SMSHistoryEntity> results = smsHistoryRepository.findByDateAutomatic(
                new Date(now.getTime() - 7200 * 1000), // 2 hours ago
                new Date(now.getTime() + 7200 * 1000), // 2 hours ahead
                true
        );
        assertNotNull("Results should not be null", results);
        assertEquals(1, results.size());
        assertEquals("Auto Message", results.getFirst().getMessage());
    }
}
