package RepositoryTests.Token;

import cz.inspire.thesis.data.EntityManagerProducer;
import cz.inspire.thesis.data.model.token.TypTokenuEntity;
import cz.inspire.thesis.data.model.token.TypTokenuLocEntity;
import cz.inspire.thesis.data.repository.token.TypTokenuRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class TypTokenuRepositoryTest {

    private TypTokenuRepository typTokenuRepository;

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

        // Access TypTokenuRepository from CDI
        typTokenuRepository = BeanProvider.getContextualReference(TypTokenuRepository.class);

        // Clear the database
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.createQuery("DELETE FROM TypTokenuLocEntity").executeUpdate();
        em.createQuery("DELETE FROM TypTokenuEntity").executeUpdate();
        em.getTransaction().commit();
    }

    @Test
    public void testFindAll() {
        assertNotNull("TypTokenuRepository should be initialized!", typTokenuRepository);

        // Create and persist locale data
        TypTokenuLocEntity loc1 = new TypTokenuLocEntity("loc1", "cs", "Nazev A", "Popis A");
        TypTokenuLocEntity loc2 = new TypTokenuLocEntity("loc2", "cs", "Nazev B", "Popis B");

        // Create and persist types with locale data
        TypTokenuEntity type1 = new TypTokenuEntity("type1", true, true, null, List.of(loc1));
        TypTokenuEntity type2 = new TypTokenuEntity("type2", true, false, null, List.of(loc2));

        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.persist(type1);
        em.persist(type2);
        em.getTransaction().commit();

        // Test the repository method
        List<TypTokenuEntity> results = typTokenuRepository.findAll("cs", 0, 2);
        assertNotNull("Results should not be null", results);
        assertEquals(2, results.size());
        assertEquals("type1", results.get(0).getId());
        assertEquals("type2", results.get(1).getId());
    }
}
