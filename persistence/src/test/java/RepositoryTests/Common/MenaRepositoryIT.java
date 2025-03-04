package RepositoryTests.Common;

import cz.inspire.common.entity.MenaEntity;
import cz.inspire.common.repository.MenaRepository;
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

@QuarkusTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MenaRepositoryIT {

    @Inject
    MenaRepository menaRepository;

    /**
     * Clears the database before tests to ensure isolation.
     */
    @BeforeAll
    @ActivateRequestContext
    public void clearDatabase() {
        List<MenaEntity> allEntities = new ArrayList<>();
        menaRepository.findAll().forEach(allEntities::add);
        if (!allEntities.isEmpty()) {
            menaRepository.deleteAll(allEntities);
        }
    }

    /**
     * Tests saving and retrieving a MenaEntity by ID.
     */
    @Test
    public void testSaveAndFindById() {
        MenaEntity entity = new MenaEntity("ID-001", "EUR", "Euro", 978, 0, 0);
        menaRepository.create(entity);

        MenaEntity retrieved = menaRepository.findById("ID-001");
        Assertions.assertNotNull(retrieved, "Entity should be present in repository.");
        Assertions.assertEquals("EUR", retrieved.getKod(), "Kod should match.");
        Assertions.assertEquals("Euro", retrieved.getVycetka(), "Vycetka should match.");
        Assertions.assertEquals(978, retrieved.getKodNum(), "KodNum should match.");
        Assertions.assertEquals(0, retrieved.getZaokrouhleniHotovost(), "ZaokrouhleniHotovost should match.");
        Assertions.assertEquals(0, retrieved.getZaokrouhleniKarta(), "ZaokrouhleniKarta should match.");
    }

    /**
     * Tests updating an existing entity's values.
     */
    @Test
    public void testUpdateEntity() {
        MenaEntity entity = new MenaEntity("ID-002", "USD", "US Dollar", 840, 1, 1);
        menaRepository.create(entity);

        // Modify values
        entity.setKod("GBP");
        entity.setVycetka("British Pound");
        entity.setKodNum(826);
        entity.setZaokrouhleniHotovost(2);
        entity.setZaokrouhleniKarta(2);
        menaRepository.create(entity);

        MenaEntity updated = menaRepository.findById("ID-002");
        Assertions.assertNotNull(updated, "Entity should still exist after update.");
        Assertions.assertEquals("GBP", updated.getKod(), "Updated kod should be GBP.");
        Assertions.assertEquals("British Pound", updated.getVycetka(), "Updated vycetka should be British Pound.");
        Assertions.assertEquals(826, updated.getKodNum(), "Updated kodNum should be 826.");
        Assertions.assertEquals(2, updated.getZaokrouhleniHotovost(), "Updated zaokrouhleniHotovost should be 2.");
        Assertions.assertEquals(2, updated.getZaokrouhleniKarta(), "Updated zaokrouhleniKarta should be 2.");
    }

    /**
     * Tests deleting an entity.
     */
    @Test
    public void testDeleteEntity() {
        MenaEntity entity = new MenaEntity("ID-003", "JPY", "Japanese Yen", 392, 0, 0);
        menaRepository.create(entity);

        menaRepository.deleteById("ID-003");
        MenaEntity deleted = menaRepository.findById("ID-003");
        Assertions.assertNull(deleted, "Entity should be deleted from repository.");
    }

    /**
     * Tests finding entities by kod (currency code).
     */
    @Test
    public void testFindByCode() {
        MenaEntity e1 = new MenaEntity("ID-004", "CZK", "Czech Koruna", 203, 1, 1);
        MenaEntity e2 = new MenaEntity("ID-005", "CZK", "Czech Koruna", 203, 0, 0);
        MenaEntity e3 = new MenaEntity("ID-006", "EUR", "Euro", 978, 0, 0);

        menaRepository.create(e1);
        menaRepository.create(e2);
        menaRepository.create(e3);

        List<MenaEntity> czkEntities = menaRepository.findByCode("CZK");
        Assertions.assertEquals(2, czkEntities.size(), "Expected 2 CZK entities.");
        Assertions.assertTrue(czkEntities.stream().allMatch(e -> "CZK".equals(e.getKod())), "All should have kod= 'CZK'.");
    }

    /**
     * Tests finding entities by kodNum (currency numeric code).
     */
    @Test
    public void testFindByCodeNum() {
        MenaEntity e1 = new MenaEntity("ID-007", "USD", "US Dollar", 840, 1, 1);
        MenaEntity e2 = new MenaEntity("ID-008", "USD", "US Dollar", 840, 0, 0);
        MenaEntity e3 = new MenaEntity("ID-009", "GBP", "British Pound", 826, 2, 2);

        menaRepository.create(e1);
        menaRepository.create(e2);
        menaRepository.create(e3);

        List<MenaEntity> usdEntities = menaRepository.findByCodeNum(840);
        Assertions.assertEquals(2, usdEntities.size(), "Expected 2 USD entities.");
        Assertions.assertTrue(usdEntities.stream().allMatch(e -> 840 == e.getKodNum()), "All should have kodNum = 840.");
    }

    /**
     * Tests that the custom queries return an empty list if no matching records exist.
     */
    @Test
    public void testFindByCodeNoResults() {
        List<MenaEntity> results = menaRepository.findByCode("ABC");
        Assertions.assertTrue(results.isEmpty(), "Expected no entities for non-existent kod.");
    }

    @Test
    public void testFindByCodeNumNoResults() {
        List<MenaEntity> results = menaRepository.findByCodeNum(999);
        Assertions.assertTrue(results.isEmpty(), "Expected no entities for non-existent kodNum.");
    }
}
