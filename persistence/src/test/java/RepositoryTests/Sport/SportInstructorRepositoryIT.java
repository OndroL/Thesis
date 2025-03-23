package RepositoryTests.Sport;

import RepositoryTests.DatabaseCleaner;
import cz.inspire.sport.entity.InstructorEntity;
import cz.inspire.sport.entity.SportEntity;
import cz.inspire.sport.entity.SportInstructorEntity;
import cz.inspire.sport.repository.SportInstructorRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SportInstructorRepositoryIT {

    @Inject
    SportInstructorRepository sportInstructorRepository;

    @Inject
    DatabaseCleaner databaseCleaner;

    @Inject
    EntityManager em;

    @BeforeAll
    @ActivateRequestContext
    public void clearDatabase() {
        databaseCleaner.clearTable(SportInstructorEntity.class, true);
        databaseCleaner.clearTable(SportEntity.class, true);
        databaseCleaner.clearTable(InstructorEntity.class, true);
    }

    // Helper method: pass null for id so that it is generated.
    private SportEntity createSport(String id) {
        return new SportEntity(id, 1, "ZB-001", "SK-001", 100, true, 60, true, 10, null, 30, 120, true, 15, null, null, true, true, 10, 90, 1, 5, 20, null, null, null, null, null, null, null, null, null);
    }

    // Helper method: pass null for id.
    private InstructorEntity createInstructor(String id) {
        return new InstructorEntity(id, "John", "Doe", 1, "john.doe@example.com", "+420", "123456789", "internal@example.com", "+420", "987654321", "Info", "Blue", null, false, null, false, 30, null, null, null);
    }

    // Helper method: pass null for id.
    private SportInstructorEntity createSportInstructor(String id, SportEntity sport, InstructorEntity instructor) {
        return new SportInstructorEntity(id, "Activity-001", "OldSport-001", false, sport, instructor);
    }

    @Order(1)
    @Test
    void testFindBySport() {
        SportEntity sport = createSport(null);
        InstructorEntity instructor = createInstructor(null);
        SportInstructorEntity entity = createSportInstructor(null, sport, instructor);

        em.persist(sport);
        em.persist(instructor);
        entity = sportInstructorRepository.create(entity);

        List<SportInstructorEntity> result = sportInstructorRepository.findBySport(sport.getId());
        assertNotNull(result, "Result should not be null.");
        assertEquals(1, result.size(), "Expected one result.");
    }

    @Order(2)
    @Test
    void testFindByInstructor() {
        SportEntity sport = createSport(null);
        InstructorEntity instructor = createInstructor(null);
        SportInstructorEntity entity = createSportInstructor(null, sport, instructor);

        em.persist(sport);
        em.persist(instructor);
        entity = sportInstructorRepository.create(entity);

        List<SportInstructorEntity> result = sportInstructorRepository.findByInstructor(instructor.getId());
        assertNotNull(result, "Result should not be null.");
        assertEquals(1, result.size(), "Expected one result.");
    }

    @Order(3)
    @Test
    void testFindBySportAndInstructor() {
        SportEntity sport = createSport(null);
        InstructorEntity instructor = createInstructor(null);
        SportInstructorEntity entity = createSportInstructor(null, sport, instructor);

        em.persist(sport);
        em.persist(instructor);
        entity = sportInstructorRepository.create(entity);

        // Assume findBySportAndInstructor now returns the entity directly (or null)
        SportInstructorEntity result = sportInstructorRepository.findBySportAndInstructor(sport.getId(), instructor.getId());
        assertNotNull(result, "Entity should be found.");
    }

    @Order(4)
    @Test
    void testFindBySportWithoutInstructor() {
        SportEntity sport = createSport(null);
        // Create a SportInstructorEntity with a null instructor.
        SportInstructorEntity entity = new SportInstructorEntity(null, "Activity-004", "OldSport-004", false, sport, null);

        em.persist(sport);
        entity = sportInstructorRepository.create(entity);

        SportInstructorEntity result = sportInstructorRepository.findBySportWithoutInstructor(sport.getId());
        assertNotNull(result, "Entity should be found.");
    }

    @Order(5)
    @Test
    void testFindByActivity() {
        SportEntity sport = createSport(null);
        InstructorEntity instructor = createInstructor(null);
        SportInstructorEntity entity = createSportInstructor(null, sport, instructor);

        em.persist(sport);
        em.persist(instructor);
        entity = sportInstructorRepository.create(entity);

        List<SportInstructorEntity> result = sportInstructorRepository.findByActivity("Activity-001");
        assertNotNull(result, "Result should not be null.");
        assertEquals(1, result.size(), "Expected one result.");
    }

    @Order(6)
    @Test
    void testCountSportInstructors() {
        SportEntity sport = createSport(null);
        InstructorEntity instructor = createInstructor(null);
        SportInstructorEntity entity = createSportInstructor(null, sport, instructor);

        em.persist(sport);
        em.persist(instructor);
        entity = sportInstructorRepository.create(entity);

        Long count = sportInstructorRepository.countSportInstructors(sport.getId());
        assertEquals(1, count, "Expected count to be 1.");
    }
}
