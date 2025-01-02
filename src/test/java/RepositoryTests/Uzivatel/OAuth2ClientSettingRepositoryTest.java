package RepositoryTests.Uzivatel;

import cz.inspire.thesis.data.model.uzivatel.OAuth2ClientSettingEntity;
import cz.inspire.thesis.data.repository.uzivatel.OAuth2ClientSettingRepository;
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

import static org.junit.Assert.*;

public class OAuth2ClientSettingRepositoryTest {

    private OAuth2ClientSettingRepository oauth2ClientSettingRepository;

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

        // Access OAuth2ClientSettingRepository from CDI
        oauth2ClientSettingRepository = BeanProvider.getContextualReference(OAuth2ClientSettingRepository.class);

        // Clear the database
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.createQuery("DELETE FROM OAuth2ClientSettingEntity").executeUpdate();
        em.getTransaction().commit();
    }

    @Test
    public void testFindAll_emptyDatabase() {
        assertNotNull("OAuth2ClientSettingRepository should be initialized!", oauth2ClientSettingRepository);

        // Test when the database is empty
        List<OAuth2ClientSettingEntity> results = oauth2ClientSettingRepository.findAll();
        assertNotNull("Results should not be null even if no records are present!", results);
        assertTrue("Results should be empty for an empty database.", results.isEmpty());
    }

    @Test
    public void testInsertAndRetrieve() {
        assertNotNull("OAuth2ClientSettingRepository should be initialized!", oauth2ClientSettingRepository);

        // Create and persist a setting
        OAuth2ClientSettingEntity setting = new OAuth2ClientSettingEntity(
                "oauth1", "clientId123", "secret", "scope1 scope2", "resource1,resource2",
                "authorization_code,refresh_token", "http://localhost/callback",
                "auto_scope", 3600, 7200
        );

        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.persist(setting);
        em.getTransaction().commit();

        // Test the repository method
        List<OAuth2ClientSettingEntity> results = oauth2ClientSettingRepository.findAll();
        assertNotNull("Results should not be null!", results);
        assertEquals(1, results.size());
        assertEquals("clientId123", results.get(0).getClientId());
    }

    @Test
    public void testFindAll_multipleRecords() {
        assertNotNull("OAuth2ClientSettingRepository should be initialized!", oauth2ClientSettingRepository);

        // Create and persist multiple settings
        OAuth2ClientSettingEntity setting1 = new OAuth2ClientSettingEntity(
                "oauth1", "clientId123", "secret1", null, null, null, null, null, 3600, 7200);
        OAuth2ClientSettingEntity setting2 = new OAuth2ClientSettingEntity(
                "oauth2", "clientId456", "secret2", null, null, null, null, null, 1800, 3600);

        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.persist(setting1);
        em.persist(setting2);
        em.getTransaction().commit();

        // Test the repository method
        List<OAuth2ClientSettingEntity> results = oauth2ClientSettingRepository.findAll();
        assertNotNull("Results should not be null!", results);
        assertEquals(2, results.size());
    }

    @Test
    public void testFindByClientId_multipleRecords() {
        assertNotNull("OAuth2ClientSettingRepository should be initialized!", oauth2ClientSettingRepository);

        // Create and persist settings with the same clientId
        OAuth2ClientSettingEntity setting1 = new OAuth2ClientSettingEntity(
                "oauth1", "clientId123", "secret1", null, null, null, null, null, 3600, 7200);
        OAuth2ClientSettingEntity setting2 = new OAuth2ClientSettingEntity(
                "oauth2", "clientId123", "secret2", null, null, null, null, null, 1800, 3600);

        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.persist(setting1);
        em.persist(setting2);
        em.getTransaction().commit();

        // Test the behavior when multiple records exist for the same clientId
        List<OAuth2ClientSettingEntity> results = oauth2ClientSettingRepository.findAll();
        assertNotNull("Results should not be null!", results);
        assertEquals(2, results.size());
        assertEquals("clientId123", results.get(0).getClientId());
        assertEquals("clientId123", results.get(1).getClientId());
    }
}
