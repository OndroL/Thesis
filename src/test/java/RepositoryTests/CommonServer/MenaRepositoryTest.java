package RepositoryTests.CommonServer;

import cz.inspire.thesis.data.EntityManagerProducer;
import cz.inspire.common.model.MenaEntity;

import cz.inspire.common.repository.MenaRepository;
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

public class MenaRepositoryTest {

    private MenaRepository menaRepository;

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

        // Access MenaRepository from CDI
        menaRepository = BeanProvider.getContextualReference(MenaRepository.class);

        // Clear the database
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.createQuery("DELETE FROM MenaEntity").executeUpdate();
        em.getTransaction().commit();
    }


    @Test
    public void testFindByCode() {
        assertNotNull("MenaRepository should be initialized!", menaRepository);

        // Test saving a Mena entity
        MenaEntity menaEntity = new MenaEntity("1", "USD", "Dollar", 840, 5, 2);
        menaRepository.save(menaEntity);

        // Test finding the Mena entity by code
        List<MenaEntity> foundMenaEntityList = menaRepository.findByCode("USD");
        assertNotNull("Result list should not be null", foundMenaEntityList);
        assertEquals(1, foundMenaEntityList.size());
        assertEquals("Dollar", foundMenaEntityList.getFirst().getVycetka());
    }

    @Test
    public void testFindByCodeNum() {
        assertNotNull("MenaRepository should be initialized!", menaRepository);

        // Test saving a Mena entity
        MenaEntity menaEntity = new MenaEntity("1", "USD", "Dollar", 840, 5, 2);
        menaRepository.save(menaEntity);

        // Test finding the Mena entity by codeNum
        List<MenaEntity> foundMenaEntityList = menaRepository.findByCodeNum(840);
        assertNotNull("Result list should not be null", foundMenaEntityList);
        assertEquals(1, foundMenaEntityList.size());
        assertEquals("USD", foundMenaEntityList.getFirst().getKod());
    }

    @Test
    public void testFindAll() {
        // Test saving multiple Mena entities
        menaRepository.save(new MenaEntity("1", "USD", "Dollar", 840, 5, 2));
        menaRepository.save(new MenaEntity("2", "EUR", "Euro", 978, 2, 2));

        // Test retrieving all Mena entities
        assertEquals(2, menaRepository.findAll().size());
    }
}