package RepositoryTests.Uzivatel;

import cz.inspire.thesis.data.model.uzivatel.RoleEntity;
import cz.inspire.thesis.data.repository.uzivatel.RoleRepository;
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

public class RoleRepositoryTest {

    private RoleRepository roleRepository;

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

        // Access RoleRepository from CDI
        roleRepository = BeanProvider.getContextualReference(RoleRepository.class);

        // Clear the database
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.createQuery("DELETE FROM RoleEntity").executeUpdate();
        em.getTransaction().commit();
    }

    @Test
    public void testFindAll_emptyDatabase() {
        assertNotNull("RoleRepository should be initialized!", roleRepository);

        // Test when the database is empty
        List<RoleEntity> results = roleRepository.findAll();
        assertNotNull("Results should not be null even if no records are present!", results);
        assertTrue("Results should be empty for an empty database.", results.isEmpty());
    }

    @Test
    public void testFindAll_singleRole() {
        assertNotNull("RoleRepository should be initialized!", roleRepository);

        // Create and persist a role
        RoleEntity role = new RoleEntity("role1", "Admin", "Administrator role");

        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.persist(role);
        em.getTransaction().commit();

        // Test the repository method
        List<RoleEntity> results = roleRepository.findAll();
        assertNotNull("Results should not be null!", results);
        assertEquals(1, results.size());
        assertEquals("Admin", results.get(0).getNazev());
    }

    @Test
    public void testFindAll_multipleRoles() {
        assertNotNull("RoleRepository should be initialized!", roleRepository);

        // Create and persist multiple roles
        RoleEntity role1 = new RoleEntity("role1", "Manager", "Manager role");
        RoleEntity role2 = new RoleEntity("role2", "Admin", "Administrator role");
        RoleEntity role3 = new RoleEntity("role3", "User", "User role");

        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.persist(role1);
        em.persist(role2);
        em.persist(role3);
        em.getTransaction().commit();

        // Test the repository method
        List<RoleEntity> results = roleRepository.findAll();
        assertNotNull("Results should not be null!", results);
        assertEquals(3, results.size());
        assertEquals("Admin", results.get(0).getNazev()); // Check order by `nazev`
        assertEquals("Manager", results.get(1).getNazev());
        assertEquals("User", results.get(2).getNazev());
    }
}
