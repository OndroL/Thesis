package RepositoryTests;

import cz.inspire.thesis.data.EntityManagerProducer;
import cz.inspire.thesis.data.model.SMSHistoryEntity;

import cz.inspire.thesis.data.repository.SMSHistoryRepository;
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

public class SMSHistoryRepositoryTest {

    private SMSHistoryRepository smsHistoryRepository;

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

        // Access SMSHistoryRepository from CDI
        smsHistoryRepository = BeanProvider.getContextualReference(SMSHistoryRepository.class);

        // Clear the database
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.createQuery("DELETE FROM SMSHistoryEntity").executeUpdate();
        em.getTransaction().commit();
    }

    @Test
    public void testFindByDate() {
        assertNotNull("SMSHistoryRepository should be initialized!", smsHistoryRepository);

        // Test saving an SMSHistory entity
        Date now = new Date();
        SMSHistoryEntity smsHistoryEntity = new SMSHistoryEntity("1", now, "Test Message", null, null, null, true);
        smsHistoryRepository.save(smsHistoryEntity);

        // Test finding by date range
        List<SMSHistoryEntity> foundList = smsHistoryRepository.findByDate(now, now);
        assertNotNull("Result list should not be null", foundList);
        assertEquals(1, foundList.size());
        assertEquals("Test Message", foundList.get(0).getMessage());
    }

    @Test
    public void testFindByDateAutomatic() {
        assertNotNull("SMSHistoryRepository should be initialized!", smsHistoryRepository);

        // Test saving an SMSHistory entity
        Date now = new Date();
        SMSHistoryEntity smsHistoryEntity = new SMSHistoryEntity("1", now, "Test Message Automatic", null, null, null, true);
        smsHistoryRepository.save(smsHistoryEntity);

        // Test finding by date range and automatic flag
        List<SMSHistoryEntity> foundList = smsHistoryRepository.findByDateAutomatic(now, now, true);
        assertNotNull("Result list should not be null", foundList);
        assertEquals(1, foundList.size());
        assertTrue(foundList.get(0).getAutomatic());
    }

    @Test
    public void testFindAll() {
        assertNotNull("SMSHistoryRepository should be initialized!", smsHistoryRepository);

        // Test saving multiple SMSHistory entities
        Date now = new Date();
        Date earlier = new Date(now.getTime() - 10000); // 10 seconds before
        Date later = new Date(now.getTime() + 10000);

        smsHistoryRepository.save(new SMSHistoryEntity("1", earlier, "Outside Earlier", null, null, null, false));
        smsHistoryRepository.save(new SMSHistoryEntity("2", now, "Within Range", null, null, null, false));
        smsHistoryRepository.save(new SMSHistoryEntity("3", later, "Outside Later", null, null, null, false));;

        // Test retrieving all SMSHistory entities
        List<SMSHistoryEntity> allHistory = smsHistoryRepository.findAll();
        assertNotNull("Result list should not be null", allHistory);
        assertEquals(3, allHistory.size());

        // Clear the database
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.createQuery("DELETE FROM SMSHistoryEntity").executeUpdate();
        em.getTransaction().commit();
    }

    @Test
    public void testFindByDateBoundaryConditions() {
        assertNotNull("SMSHistoryRepository should be initialized!", smsHistoryRepository);

        Date from = new Date();
        Date to = new Date(from.getTime() + 1000); // 1 second later

        // Save entities at boundary dates
        SMSHistoryEntity historyFrom = new SMSHistoryEntity("1", from, "Boundary From", null, null, null, false);
        SMSHistoryEntity historyTo = new SMSHistoryEntity("2", to, "Boundary To", null, null, null, false);
        smsHistoryRepository.save(historyFrom);
        smsHistoryRepository.save(historyTo);

        // Test querying within range
        List<SMSHistoryEntity> result = smsHistoryRepository.findByDate(from, to);
        assertNotNull("Result list should not be null", result);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(h -> h.getMessage().equals("Boundary From")));
        assertTrue(result.stream().anyMatch(h -> h.getMessage().equals("Boundary To")));
    }
    @Test
    public void testFindByDateOverlappingRanges() {
        assertNotNull("SMSHistoryRepository should be initialized!", smsHistoryRepository);

        Date now = new Date();
        Date earlier = new Date(now.getTime() - 10000); // 10 seconds before
        Date later = new Date(now.getTime() + 10000); // 10 seconds after

        // Save entities outside and inside the range
        smsHistoryRepository.save(new SMSHistoryEntity("1", earlier, "Outside Earlier", null, null, null, false));
        smsHistoryRepository.save(new SMSHistoryEntity("2", now, "Within Range", null, null, null, false));
        smsHistoryRepository.save(new SMSHistoryEntity("3", later, "Outside Later", null, null, null, false));

        // Query for a range that only includes "now"
        List<SMSHistoryEntity> result = smsHistoryRepository.findByDate(now, now);
        assertNotNull("Result list should not be null", result);
        assertEquals(1, result.size());
        assertEquals("Within Range", result.get(0).getMessage());
    }

    @Test
    public void testFindByDateNoMatches() {
        assertNotNull("SMSHistoryRepository should be initialized!", smsHistoryRepository);

        Date now = new Date();

        // Save entities outside the range
        smsHistoryRepository.save(new SMSHistoryEntity("1", new Date(now.getTime() - 10000), "Too Early", null, null, null, false));
        smsHistoryRepository.save(new SMSHistoryEntity("2", new Date(now.getTime() + 10000), "Too Late", null, null, null, false));

        // Query for a range with no matches
        List<SMSHistoryEntity> result = smsHistoryRepository.findByDate(now, now);
        assertNotNull("Result list should not be null", result);
        assertTrue("Result list should be empty", result.isEmpty());
    }
}