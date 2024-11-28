package RepositoryTests;

import cz.inspire.thesis.data.EntityManagerProducer;
import cz.inspire.thesis.data.model.EmailHistoryEntity;
import cz.inspire.thesis.data.repository.EmailHistoryRepository;
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

import static org.junit.Assert.*;

public class EmailHistoryRepositoryTest {

    private EmailHistoryRepository emailHistoryRepository;

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

        // Access EmailHistoryRepository from CDI
        emailHistoryRepository = BeanProvider.getContextualReference(EmailHistoryRepository.class);

        // Clear the database
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.createQuery("DELETE FROM EmailHistoryEntity ").executeUpdate();
        em.getTransaction().commit();

    }

    @Test
    public void testFindAll() {
        assertNotNull("EmailHistoryRepository should be initialized!", emailHistoryRepository);

        // Save entities
        Date now = new Date();
        emailHistoryRepository.save(new EmailHistoryEntity("1", now, "Text1", "Subject1", null, null, null, true, true, null, true));
        emailHistoryRepository.save(new EmailHistoryEntity("2", now, "Text2", "Subject2", null, null, null, true, false, null, true));

        // Test retrieving all entities
        List<EmailHistoryEntity> allHistories = emailHistoryRepository.findAll();
        assertNotNull("Result list should not be null", allHistories);
        assertEquals(2, allHistories.size());
    }

    @Test
    public void testPagination() {
        assertNotNull("EmailHistoryRepository should be initialized!", emailHistoryRepository);

        // Save multiple entities
        for (int i = 0; i < 10; i++) {
            emailHistoryRepository.save(new EmailHistoryEntity(
                    String.valueOf(i), new Date(), "Text" + i, "Subject" + i, null, null, null, true, true, null, true));
        }

        // Test retrieving entities with pagination
        List<EmailHistoryEntity> result = emailHistoryRepository.findAll(1, 5); // First 5 entities
        assertNotNull("Result list should not be null", result);
        assertEquals(5, result.size());
        assertEquals("8", result.getFirst().getId());
    }

    @Test
    public void testFindByDateRange() {
        assertNotNull("EmailHistoryRepository should be initialized!", emailHistoryRepository);

        // Save entities
        Date now = new Date();
        Date earlier = new Date(now.getTime() - 86400000L); // 1 day earlier
        Date later = new Date(now.getTime() + 86400000L); // 1 day later

        emailHistoryRepository.save(new EmailHistoryEntity("1", earlier, "Text1", "Subject1", null, null, null, true, true, null, true));
        emailHistoryRepository.save(new EmailHistoryEntity("2", now, "Text2", "Subject2", null, null, null, true, true, null, true));
        emailHistoryRepository.save(new EmailHistoryEntity("3", later, "Text3", "Subject3", null, null, null, true, true, null, true));

        // Test finding by date range
        List<EmailHistoryEntity> results = emailHistoryRepository.findByDate(earlier, later, 0, 3);
        assertNotNull("Result list should not be null", results);
        assertEquals(3, results.size());
    }

    @Test
    public void testFindByDateRangeWithPagination() {
        assertNotNull("EmailHistoryRepository should be initialized!", emailHistoryRepository);

        // Save multiple entities
        for (int i = 0; i < 10; i++) {
            emailHistoryRepository.save(new EmailHistoryEntity(
                    String.valueOf(i), new Date(), "Text" + i, "Subject" + i, null, null, null, true, true, null, true));
        }

        // Test finding by date range with pagination
        Date now = new Date();
        Date earlier = new Date(now.getTime() - 86400000L); // 1 day earlier
        Date later = new Date(now.getTime() + 86400000L); // 1 day later

        List<EmailHistoryEntity> results = emailHistoryRepository.findByDate(earlier, later, 0, 5); // First 5 entities
        assertNotNull("Result list should not be null", results);
        assertEquals(5, results.size());
    }
}
