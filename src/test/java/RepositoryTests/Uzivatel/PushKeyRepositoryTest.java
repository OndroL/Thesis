package RepositoryTests.Uzivatel;

import cz.inspire.thesis.data.model.uzivatel.PushKeyEntity;
import cz.inspire.thesis.data.model.uzivatel.UzivatelEntity;
import cz.inspire.thesis.data.repository.uzivatel.PushKeyRepository;
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

public class PushKeyRepositoryTest {

    private PushKeyRepository pushKeyRepository;

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

        // Access PushKeyRepository from CDI
        pushKeyRepository = BeanProvider.getContextualReference(PushKeyRepository.class);

        // Clear the database
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.createQuery("DELETE FROM PushKeyEntity").executeUpdate();
        em.createQuery("DELETE FROM UzivatelEntity").executeUpdate();
        em.getTransaction().commit();
    }

    @Test
    public void testFindAll_emptyDatabase() {
        assertNotNull("PushKeyRepository should be initialized!", pushKeyRepository);

        // Test when the database is empty
        List<PushKeyEntity> results = pushKeyRepository.findAll();
        assertNotNull("Results should not be null even if no records are present!", results);
        assertTrue("Results should be empty for an empty database.", results.isEmpty());
    }

    @Test
    public void testInsertAndRetrieve() {
        assertNotNull("PushKeyRepository should be initialized!", pushKeyRepository);

        // Create and persist a user
        UzivatelEntity user = new UzivatelEntity();
        user.setLogin("user1");
        user.setJmeno("John Doe");
        user.setHeslo("password");
        user.setAktivni(true);

        // Create and persist a push key
        PushKeyEntity pushKey = new PushKeyEntity("key1", "key123", user);

        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.persist(user);
        em.persist(pushKey);
        em.getTransaction().commit();

        // Test the repository method
        List<PushKeyEntity> results = pushKeyRepository.findAll();
        assertNotNull("Results should not be null!", results);
        assertEquals(1, results.size());
        assertEquals("key123", results.get(0).getKey());
    }

    @Test
    public void testFindByKey_exactMatch() {
        assertNotNull("PushKeyRepository should be initialized!", pushKeyRepository);

        // Create and persist a user
        UzivatelEntity user = new UzivatelEntity();
        user.setLogin("user1");
        user.setJmeno("John Doe");
        user.setHeslo("password");
        user.setAktivni(true);

        // Create and persist a push key
        PushKeyEntity pushKey = new PushKeyEntity("key1", "key123", user);

        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.persist(user);
        em.persist(pushKey);
        em.getTransaction().commit();

        // Test the repository method
        Optional<PushKeyEntity> result = pushKeyRepository.findByKey("key123");
        assertTrue("Result should be present", result.isPresent());
        assertEquals("key123", result.get().getKey());
        assertEquals("user1", result.get().getUzivatel().getLogin());
    }

    @Test
    public void testFindByKey_withPrefixes() {
        assertNotNull("PushKeyRepository should be initialized!", pushKeyRepository);

        // Create and persist a user
        UzivatelEntity user = new UzivatelEntity();
        user.setLogin("user1");
        user.setJmeno("John Doe");
        user.setHeslo("password");
        user.setAktivni(true);

        // Create and persist push keys
        PushKeyEntity androidKey = new PushKeyEntity("key1", "android_key123", user);
        PushKeyEntity iosKey = new PushKeyEntity("key2", "ios_key124", user);

        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.persist(user);
        em.persist(androidKey);
        em.persist(iosKey);
        em.getTransaction().commit();

        // Test the repository method for "key123"
        Optional<PushKeyEntity> result = pushKeyRepository.findByKey("key123");
        assertTrue("Result should be present", result.isPresent());
        assertTrue("Key should match",result.get().getKey().equals("android_key123"));
    }

    @Test
    public void testFindByKey_missingUzivatel() {
        assertNotNull("PushKeyRepository should be initialized!", pushKeyRepository);

        // Create and persist a push key with null uzivatel
        PushKeyEntity pushKey = new PushKeyEntity("key1", "key123", null);

        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.persist(pushKey);
        em.getTransaction().commit();

        // Test the repository method
        Optional<PushKeyEntity> result = pushKeyRepository.findByKey("key123");
        assertFalse("Result should not be present when uzivatel is null", result.isPresent());
    }
}
