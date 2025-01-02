package RepositoryTests.Uzivatel;

import cz.inspire.thesis.data.model.uzivatel.RoleEntity;
import cz.inspire.thesis.data.model.uzivatel.SkupinaEntity;
import cz.inspire.thesis.data.repository.uzivatel.SkupinaRepository;
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

public class SkupinaRepositoryTest {

    private SkupinaRepository skupinaRepository;

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

        // Access SkupinaRepository from CDI
        skupinaRepository = BeanProvider.getContextualReference(SkupinaRepository.class);

        // Clear the database
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.createQuery("DELETE FROM SkupinaEntity").executeUpdate();
        em.createQuery("DELETE FROM RoleEntity").executeUpdate();
        em.getTransaction().commit();
    }

    @Test
    public void testFindAll_emptyDatabase() {
        assertNotNull("SkupinaRepository should be initialized!", skupinaRepository);

        // Test when the database is empty
        List<SkupinaEntity> results = skupinaRepository.findAll();
        assertNotNull("Results should not be null even if no records are present!", results);
        assertTrue("Results should be empty for an empty database.", results.isEmpty());
    }

    @Test
    public void testFindAll_singleGroup() {
        assertNotNull("SkupinaRepository should be initialized!", skupinaRepository);

        // Create and persist a group
        SkupinaEntity group = new SkupinaEntity("group1", "Administrators", null, null, null);

        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.persist(group);
        em.getTransaction().commit();

        // Test the repository method
        List<SkupinaEntity> results = skupinaRepository.findAll();
        assertNotNull("Results should not be null!", results);
        assertEquals(1, results.size());
        assertEquals("Administrators", results.get(0).getNazev());
    }

    @Test
    public void testFindAll_multipleGroups() {
        assertNotNull("SkupinaRepository should be initialized!", skupinaRepository);

        // Create and persist multiple groups
        SkupinaEntity group1 = new SkupinaEntity("group1", "Administrators", null, null, null);
        SkupinaEntity group2 = new SkupinaEntity("group2", "Developers", null, null, null);
        SkupinaEntity group3 = new SkupinaEntity("group3", "Users", null, null, null);

        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.persist(group1);
        em.persist(group2);
        em.persist(group3);
        em.getTransaction().commit();

        // Test the repository method
        List<SkupinaEntity> results = skupinaRepository.findAll();
        assertNotNull("Results should not be null!", results);
        assertEquals(3, results.size());
        assertEquals("Administrators", results.get(0).getNazev()); // Check order by `nazev`
        assertEquals("Developers", results.get(1).getNazev());
        assertEquals("Users", results.get(2).getNazev());
    }

    @Test
    public void testFindAll_withPagination() {
        assertNotNull("SkupinaRepository should be initialized!", skupinaRepository);

        // Create and persist multiple groups
        SkupinaEntity group1 = new SkupinaEntity("group1", "Administrators", null, null, null);
        SkupinaEntity group2 = new SkupinaEntity("group2", "Developers", null, null, null);
        SkupinaEntity group3 = new SkupinaEntity("group3", "Users", null, null, null);

        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.persist(group1);
        em.persist(group2);
        em.persist(group3);
        em.getTransaction().commit();

        // Test the repository method with pagination
        List<SkupinaEntity> results = skupinaRepository.findAll(1, 2);
        assertNotNull("Results should not be null!", results);
        assertEquals(2, results.size());
        assertEquals("Developers", results.get(0).getNazev());
        assertEquals("Users", results.get(1).getNazev());
    }
}
