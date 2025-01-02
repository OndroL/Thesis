package RepositoryTests.Uzivatel;

import cz.inspire.thesis.data.model.uzivatel.SkupinaEntity;
import cz.inspire.thesis.data.model.uzivatel.UzivatelEntity;
import cz.inspire.thesis.data.model.uzivatel.UzivatelSessionEntity;
import cz.inspire.thesis.data.repository.uzivatel.UzivatelSessionRepository;
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

public class UzivatelSessionRepositoryTest {

    private UzivatelSessionRepository sessionRepository;

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

        // Access UzivatelSessionRepository from CDI
        sessionRepository = BeanProvider.getContextualReference(UzivatelSessionRepository.class);

        // Clear the database
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.createQuery("DELETE FROM UzivatelSessionEntity").executeUpdate();
        em.createQuery("DELETE FROM UzivatelEntity").executeUpdate();
        em.createQuery("DELETE FROM SkupinaEntity").executeUpdate();
        em.getTransaction().commit();
    }

    @Test
    public void testFindLast() {
        // Create and persist a user
        UzivatelEntity user = new UzivatelEntity("user1", "password", "John Doe", true, null, null, null, null, null, null, null);

        // Create and persist sessions
        UzivatelSessionEntity session1 = new UzivatelSessionEntity("session1", new Date(1000), null, 1, 1, "192.168.1.1", "client1", new Date(1000), user);
        UzivatelSessionEntity session2 = new UzivatelSessionEntity("session2", new Date(2000), null, 1, 1, "192.168.1.1", "client1", new Date(2000), user);

        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.persist(user);
        em.persist(session1);
        em.persist(session2);
        em.getTransaction().commit();

        // Test the repository method
        Optional<UzivatelSessionEntity> result = sessionRepository.findLast("user1");
        assertTrue("Result should be present", result.isPresent());
        assertEquals("session2", result.get().getId());
    }

    @Test
    public void testFindAllInDates() {
        // Create and persist a user
        UzivatelEntity user = new UzivatelEntity("user1", "password", "John Doe", true, null, null, null, null, null, null, null);

        // Create and persist sessions
        Date from = new Date(350000);
        Date to = new Date(450000);

        UzivatelSessionEntity session1 = new UzivatelSessionEntity("session1", new Date(4500000), null, 1, 1, "192.168.1.1", "client1", new Date(30000), user);
        UzivatelSessionEntity session2 = new UzivatelSessionEntity("session2", new Date(100000), null, 1, 1, "192.168.1.1", "client1", new Date(40000), user);

        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.persist(user);
        em.persist(session1);
        em.persist(session2);
        em.getTransaction().commit();

        // Test the repository method
        List<UzivatelSessionEntity> results = sessionRepository.findAllInDates(from, to);
        assertNotNull("Results should not be null!", results);
        assertEquals(1, results.size());
        assertEquals("session2", results.get(0).getId());
    }

    @Test
    public void testCountNewerSessions() {
        // Create and persist sessions
        Date threshold = new Date(1500);

        UzivatelSessionEntity session1 = new UzivatelSessionEntity("session1", new Date(1000), null, 1, 1, "192.168.1.1", "client1", new Date(1000), null);
        UzivatelSessionEntity session2 = new UzivatelSessionEntity("session2", new Date(2000), null, 1, 1, "192.168.1.1", "client1", new Date(2000), null);

        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.persist(session1);
        em.persist(session2);
        em.getTransaction().commit();

        // Test the repository method
        Long count = sessionRepository.countNewerSessions(threshold);
        assertNotNull("Count should not be null!", count);
        assertEquals(1L, (long) count);
    }
    @Test
    public void testFindLastRestLogin() {
        assertNotNull("SessionRepository should be initialized!", sessionRepository);

        // Create and persist a user
        UzivatelEntity user = new UzivatelEntity("user1", "password", "John Doe", true, null, null, null, null, null, null, null);

        // Create and persist sessions
        UzivatelSessionEntity session1 = new UzivatelSessionEntity("session1", new Date(1000), null, 3, 1, "192.168.1.1", "client1", new Date(1000), user);
        UzivatelSessionEntity session2 = new UzivatelSessionEntity("session2", new Date(2000), null, 3, 1, "192.168.1.1", "client1", new Date(2000), user);

        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.persist(user);
        em.persist(session1);
        em.persist(session2);
        em.getTransaction().commit();

        // Test the repository method
        Optional<UzivatelSessionEntity> result = sessionRepository.findLastRestLogin("user1");
        assertTrue("Result should be present", result.isPresent());
        assertEquals("session2", result.get().getId());
    }

    @Test
    public void testFindLoggedByType() {
        assertNotNull("SessionRepository should be initialized!", sessionRepository);

        // Create and persist users
        UzivatelEntity adminUser = new UzivatelEntity("admin", "password", "Admin User", true, null, null, null, null, null, null, null);
        UzivatelEntity regularUser = new UzivatelEntity("user1", "password", "John Doe", true, null, null, null, null, null, null, null);

        // Create and persist sessions
        UzivatelSessionEntity session1 = new UzivatelSessionEntity("session1", null, null, 3, 1, "192.168.1.1", "client1", new Date(1000), adminUser);
        UzivatelSessionEntity session2 = new UzivatelSessionEntity("session2", null, null, 3, 1, "192.168.1.1", "client1", new Date(2000), regularUser);

        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.persist(adminUser);
        em.persist(regularUser);
        em.persist(session1);
        em.persist(session2);
        em.getTransaction().commit();

        // Test the repository method
        List<UzivatelSessionEntity> results = sessionRepository.findLogged(3);
        assertNotNull("Results should not be null!", results);
        assertEquals(1, results.size());
        assertEquals("session2", results.get(0).getId());
    }

    @Test
    public void testFindLogged() {
        assertNotNull("SessionRepository should be initialized!", sessionRepository);

        // Create and persist a user
        UzivatelEntity user = new UzivatelEntity("user1", "password", "John Doe", true, null, null, null, null, null, null, null);

        // Create and persist sessions
        UzivatelSessionEntity session1 = new UzivatelSessionEntity("session1", new Date(1000), null, 1, 1, "192.168.1.1", "client1", new Date(1000), user);
        UzivatelSessionEntity session2 = new UzivatelSessionEntity("session2", new Date(2000), new Date(3000), 1, 1, "192.168.1.1", "client1", new Date(2000), user);

        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.persist(user);
        em.persist(session1);
        em.persist(session2);
        em.getTransaction().commit();

        // Test the repository method
        List<UzivatelSessionEntity> results = sessionRepository.findLogged();
        assertNotNull("Results should not be null!", results);
        assertEquals(1, results.size());
        assertEquals("session1", results.get(0).getId());
    }

    @Test
    public void testFindAllInDatesWithLogin() {
        assertNotNull("SessionRepository should be initialized!", sessionRepository);

        // Create and persist a user in a non-web group
        SkupinaEntity groupAdmin = new SkupinaEntity("admin1", "Administrators", null, null, null);
        UzivatelEntity user = new UzivatelEntity("user1", "password", "John Doe", true, null, null, null, groupAdmin, null, null, null);

        // Create and persist sessions
        Date from = new Date(10000);
        Date to = new Date(20000);
        UzivatelSessionEntity session1 = new UzivatelSessionEntity("session1", new Date(15000), new Date(18000), 1, 1, "192.168.1.1", "client1", new Date(10000), user);
        UzivatelSessionEntity session2 = new UzivatelSessionEntity("session2", new Date(25000), new Date(28000), 1, 1, "192.168.1.1", "client1", new Date(20000), user);
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.persist(groupAdmin);
        em.persist(user);
        em.persist(session1);
        em.persist(session2);
        em.getTransaction().commit();

        // Test the repository method
        List<UzivatelSessionEntity> results = sessionRepository.findAllInDatesWithLogin(from, to, "user1");
        assertNotNull("Results should not be null!", results);
        assertEquals(1, results.size());
        assertEquals("session1", results.get(0).getId());
    }

}
