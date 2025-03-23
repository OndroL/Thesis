package RepositoryTests.Sport;

import RepositoryTests.DatabaseCleaner;
import cz.inspire.sport.entity.ActivityEntity;
import cz.inspire.sport.repository.ActivityRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import java.util.List;

@Transactional
@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
public class ActivityRepositoryIT {

    @Inject
    ActivityRepository activityRepository;

    @Inject
    DatabaseCleaner databaseCleaner;

    @BeforeAll
    public void clearActivityTable() {
        databaseCleaner.clearTable(ActivityEntity.class, true);
    }

    @Order(1)
    @Test
    public void testSaveAndFindById() {
        // Pass null for the ID so it gets generated.
        ActivityEntity entity = new ActivityEntity(null, "Yoga", "Relaxing yoga session", 3, "icon1", null, null);
        entity = activityRepository.create(entity);
        String generatedId = entity.getId();

        ActivityEntity retrieved = activityRepository.findByPrimaryKey(generatedId);
        Assertions.assertNotNull(retrieved, "Entity should be present in repository.");
        Assertions.assertEquals("Yoga", retrieved.getName(), "Name should match.");
        Assertions.assertEquals("Relaxing yoga session", retrieved.getDescription(), "Description should match.");
    }

    @Order(2)
    @Test
    public void testUpdateEntity() {
        ActivityEntity entity = new ActivityEntity(null, "Pilates", "Core workout", 3, "icon2", null, null);
        entity = activityRepository.create(entity);
        String generatedId = entity.getId();

        entity.setDescription("Full-body workout");
        activityRepository.create(entity);

        ActivityEntity updated = activityRepository.findByPrimaryKey(generatedId);
        Assertions.assertNotNull(updated, "Entity should be present after update.");
        Assertions.assertEquals("Full-body workout", updated.getDescription(), "Updated description should be correct.");
    }

    @Order(3)
    @Test
    public void testDeleteEntity() {
        ActivityEntity entity = new ActivityEntity(null, "Zumba", "Dance fitness", 4, "icon3", null, null);
        entity = activityRepository.create(entity);
        String generatedId = entity.getId();

        activityRepository.deleteByPrimaryKey(generatedId);
        ActivityEntity deleted = activityRepository.findByPrimaryKey(generatedId);
        Assertions.assertNull(deleted, "Entity should be deleted from repository.");
    }

    @Order(4)
    @Test
    public void testFindAllOrdered() {
        int number_before = activityRepository.findAllOrdered().size();
        ActivityEntity e1 = new ActivityEntity(null, "Boxing", "High-intensity training", 2, "icon4", null, null);
        ActivityEntity e2 = new ActivityEntity(null, "Swimming", "Water-based exercise", 1, "icon5", null, null);

        e1 = activityRepository.create(e1);
        e2 = activityRepository.create(e2);

        List<ActivityEntity> results = activityRepository.findAllOrdered();
        Assertions.assertEquals(number_before + 2, results.size(), "Expected 2 more of activities in order.");
        // Adjusting assertion to check by name (assuming the ordering returns Swimming first)
        Assertions.assertEquals("Swimming", results.get(0).getName(), "First entity should be Swimming.");
        Assertions.assertEquals("Boxing", results.get(1).getName(), "Second entity should be Boxing.");
    }

    @Order(5)
    @Test
    public void testCountActivities() {
        long countBefore = activityRepository.countActivities();
        ActivityEntity entity = new ActivityEntity(null, "Running", "Outdoor jogging", 6, "icon6", null, null);
        activityRepository.create(entity);
        long countAfter = activityRepository.countActivities();

        Assertions.assertEquals(countBefore + 1, countAfter, "Activity count should increase by 1.");
    }
}
