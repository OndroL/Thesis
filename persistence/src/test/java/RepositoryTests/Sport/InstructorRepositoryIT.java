package RepositoryTests.Sport;

import RepositoryTests.DatabaseCleaner;
import cz.inspire.sport.entity.ActivityEntity;
import cz.inspire.sport.entity.InstructorEntity;
import cz.inspire.sport.repository.ActivityRepository;
import cz.inspire.sport.repository.InstructorRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Transactional
@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
public class InstructorRepositoryIT {

    @Inject
    InstructorRepository instructorRepository;

    @Inject
    ActivityRepository activityRepository;

    @Inject
    DatabaseCleaner databaseCleaner;

    @BeforeAll
    @ActivateRequestContext
    public void clearDatabase() {
        databaseCleaner.clearTable(InstructorEntity.class, true);
        databaseCleaner.clearTable(ActivityEntity.class, true);
    }

    @Order(1)
    @Test
    public void testSaveAndFindById() {
        // Create instructor with null ID so that it is generated.
        InstructorEntity entity = new InstructorEntity(
                null, "John", "Doe", 1, "john@example.com", "+1", "123456789",
                "john.internal@example.com", "+1", "987654321", "Experienced instructor", "blue",
                null, false, "calendar123", true, 15, new HashSet<>(), new HashSet<>(), null
        );
        entity = instructorRepository.create(entity);

        InstructorEntity retrieved = instructorRepository.findById(entity.getId());
        Assertions.assertNotNull(retrieved, "Entity should be present in repository.");
        Assertions.assertEquals("John", retrieved.getFirstName(), "First name should match.");
        Assertions.assertEquals("Doe", retrieved.getLastName(), "Last name should match.");
        Assertions.assertEquals("john@example.com", retrieved.getEmail(), "Email should match.");
    }

    @Order(2)
    @Test
    public void testFindAllOrdered() {
        InstructorEntity i1 = new InstructorEntity(
                null, "Alice", "Smith", 2, "alice@example.com", "+1", "234567890",
                "alice.internal@example.com", "+1", "987654322", "Skilled trainer", "red",
                null, false, "calendar124", true, 20, new HashSet<>(), new HashSet<>(), null
        );

        InstructorEntity i2 = new InstructorEntity(
                null, "Bob", "Brown", 3, "bob@example.com", "+1", "345678901",
                "bob.internal@example.com", "+1", "987654323", "Expert in fitness", "green",
                null, false, "calendar125", true, 30, new HashSet<>(), new HashSet<>(), null
        );

        // i1 and i2 are created in addition to the one created in testSaveAndFindById.
        i1 = instructorRepository.create(i1);
        i2 = instructorRepository.create(i2);

        List<InstructorEntity> results = instructorRepository.findAllOrdered();
        // Total instructors expected: at least 3
        Assertions.assertTrue(results.size() >= 3, "Expected at least 3 instructors in ordered list.");
        // Verify that the first instructor is the one created in testSaveAndFindById.
        Assertions.assertEquals("John", results.get(0).getFirstName(), "First instructor should be John.");
    }

    @Order(3)
    @Test
    public void testFindAllWithLimitAndDeletedFlag() {
        InstructorEntity i1 = new InstructorEntity(
                null, "Charlie", "Taylor", 4, "charlie@example.com", "+1", "456789012",
                "charlie.internal@example.com", "+1", "987654324", "Expert", "yellow",
                null, false, "calendar126", true, 25, new HashSet<>(), new HashSet<>(), null
        );
        InstructorEntity i2 = new InstructorEntity(
                null, "Daniel", "White", 5, "daniel@example.com", "+1", "567890123",
                "daniel.internal@example.com", "+1", "987654325", "Trainer", "purple",
                null, true, "calendar127", true, 40, new HashSet<>(), new HashSet<>(), null
        );

        i1 = instructorRepository.create(i1);
        i2 = instructorRepository.create(i2);

        // false flag for active instructors.
        List<InstructorEntity> results = instructorRepository.findAll(1,0, false);
        Assertions.assertEquals(1, results.size(), "Expected 1 active instructor due to limit.");
    }

    @Order(4)
    @Test
    public void testFindAllByActivity() {
        // Create an activity
        ActivityEntity activity = new ActivityEntity( null, "Yoga", "Relaxing activity", 1, "icon1", new ArrayList<>(), new ArrayList<>());
        activity = activityRepository.create(activity);
        InstructorEntity i1 = new InstructorEntity(
                null, "Eva", "Black", 6, "eva@example.com", "+1", "678901234",
                "eva.internal@example.com", "+1", "987654326", "Yoga Instructor", "pink",
                null, false, "calendar128", true, 50, new HashSet<>(), new HashSet<>(Set.of(activity)), null
        );

        InstructorEntity i2 = new InstructorEntity(
                null, "Frank", "Green", 7, "frank@example.com", "+1", "789012345",
                "frank.internal@example.com", "+1", "987654327", "Pilates Instructor", "orange",
                null, false, "calendar129", true, 60, new HashSet<>(), new HashSet<>(), null
        );
        i1 = instructorRepository.create(i1);
        i2 = instructorRepository.create(i2);

        List<InstructorEntity> results = instructorRepository.findAllByActivity(activity.getId(),2,0, false);
        Assertions.assertEquals(1, results.size(), "Expected 1 instructor linked to activity ACT-001.");
        Assertions.assertEquals(i1.getId(), results.get(0).getId(), "Expected instructor with matching ID.");
    }

    @Order(5)
    @Test
    public void testCountInstructors() {
        long activeCount = instructorRepository.countInstructors(false);
        long deletedCount = instructorRepository.countInstructors(true);

        Assertions.assertTrue(activeCount > 0, "Expected at least 1 active instructor.");
        Assertions.assertTrue(deletedCount > 0, "Expected at least 1 deleted instructor.");
    }

    @Order(6)
    @Test
    public void testCountInstructorsByActivity() {
        // Create an activity
        ActivityEntity activity = new ActivityEntity(null, "Pilates", "Core strength activity", 2, "icon2", new ArrayList<>(), new ArrayList<>());
        activity = activityRepository.create(activity);
        InstructorEntity i1 = new InstructorEntity(
                null, "Grace", "Blue", 8, "grace@example.com", "+1", "890123456",
                "grace.internal@example.com", "+1", "987654328", "Pilates Instructor", "teal",
                null, false, "calendar130", true, 70, new HashSet<>(), new HashSet<>(Set.of(activity)), null
        );
        i1 = instructorRepository.create(i1);

        long count = instructorRepository.countInstructorsByActivity(activity.getId(), false);
        Assertions.assertEquals(1, count, "Expected 1 instructor linked to activity ACT-002.");
    }

    @Order(7)
    @Test
    public void testDeleteInstructor() {
        InstructorEntity entity = new InstructorEntity(
                null, "Henry", "Red", 9, "henry@example.com", "+1", "901234567",
                "henry.internal@example.com", "+1", "987654329", "Deleted Instructor", "gray",
                null, false, "calendar131", true, 80, new HashSet<>(), new HashSet<>(), null
        );
        entity = instructorRepository.create(entity);

        String generatedId = entity.getId();
        instructorRepository.deleteById(generatedId);

        InstructorEntity deleted = instructorRepository.findById(generatedId);
        Assertions.assertNull(deleted, "Instructor should be deleted from repository.");
    }
}
