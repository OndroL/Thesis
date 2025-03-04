package RepositoryTests.Sport;

import cz.inspire.sport.entity.ActivityFavouriteEntity;
import cz.inspire.sport.repository.ActivityFavouriteRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional
@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ActivityFavouriteRepositoryIT {

    @Inject
    ActivityFavouriteRepository activityFavouriteRepository;

    @BeforeAll
    @ActivateRequestContext
    public void clearDatabase() {
        List<ActivityFavouriteEntity> allEntities = new ArrayList<>();
        activityFavouriteRepository.findAll().forEach(allEntities::add);
        if (!allEntities.isEmpty()) {
            activityFavouriteRepository.deleteAll(allEntities);
        }
    }

    @Test
    public void testSaveAndFindById() {
        ActivityFavouriteEntity entity = new ActivityFavouriteEntity("ID-001", "ZK-001", "ACT-001", 5, LocalDateTime.now());
        activityFavouriteRepository.create(entity);

        ActivityFavouriteEntity retrieved = activityFavouriteRepository.findById("ID-001");
        Assertions.assertNotNull(retrieved, "Entity should be present in repository.");
        Assertions.assertEquals("ZK-001", retrieved.getZakaznikId(), "Zakaznik ID should match.");
        Assertions.assertEquals("ACT-001", retrieved.getActivityId(), "Activity ID should match.");
        Assertions.assertEquals(5, retrieved.getPocet(), "Pocet should match.");
    }

    @Test
    public void testUpdateEntity() {
        ActivityFavouriteEntity entity = new ActivityFavouriteEntity("ID-002", "ZK-002", "ACT-002", 3, LocalDateTime.now());
        activityFavouriteRepository.create(entity);

        entity.setPocet(10);
        activityFavouriteRepository.create(entity);

        ActivityFavouriteEntity updated = activityFavouriteRepository.findById("ID-002");
        Assertions.assertNotNull(updated, "Entity should be present after update.");
        Assertions.assertEquals(10, updated.getPocet(), "Updated pocet value should be 10.");
    }

    @Test
    public void testDeleteEntity() {
        ActivityFavouriteEntity entity = new ActivityFavouriteEntity("ID-003", "ZK-003", "ACT-003", 7, LocalDateTime.now());
        activityFavouriteRepository.create(entity);

        activityFavouriteRepository.deleteById("ID-003");
        ActivityFavouriteEntity deleted = activityFavouriteRepository.findById("ID-003");
        Assertions.assertNull(deleted, "Entity should be deleted from repository.");
    }

    @Test
    public void testFindByZakaznik() {
        ActivityFavouriteEntity e1 = new ActivityFavouriteEntity("ID-004", "ZK-004", "ACT-004", 4, LocalDateTime.now());
        ActivityFavouriteEntity e2 = new ActivityFavouriteEntity("ID-005", "ZK-004", "ACT-005", 6, LocalDateTime.now());
        ActivityFavouriteEntity e3 = new ActivityFavouriteEntity("ID-006", "ZK-004", "ACT-006", 2, LocalDateTime.now());

        activityFavouriteRepository.create(e1);
        activityFavouriteRepository.create(e2);
        activityFavouriteRepository.create(e3);

        List<ActivityFavouriteEntity> results = activityFavouriteRepository.findByZakaznik("ZK-004", 10, 0);
        Assertions.assertEquals(3, results.size(), "Expected 3 activities for the zakaznik.");
        Assertions.assertEquals("ACT-005", results.getFirst().getActivityId(), "First entity should have the highest pocet.");
    }

    @Test
    public void testFindByZakaznikAktivita() {
        ActivityFavouriteEntity entity = new ActivityFavouriteEntity("ID-007", "ZK-005", "ACT-007", 8, LocalDateTime.now());
        activityFavouriteRepository.create(entity);

        Optional<ActivityFavouriteEntity> found = activityFavouriteRepository.findByZakaznikAktivita("ZK-005", "ACT-007");
        Assertions.assertTrue(found.isPresent(), "Expected to find entity with given zakaznik and activity.");
    }
}
