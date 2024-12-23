package RepositoryTests.sport;

import cz.inspire.thesis.data.EntityManagerProducer;
import cz.inspire.thesis.data.model.sport.activity.ActivityFavouriteEntity;
import cz.inspire.thesis.data.model.sport.sport.SportInstructorEntity;
import cz.inspire.thesis.data.model.sport.sport.SportEntity;
import cz.inspire.thesis.data.model.sport.sport.InstructorEntity;
import cz.inspire.thesis.data.repository.sport.sport.InstructorRepository;
import cz.inspire.thesis.data.repository.sport.sport.SportInstructorRepository;
import cz.inspire.thesis.data.repository.sport.sport.SportRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class SportInstructorRepositoryTest {

    private SportInstructorRepository sportInstructorRepository;
    private SportRepository sportRepository;
    private InstructorRepository instructorRepository;

    @Before
    public void setUp() {
        CdiContainer container = CdiContainerLoader.getCdiContainer();
        container.boot();

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");

        EntityManagerProducer producer = BeanProvider.getContextualReference(EntityManagerProducer.class);
        producer.setEntityManagerFactory(emf);

        sportInstructorRepository = BeanProvider.getContextualReference(SportInstructorRepository.class);
        sportRepository = BeanProvider.getContextualReference(SportRepository.class);
        instructorRepository = BeanProvider.getContextualReference(InstructorRepository.class);

        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.createQuery("DELETE FROM SportInstructorEntity").executeUpdate();
        em.createQuery("DELETE FROM SportEntity").executeUpdate();
        em.createQuery("DELETE FROM InstructorEntity").executeUpdate();
        em.getTransaction().commit();
    }

    @Test
    public void testFindBySport() {
        SportEntity sport = new SportEntity(
                "sport1", 1, "zbozi1", "sklad1", 100, true, 60, true, 0,
                null, 30, 120, true, 15, null, null, true, true, 10,
                90, 1, 5, 20, null, null, null, null, null, null, null, null, null
        );

        sportRepository.save(sport);

        SportInstructorEntity sportInstructor = new SportInstructorEntity("1", "activity1", null, false, sport, null);
        sportInstructorRepository.save(sportInstructor);

        List<SportInstructorEntity> results = sportInstructorRepository.findBySport("sport1");
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("1", results.get(0).getId());
    }

    @Test
    public void testFindByInstructor() {
        InstructorEntity instructor = new InstructorEntity(
                "instructor1", "John", "Doe", 1, "john.doe@example.com", "+1", "123456789",
                null, null, null, "Info", "Red", null, false, null, false, 10, null, null, null
        );

        instructorRepository.save(instructor);

        SportInstructorEntity sportInstructor = new SportInstructorEntity("1", "activity1", null, false, null, instructor);
        sportInstructorRepository.save(sportInstructor);

        List<SportInstructorEntity> results = sportInstructorRepository.findByInstructor("instructor1");
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("1", results.get(0).getId());
    }

    @Test
    public void testFindBySportAndInstructor() {
        SportEntity sport = new SportEntity(
                "sport1", 1, "zbozi1", "sklad1", 100, true, 60, true, 0,
                null, 30, 120, true, 15, null, null, true, true, 10,
                90, 1, 5, 20, null, null, null, null, null, null, null, null, null
        );

        InstructorEntity instructor = new InstructorEntity(
                "instructor1", "John", "Doe", 1, "john.doe@example.com", "+1", "123456789",
                null, null, null, "Info", "Red", null, false, null, false, 10, null, null, null
        );

        sportRepository.save(sport);
        instructorRepository.save(instructor);

        SportInstructorEntity sportInstructor = new SportInstructorEntity("1", "activity1", null, false, sport, instructor);
        sportInstructorRepository.save(sportInstructor);

        Optional<SportInstructorEntity> result = sportInstructorRepository.findBySportAndInstructor("sport1", "instructor1");
        assertNotNull(result);
        if (result.isPresent()) {
            SportInstructorEntity result_existing = result.get();
            assertEquals("1", result_existing.getId());
        }
    }

    @Test
    public void testCountSportInstructors() {
        SportEntity sport = new SportEntity(
                "sport1", 1, "zbozi1", "sklad1", 100, true, 60, true, 0,
                null, 30, 120, true, 15, null, null, true, true, 10,
                90, 1, 5, 20, null, null, null, null, null, null, null, null
        );
        sportRepository.save(sport);

        SportInstructorEntity instructor1 = new SportInstructorEntity("1", "activity1", null, false, sport, null);
        SportInstructorEntity instructor2 = new SportInstructorEntity("2", "activity2", null, false, sport, null);
        sportInstructorRepository.save(instructor1);
        sportInstructorRepository.save(instructor2);

        Long count = sportInstructorRepository.countSportInstructors("sport1");
        assertEquals(2L, (long) count);
    }

    @Test
    public void testFindByActivity() {
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();

        SportEntity sport = new SportEntity(
                "sport1", 1, "zbozi1", "sklad1", 100, true, 60, true, 0,
                null, 30, 120, true, 15, null, null, true, true, 10,
                90, 1, 5, 20, null, null, null, null, null, null, null, null
        );
        sportRepository.save(sport);

        SportInstructorEntity instructor1 = new SportInstructorEntity("1", "activity1", null, false, sport, null);
        SportInstructorEntity instructor2 = new SportInstructorEntity("2", "activity1", null, false, sport, null);
        sportInstructorRepository.save(instructor1);
        sportInstructorRepository.save(instructor2);

        em.getTransaction().commit();

        List<SportInstructorEntity> results = sportInstructorRepository.findByActivity("activity1");
        assertNotNull(results);
        assertEquals(2, results.size());
        assertTrue(results.stream().anyMatch(i -> i.getId().equals("1")));
        assertTrue(results.stream().anyMatch(i -> i.getId().equals("2")));
    }

    @Test
    public void testFindBySportWithoutInstructor() {
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();

        SportEntity sport = new SportEntity(
                "sport1", 1, "zbozi1", "sklad1", 100, true, 60, true, 0,
                null, 30, 120, true, 15, null, null, true, true, 10,
                90, 1, 5, 20, null, null, null, null, null, null, null, null
        );
        sportRepository.save(sport);

        SportInstructorEntity instructor = new SportInstructorEntity("1", "activity1", null, false, sport, null);
        sportInstructorRepository.save(instructor);

        em.getTransaction().commit();

        Optional<SportInstructorEntity> result = sportInstructorRepository.findBySportWithoutInstructor("sport1");
        assertNotNull(result);
        if (result.isPresent()) {
            SportInstructorEntity result_existing =  result.get();
            assertEquals("1", result_existing.getId());
        }
    }
}
