package RepositoryTests.Push;

import cz.inspire.thesis.data.EntityManagerProducer;
import cz.inspire.thesis.data.model.push.PushHistoryEntity;
import cz.inspire.thesis.data.model.push.PushMulticastEntity;
import cz.inspire.thesis.data.repository.push.PushMulticastRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.Date;

import static org.junit.Assert.*;

public class PushMulticastRepositoryTest {

    private PushMulticastRepository pushMulticastRepository;

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

        // Access PushMulticastRepository from CDI
        pushMulticastRepository = BeanProvider.getContextualReference(PushMulticastRepository.class);

        // Clear the database
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.createQuery("DELETE FROM PushHistoryEntity").executeUpdate();
        em.createQuery("DELETE FROM PushMulticastEntity").executeUpdate();
        em.getTransaction().commit();
    }

    @Test
    public void testFindByDateAutomatic() {
        assertNotNull("PushMulticastRepository should be initialized!", pushMulticastRepository);

        // Create and persist PushMulticastEntities
        PushMulticastEntity multicast1 = new PushMulticastEntity();
        multicast1.setId("multicast1");
        multicast1.setDate(new Date(System.currentTimeMillis() - 20000)); // 20 seconds ago
        multicast1.setAutomatic(true);
        multicast1.setSent(true);

        PushMulticastEntity multicast2 = new PushMulticastEntity();
        multicast2.setId("multicast2");
        multicast2.setDate(new Date(System.currentTimeMillis() - 10000)); // 10 seconds ago
        multicast2.setAutomatic(false);
        multicast2.setSent(true);

        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.persist(multicast1);
        em.persist(multicast2);
        em.getTransaction().commit();

        // Test the repository method
        Collection<PushMulticastEntity> results = pushMulticastRepository.findByDateAutomatic(
                new Date(System.currentTimeMillis() - 30000), // 30 seconds ago
                new Date(), // Now
                true, // Filter by automatic
                0,
                10
        );

        assertNotNull("Results should not be null", results);
        assertEquals(1, results.size());
        assertEquals("multicast1", results.iterator().next().getId());
    }

    @Test
    public void testFindByUzivatelDateAutomatic() {
        assertNotNull("PushMulticastRepository should be initialized!", pushMulticastRepository);

        // Create and persist a PushMulticastEntity
        PushMulticastEntity multicast = new PushMulticastEntity();
        multicast.setId("multicast1");
        multicast.setDate(new Date(System.currentTimeMillis() - 10000)); // 10 seconds ago
        multicast.setAutomatic(true);
        multicast.setSent(true);

        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.persist(multicast);
        em.getTransaction().commit();

        // Create and persist PushHistoryEntities
        PushHistoryEntity history1 = new PushHistoryEntity("history1", "user1", true, false, "multicast1");
        PushHistoryEntity history2 = new PushHistoryEntity("history2", "user1", false, false, "multicast1");

        em.getTransaction().begin();
        em.persist(history1);
        em.persist(history2);
        em.getTransaction().commit();

        // Test the repository method
        Collection<PushMulticastEntity> results = pushMulticastRepository.findByUzivatelDateAutomatic(
                new Date(System.currentTimeMillis() - 20000), // 20 seconds ago
                new Date(), // Now
                true, // Automatic filter
                "user1", // uzivatelId
                0,
                10
        );

        assertNotNull("Results should not be null", results);
        assertEquals(1, results.size());
        assertEquals("multicast1", results.iterator().next().getId());
    }
}
