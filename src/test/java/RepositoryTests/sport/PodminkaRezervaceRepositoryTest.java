package RepositoryTests.sport;

import cz.inspire.thesis.data.EntityManagerProducer;
import cz.inspire.thesis.data.model.sport.objekt.ObjektEntity;
import cz.inspire.thesis.data.model.sport.objekt.PodminkaRezervaceEntity;
import cz.inspire.thesis.data.repository.sport.objekt.PodminkaRezervaceRepository;
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

public class PodminkaRezervaceRepositoryTest {

    private PodminkaRezervaceRepository repository;

    @Before
    public void setUp() {
        CdiContainer container = CdiContainerLoader.getCdiContainer();
        container.boot();

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        EntityManagerProducer producer = BeanProvider.getContextualReference(EntityManagerProducer.class);
        producer.setEntityManagerFactory(emf);

        repository = BeanProvider.getContextualReference(PodminkaRezervaceRepository.class);

        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.createQuery("DELETE FROM PodminkaRezervaceEntity").executeUpdate();
        em.createQuery("DELETE FROM ObjektEntity").executeUpdate();
        em.getTransaction().commit();
    }

    private ObjektEntity createObjekt(String id) {
        return new ObjektEntity(
                id, 100, 15, 1, true, 30, 120, 10, 20, 5, 15, true, 2,
                null, null, true, false, false, true, false, true, true, true,
                false, true, 5, 10, 15, "calendarId", true, 30, null,
                null, null, null, null, null, null
        );
    }

    @Test
    public void testFindAll() {
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();

        ObjektEntity objekt = createObjekt("obj1");
        em.persist(objekt);

        PodminkaRezervaceEntity condition1 = new PodminkaRezervaceEntity("id1", "Condition 1", 1, "reservation1", false, objekt);
        PodminkaRezervaceEntity condition2 = new PodminkaRezervaceEntity("id2", "Condition 2", 2, "reservation2", true, objekt);

        em.persist(condition1);
        em.persist(condition2);

        em.getTransaction().commit();

        List<PodminkaRezervaceEntity> results = repository.findAll();
        assertNotNull(results);
        assertEquals(2, results.size());
    }

    @Test
    public void testFindAllWithPagination() {
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();

        ObjektEntity objekt = createObjekt("obj1");
        em.persist(objekt);

        for (int i = 1; i <= 5; i++) {
            PodminkaRezervaceEntity condition = new PodminkaRezervaceEntity("id" + i, "Condition " + i, i, "reservation" + i, i % 2 == 0, objekt);
            em.persist(condition);
        }

        em.getTransaction().commit();

        List<PodminkaRezervaceEntity> results = repository.findAll(0, 3);
        assertNotNull(results);
        assertEquals(3, results.size());
        assertEquals("id1", results.get(0).getId());
        assertEquals("id2", results.get(1).getId());
        assertEquals("id3", results.get(2).getId());
    }

    @Test
    public void testFindByObjekt() {
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();

        ObjektEntity objekt = createObjekt("obj1");
        em.persist(objekt);

        for (int i = 1; i <= 5; i++) {
            PodminkaRezervaceEntity condition = new PodminkaRezervaceEntity("id" + i, "Condition " + i, i, "reservation" + i, i % 2 == 0, objekt);
            em.persist(condition);
        }

        em.getTransaction().commit();

        List<PodminkaRezervaceEntity> results = repository.findByObjekt("obj1", 0, 3);
        assertNotNull(results);
        assertEquals(3, results.size());
        assertEquals("id1", results.get(0).getId());
        assertEquals("id2", results.get(1).getId());
        assertEquals("id3", results.get(2).getId());
    }

    @Test
    public void testCountAllByObject() {
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();

        ObjektEntity objekt = createObjekt("obj1");
        em.persist(objekt);

        for (int i = 1; i <= 5; i++) {
            PodminkaRezervaceEntity condition = new PodminkaRezervaceEntity("id" + i, "Condition " + i, i, "reservation" + i, i % 2 == 0, objekt);
            em.persist(condition);
        }

        em.getTransaction().commit();

        Long count = repository.countAllByObject("obj1");
        assertEquals(Long.valueOf(5), count);
    }

    @Test
    public void testCountAll() {
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();

        ObjektEntity objekt = createObjekt("obj1");
        em.persist(objekt);

        for (int i = 1; i <= 5; i++) {
            PodminkaRezervaceEntity condition = new PodminkaRezervaceEntity("id" + i, "Condition " + i, i, "reservation" + i, i % 2 == 0, objekt);
            em.persist(condition);
        }

        em.getTransaction().commit();

        Long count = repository.countAll();
        assertEquals(Long.valueOf(5), count);
    }

    @Test
    public void testGetObjectIdsByReservationConditionObject() {
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();

        ObjektEntity objekt = createObjekt("obj1");
        em.persist(objekt);

        PodminkaRezervaceEntity condition = new PodminkaRezervaceEntity("id1", "Condition 1", 1, "reservation1", false, objekt);
        em.persist(condition);

        em.getTransaction().commit();

        List<String> objectIds = repository.getObjectIdsByReservationConditionObject("reservation1");
        assertNotNull(objectIds);
        assertEquals(1, objectIds.size());
        assertEquals("obj1", objectIds.get(0));
    }

    @Test
    public void testGetMaxPriority() {
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();

        ObjektEntity objekt = createObjekt("obj1");
        em.persist(objekt);

        for (int i = 1; i <= 5; i++) {
            PodminkaRezervaceEntity condition = new PodminkaRezervaceEntity("id" + i, "Condition " + i, i, "reservation" + i, i % 2 == 0, objekt);
            em.persist(condition);
        }

        em.getTransaction().commit();

        Long maxPriority = repository.getMaxPriority();
        assertEquals(Long.valueOf(5), maxPriority);
    }
}