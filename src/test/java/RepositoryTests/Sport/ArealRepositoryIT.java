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
import org.junit.jupiter.api.*;

import java.util.*;

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
        ArealEntity entity = new ArealEntity("ID-001", 5, createLocaleData("LOC-001", "en", "Sports Area", "A great place"), null, new ArrayList<>(), new ArrayList<>());
        em.persist(entity);
        em.flush();

        Optional<ArealEntity> retrieved = arealRepository.findById("ID-001");

        Assertions.assertTrue(retrieved.isPresent(), "Entity should be present in repository.");
        Assertions.assertEquals(5, retrieved.get().getPocetNavazujucichRez(), "PocetNavazujucichRez should match.");
        Assertions.assertEquals("en", retrieved.get().getLocaleData().getFirst().getJazyk(), "LocaleData should contain expected language.");
    }

    @Test
    public void testFindRoot() {
        ArealEntity root1 = new ArealEntity("ID-002", 1, new ArrayList<>(), null, new ArrayList<>(), new ArrayList<>());
        ArealEntity root2 = new ArealEntity("ID-003", 1, new ArrayList<>(), null, new ArrayList<>(), new ArrayList<>());

        List<ArealLocEntity> root1Loc = createLocaleData("LOC-002", "cz", "Root Area 1", "Top-level area");
        List<ArealLocEntity> root2Loc = createLocaleData("LOC-003", "cz", "Root Area 2", "Top-level area");

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
        ArealEntity parent = new ArealEntity("ID-004", 2, createLocaleData("LOC-004", "en", "Parent Area", "Main area"), null, new ArrayList<>(), new ArrayList<>());
        ArealEntity child1 = new ArealEntity("ID-005", 1, createLocaleData("LOC-005", "en", "Child Area 1", "Sub area 1"), parent, new ArrayList<>(), new ArrayList<>());
        ArealEntity child2 = new ArealEntity("ID-006", 1, createLocaleData("LOC-006", "en", "Child Area 2", "Sub area 2"), parent, new ArrayList<>(), new ArrayList<>());

        em.persist(parent);
        em.persist(child1);
        em.persist(child2);
        em.flush();

        List<ArealEntity> results = arealRepository.findByParent("ID-004", "en");

        Assertions.assertEquals(2, results.size(), "Expected 2 child areas.");
    }

    @Test
    public void testFindByParentWithLimit() {
        ArealEntity parent = new ArealEntity("ID-007", 1, createLocaleData("LOC-007", "en", "Limited Parent Area", "Main area"), null, new ArrayList<>(), new ArrayList<>());
        ArealEntity child1 = new ArealEntity("ID-008", 1, createLocaleData("LOC-008", "en", "Limited Child 1", "Limited sub area 1"), parent, new ArrayList<>(), new ArrayList<>());
        ArealEntity child2 = new ArealEntity("ID-009", 1, createLocaleData("LOC-009", "en", "Limited Child 2", "Limited sub area 2"), parent, new ArrayList<>(), new ArrayList<>());

        em.persist(parent);
        em.persist(child1);
        em.persist(child2);
        em.flush();

        List<ArealEntity> results = arealRepository.findByParentWithLimit("ID-007", "en", Limit.of(1));

        Assertions.assertEquals(1, results.size(), "Expected only 1 result due to limit.");
    }

    @Test
    public void testFindIfChild() {
        ArealEntity parent = new ArealEntity("ID-010", 2, createLocaleData("LOC-010", "en", "Parent Zone", "Main zone"), null, new ArrayList<>(), new ArrayList<>());
        ArealEntity child = new ArealEntity("ID-011", 1, createLocaleData("LOC-011", "en", "Child Zone", "Sub zone"), parent, new ArrayList<>(), new ArrayList<>());

        em.persist(parent);
        em.persist(child);
        em.flush();

        ArealEntity found = arealRepository.findIfChild("ID-011", "ID-010");

        Assertions.assertNotNull(found, "Expected to find child ArealEntity.");
        Assertions.assertEquals("ID-011", found.getId(), "Expected found entity to be ID-011.");
    }

    @Test
    public void testFindArealIdsByParent() {
        ArealEntity parent = new ArealEntity("ID-012", 2, createLocaleData("LOC-012", "en", "Parent Complex", "Main complex"), null, new ArrayList<>(), new ArrayList<>());
        ArealEntity child1 = new ArealEntity("ID-013", 1, createLocaleData("LOC-013", "en", "Child Complex 1", "Sub complex 1"), parent, new ArrayList<>(), new ArrayList<>());
        ArealEntity child2 = new ArealEntity("ID-014", 1, createLocaleData("LOC-014", "en", "Child Complex 2", "Sub complex 2"), parent, new ArrayList<>(), new ArrayList<>());

        em.persist(parent);
        em.persist(child1);
        em.persist(child2);
        em.flush();

        List<String> results = arealRepository.findArealIdsByParent("ID-012");

        Assertions.assertEquals(2, results.size(), "Expected 2 child areas.");
    }

    @Test
    public void testDeleteEntity() {
        ArealEntity entity = new ArealEntity("ID-015", 3, createLocaleData("LOC-015", "en", "Deletable Area", "To be removed"), null, new ArrayList<>(), new ArrayList<>());
        arealRepository.save(entity);

        arealRepository.deleteById("ID-015");
        Optional<ArealEntity> deleted = arealRepository.findById("ID-015");

        Assertions.assertFalse(deleted.isPresent(), "Entity should be deleted from repository.");
    }

    @Test
    public void testFindRootWithLimit() {
        ArealEntity root1 = new ArealEntity("ID-016", 1, createLocaleData("LOC-016", "en", "Root Limited 1", "Top-level area"), null, new ArrayList<>(), new ArrayList<>());
        ArealEntity root2 = new ArealEntity("ID-017", 1, createLocaleData("LOC-017", "en", "Root Limited 2", "Top-level area"), null, new ArrayList<>(), new ArrayList<>());
        ArealEntity root3 = new ArealEntity("ID-018", 1, createLocaleData("LOC-018", "en", "Root Limited 3", "Top-level area"), null, new ArrayList<>(), new ArrayList<>());

        em.persist(root1);
        em.persist(root2);
        em.persist(root3);
        em.flush();

        List<ArealEntity> results = arealRepository.findRootWithLimit("en", Limit.of(2));

        Assertions.assertEquals(2, results.size(), "Expected only 2 root areas due to limit.");
    }

    @Test
    public void testFindAllOrdered() {
        ArealEntity area1 = new ArealEntity("ID-019", 1, createLocaleData("LOC-019", "en", "Ordered Area 1", "Test area"), null, new ArrayList<>(), new ArrayList<>());
        ArealEntity area2 = new ArealEntity("ID-020", 1, createLocaleData("LOC-020", "en", "Ordered Area 2", "Test area"), null, new ArrayList<>(), new ArrayList<>());
        ArealEntity area3 = new ArealEntity("ID-021", 1, createLocaleData("LOC-021", "en", "Ordered Area 3", "Test area"), null, new ArrayList<>(), new ArrayList<>());

        em.persist(area1);
        em.persist(area2);
        em.persist(area3);
        em.flush();

        List<ArealEntity> results = arealRepository.findAllOrdered();

        Assertions.assertEquals(3, results.size(), "Expected 3 ordered areas.");
        Assertions.assertEquals("ID-019", results.get(0).getId(), "First entity should be ID-019.");
        Assertions.assertEquals("ID-020", results.get(1).getId(), "Second entity should be ID-020.");
        Assertions.assertEquals("ID-021", results.get(2).getId(), "Third entity should be ID-021.");
    }

    @Test
    public void testFindAll() {
        databaseCleaner.clearTable(ArealEntity.class, true);
        ArealEntity area1 = new ArealEntity("ID-022", 1, createLocaleData("LOC-022", "en", "All Area 1", "Test area"), null, new ArrayList<>(), new ArrayList<>());
        ArealEntity area2 = new ArealEntity("ID-023", 1, createLocaleData("LOC-023", "en", "All Area 2", "Test area"), null, new ArrayList<>(), new ArrayList<>());

        em.persist(area1);
        em.persist(area2);
        em.flush();

        List<ArealEntity> results = arealRepository.findAll().toList();

        Assertions.assertEquals(2, results.size(), "Expected 2 areas in findAll.");
    }
}
