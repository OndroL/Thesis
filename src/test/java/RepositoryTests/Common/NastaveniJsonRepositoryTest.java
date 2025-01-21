package RepositoryTests.Common;

import cz.inspire.common.entity.NastaveniJsonEntity;
import cz.inspire.common.repository.NastaveniJsonRepository;
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

public class NastaveniJsonRepositoryTest {

    private NastaveniJsonRepository nastaveniJsonRepository;

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

        // Access NastaveniJsonRepository from CDI
        nastaveniJsonRepository = BeanProvider.getContextualReference(NastaveniJsonRepository.class);

        // Clear the database
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.createQuery("DELETE FROM NastaveniJsonEntity").executeUpdate();
        em.getTransaction().commit();
    }

    @Test
    public void testSaveAndFindById() {
        assertNotNull("NastaveniJsonRepository should be initialized!", nastaveniJsonRepository);

        NastaveniJsonEntity entity = new NastaveniJsonEntity("key1", "value1");
        nastaveniJsonRepository.save(entity);

        NastaveniJsonEntity foundEntity = nastaveniJsonRepository.findById(entity.getKey()).orElse(null);
        assertNotNull("Entity should be found!", foundEntity);
        assertEquals("key1", foundEntity.getKey());
        assertEquals("value1", foundEntity.getValue());
    }
}
