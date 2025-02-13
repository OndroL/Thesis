package RepositoryTests.Sport;

import RepositoryTests.DatabaseCleaner;
import cz.inspire.sport.entity.InstructorEntity;
import cz.inspire.sport.entity.ActivityEntity;
import cz.inspire.sport.repository.ActivityRepository;
import cz.inspire.sport.repository.InstructorRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.data.Limit;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;

import java.util.*;

@Transactional
@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
        InstructorEntity entity = new InstructorEntity("INS-001", "John", "Doe", 1, "john@example.com", "+1", "123456789",
                "john.internal@example.com", "+1", "987654321", "Experienced instructor", "blue", null, false,
                "calendar123", true, 15, new HashSet<>(), new HashSet<>(), null);

        instructorRepository.save(entity);

        Optional<InstructorEntity> retrieved = instructorRepository.findById("INS-001");
        Assertions.assertTrue(retrieved.isPresent(), "Entity should be present in repository.");
        Assertions.assertEquals("John", retrieved.get().getFirstName(), "First name should match.");
        Assertions.assertEquals("Doe", retrieved.get().getLastName(), "Last name should match.");
        Assertions.assertEquals("john@example.com", retrieved.get().getEmail(), "Email should match.");
    }

    @Order(2)
    @Test
    public void testFindAllOrdered() {
        InstructorEntity i1 = new InstructorEntity("INS-002", "Alice", "Smith", 2, "alice@example.com", "+1", "234567890",
                "alice.internal@example.com", "+1", "987654322", "Skilled trainer", "red", null, false,
                "calendar124", true, 20, new HashSet<>(), new HashSet<>(), null);

        InstructorEntity i2 = new InstructorEntity("INS-003", "Bob", "Brown", 3, "bob@example.com", "+1", "345678901",
                "bob.internal@example.com", "+1", "987654323", "Expert in fitness", "green", null, false,
                "calendar125", true, 30, new HashSet<>(), new HashSet<>(), null);

        instructorRepository.save(i1);
        instructorRepository.save(i2);

        List<InstructorEntity> results = instructorRepository.findAllOrdered();
        Assertions.assertEquals(3, results.size(), "Expected 3 instructors in ordered list.");
        Assertions.assertEquals("INS-001", results.getFirst().getId(), "First instructor should be INS-001.");
    }

    @Order(3)
    @Test
    public void testFindAllWithLimitAndDeletedFlag() {
        InstructorEntity i1 = new InstructorEntity("INS-004", "Charlie", "Taylor", 4, "charlie@example.com", "+1", "456789012",
                "charlie.internal@example.com", "+1", "987654324", "Expert", "yellow", null, false,
                "calendar126", true, 25, new HashSet<>(), new HashSet<>(), null);

        InstructorEntity i2 = new InstructorEntity("INS-005", "Daniel", "White", 5, "daniel@example.com", "+1", "567890123",
                "daniel.internal@example.com", "+1", "987654325", "Trainer", "purple", null, true,
                "calendar127", true, 40, new HashSet<>(), new HashSet<>(), null);

        instructorRepository.save(i1);
        instructorRepository.save(i2);

        List<InstructorEntity> results = instructorRepository.findAll(Limit.of(1), false);
        Assertions.assertEquals(1, results.size(), "Expected 1 active instructor due to limit.");
    }

    @Order(4)
    @Test
    public void testFindAllByActivity() {
        ActivityEntity activity = new ActivityEntity("ACT-001", "Yoga", "Relaxing activity", 1, "icon1", new ArrayList<>(), new ArrayList<>());
        InstructorEntity i1 = new InstructorEntity("INS-006", "Eva", "Black", 6, "eva@example.com", "+1", "678901234",
                "eva.internal@example.com", "+1", "987654326", "Yoga Instructor", "pink", null, false,
                "calendar128", true, 50, new HashSet<>(), new HashSet<>(Set.of(activity)), null);

        InstructorEntity i2 = new InstructorEntity("INS-007", "Frank", "Green", 7, "frank@example.com", "+1", "789012345",
                "frank.internal@example.com", "+1", "987654327", "Pilates Instructor", "orange", null, false,
                "calendar129", true, 60, new HashSet<>(), new HashSet<>(), null);
        activityRepository.save(activity);
        instructorRepository.save(i1);
        instructorRepository.save(i2);

        List<InstructorEntity> results = instructorRepository.findAllByActivity("ACT-001", Limit.of(2), false);
        Assertions.assertEquals(1, results.size(), "Expected 1 instructor linked to Yoga.");
        Assertions.assertEquals("INS-006", results.getFirst().getId(), "Expected instructor INS-006.");
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
        ActivityEntity activity = new ActivityEntity("ACT-002", "Pilates", "Core strength activity", 2, "icon2", new ArrayList<>(), new ArrayList<>());
        InstructorEntity i1 = new InstructorEntity("INS-008", "Grace", "Blue", 8, "grace@example.com", "+1", "890123456",
                "grace.internal@example.com", "+1", "987654328", "Pilates Instructor", "teal", null, false,
                "calendar130", true, 70, new HashSet<>(), new HashSet<>(Set.of(activity)), null);
        activityRepository.save(activity);
        instructorRepository.save(i1);

        long count = instructorRepository.countInstructorsByActivity("ACT-002", false);
        Assertions.assertEquals(1, count, "Expected 1 instructor linked to Pilates.");
    }

    @Order(7)
    @Test
    public void testDeleteInstructor() {
        InstructorEntity entity = new InstructorEntity("INS-009", "Henry", "Red", 9, "henry@example.com", "+1", "901234567",
                "henry.internal@example.com", "+1", "987654329", "Deleted Instructor", "gray", null, false,
                "calendar131", true, 80, new HashSet<>(), new HashSet<>(), null);

        instructorRepository.save(entity);
        instructorRepository.deleteById("INS-009");

        Optional<InstructorEntity> deleted = instructorRepository.findById("INS-009");
        Assertions.assertFalse(deleted.isPresent(), "Instructor should be deleted from repository.");
    }
}
