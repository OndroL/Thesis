package RepositoryTests.Uzivatel;

import cz.inspire.thesis.data.model.uzivatel.UzivatelLoginAttemptEntity;
import cz.inspire.thesis.data.repository.uzivatel.UzivatelLoginAttemptRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import cz.inspire.thesis.data.EntityManagerProducer;
import jakarta.persistence.Persistence;
import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class UzivatelLoginAttemptRepositoryTest {

    private UzivatelLoginAttemptRepository loginAttemptRepository;

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

        // Access UzivatelLoginAttemptRepository from CDI
        loginAttemptRepository = BeanProvider.getContextualReference(UzivatelLoginAttemptRepository.class);

        // Clear the database
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.createQuery("DELETE FROM UzivatelLoginAttemptEntity").executeUpdate();
        em.getTransaction().commit();
    }

    @Test
    public void testFindAll_emptyDatabase() {
        assertNotNull("LoginAttemptRepository should be initialized!", loginAttemptRepository);

        // Test when the database is empty
        List<UzivatelLoginAttemptEntity> results = loginAttemptRepository.findAll();
        assertNotNull("Results should not be null even if no records are present!", results);
        assertTrue("Results should be empty for an empty database.", results.isEmpty());
    }

    @Test
    public void testFindLastByLoginAndIp() {
        assertNotNull("LoginAttemptRepository should be initialized!", loginAttemptRepository);

        // Create and persist login attempts
        Date now = new Date();
        UzivatelLoginAttemptEntity attempt1 = new UzivatelLoginAttemptEntity("1", "user1", new Date(now.getTime() - 10000), null, "192.168.1.1");
        UzivatelLoginAttemptEntity attempt2 = new UzivatelLoginAttemptEntity("2", "user1", now, null, "192.168.1.1");

        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.persist(attempt1);
        em.persist(attempt2);
        em.getTransaction().commit();

        // Test the repository method
        Optional<UzivatelLoginAttemptEntity> result = loginAttemptRepository.findLastByLoginAndIp("user1", "192.168.1.1");
        assertTrue("Result should be present", result.isPresent());
        assertEquals("2", result.get().getId());
    }

    @Test
    public void testFindByLoginAndIpFromRecentTime() {
        assertNotNull("LoginAttemptRepository should be initialized!", loginAttemptRepository);

        // Create and persist login attempts
        Date now = new Date();
        Date referenceTime = new Date(now.getTime() - 5000);
        UzivatelLoginAttemptEntity attempt1 = new UzivatelLoginAttemptEntity("1", "user1", new Date(now.getTime() - 10000), null, "192.168.1.1");
        UzivatelLoginAttemptEntity attempt2 = new UzivatelLoginAttemptEntity("2", "user1", now, null, "192.168.1.1");

        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.persist(attempt1);
        em.persist(attempt2);
        em.getTransaction().commit();

        // Test the repository method
        List<UzivatelLoginAttemptEntity> results = loginAttemptRepository.findByLoginAndIpFromRecentTime("user1", "192.168.1.1", referenceTime);
        assertNotNull("Results should not be null!", results);
        assertEquals(1, results.size());
        assertEquals("2", results.get(0).getId());
    }

    @Test
    public void testFindAttemptsOnlyOlderThan() {
        assertNotNull("LoginAttemptRepository should be initialized!", loginAttemptRepository);

        // Create and persist login attempts
        Date now = new Date();
        Date referenceTime = new Date(now.getTime() - 5000);
        UzivatelLoginAttemptEntity attempt1 = new UzivatelLoginAttemptEntity("1", "user1", new Date(now.getTime() - 10000), null, "192.168.1.1");
        UzivatelLoginAttemptEntity attempt2 = new UzivatelLoginAttemptEntity("2", "user1", now, null, "192.168.1.1");

        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.persist(attempt1);
        em.persist(attempt2);
        em.getTransaction().commit();

        // Test the repository method
        List<UzivatelLoginAttemptEntity> results = loginAttemptRepository.findAttemptsOnlyOlderThan(referenceTime);
        assertNotNull("Results should not be null!", results);
        assertEquals(1, results.size());
        assertEquals("1", results.get(0).getId());
    }

    @Test
    public void testCountLoginAttemptsInTime() {
        assertNotNull("LoginAttemptRepository should be initialized!", loginAttemptRepository);

        // Create and persist login attempts
        Date now = new Date();
        Date referenceTime = new Date(now.getTime() - 5000);
        UzivatelLoginAttemptEntity attempt1 = new UzivatelLoginAttemptEntity("1", "user1", new Date(now.getTime() - 10000), null, "192.168.1.1");
        UzivatelLoginAttemptEntity attempt2 = new UzivatelLoginAttemptEntity("2", "user1", now, null, "192.168.1.1");

        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.persist(attempt1);
        em.persist(attempt2);
        em.getTransaction().commit();

        // Test the repository method
        Long count = loginAttemptRepository.countLoginAttemptsInTime("user1", "192.168.1.1", referenceTime);
        assertNotNull("Count should not be null!", count);
        assertEquals(1L, (long) count);
    }

    @Test
    public void testFindBlockingOlderThan() {
        assertNotNull("LoginAttemptRepository should be initialized!", loginAttemptRepository);

        // Create and persist login attempts
        Date now = new Date();
        Date referenceTime = new Date(now.getTime() - 5000);

        UzivatelLoginAttemptEntity attempt1 = new UzivatelLoginAttemptEntity(
                "1", "user1", new Date(now.getTime() - 10000), new Date(now.getTime() - 10000), "192.168.1.1"
        ); // Blocked time older than reference
        UzivatelLoginAttemptEntity attempt2 = new UzivatelLoginAttemptEntity(
                "2", "user2", new Date(now.getTime() - 2000), new Date(now.getTime() - 1000), "192.168.1.2"
        ); // Blocked time newer than reference

        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.persist(attempt1);
        em.persist(attempt2);
        em.getTransaction().commit();

        // Test the repository method
        List<UzivatelLoginAttemptEntity> results = loginAttemptRepository.findBlockingOlderThan(referenceTime);
        assertNotNull("Results should not be null!", results);
        assertEquals(1, results.size());
        assertEquals("1", results.get(0).getId());
    }
}
