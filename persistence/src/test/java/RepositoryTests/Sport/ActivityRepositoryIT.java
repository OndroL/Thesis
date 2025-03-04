package RepositoryTests.Sport;

import RepositoryTests.DatabaseCleaner;
import cz.inspire.sport.entity.ActivityEntity;
import cz.inspire.sport.repository.ActivityRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Optional;

@Transactional
@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ActivityRepositoryIT {

    @Inject
    ActivityRepository activityRepository;

    @Inject
    DatabaseCleaner databaseCleaner;

    @BeforeAll
    public void clearActivityTable() {
        databaseCleaner.clearTable(ActivityEntity.class, true);
    }

    @Test
    public void testSaveAndFindById() {
        ActivityEntity entity = new ActivityEntity("ID-001", "Yoga", "Relaxing yoga session", 1, "icon1", null, null);
        activityRepository.create(entity);

        ActivityEntity retrieved = activityRepository.findById("ID-001");
        Assertions.assertNotNull(retrieved, "Entity should be present in repository.");
        Assertions.assertEquals("Yoga", retrieved.getName(), "Name should match.");
        Assertions.assertEquals("Relaxing yoga session", retrieved.getDescription(), "Description should match.");
    }

    @Test
    public void testUpdateEntity() {
        ActivityEntity entity = new ActivityEntity("ID-002", "Pilates", "Core workout", 2, "icon2", null, null);
        activityRepository.create(entity);

        entity.setDescription("Full-body workout");
        activityRepository.create(entity);

        ActivityEntity updated = activityRepository.findById("ID-002");
        Assertions.assertNotNull(updated, "Entity should be present after update.");
        Assertions.assertEquals("Full-body workout", updated.getDescription(), "Updated description should be correct.");
    }

    @Test
    public void testDeleteEntity() {
        ActivityEntity entity = new ActivityEntity("ID-003", "Zumba", "Dance fitness", 3, "icon3", null, null);
        activityRepository.create(entity);

        activityRepository.deleteById("ID-003");
        ActivityEntity deleted = activityRepository.findById("ID-003");
        Assertions.assertNull(deleted, "Entity should be deleted from repository.");
    }

    @Test
    public void testFindAllOrdered() {
        ActivityEntity e1 = new ActivityEntity("ID-004", "Boxing", "High-intensity training", 5, "icon4", null, null);
        ActivityEntity e2 = new ActivityEntity("ID-005", "Swimming", "Water-based exercise", 4, "icon5", null, null);

        activityRepository.create(e1);
        activityRepository.create(e2);

        List<ActivityEntity> results = activityRepository.findAllOrdered();
        Assertions.assertEquals(2, results.size(), "Expected 2 activities in order.");
        Assertions.assertEquals("ID-005", results.get(0).getId(), "First entity should be Swimming.");
        Assertions.assertEquals("ID-004", results.get(1).getId(), "Second entity should be Boxing.");
    }

    @Test
    public void testCountActivities() {
        long countBefore = activityRepository.countActivities();
        ActivityEntity entity = new ActivityEntity("ID-006", "Running", "Outdoor jogging", 6, "icon6", null, null);
        activityRepository.create(entity);
        long countAfter = activityRepository.countActivities();

        Assertions.assertEquals(countBefore + 1, countAfter, "Activity count should increase by 1.");
    }
}
