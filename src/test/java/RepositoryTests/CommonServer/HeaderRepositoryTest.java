package RepositoryTests.CommonServer;

import cz.inspire.thesis.data.EntityManagerProducer;
import cz.inspire.common.entity.HeaderEntity;
import cz.inspire.common.repository.HeaderRepository;
import jakarta.persistence.*;
import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class HeaderRepositoryTest {

    private HeaderRepository headerRepository;

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

        // Access HeaderRepository from CDI
        headerRepository = BeanProvider.getContextualReference(HeaderRepository.class);

        // Clear the database
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.createQuery("DELETE FROM HeaderEntity").executeUpdate();
        em.getTransaction().commit();
    }


    @Test
    public void testFindValidAttributes() {
        assertNotNull("HeaderRepository should be initialized!", headerRepository);

        // Test saving and querying a header
        headerRepository.save(new HeaderEntity("1", 10, 1));
        List<HeaderEntity> validHeaderEntities = headerRepository.findValidAtributes();

        assertEquals(1, validHeaderEntities.size());
    }
}