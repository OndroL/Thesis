package RepositoryTests.Token;

import cz.inspire.thesis.data.EntityManagerProducer;
import cz.inspire.thesis.data.model.token.TokenConfirmationEntity;
import cz.inspire.thesis.data.repository.token.TokenConfirmationRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class TokenConfirmationRepositoryTest {

    private TokenConfirmationRepository tokenConfirmationRepository;

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

        // Access TokenConfirmationRepository from CDI
        tokenConfirmationRepository = BeanProvider.getContextualReference(TokenConfirmationRepository.class);

        // Clear the database
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.createQuery("DELETE FROM TokenConfirmationEntity").executeUpdate();
        em.getTransaction().commit();
    }

    @Test
    public void testFindByDate() {
        assertNotNull("TokenConfirmationRepository should be initialized!", tokenConfirmationRepository);

        // Create and persist TokenConfirmationEntities
        Date now = new Date();
        Date earlier = new Date(System.currentTimeMillis() - 10000); // 10 seconds earlier
        Date later = new Date(System.currentTimeMillis() + 10000);  // 10 seconds later

        TokenConfirmationEntity entity1 = new TokenConfirmationEntity("id1", earlier, "zakaznik1", "uzivatel1", "token1", true);
        TokenConfirmationEntity entity2 = new TokenConfirmationEntity("id2", now, "zakaznik2", "uzivatel2", "token2", true);
        TokenConfirmationEntity entity3 = new TokenConfirmationEntity("id3", later, "zakaznik3", "uzivatel3", "token3", false);

        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.persist(entity1);
        em.persist(entity2);
        em.persist(entity3);
        em.getTransaction().commit();

        // Test the repository method
        List<TokenConfirmationEntity> results = tokenConfirmationRepository.findByDate(earlier, now);
        assertNotNull("Results should not be null", results);
        assertEquals(2, results.size());
        assertTrue("Results should contain entity1", results.contains(entity1));
        assertTrue("Results should contain entity2", results.contains(entity2));
        assertFalse("Results should not contain entity3", results.contains(entity3));
    }
}
