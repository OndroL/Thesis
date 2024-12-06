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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class ActivityRepositoryTest {

    private ActivityRepository activityRepository;
    private InstructorRepository instructorRepository;

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
        activityRepository = BeanProvider.getContextualReference(ActivityRepository.class);
        instructorRepository = BeanProvider.getContextualReference(InstructorRepository.class);

        // Clear the database
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.createQuery("DELETE FROM ActivityEntity").executeUpdate();
        em.createQuery("DELETE FROM InstructorEntity").executeUpdate();
        em.getTransaction().commit();
    }

    @Test
    public void testFindAll() {
        assertNotNull("ActivityRepository should be initialized!", activityRepository);

        // Save entities
        activityRepository.save(new ActivityEntity("1", "Activity1", null, 1, null, null, null));
        activityRepository.save(new ActivityEntity("2", "Activity2", null, 2, null, null, null));

        // Test retrieving all entities
        List<ActivityEntity> allActivities = activityRepository.findAll();
        assertNotNull("Result list should not be null", allActivities);
        assertEquals(2, allActivities.size());
    }

    @Test
    public void testPagination() {
        assertNotNull("ActivityRepository should be initialized!", activityRepository);

        // Save multiple entities
        for (int i = 0; i < 10; i++) {
            activityRepository.save(new ActivityEntity(String.valueOf(i), "Activity" + i, null, i, null, null, null));
        }

        // Test retrieving entities with pagination
        List<ActivityEntity> result = activityRepository.findAll(1, 5); // Retrieve activities 1 to 5
        assertNotNull("Result list should not be null", result);
        assertEquals(5, result.size());
    }

    @Test
    public void testFindAllByInstructor() {
        assertNotNull("ActivityRepository should be initialized!", activityRepository);
        assertNotNull("InstructorRepository should be initialized!", instructorRepository);

        // Create and save instructor
        InstructorEntity instructor = new InstructorEntity("instructor1", "John", "Doe", 1, "john.doe@example.com", "+1", "123456789", null, null, null, "Info", "Red", null, false, null, false, 10, new ArrayList<>(), null);

        // Save instructor
        instructorRepository.save(instructor);

        // Link activities to instructor and save
        ActivityEntity activity1 = new ActivityEntity("1", "Activity1", null, 1, null, null, null);
        ActivityEntity activity2 = new ActivityEntity("2", "Activity2", null, 2, null, null, null);

        // Maintain bidirectional relationship
        activity1.setInstructors(Set.of(instructor));
        activity2.setInstructors(Set.of(instructor));
        instructor.setActivities(Set.of(activity1, activity2));

        activityRepository.save(activity1);
        activityRepository.save(activity2);
        instructorRepository.save(instructor);

        // Test retrieving activities by instructor ID
        List<ActivityEntity> activities = activityRepository.findAllByInstructor("instructor1", 0, 5);
        assertNotNull("Result list should not be null", activities);
        assertEquals(2, activities.size());
    }

    @Test
    public void testCountActivities() {
        assertNotNull("ActivityRepository should be initialized!", activityRepository);

        // Save entities
        activityRepository.save(new ActivityEntity("1", "Activity1", null, 1, null, null, null));
        activityRepository.save(new ActivityEntity("2", "Activity2", null, 2, null, null, null));

        // Test counting activities
        Long count = activityRepository.countActivities();
        assertEquals(2L, (long) count);
    }

    @Test
    public void testCountActivitiesByInstructor() {
        assertNotNull("ActivityRepository should be initialized!", activityRepository);
        assertNotNull("InstructorRepository should be initialized!", instructorRepository);

        // Create and save instructor
        InstructorEntity instructor = new InstructorEntity("instructor1", "John", "Doe", 1, "john.doe@example.com", "+1", "123456789", null, null, null, "Info", "Red", null, false, null, false, 10, new ArrayList<>(), null);

        // Save instructor
        instructorRepository.save(instructor);

        // Link activities to instructor and save
        ActivityEntity activity1 = new ActivityEntity("1", "Activity1", null, 1, null, null, null);
        ActivityEntity activity2 = new ActivityEntity("2", "Activity2", null, 2, null, null, null);

        // Maintain bidirectional relationship
        activity1.setInstructors(Set.of(instructor));
        activity2.setInstructors(Set.of(instructor));
        instructor.setActivities(Set.of(activity1, activity2));

        activityRepository.save(activity1);
        activityRepository.save(activity2);
        instructorRepository.save(instructor);

        // Test counting activities by instructor ID
        Long count = activityRepository.countActivitiesByInstructor("instructor1");
        assertEquals(2L, (long) count);
    }
}