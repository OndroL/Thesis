package RepositoryTests.Token;

import cz.inspire.thesis.data.EntityManagerProducer;
import cz.inspire.thesis.data.model.token.TokenEntity;
import cz.inspire.thesis.data.model.token.TypTokenuEntity;
import cz.inspire.thesis.data.repository.token.TokenRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class TokenRepositoryTest {

    private TokenRepository tokenRepository;

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

        // Access TokenRepository from CDI
        tokenRepository = BeanProvider.getContextualReference(TokenRepository.class);

        // Clear the database
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.createQuery("DELETE FROM TokenEntity").executeUpdate();
        em.createQuery("DELETE FROM TypTokenuEntity").executeUpdate();
        em.getTransaction().commit();
    }

    @Test
    public void testFindAll() {
        assertNotNull("TokenRepository should be initialized!", tokenRepository);

        // Create and persist some TokenEntity
        TokenEntity token1 = new TokenEntity("id1", "popis1", null);
        TokenEntity token2 = new TokenEntity("id2", "popis2", null);

        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.persist(token1);
        em.persist(token2);
        em.getTransaction().commit();

        // Test the repository method
        List<TokenEntity> results = tokenRepository.findAll();
        assertNotNull("Results should not be null", results);
        assertEquals(2, results.size());
    }

    @Test
    public void testFindAllWithPagination() {
        assertNotNull("TokenRepository should be initialized!", tokenRepository);

        // Create and persist some TokenEntity
        TokenEntity token1 = new TokenEntity("id1", "popis1", null);
        TokenEntity token2 = new TokenEntity("id2", "popis2", null);
        TokenEntity token3 = new TokenEntity("id3", "popis3", null);

        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.persist(token1);
        em.persist(token2);
        em.persist(token3);
        em.getTransaction().commit();

        // Test the repository method
        List<TokenEntity> results = tokenRepository.findAll(0, 2);
        assertNotNull("Results should not be null", results);
        assertEquals(2, results.size());
    }

    @Test
    public void testFindByTyp() {
        assertNotNull("TokenRepository should be initialized!", tokenRepository);

        // Create and persist a TypTokenuEntity
        TypTokenuEntity typ = new TypTokenuEntity("typ1", true, true, null, null);

        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.persist(typ);
        em.getTransaction().commit();

        // Create and persist some TokenEntity with the TypTokenuEntity
        TokenEntity token1 = new TokenEntity("id1", "popis1", typ);
        TokenEntity token2 = new TokenEntity("id2", "popis2", typ);

        em.getTransaction().begin();
        em.persist(token1);
        em.persist(token2);
        em.getTransaction().commit();

        // Test the repository method
        List<TokenEntity> results = tokenRepository.findByTyp("typ1", 0, 2);
        assertNotNull("Results should not be null", results);
        assertEquals(2, results.size());
    }

    @Test
    public void testFindBySuffix() {
        assertNotNull("TokenRepository should be initialized!", tokenRepository);

        // Create and persist a TokenEntity
        TokenEntity token = new TokenEntity("id_suffix", "popis", null);

        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.persist(token);
        em.getTransaction().commit();

        // Test the repository method
        Optional<TokenEntity> result = tokenRepository.findBySuffix("%suffix");
        assertTrue("Result should not be empty", result.isPresent());
        assertEquals("id_suffix", result.get().getId());
    }
}
