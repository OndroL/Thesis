package RepositoryTests.sport;

import cz.inspire.thesis.data.EntityManagerProducer;
import cz.inspire.thesis.data.model.sport.objekt.ArealEntity;
import cz.inspire.thesis.data.model.sport.objekt.ArealLocEntity;
import cz.inspire.thesis.data.repository.sport.objekt.ArealLocRepository;
import cz.inspire.thesis.data.repository.sport.objekt.ArealRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ArealRepositoryTest {

    private ArealRepository repository;
    private ArealLocRepository arealLocRepository;

    @Before
    public void setUp() {
        CdiContainer container = CdiContainerLoader.getCdiContainer();
        container.boot();

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        EntityManagerProducer producer = BeanProvider.getContextualReference(EntityManagerProducer.class);
        producer.setEntityManagerFactory(emf);

        repository = BeanProvider.getContextualReference(ArealRepository.class);
        arealLocRepository = BeanProvider.getContextualReference(ArealLocRepository.class);

        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.createQuery("DELETE FROM ArealLocEntity").executeUpdate();
        em.createQuery("DELETE FROM ArealEntity").executeUpdate();
        em.getTransaction().commit();
    }

    @Test
    public void testFindAll() {
        repository.save(new ArealEntity("1", 5, null, null, null, null));
        repository.save(new ArealEntity("2", 3, null, null, null, null));

        List<ArealEntity> allAreals = repository.findAll();
        assertNotNull(allAreals);
        assertEquals(2, allAreals.size());
    }

    @Test
    public void testFindAllWithLocaleData() {
        ArealEntity areal = new ArealEntity("areal1", 2, new ArrayList<>(), null, null, null);

        ArealLocEntity loc1 = new ArealLocEntity("loc1", "en", "Name1", "Description1", areal);
        ArealLocEntity loc2 = new ArealLocEntity("loc2", "cz", "Name2", "Description2", areal);

        areal.setLocaleData(List.of(loc1, loc2));

        repository.save(areal);

        List<ArealEntity> areals = repository.findAll();
        assertNotNull(areals);
        assertEquals(1, areals.size());
        assertEquals(2, areals.get(0).getLocaleData().size());
    }

    @Test
    public void testFindByParentWithLocaleData() {
        ArealEntity parent = new ArealEntity("parent1", 2, new ArrayList<>(), null, null, null);
        repository.save(parent);

        ArealEntity child1 = new ArealEntity("child1", 1, new ArrayList<>(), parent, null, null);
        ArealEntity child2 = new ArealEntity("child2", 1, new ArrayList<>(), parent, null, null);

        ArealLocEntity child1Loc = new ArealLocEntity("locChild1", "en", "Child1 Name", "Child1 Desc", child1);
        ArealLocEntity child2Loc = new ArealLocEntity("locChild2", "en", "Child2 Name", "Child2 Desc", child2);

        child1.setLocaleData(List.of(child1Loc));
        child2.setLocaleData(List.of(child2Loc));

        repository.save(child1);
        repository.save(child2);

        List<ArealEntity> children = repository.findByParent("parent1", "en");
        assertNotNull(children);
        assertEquals(2, children.size());
    }

    @Test
    public void testFindRootWithLocaleData() {
        ArealEntity root1 = new ArealEntity("root1", 5, new ArrayList<>(), null, null, null);
        ArealEntity root2 = new ArealEntity("root2", 3, new ArrayList<>(), null, null, null);

        ArealLocEntity loc1 = new ArealLocEntity("locRoot1", "en", "Root1 Name", "Root1 Desc", root1);
        ArealLocEntity loc2 = new ArealLocEntity("locRoot2", "en", "Root2 Name", "Root2 Desc", root2);

        root1.setLocaleData(List.of(loc1));
        root2.setLocaleData(List.of(loc2));

        repository.save(root1);
        repository.save(root2);

        List<ArealEntity> roots = repository.findRoot("en");
        assertNotNull(roots);
        assertEquals(2, roots.size());
    }

    @Test
    public void testOrphanRemoval() {
        // Create and save ArealEntity with associated ArealLocEntity
        ArealEntity areal = new ArealEntity("areal1", 2, new ArrayList<>(), null, null, null);
        ArealLocEntity loc1 = new ArealLocEntity("loc1", "en", "Name1", "Description1", areal);
        areal.setLocaleData(List.of(loc1));
        repository.save(areal);

        // Verify the ArealEntity and ArealLocEntity are saved
        List<ArealEntity> areals = repository.findAll();
        assertEquals(1, areals.size());
        assertEquals(1, areals.get(0).getLocaleData().size());

        // Use EntityManager to reattach the ArealEntity
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        ArealEntity managedAreal = em.merge(repository.findById("areal1"));
        em.remove(managedAreal); // Perform the removal
        em.getTransaction().commit();

        // Verify the ArealEntity and ArealLocEntity are removed
        areals = repository.findAll();
        assertEquals(0, areals.size());

        List<ArealLocEntity> localeData = arealLocRepository.findByArealAndJazyk("areal1", "en");
        assertEquals(0, localeData.size());
    }



    @Test
    public void testFindByParentWithPagination() {
        // Create parent
        ArealEntity parent = new ArealEntity("parent1", 2, new ArrayList<>(), null, null, null);
        repository.save(parent);

        // Create children
        ArealEntity child1 = new ArealEntity("child1", 1, new ArrayList<>(), parent, null, null);
        ArealEntity child2 = new ArealEntity("child2", 1, new ArrayList<>(), parent, null, null);
        ArealEntity child3 = new ArealEntity("child3", 1, new ArrayList<>(), parent, null, null);

        // Add locale data to children
        ArealLocEntity child1Loc = new ArealLocEntity("locChild1", "en", "Child1 Name", "Child1 Desc", child1);
        ArealLocEntity child2Loc = new ArealLocEntity("locChild2", "en", "Child2 Name", "Child2 Desc", child2);
        ArealLocEntity child3Loc = new ArealLocEntity("locChild3", "en", "Child3 Name", "Child3 Desc", child3);

        child1.setLocaleData(List.of(child1Loc));
        child2.setLocaleData(List.of(child2Loc));
        child3.setLocaleData(List.of(child3Loc));

        // Save entities
        repository.save(child1);
        repository.save(child2);
        repository.save(child3);

        // Query with pagination
        List<ArealEntity> childrenPage1 = repository.findByParent("parent1", "en", 0, 2);
        assertNotNull(childrenPage1);
        assertEquals(2, childrenPage1.size());

        List<ArealEntity> childrenPage2 = repository.findByParent("parent1", "en", 2, 2);
        assertNotNull(childrenPage2);
        assertEquals(1, childrenPage2.size());
    }

    @Test
    public void testFindRootWithPagination() {
        // Create root areas
        ArealEntity root1 = new ArealEntity("root1", 5, new ArrayList<>(), null, null, null);
        ArealEntity root2 = new ArealEntity("root2", 3, new ArrayList<>(), null, null, null);
        ArealEntity root3 = new ArealEntity("root3", 2, new ArrayList<>(), null, null, null);

        // Create locale data for roots
        ArealLocEntity loc1 = new ArealLocEntity("locRoot1", "en", "Root1 Name", "Root1 Desc", root1);
        ArealLocEntity loc2 = new ArealLocEntity("locRoot2", "en", "Root2 Name", "Root2 Desc", root2);
        ArealLocEntity loc3 = new ArealLocEntity("locRoot3", "en", "Root3 Name", "Root3 Desc", root3);

        root1.setLocaleData(List.of(loc1));
        root2.setLocaleData(List.of(loc2));
        root3.setLocaleData(List.of(loc3));

        // Save entities
        repository.save(root1);
        repository.save(root2);
        repository.save(root3);

        // Query with pagination
        List<ArealEntity> rootsPage1 = repository.findRoot("en", 0, 2);
        assertNotNull(rootsPage1);
        assertEquals(2, rootsPage1.size());

        List<ArealEntity> rootsPage2 = repository.findRoot("en", 2, 2);
        assertNotNull(rootsPage2);
        assertEquals(1, rootsPage2.size());
    }

    @Test
    public void testFindIfChild() {
        // Create parent
        ArealEntity parent = new ArealEntity("parent1", 2, new ArrayList<>(), null, null, null);
        repository.save(parent);

        // Create child
        ArealEntity child = new ArealEntity("child1", 1, new ArrayList<>(), parent, null, null);
        repository.save(child);

        // Query to check if child belongs to the parent
        ArealEntity result = repository.findIfChild("child1", "parent1");
        assertNotNull(result);
        assertEquals("child1", result.getId());
    }


}
