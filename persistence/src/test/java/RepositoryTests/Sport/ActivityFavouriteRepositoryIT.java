package RepositoryTests.Sport;

import cz.inspire.sport.entity.ActivityFavouriteEntity;
import cz.inspire.sport.repository.ActivityFavouriteRepository;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional
@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
public class ActivityFavouriteRepositoryIT {

    @Inject
    ActivityFavouriteRepository activityFavouriteRepository;

    @BeforeAll
    @ActivateRequestContext
    public void clearDatabase() {
        List<ActivityFavouriteEntity> allEntities = new ArrayList<>(activityFavouriteRepository.findAll());
        if (!allEntities.isEmpty()) {
            activityFavouriteRepository.deleteAll(allEntities);
        }
    }

    @Order(1)
    @Test
    public void testSaveAndFindById() {
        // Pass null as the ID to let the provider generate it.
        ActivityFavouriteEntity entity = new ActivityFavouriteEntity(
                null, "ZK-001", "ACT-001", 5, LocalDateTime.now()
        );
        entity = activityFavouriteRepository.create(entity);
        String generatedId = entity.getId();

        ActivityFavouriteEntity retrieved = activityFavouriteRepository.findByPrimaryKey(generatedId);
        Assertions.assertNotNull(retrieved, "Entity should be present in repository.");
        Assertions.assertEquals("ZK-001", retrieved.getZakaznikId(), "Zakaznik ID should match.");
        Assertions.assertEquals("ACT-001", retrieved.getActivityId(), "Activity ID should match.");
        Assertions.assertEquals(5, retrieved.getPocet(), "Pocet should match.");
    }

    @Order(2)
    @Test
    public void testUpdateEntity() {
        // Create entity with null ID.
        ActivityFavouriteEntity entity = new ActivityFavouriteEntity(
                null, "ZK-002", "ACT-002", 3, LocalDateTime.now()
        );
        entity = activityFavouriteRepository.create(entity);
        String generatedId = entity.getId();

        entity.setPocet(10);
        activityFavouriteRepository.create(entity);

        ActivityFavouriteEntity updated = activityFavouriteRepository.findByPrimaryKey(generatedId);
        Assertions.assertNotNull(updated, "Entity should be present after update.");
        Assertions.assertEquals(10, updated.getPocet(), "Updated pocet value should be 10.");
    }

    @Order(3)
    @Test
    public void testDeleteEntity() {
        // Create entity with null ID.
        ActivityFavouriteEntity entity = new ActivityFavouriteEntity(
                null, "ZK-003", "ACT-003", 7, LocalDateTime.now()
        );
        entity = activityFavouriteRepository.create(entity);
        String generatedId = entity.getId();

        activityFavouriteRepository.deleteByPrimaryKey(generatedId);
        ActivityFavouriteEntity deleted = activityFavouriteRepository.findByPrimaryKey(generatedId);
        Assertions.assertNull(deleted, "Entity should be deleted from repository.");
    }

    @Order(4)
    @Test
    public void testFindByZakaznik() {
        // Create multiple entities with null IDs.
        ActivityFavouriteEntity e1 = new ActivityFavouriteEntity(
                null, "ZK-004", "ACT-004", 4, LocalDateTime.now()
        );
        ActivityFavouriteEntity e2 = new ActivityFavouriteEntity(
                null, "ZK-004", "ACT-005", 6, LocalDateTime.now()
        );
        ActivityFavouriteEntity e3 = new ActivityFavouriteEntity(
                null, "ZK-004", "ACT-006", 2, LocalDateTime.now()
        );

        e1 = activityFavouriteRepository.create(e1);
        e2 = activityFavouriteRepository.create(e2);
        e3 = activityFavouriteRepository.create(e3);

        List<ActivityFavouriteEntity> results = activityFavouriteRepository.findByZakaznik("ZK-004", 10, 0);
        Assertions.assertEquals(3, results.size(), "Expected 3 activities for the zakaznik.");
        // Assuming that findByZakaznik returns results ordered by pocet descending,
        // the first entity should have the highest pocet (which is 6 in this case).
        Assertions.assertEquals("ACT-005", results.get(0).getActivityId(), "First entity should have the highest pocet.");
    }

    @Order(5)
    @Test
    public void testFindByZakaznikAktivita() {
        // Create entity with null ID.
        ActivityFavouriteEntity entity = new ActivityFavouriteEntity(
                null, "ZK-005", "ACT-007", 8, LocalDateTime.now()
        );
        activityFavouriteRepository.create(entity);

        Optional<ActivityFavouriteEntity> found = activityFavouriteRepository.findByZakaznikAktivita("ZK-005", "ACT-007");
        Assertions.assertTrue(found.isPresent(), "Expected to find entity with given zakaznik and activity.");
    }
}
