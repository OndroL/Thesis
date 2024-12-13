package RepositoryTests.sport;

import cz.inspire.thesis.data.EntityManagerProducer;
import cz.inspire.thesis.data.model.sport.sport.SportEntity;
import cz.inspire.thesis.data.model.sport.sport.SportKategorieEntity;
import cz.inspire.thesis.data.model.sport.sport.SportKategorieLocEntity;
import cz.inspire.thesis.data.repository.sport.sport.SportKategorieRepository;
import cz.inspire.thesis.data.repository.sport.sport.SportRepository;
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
    private SportRepository sportRepository;

    @Before
    public void setUp() {
        CdiContainer container = CdiContainerLoader.getCdiContainer();
        container.boot();

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        EntityManagerProducer producer = BeanProvider.getContextualReference(EntityManagerProducer.class);
        producer.setEntityManagerFactory(emf);

        sportKategorieRepository = BeanProvider.getContextualReference(SportKategorieRepository.class);
        sportRepository = BeanProvider.getContextualReference(SportRepository.class);

        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.createQuery("DELETE FROM SportKategorieLocEntity").executeUpdate();
        em.createQuery("DELETE FROM SportKategorieEntity").executeUpdate();
        em.createQuery("DELETE FROM SportEntity").executeUpdate();
        em.getTransaction().commit();
    }

    @Test
    public void testFindAll() {
        // Create categories with locale data
        SportKategorieEntity category1 = new SportKategorieEntity("category1", "facility1", "service1", null, null, null, null);
        SportKategorieLocEntity locale1 = new SportKategorieLocEntity("locale1", "en", "Category 1", "Description 1");
        category1.setLocaleData(List.of(locale1));
        sportKategorieRepository.save(category1);

        SportKategorieEntity category2 = new SportKategorieEntity("category2", "facility2", "service2", null, null, null, null);
        SportKategorieLocEntity locale2 = new SportKategorieLocEntity("locale2", "en", "Category 2", "Description 2");
        category2.setLocaleData(List.of(locale2));
        sportKategorieRepository.save(category2);

        // Validate query
        List<SportKategorieEntity> categories = sportKategorieRepository.findAll();
        assertNotNull(categories);
        assertEquals(2, categories.size());
    }

    @Test
    public void testFindRoot() {
        // Create root and child categories with locale data
        SportKategorieEntity rootCategory = new SportKategorieEntity("rootCategory", "facility1", "service1", null, null, null, null);
        SportKategorieLocEntity rootLocale = new SportKategorieLocEntity("localeRoot", "en", "Root Category", "Description Root");
        rootCategory.setLocaleData(List.of(rootLocale));
        sportKategorieRepository.save(rootCategory);

        SportKategorieEntity childCategory = new SportKategorieEntity("childCategory", "facility2", "service2", rootCategory, null, null, null);
        SportKategorieLocEntity childLocale = new SportKategorieLocEntity("localeChild", "en", "Child Category", "Description Child");
        childCategory.setLocaleData(List.of(childLocale));
        sportKategorieRepository.save(childCategory);

        // Validate query
        List<SportKategorieEntity> roots = sportKategorieRepository.findRoot();
        assertNotNull(roots);
        assertEquals(1, roots.size());
        assertEquals("rootCategory", roots.get(0).getId());
    }

    @Test
    public void testFindAllByNadrazenaKategorie() {
        // Create root and child categories with locale data
        SportKategorieEntity rootCategory = new SportKategorieEntity("rootCategory", "facility1", "service1", null, null, null, null);
        SportKategorieLocEntity rootLocale = new SportKategorieLocEntity("localeRoot", "en", "Root Category", "Description Root");
        rootCategory.setLocaleData(List.of(rootLocale));
        sportKategorieRepository.save(rootCategory);

        SportKategorieEntity childCategory1 = new SportKategorieEntity("childCategory1", "facility2", "service2", rootCategory, null, null, null);
        SportKategorieLocEntity childLocale1 = new SportKategorieLocEntity("localeChild1", "en", "Child Category 1", "Description Child 1");
        childCategory1.setLocaleData(List.of(childLocale1));
        sportKategorieRepository.save(childCategory1);

        SportKategorieEntity childCategory2 = new SportKategorieEntity("childCategory2", "facility3", "service3", rootCategory, null, null, null);
        SportKategorieLocEntity childLocale2 = new SportKategorieLocEntity("localeChild2", "en", "Child Category 2", "Description Child 2");
        childCategory2.setLocaleData(List.of(childLocale2));
        sportKategorieRepository.save(childCategory2);

        // Validate query
        List<SportKategorieEntity> children = sportKategorieRepository.findAllByNadrazenaKategorie("rootCategory");
        assertNotNull(children);
        assertEquals(2, children.size());
    }

    @Test
    public void testCount() {
        // Create categories with locale data
        SportKategorieEntity category1 = new SportKategorieEntity("category1", "facility1", "service1", null, null, null, null);
        SportKategorieLocEntity locale1 = new SportKategorieLocEntity("locale1", "en", "Category 1", "Description 1");
        category1.setLocaleData(List.of(locale1));
        sportKategorieRepository.save(category1);

        SportKategorieEntity category2 = new SportKategorieEntity("category2", "facility2", "service2", null, null, null, null);
        SportKategorieLocEntity locale2 = new SportKategorieLocEntity("locale2", "en", "Category 2", "Description 2");
        category2.setLocaleData(List.of(locale2));
        sportKategorieRepository.save(category2);

        // Validate query
        Long count = sportKategorieRepository.count();
        assertEquals(Long.valueOf(2), count);
    }


    @Test
    public void testCountRoot() {
        // Create root and child categories
        SportKategorieEntity rootCategory = new SportKategorieEntity("rootCategory", "facility1", "service1", null, null, null, null);
        sportKategorieRepository.save(rootCategory);

        SportKategorieEntity childCategory = new SportKategorieEntity("childCategory", "facility2", "service2", rootCategory, null, null, null);
        sportKategorieRepository.save(childCategory);

        // Validate query
        Long rootCount = sportKategorieRepository.countRoot();
        assertEquals(Long.valueOf(1), rootCount);
    }

    @Test
    public void testCountByNadrazenaKategorie() {
        // Create root and child categories
        SportKategorieEntity rootCategory = new SportKategorieEntity("rootCategory", "facility1", "service1", null, null, null, null);
        sportKategorieRepository.save(rootCategory);

        SportKategorieEntity childCategory1 = new SportKategorieEntity("childCategory1", "facility2", "service2", rootCategory, null, null, null);
        sportKategorieRepository.save(childCategory1);

        SportKategorieEntity childCategory2 = new SportKategorieEntity("childCategory2", "facility3", "service3", rootCategory, null, null, null);
        sportKategorieRepository.save(childCategory2);

        // Validate query
        Long childCount = sportKategorieRepository.countByNadrazenaKategorie("rootCategory");
        assertEquals(Long.valueOf(2), childCount);
    }

    @Test
    public void testFindAllByMultisportFacilityId() {
        // Create categories with locale data
        SportKategorieEntity category1 = new SportKategorieEntity("category1", "facility1", "service1", null, null, null, null);
        SportKategorieLocEntity locale1 = new SportKategorieLocEntity("locale1", "en", "Category 1", "Description 1");
        category1.setLocaleData(List.of(locale1));
        sportKategorieRepository.save(category1);

        SportKategorieEntity category2 = new SportKategorieEntity("category2", "facility1", "service2", null, null, null, null);
        SportKategorieLocEntity locale2 = new SportKategorieLocEntity("locale2", "en", "Category 2", "Description 2");
        category2.setLocaleData(List.of(locale2));
        sportKategorieRepository.save(category2);

        SportKategorieEntity category3 = new SportKategorieEntity("category3", "facility2", "service3", null, null, null, null);
        SportKategorieLocEntity locale3 = new SportKategorieLocEntity("locale3", "en", "Category 3", "Description 3");
        category3.setLocaleData(List.of(locale3));
        sportKategorieRepository.save(category3);

        // Validate query
        List<SportKategorieEntity> categories = sportKategorieRepository.findAllByMultisportFacilityId("facility1");
        assertNotNull(categories);
        assertEquals(2, categories.size());
        assertTrue(categories.stream().anyMatch(c -> c.getId().equals("category1")));
        assertTrue(categories.stream().anyMatch(c -> c.getId().equals("category2")));
    }
}