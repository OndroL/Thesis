package RepositoryTests.Common;

import cz.inspire.common.entity.HeaderEntity;
import cz.inspire.common.repository.HeaderRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

@QuarkusTest
public class HeaderRepositoryIT {

    @Inject
    HeaderRepository headerRepository;  // The interface is injected,
    // Quarkus will bind the generated class

    @Test
    public void testSaveAndFindValidAttributes() {
        // Create a new entity
        HeaderEntity entity = new HeaderEntity("ID-001", 123, 10);

        // Save it
        HeaderEntity saved = headerRepository.save(entity);
        Assertions.assertNotNull(saved, "Expected a non-null returned entity from save()");

        // Query
        List<HeaderEntity> results = headerRepository.findValidAttributes();
        Assertions.assertFalse(results.isEmpty(), "Expected to find at least one entity");

        // Verify the saved entity is in the list
        HeaderEntity found = results.stream()
                .filter(e -> "ID-001".equals(e.getId()))
                .findFirst()
                .orElse(null);

        Assertions.assertNotNull(found, "Expected to find entity with ID=ID-001");
        Assertions.assertEquals(123, found.getField());
        Assertions.assertEquals(10, found.getLocation());
    }
}