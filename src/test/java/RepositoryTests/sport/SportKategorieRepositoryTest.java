package RepositoryTests.sport;

import cz.inspire.thesis.data.EntityManagerProducer;
import cz.inspire.thesis.data.model.sport.sport.SportKategorieEntity;
import cz.inspire.thesis.data.model.sport.sport.SportKategorieLocEntity;
import cz.inspire.thesis.data.repository.sport.sport.SportKategorieRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class SportKategorieRepositoryTest {

    private SportKategorieRepository sportKategorieRepository;

    @Before
    public void setUp() {
        CdiContainer container = CdiContainerLoader.getCdiContainer();
        container.boot();

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");

        EntityManagerProducer producer = BeanProvider.getContextualReference(EntityManagerProducer.class);
        producer.setEntityManagerFactory(emf);

        sportKategorieRepository = BeanProvider.getContextualReference(SportKategorieRepository.class);

        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.createQuery("DELETE FROM SportKategorieEntity").executeUpdate();
        em.createQuery("DELETE FROM SportKategorieLocEntity").executeUpdate();
        em.getTransaction().commit();
    }

    private SportKategorieEntity createCategory(EntityManager em, String id, String facilityId, String serviceUUID, SportKategorieEntity parent) {
        SportKategorieEntity category = new SportKategorieEntity(
                id, facilityId, serviceUUID, parent, null, null, null
        );
        em.persist(category);
        return category;
    }

    private SportKategorieLocEntity createCategoryLocale(EntityManager em, String id, String jazyk, String nazev, String popis, SportKategorieEntity category) {
        SportKategorieLocEntity locale = new SportKategorieLocEntity(
                id, jazyk, nazev, popis, category
        );
        em.persist(locale);
        return locale;
    }

    @Test
    public void testFindAll() {
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();

        SportKategorieEntity category = createCategory(em, "category1", "facility1", "service1", null);
        createCategoryLocale(em, "locale1", "en", "Category 1", "Description 1", category);

        em.getTransaction().commit();

        List<SportKategorieEntity> categories = sportKategorieRepository.findAll();
        assertNotNull(categories);
        assertEquals(1, categories.size());
        assertEquals("category1", categories.get(0).getId());
    }

    @Test
    public void testFindRoot() {
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();

        SportKategorieEntity rootCategory = createCategory(em, "rootCategory", "facility1", "service1", null);
        createCategoryLocale(em, "locale1", "en", "Root Category", "Description", rootCategory);

        SportKategorieEntity childCategory = createCategory(em, "childCategory", "facility2", "service2", rootCategory);
        createCategoryLocale(em, "locale2", "en", "Child Category", "Description", childCategory);

        em.getTransaction().commit();

        List<SportKategorieEntity> roots = sportKategorieRepository.findRoot();
        assertNotNull(roots);
        assertEquals(1, roots.size());
        assertEquals("rootCategory", roots.get(0).getId());
    }

    @Test
    public void testFindAllByNadrazenaKategorie() {
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();

        SportKategorieEntity rootCategory = createCategory(em, "rootCategory", "facility1", "service1", null);
        createCategoryLocale(em, "locale1", "en", "Root Category", "Description", rootCategory);

        SportKategorieEntity childCategory = createCategory(em, "childCategory", "facility2", "service2", rootCategory);
        createCategoryLocale(em, "locale2", "en", "Child Category", "Description", childCategory);

        em.getTransaction().commit();

        List<SportKategorieEntity> children = sportKategorieRepository.findAllByNadrazenaKategorie("rootCategory");
        assertNotNull(children);
        assertEquals(1, children.size());
        assertEquals("childCategory", children.get(0).getId());
    }

    @Test
    public void testFindAllWithPagination() {
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();

        SportKategorieEntity category1 = createCategory(em, "category1", "facility1", "service1", null);
        createCategoryLocale(em, "locale1", "en", "Category 1", "Description 1", category1);

        SportKategorieEntity category2 = createCategory(em, "category2", "facility2", "service2", null);
        createCategoryLocale(em, "locale2", "en", "Category 2", "Description 2", category2);

        em.getTransaction().commit();

        List<SportKategorieEntity> categories = sportKategorieRepository.findAll(0, 1);
        assertNotNull(categories);
        assertEquals(1, categories.size());
        assertEquals("category1", categories.get(0).getId());
    }

    @Test
    public void testCount() {
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();

        createCategory(em, "category1", "facility1", "service1", null);
        createCategory(em, "category2", "facility2", "service2", null);

        em.getTransaction().commit();

        Long count = sportKategorieRepository.count();
        assertEquals(Long.valueOf(2), count);
    }

    @Test
    public void testCountRoot() {
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();

        SportKategorieEntity rootCategory = createCategory(em, "rootCategory", "facility1", "service1", null);
        createCategory(em, "childCategory", "facility2", "service2", rootCategory);

        em.getTransaction().commit();

        Long rootCount = sportKategorieRepository.countRoot();
        assertEquals(Long.valueOf(1), rootCount);
    }

    @Test
    public void testCountByNadrazenaKategorie() {
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();

        SportKategorieEntity rootCategory = createCategory(em, "rootCategory", "facility1", "service1", null);
        createCategory(em, "childCategory1", "facility2", "service2", rootCategory);
        createCategory(em, "childCategory2", "facility3", "service3", rootCategory);

        em.getTransaction().commit();

        Long childCount = sportKategorieRepository.countByNadrazenaKategorie("rootCategory");
        assertEquals(Long.valueOf(2), childCount);
    }

    @Test
    public void testFindAllByMultisportFacilityId() {
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();

        SportKategorieEntity category1 = createCategory(em, "category1", "facility1", "service1", null);
        createCategoryLocale(em, "locale1", "en", "Category 1", "Description 1", category1);

        SportKategorieEntity category2 = createCategory(em, "category2", "facility1", "service2", null);
        createCategoryLocale(em, "locale2", "en", "Category 2", "Description 2", category2);

        SportKategorieEntity category3 = createCategory(em, "category3", "facility2", "service3", null);
        createCategoryLocale(em, "locale3", "en", "Category 3", "Description 3", category3);

        em.getTransaction().commit();

        List<SportKategorieEntity> categories = sportKategorieRepository.findAllByMultisportFacilityId("facility1");
        assertNotNull(categories);
        assertEquals(2, categories.size());
        assertTrue(categories.stream().anyMatch(c -> c.getId().equals("category1")));
        assertTrue(categories.stream().anyMatch(c -> c.getId().equals("category2")));
    }
}
