package RepositoryTests.Sport;

import cz.inspire.sport.entity.ArealLocEntity;
import cz.inspire.sport.repository.ArealLocRepository;
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
public class ArealLocRepositoryIT {

    @Inject
    ArealLocRepository arealLocRepository;

    @BeforeAll
    @ActivateRequestContext
    public void clearDatabase() {
        List<ArealLocEntity> allEntities = new ArrayList<>(arealLocRepository.findAll());
        if (!allEntities.isEmpty()) {
            arealLocRepository.deleteAll(allEntities);
        }
    }

    @Test
    public void testSaveAndFindById() {
        // Pass null for the ID so it gets generated automatically.
        ArealLocEntity entity = new ArealLocEntity(null, "en", "Sports Area", "A great place");
        entity = arealLocRepository.create(entity);
        String generatedId = entity.getId();
        Assertions.assertNotNull(generatedId, "Generated ID should not be null");

        ArealLocEntity retrieved = arealLocRepository.findByPrimaryKey(generatedId);
        Assertions.assertNotNull(retrieved, "Entity should be present in repository.");
        Assertions.assertEquals("en", retrieved.getJazyk(), "Language should match.");
        Assertions.assertEquals("Sports Area", retrieved.getNazev(), "Name should match.");
        Assertions.assertEquals("A great place", retrieved.getPopis(), "Description should match.");
    }

    @Test
    public void testUpdateEntity() {
        ArealLocEntity entity = new ArealLocEntity(null, "cs", "Sportovní areál", "Skvělé místo");
        entity = arealLocRepository.create(entity);
        String generatedId = entity.getId();
        Assertions.assertNotNull(generatedId, "Generated ID should not be null");

        entity.setNazev("Updated Sportovní areál");
        entity.setPopis("Updated description");
        arealLocRepository.create(entity);

        ArealLocEntity updated = arealLocRepository.findByPrimaryKey(generatedId);
        Assertions.assertNotNull(updated, "Updated entity should be found.");
        Assertions.assertEquals("Updated Sportovní areál", updated.getNazev(), "Name should be updated.");
        Assertions.assertEquals("Updated description", updated.getPopis(), "Description should be updated.");
    }

    @Test
    public void testDeleteEntity() {
        ArealLocEntity entity = new ArealLocEntity(null, "de", "Sportplatz", "Toller Ort");
        entity = arealLocRepository.create(entity);
        String generatedId = entity.getId();
        Assertions.assertNotNull(generatedId, "Generated ID should not be null");

        arealLocRepository.deleteByPrimaryKey(generatedId);
        ArealLocEntity deleted = arealLocRepository.findByPrimaryKey(generatedId);
        Assertions.assertNull(deleted, "Entity should be deleted from repository.");
    }
}
