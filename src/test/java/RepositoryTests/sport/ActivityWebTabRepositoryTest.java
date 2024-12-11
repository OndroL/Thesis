package RepositoryTests.sport;

import cz.inspire.thesis.data.EntityManagerProducer;
import cz.inspire.thesis.data.model.sport.activity.ActivityWebTabEntity;
import cz.inspire.thesis.data.repository.sport.activity.ActivityWebTabRepository;
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

public class ActivityWebTabRepositoryTest {

    private ActivityWebTabRepository repository;

    @Before
    public void setUp() {
        CdiContainer container = CdiContainerLoader.getCdiContainer();
        container.boot();

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        EntityManagerProducer producer = BeanProvider.getContextualReference(EntityManagerProducer.class);
        producer.setEntityManagerFactory(emf);

        repository = BeanProvider.getContextualReference(ActivityWebTabRepository.class);

        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.createQuery("DELETE FROM ActivityWebTabEntity").executeUpdate();
        em.getTransaction().commit();
    }

    @Test
    public void testFindAll() {
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();

        ActivityWebTabEntity entity1 = new ActivityWebTabEntity("id1", "sport1", "activity1", "object1", 1);
        ActivityWebTabEntity entity2 = new ActivityWebTabEntity("id2", "sport2", "activity2", "object2", 2);

        em.persist(entity1);
        em.persist(entity2);

        em.getTransaction().commit();

        List<ActivityWebTabEntity> results = repository.findAll();
        assertNotNull(results);
        assertEquals(2, results.size());
    }

    @Test
    public void testFindBySport() {
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();

        ActivityWebTabEntity entity1 = new ActivityWebTabEntity("id1", "sport1", "activity1", "object1", 1);
        ActivityWebTabEntity entity2 = new ActivityWebTabEntity("id2", "sport1", "activity2", "object2", 2);
        ActivityWebTabEntity entity3 = new ActivityWebTabEntity("id3", "sport2", "activity3", "object3", 3);

        em.persist(entity1);
        em.persist(entity2);
        em.persist(entity3);

        em.getTransaction().commit();

        List<ActivityWebTabEntity> results = repository.findBySport("sport1");
        assertNotNull(results);
        assertEquals(2, results.size());
        assertTrue(results.stream().anyMatch(e -> e.getId().equals("id1")));
        assertTrue(results.stream().anyMatch(e -> e.getId().equals("id2")));
    }

    @Test
    public void testFindByActivity() {
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();

        ActivityWebTabEntity entity1 = new ActivityWebTabEntity("id1", "sport1", "activity1", "object1", 1);
        ActivityWebTabEntity entity2 = new ActivityWebTabEntity("id2", "sport2", "activity1", "object2", 2);
        ActivityWebTabEntity entity3 = new ActivityWebTabEntity("id3", "sport3", "activity2", "object3", 3);

        em.persist(entity1);
        em.persist(entity2);
        em.persist(entity3);

        em.getTransaction().commit();

        List<ActivityWebTabEntity> results = repository.findByActivity("activity1");
        assertNotNull(results);
        assertEquals(2, results.size());
        assertTrue(results.stream().anyMatch(e -> e.getId().equals("id1")));
        assertTrue(results.stream().anyMatch(e -> e.getId().equals("id2")));
    }

    @Test
    public void testFindByObject() {
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();

        ActivityWebTabEntity entity1 = new ActivityWebTabEntity("id1", "sport1", "activity1", "object1", 1);
        ActivityWebTabEntity entity2 = new ActivityWebTabEntity("id2", "sport2", "activity2", "object2", 2);
        ActivityWebTabEntity entity3 = new ActivityWebTabEntity("id3", "sport3", "activity3", "object1", 3);

        em.persist(entity1);
        em.persist(entity2);
        em.persist(entity3);

        em.getTransaction().commit();

        List<ActivityWebTabEntity> results = repository.findByObject("object1");
        assertNotNull(results);
        assertEquals(2, results.size());
        assertTrue(results.stream().anyMatch(e -> e.getId().equals("id1")));
        assertTrue(results.stream().anyMatch(e -> e.getId().equals("id3")));
    }
}
