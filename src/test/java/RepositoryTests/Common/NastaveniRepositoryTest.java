package RepositoryTests.Common;

import cz.inspire.common.entity.NastaveniEntity;
import cz.inspire.common.repository.NastaveniRepository;
import cz.inspire.EntityManagerProducer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class NastaveniRepositoryTest {

    private NastaveniRepository nastaveniRepository;

    @Before
    public void setUp() {
        // Boot the CDI container
        CdiContainer container = CdiContainerLoader.getCdiContainer();
        container.boot();

        // Create the EntityManagerFactory
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");

        // Access EntityManagerProducer and set the EntityManagerFactory
        EntityManagerProducer producer = BeanProvider.getContextualReference(EntityManagerProducer.class);
        producer.setEntityManagerFactory(emf);

        // Access NastaveniRepository from CDI
        nastaveniRepository = BeanProvider.getContextualReference(NastaveniRepository.class);

        // Clear the database
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.createQuery("DELETE FROM NastaveniEntity").executeUpdate();
        em.getTransaction().commit();
    }

    @Test
    public void testSaveAndFindById() {
        assertNotNull("NastaveniRepository should be initialized!", nastaveniRepository);

        NastaveniEntity entity = new NastaveniEntity("key1", "value1");
        nastaveniRepository.save(entity);

        NastaveniEntity foundEntity = nastaveniRepository.findById(entity.getKey()).orElse(null);
        assertNotNull("Entity should be found!", foundEntity);
        assertEquals("key1", foundEntity.getKey());
        assertEquals("value1", foundEntity.getValue());
    }
}

