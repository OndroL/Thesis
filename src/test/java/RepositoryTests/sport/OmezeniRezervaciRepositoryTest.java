package RepositoryTests.sport;

import cz.inspire.thesis.data.EntityManagerProducer;
import cz.inspire.thesis.data.model.sport.objekt.OmezeniRezervaciEntity;
import cz.inspire.thesis.data.repository.sport.objekt.OmezeniRezervaciRepository;
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
import java.util.List;

import static org.junit.Assert.*;

public class OmezeniRezervaciRepositoryTest {

    private OmezeniRezervaciRepository repository;

    @Before
    public void setUp() {
        CdiContainer container = CdiContainerLoader.getCdiContainer();
        container.boot();

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        EntityManagerProducer producer = BeanProvider.getContextualReference(EntityManagerProducer.class);
        producer.setEntityManagerFactory(emf);

        repository = BeanProvider.getContextualReference(OmezeniRezervaciRepository.class);

        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.createQuery("DELETE FROM OmezeniRezervaciEntity").executeUpdate();
        em.getTransaction().commit();
    }

    @Test
    public void testFindAll() {
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();

        // Create and configure OtviraciDoba instances
        OtviraciDoba openHours1 = new OtviraciDoba();
        openHours1.setOpeningHours(1, LocalTime.of(8, 0), LocalTime.of(18, 0)); // Monday: 08:00 - 18:00
        openHours1.setOpeningHours(2, LocalTime.of(9, 0), LocalTime.of(17, 0)); // Tuesday: 09:00 - 17:00

        OtviraciDoba openHours2 = new OtviraciDoba();
        openHours2.setOpeningHours(3, LocalTime.of(10, 0), LocalTime.of(16, 0)); // Wednesday: 10:00 - 16:00
        openHours2.setOpeningHours(4, LocalTime.of(12, 0), LocalTime.of(20, 0)); // Thursday: 12:00 - 20:00

        // Create and persist OmezeniRezervaciEntity instances
        OmezeniRezervaciEntity restriction1 = new OmezeniRezervaciEntity("obj1", openHours1);
        OmezeniRezervaciEntity restriction2 = new OmezeniRezervaciEntity("obj2", openHours2);

        em.persist(restriction1);
        em.persist(restriction2);

        em.getTransaction().commit();

        // Query all restrictions
        List<OmezeniRezervaciEntity> results = repository.findAll();
        assertNotNull(results);
        assertEquals(2, results.size());

        // Validate individual entries
        OmezeniRezervaciEntity result1 = results.stream().filter(r -> r.getObjektId().equals("obj1")).findFirst().orElse(null);
        OmezeniRezervaciEntity result2 = results.stream().filter(r -> r.getObjektId().equals("obj2")).findFirst().orElse(null);

        assertNotNull(result1);
        assertNotNull(result2);

        // Validate opening hours for restriction1
        OtviraciDoba retrievedHours1 = result1.getOmezeni();
        assertNotNull(retrievedHours1);
        assertEquals(LocalTime.of(8, 0), retrievedHours1.getOpeningHours(1).getOpen());
        assertEquals(LocalTime.of(18, 0), retrievedHours1.getOpeningHours(1).getClose());
        assertEquals(LocalTime.of(9, 0), retrievedHours1.getOpeningHours(2).getOpen());
        assertEquals(LocalTime.of(17, 0), retrievedHours1.getOpeningHours(2).getClose());

        // Validate opening hours for restriction2
        OtviraciDoba retrievedHours2 = result2.getOmezeni();
        assertNotNull(retrievedHours2);
        assertEquals(LocalTime.of(10, 0), retrievedHours2.getOpeningHours(3).getOpen());
        assertEquals(LocalTime.of(16, 0), retrievedHours2.getOpeningHours(3).getClose());
        assertEquals(LocalTime.of(12, 0), retrievedHours2.getOpeningHours(4).getOpen());
        assertEquals(LocalTime.of(20, 0), retrievedHours2.getOpeningHours(4).getClose());
    }

}
