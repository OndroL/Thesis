package RepositoryTests.Sport;

import cz.inspire.sport.entity.ActivityWebTabEntity;
import cz.inspire.sport.repository.ActivityWebTabRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import java.util.ArrayList;
import java.util.List;

@Transactional
@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
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

    @Order(1)
    @Test
    public void testSaveAndFindById() {
        ActivityWebTabEntity entity = new ActivityWebTabEntity(null, "sport-1", "activity-1", "object-1", 2);
        activityWebTabRepository.create(entity);
        String generatedId = entity.getId();

        ActivityWebTabEntity retrieved = activityWebTabRepository.findById(generatedId);
        Assertions.assertNotNull(retrieved, "Entity should be present in repository.");
        Assertions.assertEquals("sport-1", retrieved.getSportId(), "Sport ID should match.");
        Assertions.assertEquals("activity-1", retrieved.getActivityId(), "Activity ID should match.");
        Assertions.assertEquals("object-1", retrieved.getObjectId(), "Object ID should match.");
        Assertions.assertEquals(2, retrieved.getTabIndex(), "Tab index should match.");
    }

    @Order(2)
    @Test
    public void testFindBySport() {
        ActivityWebTabEntity e1 = new ActivityWebTabEntity(null, "sport-2", "activity-2", "object-2", 1);
        ActivityWebTabEntity e2 = new ActivityWebTabEntity(null, "sport-2", "activity-3", "object-3", 3);

        activityWebTabRepository.create(e1);
        activityWebTabRepository.create(e2);

        List<ActivityWebTabEntity> results = activityWebTabRepository.findBySport("sport-2");
        Assertions.assertEquals(2, results.size(), "Expected 2 entities for sport-2.");
    }

    @Order(3)
    @Test
    public void testFindByActivity() {
        ActivityWebTabEntity entity = new ActivityWebTabEntity(null, "sport-3", "activity-4", "object-4", 5);
        activityWebTabRepository.create(entity);

        List<ActivityWebTabEntity> results = activityWebTabRepository.findByActivity("activity-4");
        Assertions.assertEquals(1, results.size(), "Expected 1 entity for activity-4.");
    }

    @Order(4)
    @Test
    public void testFindByObject() {
        ActivityWebTabEntity entity = new ActivityWebTabEntity(null, "sport-4", "activity-5", "object-5", 0);
        activityWebTabRepository.create(entity);

        List<ActivityWebTabEntity> results = activityWebTabRepository.findByObject("object-5");
        Assertions.assertEquals(1, results.size(), "Expected 1 entity for object-5.");
    }

    @Order(5)
    @Test
    public void testDeleteEntity() {
        ActivityWebTabEntity entity = new ActivityWebTabEntity(null, "sport-6", "activity-6", "object-6", 4);
        activityWebTabRepository.create(entity);
        String generatedId = entity.getId();

        activityWebTabRepository.deleteById(generatedId);
        ActivityWebTabEntity deleted = activityWebTabRepository.findById(generatedId);
        Assertions.assertNull(deleted, "Entity should be deleted from repository.");
    }
}
