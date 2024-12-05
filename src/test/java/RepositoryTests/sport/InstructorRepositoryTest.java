package RepositoryTests.sport;

import cz.inspire.thesis.data.EntityManagerProducer;
import cz.inspire.thesis.data.model.sport.activity.ActivityEntity;
import cz.inspire.thesis.data.model.sport.sport.InstructorEntity;
import cz.inspire.thesis.data.repository.sport.activity.ActivityRepository;
import cz.inspire.thesis.data.repository.sport.sport.InstructorRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class InstructorRepositoryTest {

    private InstructorRepository instructorRepository;
    private ActivityRepository activityRepository;

    @Before
    public void setUp() {
        // Boot CDI container
        CdiContainer container = CdiContainerLoader.getCdiContainer();
        container.boot();

        // Create the EntityManagerFactory
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");

        // Access EntityManagerProducer and set the EntityManagerFactory
        EntityManagerProducer producer = BeanProvider.getContextualReference(EntityManagerProducer.class);
        producer.setEntityManagerFactory(emf);

        // Access repositories from CDI
        instructorRepository = BeanProvider.getContextualReference(InstructorRepository.class);
        activityRepository = BeanProvider.getContextualReference(ActivityRepository.class);

        // Clear the database
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.createQuery("DELETE FROM InstructorEntity").executeUpdate();
        em.createQuery("DELETE FROM ActivityEntity").executeUpdate();
        em.getTransaction().commit();
    }

    @Test
    public void testFindAll() {
        // Create and save instructors
        instructorRepository.save(new InstructorEntity("1", "John", "Doe", 1, "john@example.com", "+1", "123", null, null, null, "Info", "Red", null, false, null, false, 10, null, null));
        instructorRepository.save(new InstructorEntity("2", "Jane", "Doe", 2, "jane@example.com", "+1", "456", null, null, null, "Info", "Blue", null, false, null, false, 20, null, null));

        // Query all instructors
        List<InstructorEntity> instructors = instructorRepository.findAll();
        assertNotNull("Result should not be null", instructors);
        assertEquals(2, instructors.size());
    }

    @Test
    public void testFindAllWithPaginationAndDeleted() {
        // Create and save instructors
        instructorRepository.save(new InstructorEntity("1", "John", "Doe", 1, "john@example.com", "+1", "123", null, null, null, "Info", "Red", null, false, null, false, 10, null, null));
        instructorRepository.save(new InstructorEntity("2", "Jane", "Doe", 2, "jane@example.com", "+1", "456", null, null, null, "Info", "Blue", null, true, null, false, 20, null, null));

        // Query instructors with pagination and deleted status
        List<InstructorEntity> instructors = instructorRepository.findAll(0, 2, false);
        assertNotNull("Result should not be null", instructors);
        assertEquals(1, instructors.size());
        assertEquals("John", instructors.getFirst().getFirstName());
    }

    @Test
    public void testFindAllByActivity() {
        // Create and save activity and instructors
        ActivityEntity activity = new ActivityEntity("activity1", "Yoga", null, 1, null, null, null);
        activityRepository.save(activity);

        InstructorEntity instructor1 = new InstructorEntity("1", "John", "Doe", 1, "john@example.com", "+1", "123", null, null, null, "Info", "Red", null, false, null, false, 10, null, null);
        InstructorEntity instructor2 = new InstructorEntity("2", "Jane", "Doe", 2, "jane@example.com", "+1", "456", null, null, null, "Info", "Blue", null, false, null, false, 20, null, null);

        // Link activity to instructors
        instructor1.setActivities(Set.of(activity));
        instructor2.setActivities(Set.of(activity));
        activity.setInstructors(Set.of(instructor1, instructor2));

        instructorRepository.save(instructor1);
        instructorRepository.save(instructor2);
        activityRepository.save(activity);

        // Query instructors by activity ID
        List<InstructorEntity> instructors = instructorRepository.findAllByActivity("activity1", 0, 5, false);
        assertNotNull("Result should not be null", instructors);
        assertEquals(2, instructors.size());
    }

    @Test
    public void testCountInstructors() {
        // Create and save instructors
        instructorRepository.save(new InstructorEntity("1", "John", "Doe", 1, "john@example.com", "+1", "123", null, null, null, "Info", "Red", null, false, null, false, 10, null, null));
        instructorRepository.save(new InstructorEntity("2", "Jane", "Doe", 2, "jane@example.com", "+1", "456", null, null, null, "Info", "Blue", null, true, null, false, 20, null, null));

        // Count instructors
        Long count = instructorRepository.countInstructors(false);
        assertEquals(1L, (long) count);
    }

    @Test
    public void testCountInstructorsByActivity() {
        // Create and save activity and instructors
        ActivityEntity activity = new ActivityEntity("activity1", "Yoga", null, 1, null, null, null);
        activityRepository.save(activity);

        InstructorEntity instructor1 = new InstructorEntity("1", "John", "Doe", 1, "john@example.com", "+1", "123", null, null, null, "Info", "Red", null, false, null, false, 10, null, null);
        InstructorEntity instructor2 = new InstructorEntity("2", "Jane", "Doe", 2, "jane@example.com", "+1", "456", null, null, null, "Info", "Blue", null, false, null, false, 20, null, null);

        // Link activity to instructors
        instructor1.setActivities(Set.of(activity));
        instructor2.setActivities(Set.of(activity));
        activity.setInstructors(Set.of(instructor1, instructor2));

        instructorRepository.save(instructor1);
        instructorRepository.save(instructor2);
        activityRepository.save(activity);

        // Count instructors by activity ID
        Long count = instructorRepository.countInstructorsByActivity("activity1", false);
        assertEquals(2L, (long) count);
    }
}
