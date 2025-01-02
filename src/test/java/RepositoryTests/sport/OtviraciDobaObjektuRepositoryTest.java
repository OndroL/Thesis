package RepositoryTests.sport;

import cz.inspire.thesis.data.EntityManagerProducer;
import cz.inspire.thesis.data.model.sport.objekt.OtviraciDobaObjektuEntity;
import cz.inspire.thesis.data.model.sport.objekt.OtviraciDobaObjektuPK;
import cz.inspire.thesis.data.repository.sport.objekt.OtviraciDobaObjektuRepository;
import cz.inspire.thesis.data.utils.OtviraciDoba;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class OtviraciDobaObjektuRepositoryTest {

    private OtviraciDobaObjektuRepository repository;

    @Before
    public void setUp() {
        CdiContainer container = CdiContainerLoader.getCdiContainer();
        container.boot();

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        EntityManagerProducer producer = BeanProvider.getContextualReference(EntityManagerProducer.class);
        producer.setEntityManagerFactory(emf);

        repository = BeanProvider.getContextualReference(OtviraciDobaObjektuRepository.class);

        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.createQuery("DELETE FROM OtviraciDobaObjektuEntity").executeUpdate();
        em.getTransaction().commit();
    }

    private OtviraciDoba createOtviraciDoba() {
        OtviraciDoba od = new OtviraciDoba();
        od.setOpeningHours(1, LocalTime.of(8, 0), LocalTime.of(18, 0));
        od.setOpeningHours(2, LocalTime.of(9, 0), LocalTime.of(17, 0));
        return od;
    }

    @Test
    public void testFindAll() {
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();

        OtviraciDobaObjektuEntity entity1 = new OtviraciDobaObjektuEntity(
                new OtviraciDobaObjektuPK("obj1", new Date()), createOtviraciDoba()
        );
        OtviraciDobaObjektuEntity entity2 = new OtviraciDobaObjektuEntity(
                new OtviraciDobaObjektuPK("obj2", new Date()), createOtviraciDoba()
        );

        em.persist(entity1);
        em.persist(entity2);

        em.getTransaction().commit();

        List<OtviraciDobaObjektuEntity> results = repository.findAll();
        assertNotNull(results);
        assertEquals(2, results.size());
    }

    @Test
    public void testFindByObjekt() {
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();

        OtviraciDobaObjektuEntity entity1 = new OtviraciDobaObjektuEntity(
                new OtviraciDobaObjektuPK("obj1", new Date(System.currentTimeMillis() - 100000)), createOtviraciDoba()
        );
        OtviraciDobaObjektuEntity entity2 = new OtviraciDobaObjektuEntity(
                new OtviraciDobaObjektuPK("obj1", new Date()), createOtviraciDoba()
        );

        em.persist(entity1);
        em.persist(entity2);

        em.getTransaction().commit();

        List<OtviraciDobaObjektuEntity> results = repository.findByObjekt("obj1");
        assertNotNull(results);
        assertEquals(2, results.size());
        assertTrue(results.get(0).getId().getPlatnostOd().after(results.get(1).getId().getPlatnostOd()));
    }

    @Test
    public void testFindCurrent() {
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();

        Date now = new Date();
        OtviraciDobaObjektuEntity entity = new OtviraciDobaObjektuEntity(
                new OtviraciDobaObjektuPK("obj1", new Date(System.currentTimeMillis() - 100000)), createOtviraciDoba()
        );
        em.persist(entity);

        em.getTransaction().commit();

        OtviraciDobaObjektuEntity result = repository.findCurrent("obj1", now);
        assertNotNull(result);
        assertEquals("obj1", result.getId().getObjektId());
    }

    @Test
    public void testFindAfter() {
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();

        Date now = new Date();
        OtviraciDobaObjektuEntity entity1 = new OtviraciDobaObjektuEntity(
                new OtviraciDobaObjektuPK("obj1", new Date(System.currentTimeMillis() + 100000)), createOtviraciDoba()
        );
        OtviraciDobaObjektuEntity entity2 = new OtviraciDobaObjektuEntity(
                new OtviraciDobaObjektuPK("obj1", new Date(System.currentTimeMillis() + 200000)), createOtviraciDoba()
        );

        em.persist(entity1);
        em.persist(entity2);

        em.getTransaction().commit();

        List<OtviraciDobaObjektuEntity> results = repository.findAfter("obj1", now);
        assertNotNull(results);
        assertEquals(2, results.size());
        assertTrue(results.get(0).getId().getPlatnostOd().after(now));
    }
}
