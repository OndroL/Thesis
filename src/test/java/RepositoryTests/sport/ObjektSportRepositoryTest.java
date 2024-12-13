package RepositoryTests.sport;

import cz.inspire.thesis.data.EntityManagerProducer;
import cz.inspire.thesis.data.model.sport.objekt.*;
import cz.inspire.thesis.data.model.sport.sport.SportEntity;
import cz.inspire.thesis.data.repository.sport.objekt.ObjektSportRepository;
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

public class ObjektSportRepositoryTest {

    private ObjektSportRepository repository;

    @Before
    public void setUp() {
        CdiContainer container = CdiContainerLoader.getCdiContainer();
        container.boot();

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        EntityManagerProducer producer = BeanProvider.getContextualReference(EntityManagerProducer.class);
        producer.setEntityManagerFactory(emf);

        repository = BeanProvider.getContextualReference(ObjektSportRepository.class);

        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.createQuery("DELETE FROM SportInstructorEntity ").executeUpdate();
        em.createQuery("DELETE FROM ObjektSportEntity").executeUpdate();
        em.createQuery("DELETE FROM ObjektEntity").executeUpdate();
        em.createQuery("DELETE FROM SportEntity").executeUpdate();
        em.getTransaction().commit();
    }

    @Test
    public void testFindByObjekt() {
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();

        // Create and persist SportEntity
        SportEntity sport = new SportEntity(
                "sport1", 1, "zbozi1", "sklad1", 100, true, 60, true, 0,
                null, 30, 120, true, 15, null, null, true, true, 10,
                90, 1, 5, 20, null, null, null, null, null, null, null, null
        );
        em.persist(sport);

        // Create and persist ObjektEntity
        ObjektEntity objekt = new ObjektEntity(
                "obj1", 100, 15, 1, true, 30, 120, 10, 20, 5, 15, true, 2,
                null, null, true, false, false, true, false, true, true, true,
                false, true, 5, 10, 15, "calendarId1", true, 30, null,
                null, null, null, null, null, null
        );
        em.persist(objekt);

        // Create and persist ObjektSportEntity
        ObjektSportPK pk1 = new ObjektSportPK("objSport1", 1);
        ObjektSportEntity objektSport1 = new ObjektSportEntity(pk1, sport, objekt);
        em.persist(objektSport1);

        ObjektSportPK pk2 = new ObjektSportPK("objSport2", 2);
        ObjektSportEntity objektSport2 = new ObjektSportEntity(pk2, sport, objekt);
        em.persist(objektSport2);

        em.getTransaction().commit();

        // Query for ObjektSportEntity by objektId
        List<ObjektSportEntity> results = repository.findByObjekt("obj1");
        assertNotNull(results);
        assertEquals(2, results.size());
        assertTrue(results.stream().anyMatch(o -> o.getId().getId().equals("objSport1")));
        assertTrue(results.stream().anyMatch(o -> o.getId().getId().equals("objSport2")));
    }
}
