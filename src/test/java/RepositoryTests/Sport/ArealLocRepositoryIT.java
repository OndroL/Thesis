package RepositoryTests.Sport;

import cz.inspire.sport.entity.ArealLocEntity;
import cz.inspire.sport.repository.ArealLocRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional
@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ArealLocRepositoryIT {

    @Inject
    ArealLocRepository arealLocRepository;

    @BeforeAll
    @ActivateRequestContext
    public void clearDatabase() {
        List<ArealLocEntity> allEntities = new ArrayList<>();
        arealLocRepository.findAll().forEach(allEntities::add);
        if (!allEntities.isEmpty()) {
            arealLocRepository.deleteAll(allEntities);
        }
    }

    @Test
    public void testSaveAndFindById() {
        ArealLocEntity entity = new ArealLocEntity("LOC-001", "en", "Sports Area", "A great place");
        arealLocRepository.save(entity);

        Optional<ArealLocEntity> retrieved = arealLocRepository.findById("LOC-001");
        Assertions.assertTrue(retrieved.isPresent(), "Entity should be present in repository.");
        Assertions.assertEquals("en", retrieved.get().getJazyk(), "Language should match.");
        Assertions.assertEquals("Sports Area", retrieved.get().getNazev(), "Name should match.");
        Assertions.assertEquals("A great place", retrieved.get().getPopis(), "Description should match.");
    }

    @Test
    public void testUpdateEntity() {
        ArealLocEntity entity = new ArealLocEntity("LOC-002", "cs", "Sportovní areál", "Skvělé místo");
        arealLocRepository.save(entity);

        entity.setNazev("Updated Sportovní areál");
        entity.setPopis("Updated description");
        arealLocRepository.save(entity);

        Optional<ArealLocEntity> updated = arealLocRepository.findById("LOC-002");
        Assertions.assertTrue(updated.isPresent(), "Updated entity should be found.");
        Assertions.assertEquals("Updated Sportovní areál", updated.get().getNazev(), "Name should be updated.");
        Assertions.assertEquals("Updated description", updated.get().getPopis(), "Description should be updated.");
    }

    @Test
    public void testDeleteEntity() {
        ArealLocEntity entity = new ArealLocEntity("LOC-003", "de", "Sportplatz", "Toller Ort");
        arealLocRepository.save(entity);

        arealLocRepository.deleteById("LOC-003");
        Optional<ArealLocEntity> deleted = arealLocRepository.findById("LOC-003");
        Assertions.assertFalse(deleted.isPresent(), "Entity should be deleted from repository.");
    }
}
