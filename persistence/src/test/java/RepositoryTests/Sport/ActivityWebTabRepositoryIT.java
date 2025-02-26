package RepositoryTests.Sport;

import cz.inspire.sport.entity.ActivityWebTabEntity;
import cz.inspire.sport.repository.ActivityWebTabRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional
@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ActivityWebTabRepositoryIT {

    @Inject
    ActivityWebTabRepository activityWebTabRepository;

    @BeforeAll
    @ActivateRequestContext
    public void clearDatabase() {
        List<ActivityWebTabEntity> allEntities = new ArrayList<>();
        activityWebTabRepository.findAll().forEach(allEntities::add);
        if (!allEntities.isEmpty()) {
            activityWebTabRepository.deleteAll(allEntities);
        }
    }

    @Test
    public void testSaveAndFindById() {
        ActivityWebTabEntity entity = new ActivityWebTabEntity("ID-001", "sport-1", "activity-1", "object-1", 2);
        activityWebTabRepository.save(entity);

        Optional<ActivityWebTabEntity> retrieved = activityWebTabRepository.findById("ID-001");
        Assertions.assertTrue(retrieved.isPresent(), "Entity should be present in repository.");
        Assertions.assertEquals("sport-1", retrieved.get().getSportId(), "Sport ID should match.");
        Assertions.assertEquals("activity-1", retrieved.get().getActivityId(), "Activity ID should match.");
        Assertions.assertEquals("object-1", retrieved.get().getObjectId(), "Object ID should match.");
        Assertions.assertEquals(2, retrieved.get().getTabIndex(), "Tab index should match.");
    }

    @Test
    public void testFindBySport() {
        ActivityWebTabEntity e1 = new ActivityWebTabEntity("ID-002", "sport-2", "activity-2", "object-2", 1);
        ActivityWebTabEntity e2 = new ActivityWebTabEntity("ID-003", "sport-2", "activity-3", "object-3", 3);

        activityWebTabRepository.save(e1);
        activityWebTabRepository.save(e2);

        List<ActivityWebTabEntity> results = activityWebTabRepository.findBySport("sport-2");
        Assertions.assertEquals(2, results.size(), "Expected 2 entities for sport-2.");
    }

    @Test
    public void testFindByActivity() {
        ActivityWebTabEntity entity = new ActivityWebTabEntity("ID-004", "sport-3", "activity-4", "object-4", 5);
        activityWebTabRepository.save(entity);

        List<ActivityWebTabEntity> results = activityWebTabRepository.findByActivity("activity-4");
        Assertions.assertEquals(1, results.size(), "Expected 1 entity for activity-4.");
    }

    @Test
    public void testFindByObject() {
        ActivityWebTabEntity entity = new ActivityWebTabEntity("ID-005", "sport-4", "activity-5", "object-5", 0);
        activityWebTabRepository.save(entity);

        List<ActivityWebTabEntity> results = activityWebTabRepository.findByObject("object-5");
        Assertions.assertEquals(1, results.size(), "Expected 1 entity for object-5.");
    }

    @Test
    public void testDeleteEntity() {
        ActivityWebTabEntity entity = new ActivityWebTabEntity("ID-006", "sport-6", "activity-6", "object-6", 4);
        activityWebTabRepository.save(entity);

        activityWebTabRepository.deleteById("ID-006");
        Optional<ActivityWebTabEntity> deleted = activityWebTabRepository.findById("ID-006");
        Assertions.assertFalse(deleted.isPresent(), "Entity should be deleted from repository.");
    }
}
