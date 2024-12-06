package RepositoryTests.sport;

import cz.inspire.thesis.data.EntityManagerProducer;
import cz.inspire.thesis.data.model.sport.activity.ActivityFavouriteEntity;
import cz.inspire.thesis.data.repository.sport.activity.ActivityFavouriteRepository;
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
import java.util.Optional;

import static org.junit.Assert.*;

public class ActivityFavouriteRepositoryTest {

    private ActivityFavouriteRepository repository;

    @Before
    public void setUp() {
        CdiContainer container = CdiContainerLoader.getCdiContainer();
        container.boot();

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        EntityManagerProducer producer = BeanProvider.getContextualReference(EntityManagerProducer.class);
        producer.setEntityManagerFactory(emf);

        repository = BeanProvider.getContextualReference(ActivityFavouriteRepository.class);

        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.createQuery("DELETE FROM ActivityFavouriteEntity").executeUpdate();
        em.getTransaction().commit();
    }

    @Test
    public void testFindByZakaznikWithOrderingAndPagination() {
        String zakaznikId = "customer1";

        for (int i = 0; i < 10; i++) {
            repository.save(new ActivityFavouriteEntity(
                    String.valueOf(i),
                    zakaznikId,
                    "activity" + i,
                    10 - i,
                    new Date(System.currentTimeMillis() - i * 1000)
            ));
        }

        List<ActivityFavouriteEntity> favourites = repository.findByZakaznik(zakaznikId, 5, 0);

        assertNotNull(favourites);
        assertEquals(5, favourites.size());
        assertEquals("activity0", favourites.get(0).getActivityId());
        assertEquals(10, favourites.get(0).getPocet());
    }

    @Test
    public void testFindByZakaznikBoundaryConditions() {
        String zakaznikId = "customer2";

        repository.save(new ActivityFavouriteEntity("1", zakaznikId, "activity1", 5, new Date(System.currentTimeMillis() - 3000)));
        repository.save(new ActivityFavouriteEntity("2", zakaznikId, "activity2", 3, new Date(System.currentTimeMillis() - 2000)));
        repository.save(new ActivityFavouriteEntity("3", zakaznikId, "activity3", 7, new Date(System.currentTimeMillis() - 1000)));


        List<ActivityFavouriteEntity> favourites = repository.findByZakaznik(zakaznikId, 1, 0);

        assertNotNull(favourites);
        assertEquals(1, favourites.size());
        assertEquals("activity3", favourites.get(0).getActivityId());
    }

    @Test
    public void testFindByZakaznikPaginationAlignment() {
        String zakaznikId = "customer2";

        repository.save(new ActivityFavouriteEntity("1", zakaznikId, "activity1", 5, new Date(System.currentTimeMillis() - 3000)));
        repository.save(new ActivityFavouriteEntity("2", zakaznikId, "activity2", 3, new Date(System.currentTimeMillis() - 2000)));
        repository.save(new ActivityFavouriteEntity("3", zakaznikId, "activity3", 7, new Date(System.currentTimeMillis() - 1000)));

        // Test different offsets to align JPA with SQL behavior
        List<ActivityFavouriteEntity> favouritesOffset0 = repository.findByZakaznik(zakaznikId, 1, 0);
        assertNotNull(favouritesOffset0);
        assertEquals(1, favouritesOffset0.size());
        assertEquals("activity3", favouritesOffset0.get(0).getActivityId());

        List<ActivityFavouriteEntity> favouritesOffset1 = repository.findByZakaznik(zakaznikId, 1, 1);
        assertNotNull(favouritesOffset1);
        assertEquals(1, favouritesOffset1.size());
        assertEquals("activity1", favouritesOffset1.get(0).getActivityId());

        List<ActivityFavouriteEntity> favouritesOffset2 = repository.findByZakaznik(zakaznikId, 1, 2);
        assertNotNull(favouritesOffset2);
        assertEquals(1, favouritesOffset2.size());
        assertEquals("activity2", favouritesOffset2.get(0).getActivityId());
    }



    @Test
    public void testFindByZakaznikAktivitaWithNonExistentCombination() {
        String zakaznikId = "customer3";
        String activityId = "activityX";

        Optional<ActivityFavouriteEntity> favourite = repository.findByZakaznikAktivita(zakaznikId, activityId);

        assertTrue(favourite.isEmpty());
    }

    @Test
    public void testFindByZakaznikAktivitaExactMatch() {
        String zakaznikId = "customer4";
        String activityId = "activity1";

        ActivityFavouriteEntity entity = new ActivityFavouriteEntity(
                "1", zakaznikId, activityId, 5, new Date()
        );
        repository.save(entity);

        Optional<ActivityFavouriteEntity> favourite = repository.findByZakaznikAktivita(zakaznikId, activityId);

        assertNotNull(favourite);
        if (favourite.isPresent()) {
            ActivityFavouriteEntity favourite_existing =  favourite.get();
            assertEquals(activityId, favourite_existing.getActivityId());
            assertEquals(zakaznikId, favourite_existing.getZakaznikId());
        }
    }

    @Test
    public void testFindByZakaznikWithSameCountDifferentDates() {
        String zakaznikId = "customer5";

        repository.save(new ActivityFavouriteEntity("1", zakaznikId, "activity1", 5, new Date(System.currentTimeMillis() - 1000)));
        repository.save(new ActivityFavouriteEntity("2", zakaznikId, "activity2", 5, new Date(System.currentTimeMillis() - 2000)));

        List<ActivityFavouriteEntity> favourites = repository.findByZakaznik(zakaznikId, 10, 0);

        assertNotNull(favourites);
        assertEquals(2, favourites.size());
        assertEquals("activity1", favourites.get(0).getActivityId());
        assertEquals("activity2", favourites.get(1).getActivityId());
    }
}
