package RepositoryTests.Uzivatel;

import cz.inspire.thesis.data.model.uzivatel.ExternalClientEntity;
import cz.inspire.thesis.data.model.uzivatel.OAuth2ClientSettingEntity;
import cz.inspire.thesis.data.repository.uzivatel.ExternalClientRepository;
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

public class ExternalClientRepositoryTest {

    private ExternalClientRepository externalClientRepository;

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

        // Access ExternalClientRepository from CDI
        externalClientRepository = BeanProvider.getContextualReference(ExternalClientRepository.class);

        // Clear the database
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.createQuery("DELETE FROM ExternalClientEntity").executeUpdate();
        em.createQuery("DELETE FROM OAuth2ClientSettingEntity").executeUpdate();
        em.getTransaction().commit();
    }

    @Test
    public void testFindAll() {
        assertNotNull("ExternalClientRepository should be initialized!", externalClientRepository);

        // Create and persist external clients
        ExternalClientEntity client1 = new ExternalClientEntity("id1", "Client A", null, null);
        ExternalClientEntity client2 = new ExternalClientEntity("id2", "Client B", null, null);

        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.persist(client1);
        em.persist(client2);
        em.getTransaction().commit();

        // Test the repository method
        List<ExternalClientEntity> results = externalClientRepository.findAll();
        assertNotNull("Results should not be null", results);
        assertEquals(2, results.size());
    }

    @Test
    public void testFindByOAuth2ClientId() {
        assertNotNull("ExternalClientRepository should be initialized!", externalClientRepository);

        // Create and persist OAuth2 client setting
        OAuth2ClientSettingEntity oauthSetting = new OAuth2ClientSettingEntity(
                "oauth1", "clientId123", "secret", null, null, null, null, null, null, null);
        ExternalClientEntity client = new ExternalClientEntity("id1", "Client A", null, oauthSetting);

        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.persist(oauthSetting);
        em.persist(client);
        em.getTransaction().commit();

        // Test the repository method
        Optional<ExternalClientEntity> result = externalClientRepository.findByOAuth2ClientId("clientId123");
        assertTrue("Result should be present", result.isPresent());
        assertEquals("id1", result.get().getId());
    }
}
