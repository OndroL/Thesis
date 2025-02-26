package RepositoryTests.Sport;

import RepositoryTests.DatabaseCleaner;
import cz.inspire.sport.entity.ArealEntity;
import cz.inspire.sport.entity.ArealLocEntity;
import cz.inspire.sport.repository.ArealRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.data.Limit;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional
@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ArealRepositoryIT {

    @Inject
    ArealRepository arealRepository;

    @Inject
    DatabaseCleaner databaseCleaner;

    @Inject
    EntityManager em;

    @BeforeAll
    @ActivateRequestContext
    public void clearDatabase() {
        databaseCleaner.clearTable(ArealEntity.class, true);
        databaseCleaner.clearTable(ArealLocEntity.class, true);
    }

    private List<ArealLocEntity> createLocaleData(String id, String language, String name, String description) {
        return List.of(new ArealLocEntity(id, language, name, description));
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
        em.persist(entity);
        em.flush();

        Optional<ArealEntity> retrieved = arealRepository.findById(entity.getId());

        Assertions.assertTrue(retrieved.isPresent(), "Entity should be present in repository.");
        Assertions.assertEquals(5, retrieved.get().getPocetNavazujucichRez(), "PocetNavazujucichRez should match.");
        Assertions.assertEquals("en", retrieved.get().getLocaleData().get(0).getJazyk(), "LocaleData should contain expected language.");
    }

    @Test
    public void testFindRoot() {
        ArealEntity root1 = new ArealEntity(null, 1, new ArrayList<>(), null, new ArrayList<>(), new ArrayList<>());
        ArealEntity root2 = new ArealEntity(null, 1, new ArrayList<>(), null, new ArrayList<>(), new ArrayList<>());

        List<ArealLocEntity> root1Loc = createLocaleData(null, "cz", "Root Area 1", "Top-level area");
        List<ArealLocEntity> root2Loc = createLocaleData(null, "cz", "Root Area 2", "Top-level area");

        root1.setLocaleData(root1Loc);
        root2.setLocaleData(root2Loc);

        em.persist(root1);
        em.persist(root2);
        em.flush();

        List<ArealEntity> results = arealRepository.findRoot("cz");

        Assertions.assertEquals(2, results.size(), "Expected 2 root areas.");
    }

    @Test
    public void testFindByParent() {
        ArealEntity parent = new ArealEntity(null, 2, createLocaleData(null, "en", "Parent Area", "Main area"), null, new ArrayList<>(), new ArrayList<>());
        ArealEntity child1 = new ArealEntity(null, 1, createLocaleData(null, "en", "Child Area 1", "Sub area 1"), parent, new ArrayList<>(), new ArrayList<>());
        ArealEntity child2 = new ArealEntity(null, 1, createLocaleData(null, "en", "Child Area 2", "Sub area 2"), parent, new ArrayList<>(), new ArrayList<>());

        em.persist(parent);
        em.persist(child1);
        em.persist(child2);
        em.flush();

        List<ArealEntity> results = arealRepository.findByParent(parent.getId(), "en");

        Assertions.assertEquals(2, results.size(), "Expected 2 child areas.");
    }

    @Test
    public void testFindByParentWithLimit() {
        ArealEntity parent = new ArealEntity(null, 1, createLocaleData(null, "en", "Limited Parent Area", "Main area"), null, new ArrayList<>(), new ArrayList<>());
        ArealEntity child1 = new ArealEntity(null, 1, createLocaleData(null, "en", "Limited Child 1", "Limited sub area 1"), parent, new ArrayList<>(), new ArrayList<>());
        ArealEntity child2 = new ArealEntity(null, 1, createLocaleData(null, "en", "Limited Child 2", "Limited sub area 2"), parent, new ArrayList<>(), new ArrayList<>());

        em.persist(parent);
        em.persist(child1);
        em.persist(child2);
        em.flush();

        List<ArealEntity> results = arealRepository.findByParentWithLimit(parent.getId(), "en", Limit.of(1));

        Assertions.assertEquals(1, results.size(), "Expected only 1 result due to limit.");
    }

    @Test
    public void testFindIfChild() {
        ArealEntity parent = new ArealEntity(null, 2, createLocaleData(null, "en", "Parent Zone", "Main zone"), null, new ArrayList<>(), new ArrayList<>());
        ArealEntity child = new ArealEntity(null, 1, createLocaleData(null, "en", "Child Zone", "Sub zone"), parent, new ArrayList<>(), new ArrayList<>());

        em.persist(parent);
        em.persist(child);
        em.flush();

        Optional<ArealEntity> found = arealRepository.findIfChild(child.getId(), parent.getId());

        Assertions.assertTrue(found.isPresent(), "Expected to find the child ArealEntity.");
        Assertions.assertEquals(child.getId(), found.get().getId(), "Returned entity should match child's ID.");
    }

    @Test
    public void testGetArealIdsByParent() {
        ArealEntity parent = new ArealEntity(null, 2, createLocaleData(null, "en", "Parent Complex", "Main complex"), null, new ArrayList<>(), new ArrayList<>());
        ArealEntity child1 = new ArealEntity(null, 1, createLocaleData(null, "en", "Child Complex 1", "Sub complex 1"), parent, new ArrayList<>(), new ArrayList<>());
        ArealEntity child2 = new ArealEntity(null, 1, createLocaleData(null, "en", "Child Complex 2", "Sub complex 2"), parent, new ArrayList<>(), new ArrayList<>());

        em.persist(parent);
        em.persist(child1);
        em.persist(child2);
        em.flush();

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

        arealRepository.save(entity);
        em.flush();

        String generatedId = entity.getId();
        arealRepository.deleteById(generatedId);

        Optional<ArealEntity> deleted = arealRepository.findById(generatedId);
        Assertions.assertFalse(deleted.isPresent(), "Entity should be deleted from repository.");
    }

    @Test
    public void testFindRootWithLimit() {
        ArealEntity root1 = new ArealEntity(null, 1, createLocaleData(null, "en", "Root Limited 1", "Top-level area"), null, new ArrayList<>(), new ArrayList<>());
        ArealEntity root2 = new ArealEntity(null, 1, createLocaleData(null, "en", "Root Limited 2", "Top-level area"), null, new ArrayList<>(), new ArrayList<>());
        ArealEntity root3 = new ArealEntity(null, 1, createLocaleData(null, "en", "Root Limited 3", "Top-level area"), null, new ArrayList<>(), new ArrayList<>());

        em.persist(root1);
        em.persist(root2);
        em.persist(root3);
        em.flush();

        List<ArealEntity> results = arealRepository.findRootWithLimit("en", Limit.of(2));
        Assertions.assertEquals(2, results.size(), "Expected only 2 root areas due to limit.");
    }

    @Test
    public void testFindAllOrdered() {
        ArealEntity area1 = new ArealEntity(null, 1, createLocaleData(null, "en", "Ordered Area 1", "Test area"), null, new ArrayList<>(), new ArrayList<>());
        ArealEntity area2 = new ArealEntity(null, 1, createLocaleData(null, "en", "Ordered Area 2", "Test area"), null, new ArrayList<>(), new ArrayList<>());
        ArealEntity area3 = new ArealEntity(null, 1, createLocaleData(null, "en", "Ordered Area 3", "Test area"), null, new ArrayList<>(), new ArrayList<>());

        em.clear();
        em.persist(area1);
        em.persist(area2);
        em.persist(area3);
        em.flush();

        List<ArealEntity> results = arealRepository.findAllOrdered();

        Assertions.assertTrue(results.size() >= 3, "Expected at least 3 ordered areas.");
        Assertions.assertTrue(results.get(0).getId().compareTo(results.get(1).getId()) < 0, "Something went wrong");
        Assertions.assertTrue(results.get(1).getId().compareTo(results.get(2).getId()) < 0, "Something went wrong");
    }

    @Test
    public void testFindAll() {
        databaseCleaner.clearTable(ArealEntity.class, true);
        ArealEntity area1 = new ArealEntity(null, 1, createLocaleData(null, "en", "All Area 1", "Test area"), null, new ArrayList<>(), new ArrayList<>());
        ArealEntity area2 = new ArealEntity(null, 1, createLocaleData(null, "en", "All Area 2", "Test area"), null, new ArrayList<>(), new ArrayList<>());

        em.clear();
        em.persist(area1);
        em.persist(area2);
        em.flush();

        List<ArealEntity> results = arealRepository.findAll().toList();

        Assertions.assertEquals(2, results.size(), "Expected 2 areas in findAll.");
    }
}
