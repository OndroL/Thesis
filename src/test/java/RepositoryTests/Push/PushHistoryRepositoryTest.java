package RepositoryTests.Push;

import cz.inspire.thesis.data.EntityManagerProducer;
import cz.inspire.thesis.data.model.push.PushHistoryEntity;
import cz.inspire.thesis.data.model.push.PushMulticastEntity;
import cz.inspire.thesis.data.repository.push.PushHistoryRepository;
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

public class PushHistoryRepositoryTest {

    private PushHistoryRepository pushHistoryRepository;

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

        // Access PushHistoryRepository from CDI
        pushHistoryRepository = BeanProvider.getContextualReference(PushHistoryRepository.class);

        // Clear the database
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.createQuery("DELETE FROM PushHistoryEntity").executeUpdate();
        em.createQuery("DELETE FROM PushMulticastEntity").executeUpdate();
        em.getTransaction().commit();
    }

    @Test
    public void testFindByMulticastIdUzivatel() {
        assertNotNull("PushHistoryRepository should be initialized!", pushHistoryRepository);

        // Create and persist a PushMulticastEntity
        PushMulticastEntity multicast = new PushMulticastEntity();
        multicast.setId("multicast1");
        multicast.setDate(new Date());
        multicast.setBody("Test body");
        multicast.setSent(true);

        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.persist(multicast);
        em.getTransaction().commit();

        // Create and persist PushHistoryEntities
        PushHistoryEntity history1 = new PushHistoryEntity("history1", "user1", true, false, multicast.getId());
        PushHistoryEntity history2 = new PushHistoryEntity("history2", "user1", true, false, multicast.getId());

        em.getTransaction().begin();
        em.persist(history1);
        em.persist(history2);
        em.getTransaction().commit();

        // Test the repository method
        Collection<PushHistoryEntity> results = pushHistoryRepository.findByMulticastIdUzivatel("user1", "multicast1");
        assertNotNull("Results should not be null", results);
        assertEquals(2, results.size());
    }

    @Test
    public void testFindSuccessfulSentByUzivatel() {
        assertNotNull("PushHistoryRepository should be initialized!", pushHistoryRepository);

        // Create and persist a PushMulticastEntity
        PushMulticastEntity multicast = new PushMulticastEntity();
        multicast.setId("multicast1");
        multicast.setDate(new Date());
        multicast.setBody("Test body");
        multicast.setSent(true);

        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.persist(multicast);
        em.getTransaction().commit();

        // Create and persist PushHistoryEntities
        Date lastDate = new Date(System.currentTimeMillis() - 10000); // 10 seconds earlier
        PushHistoryEntity history1 = new PushHistoryEntity("history1", "user1", true, false, multicast.getId());
        PushHistoryEntity history2 = new PushHistoryEntity("history2", "user1", false, false, multicast.getId());

        em.getTransaction().begin();
        em.persist(history1);
        em.persist(history2);
        em.getTransaction().commit();

        // Test the repository method
        Collection<PushHistoryEntity> results = pushHistoryRepository.findSuccessfulSentByUzivatel("user1", false, lastDate, 0, 2);
        assertNotNull("Results should not be null", results);
        assertEquals(2, results.size());
    }
}
