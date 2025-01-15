package RepositoryTests.License;

import cz.inspire.EntityManagerProducer;
import cz.inspire.license.entity.LicenseEntity;
import cz.inspire.license.repository.LicenseRepository;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class LicenseRepositoryTest {

    private LicenseRepository licenseRepository;

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

        // Access LicenseRepository from CDI
        licenseRepository = BeanProvider.getContextualReference(LicenseRepository.class);

        // Clear the database
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.createQuery("DELETE FROM LicenseEntity").executeUpdate();
        em.getTransaction().commit();
    }

    @Test
    public void testSaveAndFindAll() {
        assertNotNull("LicenseRepository should be initialized!", licenseRepository);

        // Create and save a LicenseEntity
        LicenseEntity license = new LicenseEntity(
                "1", "Customer1", true, true,
                new Date(), true, new Date(), true,
                10, 5, 50, 20, 10,
                5, 15, 10, true, 12345L, "hash123",
                new Date(), new Date(), 1001
        );
        licenseRepository.save(license);

        // Retrieve all licenses
        List<LicenseEntity> licenses = licenseRepository.findAll();
        assertNotNull("License list should not be null", licenses);
        assertEquals(1, licenses.size());
        assertEquals("1", licenses.getFirst().getId());
    }

    @Test
    public void testSaveMultipleAndFindAll() {
        LicenseEntity license1 = new LicenseEntity(
                "1", "Customer1", true, true,
                new Date(), true, new Date(), true,
                10, 5, 50, 20, 10,
                5, 12, 15, true, 12345L, "hash123",
                new Date(), new Date(), 1001
        );

        LicenseEntity license2 = new LicenseEntity(
                "2", "Customer2", false, false,
                new Date(), false, new Date(), false,
                20, 10, 100, 40, 20,
                10,10, 30, false, 54321L, "hash456",
                new Date(), new Date(), 1002
        );

        licenseRepository.save(license1);
        licenseRepository.save(license2);

        // Retrieve all licenses
        List<LicenseEntity> licenses = licenseRepository.findAll();
        assertNotNull("License list should not be null", licenses);
        assertEquals(2, licenses.size());
    }
}
