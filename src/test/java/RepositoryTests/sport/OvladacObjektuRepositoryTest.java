package RepositoryTests.sport;

import cz.inspire.thesis.data.EntityManagerProducer;
import cz.inspire.thesis.data.model.sport.objekt.OvladacObjektuEntity;
import cz.inspire.thesis.data.repository.sport.objekt.OvladacObjektuRepository;
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

public class OvladacObjektuRepositoryTest {

    private OvladacObjektuRepository repository;

    @Before
    public void setUp() {
        CdiContainer container = CdiContainerLoader.getCdiContainer();
        container.boot();

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        EntityManagerProducer producer = BeanProvider.getContextualReference(EntityManagerProducer.class);
        producer.setEntityManagerFactory(emf);

        repository = BeanProvider.getContextualReference(OvladacObjektuRepository.class);

        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.createQuery("DELETE FROM OvladacObjektuEntity").executeUpdate();
        em.getTransaction().commit();
    }

    @Test
    public void testFindAll() {
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();

        OvladacObjektuEntity entity1 = new OvladacObjektuEntity("id1", "ovladac1", "1,2,3", true, false, 10, 5, null, "obj1");
        OvladacObjektuEntity entity2 = new OvladacObjektuEntity("id2", "ovladac2", "4,5,6", false, true, 20, 10, null, "obj2");

        em.persist(entity1);
        em.persist(entity2);

        em.getTransaction().commit();

        List<OvladacObjektuEntity> results = repository.findAll();
        assertNotNull(results);
        assertEquals(2, results.size());
    }

    @Test
    public void testFindWithOvladacObjektu() {
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();

        OvladacObjektuEntity entity1 = new OvladacObjektuEntity("id1", "ovladac1", "1,2,3", true, false, 10, 5, null, "obj1");
        OvladacObjektuEntity entity2 = new OvladacObjektuEntity("id2", "ovladac1", "4,5,6", false, true, 20, 10, null, "obj2");
        OvladacObjektuEntity entity3 = new OvladacObjektuEntity("id3", "ovladac2", "7,8,9", true, true, 15, 7, null, "obj3");

        em.persist(entity1);
        em.persist(entity2);
        em.persist(entity3);

        em.getTransaction().commit();

        List<OvladacObjektuEntity> results = repository.findWithOvladacObjektu("ovladac1");
        assertNotNull(results);
        assertEquals(2, results.size());
        assertTrue(results.stream().anyMatch(o -> o.getId().equals("id1")));
        assertTrue(results.stream().anyMatch(o -> o.getId().equals("id2")));
    }

    @Test
    public void testFindByObjekt() {
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();

        OvladacObjektuEntity entity1 = new OvladacObjektuEntity("id1", "ovladac1", "1,2,3", true, false, 10, 5, null, "obj1");
        OvladacObjektuEntity entity2 = new OvladacObjektuEntity("id2", "ovladac2", "4,5,6", false, true, 20, 10, null, "obj1");
        OvladacObjektuEntity entity3 = new OvladacObjektuEntity("id3", "ovladac3", "7,8,9", true, true, 15, 7, null, "obj2");

        em.persist(entity1);
        em.persist(entity2);
        em.persist(entity3);

        em.getTransaction().commit();

        List<OvladacObjektuEntity> results = repository.findByObjekt("obj1");
        assertNotNull(results);
        assertEquals(2, results.size());
        assertTrue(results.stream().anyMatch(o -> o.getId().equals("id1")));
        assertTrue(results.stream().anyMatch(o -> o.getId().equals("id2")));
    }
}
