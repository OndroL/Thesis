package RepositoryTests.Common;

import cz.inspire.common.entity.HeaderEntity;
import cz.inspire.common.repository.HeaderRepository;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class HeaderRepositoryTest {

    @Inject
    private HeaderRepository headerRepository;

    private EntityManager entityManager;

    @Before
    public void setUp() {
        // Initialize EntityManagerFactory
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        entityManager = emf.createEntityManager();

        // Retrieve HeaderRepository using CDI
        headerRepository = CDI.current().select(HeaderRepository.class).get();
        assertNotNull("HeaderRepository should be initialized!", headerRepository);

        // Clear the database
        entityManager.getTransaction().begin();
        entityManager.createQuery("DELETE FROM HeaderEntity").executeUpdate();
        entityManager.getTransaction().commit();
    }


    @Test
    public void testFindValidAttributes() {
        assertNotNull("HeaderRepository should be initialized!", headerRepository);

        // Test saving and querying a header
        headerRepository.save(new HeaderEntity("1", 10, 1));
        List<HeaderEntity> validHeaderEntities = headerRepository.findValidAttributes();

        assertEquals(1, validHeaderEntities.size());
    }
}