package RepositoryTests.Uzivatel;

import cz.inspire.thesis.data.model.uzivatel.SkupinaEntity;
import cz.inspire.thesis.data.model.uzivatel.UzivatelEntity;
import cz.inspire.thesis.data.repository.uzivatel.UzivatelRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import cz.inspire.thesis.data.EntityManagerProducer;
import jakarta.persistence.Persistence;
import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class UzivatelRepositoryTest {

    private UzivatelRepository uzivatelRepository;

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

        // Access UzivatelRepository from CDI
        uzivatelRepository = BeanProvider.getContextualReference(UzivatelRepository.class);

        // Clear the database
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.createQuery("DELETE FROM UzivatelEntity").executeUpdate();
        em.createQuery("DELETE FROM SkupinaEntity").executeUpdate();
        em.getTransaction().commit();
    }

    @Test
    public void testFindAll_emptyDatabase() {
        assertNotNull("UzivatelRepository should be initialized!", uzivatelRepository);

        // Test when the database is empty
        List<UzivatelEntity> results = uzivatelRepository.findAll();
        assertNotNull("Results should not be null even if no records are present!", results);
        assertTrue("Results should be empty for an empty database.", results.isEmpty());
    }

    @Test
    public void testFindBySkupina() {
        assertNotNull("UzivatelRepository should be initialized!", uzivatelRepository);

        // Create and persist a group
        SkupinaEntity group = new SkupinaEntity("group1", "Administrators", null, null, null);
        // Create and persist users
        UzivatelEntity user1 = new UzivatelEntity("user1", "password", "John Doe", true, null, null, null, group, null, null, null);
        UzivatelEntity user2 = new UzivatelEntity("user2", "password", "Jane Smith", true, null, null, null, group, null, null, null);

        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.persist(group);
        em.persist(user1);
        em.persist(user2);
        em.getTransaction().commit();

        // Test the repository method
        List<UzivatelEntity> results = uzivatelRepository.findBySkupina("group1");
        assertNotNull("Results should not be null!", results);
        assertEquals(2, results.size());
    }

    @Test
    public void testFindByAktivni() {
        assertNotNull("UzivatelRepository should be initialized!", uzivatelRepository);

        // Create and persist users
        UzivatelEntity activeUser = new UzivatelEntity("user1", "password", "John Doe", true, null, null, null, null, null, null, null);
        UzivatelEntity inactiveUser = new UzivatelEntity("user2", "password", "Jane Smith", false, null, null, null, null, null, null, null);

        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.persist(activeUser);
        em.persist(inactiveUser);
        em.getTransaction().commit();

        // Test the repository method
        List<UzivatelEntity> results = uzivatelRepository.findByAktivni(true);
        assertNotNull("Results should not be null!", results);
        assertEquals(1, results.size());
        assertEquals("user1", results.get(0).getLogin());
    }

    @Test
    public void testFindByAuthKey() {
        assertNotNull("UzivatelRepository should be initialized!", uzivatelRepository);

        // Create and persist a user
        UzivatelEntity user = new UzivatelEntity("user1", "password", "John Doe", true, null, "authKey123", null, null, null, null, null);

        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();

        // Test the repository method
        Optional<UzivatelEntity> result = uzivatelRepository.findByAuthKey("authKey123", 1);
        assertTrue("Result should be present", result.isPresent());
        assertEquals("user1", result.get().getLogin());
    }

    @Test
    public void testFindAllNotWeb() {
        assertNotNull("UzivatelRepository should be initialized!", uzivatelRepository);

        // Create and persist groups
        SkupinaEntity groupWeb = new SkupinaEntity("web", "Web Users", null, null, null);
        SkupinaEntity groupAdmin = new SkupinaEntity("admin", "Administrators", null, null, null);

        // Create and persist users
        UzivatelEntity user1 = new UzivatelEntity("user1", "password", "John Doe", true, null, null, null, groupWeb, null, null, null);
        UzivatelEntity user2 = new UzivatelEntity("user2", "password", "Jane Smith", true, null, null, null, groupAdmin, null, null, null);

        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.persist(groupWeb);
        em.persist(groupAdmin);
        em.persist(user1);
        em.persist(user2);
        em.getTransaction().commit();

        // Test the repository method
        List<UzivatelEntity> results = uzivatelRepository.findAllNotWeb();
        assertNotNull("Results should not be null!", results);
        assertEquals(1, results.size());
        assertEquals("user2", results.get(0).getLogin());
    }

    @Test
    public void testFindByAktivacniToken() {
        assertNotNull("UzivatelRepository should be initialized!", uzivatelRepository);

        // Create and persist a user with aktivacniToken
        UzivatelEntity user = new UzivatelEntity("user1", "password", "John Doe", true, "activationToken123", null, null, null, null, null, null);

        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();

        // Test the repository method
        Optional<UzivatelEntity> result = uzivatelRepository.findByAktivacniToken("activationToken123");
        assertTrue("Result should be present", result.isPresent());
        assertEquals("user1", result.get().getLogin());
    }

    @Test
    public void testFindBySkupinaWithPagination() {
        assertNotNull("UzivatelRepository should be initialized!", uzivatelRepository);

        // Create and persist a group
        SkupinaEntity group = new SkupinaEntity("group1", "Administrators", null, null, null);

        // Create and persist users
        UzivatelEntity user1 = new UzivatelEntity("user1", "password", "John Doe", true, null, null, null, group, null, null, null);
        UzivatelEntity user2 = new UzivatelEntity("user2", "password", "Jane Smith", true, null, null, null, group, null, null, null);
        UzivatelEntity user3 = new UzivatelEntity("user3", "password", "Bob Brown", true, null, null, null, group, null, null, null);

        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.persist(group);
        em.persist(user1);
        em.persist(user2);
        em.persist(user3);
        em.getTransaction().commit();

        // Test the repository method with pagination
        List<UzivatelEntity> results = uzivatelRepository.findBySkupina("group1", 1, 2);
        assertNotNull("Results should not be null!", results);
        assertEquals(2, results.size());
        assertEquals("user2", results.get(0).getLogin());
        assertEquals("user3", results.get(1).getLogin());
    }

}
