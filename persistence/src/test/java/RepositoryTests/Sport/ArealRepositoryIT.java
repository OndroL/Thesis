package RepositoryTests.Sport;

import RepositoryTests.DatabaseCleaner;
import cz.inspire.sport.entity.ArealEntity;
import cz.inspire.sport.entity.ArealLocEntity;
import cz.inspire.sport.repository.ArealRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;
import java.util.List;

@Transactional
@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ArealRepositoryIT {

    @Inject
    ArealRepository arealRepository;

    @Inject
    DatabaseCleaner databaseCleaner;

    // Helper method to create locale data; note that the ID is set to null for generation.
    private List<ArealLocEntity> createLocaleData(String id, String language, String name, String description) {
        return List.of(new ArealLocEntity(null, language, name, description));
    }

    @BeforeAll
    @ActivateRequestContext
    public void clearDatabase() {
        databaseCleaner.clearTable(ArealEntity.class, true);
        databaseCleaner.clearTable(ArealLocEntity.class, true);
    }

    @Test
    public void testSaveAndFindById() {
        ArealEntity entity = new ArealEntity(
                null,
                5,
                createLocaleData(null, "en", "Sports Area", "A great place"),
                null,
                new ArrayList<>(),
                new ArrayList<>()
        );
        entity = arealRepository.create(entity);

        ArealEntity retrieved = arealRepository.findByPrimaryKey(entity.getId());
        Assertions.assertNotNull(retrieved, "Entity should be present in repository.");
        Assertions.assertEquals(5, retrieved.getPocetNavazujucichRez(), "PocetNavazujucichRez should match.");
        Assertions.assertEquals("en", retrieved.getLocaleData().get(0).getJazyk(), "LocaleData should contain expected language.");
    }

    @Test
    public void testFindRoot() {
        ArealEntity root1 = new ArealEntity(null, 1, new ArrayList<>(), null, new ArrayList<>(), new ArrayList<>());
        ArealEntity root2 = new ArealEntity(null, 1, new ArrayList<>(), null, new ArrayList<>(), new ArrayList<>());

        List<ArealLocEntity> root1Loc = createLocaleData(null, "cz", "Root Area 1", "Top-level area");
        List<ArealLocEntity> root2Loc = createLocaleData(null, "cz", "Root Area 2", "Top-level area");

        root1.setLocaleData(root1Loc);
        root2.setLocaleData(root2Loc);

        root1 = arealRepository.create(root1);
        root2 = arealRepository.create(root2);

        List<ArealEntity> results = arealRepository.findRoot("cz");
        Assertions.assertEquals(2, results.size(), "Expected 2 root areas.");
    }

    @Test
    public void testFindByParent() {
        ArealEntity parent = new ArealEntity(null, 2, createLocaleData(null, "en", "Parent Area", "Main area"), null, new ArrayList<>(), new ArrayList<>());
        ArealEntity child1 = new ArealEntity(null, 1, createLocaleData(null, "en", "Child Area 1", "Sub area 1"), parent, new ArrayList<>(), new ArrayList<>());
        ArealEntity child2 = new ArealEntity(null, 1, createLocaleData(null, "en", "Child Area 2", "Sub area 2"), parent, new ArrayList<>(), new ArrayList<>());

        parent = arealRepository.create(parent);
        child1 = arealRepository.create(child1);
        child2 = arealRepository.create(child2);

        List<ArealEntity> results = arealRepository.findByParent(parent.getId(), "en");
        Assertions.assertEquals(2, results.size(), "Expected 2 child areas.");
    }

    @Test
    public void testFindByParentWithPagination() {
        ArealEntity parent = new ArealEntity(null, 1, createLocaleData(null, "en", "Limited Parent Area", "Main area"), null, new ArrayList<>(), new ArrayList<>());
        ArealEntity child1 = new ArealEntity(null, 1, createLocaleData(null, "en", "Limited Child 1", "Limited sub area 1"), parent, new ArrayList<>(), new ArrayList<>());
        ArealEntity child2 = new ArealEntity(null, 1, createLocaleData(null, "en", "Limited Child 2", "Limited sub area 2"), parent, new ArrayList<>(), new ArrayList<>());

        parent = arealRepository.create(parent);
        child1 = arealRepository.create(child1);
        child2 = arealRepository.create(child2);

        List<ArealEntity> results = arealRepository.findByParent(parent.getId(), "en", 1, 0);
        Assertions.assertEquals(1, results.size(), "Expected only 1 result due to limit.");
    }

    @Test
    public void testFindIfChild() {
        ArealEntity parent = new ArealEntity(null, 2, createLocaleData(null, "en", "Parent Zone", "Main zone"), null, new ArrayList<>(), new ArrayList<>());
        ArealEntity child = new ArealEntity(null, 1, createLocaleData(null, "en", "Child Zone", "Sub zone"), parent, new ArrayList<>(), new ArrayList<>());

        parent = arealRepository.create(parent);
        child = arealRepository.create(child);

        ArealEntity found = arealRepository.findIfChild(child.getId(), parent.getId());
        Assertions.assertNotNull(found, "Expected to find the child ArealEntity.");
        Assertions.assertEquals(child.getId(), found.getId(), "Returned entity should match child's ID.");
    }

    @Test
    public void testGetArealIdsByParent() {
        ArealEntity parent = new ArealEntity(null, 2, createLocaleData(null, "en", "Parent Complex", "Main complex"), null, new ArrayList<>(), new ArrayList<>());
        ArealEntity child1 = new ArealEntity(null, 1, createLocaleData(null, "en", "Child Complex 1", "Sub complex 1"), parent, new ArrayList<>(), new ArrayList<>());
        ArealEntity child2 = new ArealEntity(null, 1, createLocaleData(null, "en", "Child Complex 2", "Sub complex 2"), parent, new ArrayList<>(), new ArrayList<>());

        parent = arealRepository.create(parent);
        child1 = arealRepository.create(child1);
        child2 = arealRepository.create(child2);

        List<String> results = arealRepository.getArealIdsByParent(parent.getId());
        Assertions.assertEquals(2, results.size(), "Expected 2 child areas.");
    }

    @Test
    public void testDeleteEntity() {
        ArealEntity entity = new ArealEntity(
                null,
                3,
                createLocaleData(null, "en", "Deletable Area", "To be removed"),
                null,
                new ArrayList<>(),
                new ArrayList<>()
        );

        entity = arealRepository.create(entity);

        String generatedId = entity.getId();
        arealRepository.deleteByPrimaryKey(generatedId);

        ArealEntity deleted = arealRepository.findByPrimaryKey(generatedId);
        Assertions.assertNull(deleted, "Entity should be deleted from repository.");
    }

    @Test
    public void testFindRootWithPagination() {
        ArealEntity root1 = new ArealEntity(null, 1, createLocaleData(null, "en", "Root Limited 1", "Top-level area"), null, new ArrayList<>(), new ArrayList<>());
        ArealEntity root2 = new ArealEntity(null, 1, createLocaleData(null, "en", "Root Limited 2", "Top-level area"), null, new ArrayList<>(), new ArrayList<>());
        ArealEntity root3 = new ArealEntity(null, 1, createLocaleData(null, "en", "Root Limited 3", "Top-level area"), null, new ArrayList<>(), new ArrayList<>());

        root1 = arealRepository.create(root1);
        root2 = arealRepository.create(root2);
        root3 = arealRepository.create(root3);

        List<ArealEntity> results = arealRepository.findRoot("en", 2, 0);
        Assertions.assertEquals(2, results.size(), "Expected only 2 root areas due to limit.");
    }

    @Test
    public void testFindAllOrdered() {
        ArealEntity area1 = new ArealEntity(null, 1, createLocaleData(null, "en", "Ordered Area 1", "Test area"), null, new ArrayList<>(), new ArrayList<>());
        ArealEntity area2 = new ArealEntity(null, 1, createLocaleData(null, "en", "Ordered Area 2", "Test area"), null, new ArrayList<>(), new ArrayList<>());
        ArealEntity area3 = new ArealEntity(null, 1, createLocaleData(null, "en", "Ordered Area 3", "Test area"), null, new ArrayList<>(), new ArrayList<>());

        arealRepository.create(area1);
        arealRepository.create(area2);
        arealRepository.create(area3);

        List<ArealEntity> results = arealRepository.findAllOrdered();

        Assertions.assertTrue(results.size() >= 3, "Expected at least 3 ordered areas.");
        // For example, check that the IDs are in ascending order.
        Assertions.assertTrue(results.get(0).getId().compareTo(results.get(1).getId()) < 0, "Ordering failed between 0 and 1");
        Assertions.assertTrue(results.get(1).getId().compareTo(results.get(2).getId()) < 0, "Ordering failed between 1 and 2");
    }

    @Test
    public void testFindAll() {
        databaseCleaner.clearTable(ArealEntity.class, true);
        ArealEntity area1 = new ArealEntity(null, 1, createLocaleData(null, "en", "All Area 1", "Test area"), null, new ArrayList<>(), new ArrayList<>());
        ArealEntity area2 = new ArealEntity(null, 1, createLocaleData(null, "en", "All Area 2", "Test area"), null, new ArrayList<>(), new ArrayList<>());

        arealRepository.create(area1);
        arealRepository.create(area2);

        List<ArealEntity> results = arealRepository.findAll();
        Assertions.assertEquals(2, results.size(), "Expected 2 areas in findAll.");
    }
}
