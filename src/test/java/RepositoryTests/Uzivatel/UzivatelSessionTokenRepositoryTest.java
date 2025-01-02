package RepositoryTests.Uzivatel;

import cz.inspire.thesis.data.model.uzivatel.UzivatelSessionTokenEntity;
import cz.inspire.thesis.data.repository.uzivatel.UzivatelSessionTokenRepository;
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
import java.util.Optional;

import static org.junit.Assert.*;

public class UzivatelSessionTokenRepositoryTest {

    private UzivatelSessionTokenRepository tokenRepository;

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

        // Access UzivatelSessionTokenRepository from CDI
        tokenRepository = BeanProvider.getContextualReference(UzivatelSessionTokenRepository.class);

        // Clear the database
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.createQuery("DELETE FROM UzivatelSessionTokenEntity").executeUpdate();
        em.getTransaction().commit();
    }

    @Test
    public void testFindByUsername() {
        assertNotNull("TokenRepository should be initialized!", tokenRepository);

        // Create and persist session tokens
        UzivatelSessionTokenEntity token1 = new UzivatelSessionTokenEntity("series1", "user1", "token1", new Date());
        UzivatelSessionTokenEntity token2 = new UzivatelSessionTokenEntity("series2", "user2", "token2", new Date());

        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.persist(token1);
        em.persist(token2);
        em.getTransaction().commit();

        // Test the repository method
        Optional<UzivatelSessionTokenEntity> result = tokenRepository.findByUsername("user1", 1);
        assertTrue("Result should be present", result.isPresent());
        assertEquals("series1", result.get().getSeries());
    }

    @Test
    public void testCountByUsername() {
        assertNotNull("TokenRepository should be initialized!", tokenRepository);

        // Create and persist session tokens
        UzivatelSessionTokenEntity token1 = new UzivatelSessionTokenEntity("series1", "user1", "token1", new Date());
        UzivatelSessionTokenEntity token2 = new UzivatelSessionTokenEntity("series2", "user1", "token2", new Date());
        UzivatelSessionTokenEntity token3 = new UzivatelSessionTokenEntity("series3", "user2", "token3", new Date());

        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.persist(token1);
        em.persist(token2);
        em.persist(token3);
        em.getTransaction().commit();

        // Test the repository method
        Long count = tokenRepository.countByUsername("user1");
        assertNotNull("Count should not be null!", count);
        assertEquals(2L, (long) count);
    }
}
