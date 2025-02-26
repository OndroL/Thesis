package RepositoryTests.Sport;

import RepositoryTests.DatabaseCleaner;
import cz.inspire.sport.entity.*;
import cz.inspire.sport.repository.SportInstructorRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Optional;

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

    private SportEntity createSport(String id) {
        return new SportEntity(id, 1, "ZB-001", "SK-001", 100, true, 60, true, 10, null, 30, 120, true, 15, null, null, true, true, 10, 90, 1, 5, 20, null, null, null, null, null, null, null, null, null);
    }

    private InstructorEntity createInstructor(String id) {
        return new InstructorEntity(id, "John", "Doe", 1, "john.doe@example.com", "+420", "123456789", "internal@example.com", "+420", "987654321", "Info", "Blue", null, false, null, false, 30, null, null, null);
    }

    private SportInstructorEntity createSportInstructor(String id, SportEntity sport, InstructorEntity instructor) {
        return new SportInstructorEntity(id, "Activity-001", "OldSport-001", false, sport, instructor);
    }

    @Test
    @Order(1)
    void testFindBySport() {
        SportEntity sport = createSport(null);
        InstructorEntity instructor = createInstructor(null);
        SportInstructorEntity entity = createSportInstructor(null, sport, instructor);

        em.persist(sport);
        em.persist(instructor);
        em.persist(entity);
        em.flush();

        List<SportInstructorEntity> result = sportInstructorRepository.findBySport(sport.getId());

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @Order(2)
    void testFindByInstructor() {
        SportEntity sport = createSport(null);
        InstructorEntity instructor = createInstructor(null);
        SportInstructorEntity entity = createSportInstructor(null, sport, instructor);

        em.persist(sport);
        em.persist(instructor);
        em.persist(entity);
        em.flush();

        List<SportInstructorEntity> result = sportInstructorRepository.findByInstructor(instructor.getId());

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @Order(3)
    void testFindBySportAndInstructor() {
        SportEntity sport = createSport(null);
        InstructorEntity instructor = createInstructor(null);
        SportInstructorEntity entity = createSportInstructor(null, sport, instructor);

        em.persist(sport);
        em.persist(instructor);
        em.persist(entity);
        em.flush();

        Optional<SportInstructorEntity> result = sportInstructorRepository.findBySportAndInstructor(sport.getId(), instructor.getId());

        assertTrue(result.isPresent());
    }

    @Test
    @Order(4)
    void testFindBySportWithoutInstructor() {
        SportEntity sport = createSport(null);
        SportInstructorEntity entity = new SportInstructorEntity(null, "Activity-004", "OldSport-004", false, sport, null);

        em.persist(sport);
        em.persist(entity);
        em.flush();

        Optional<SportInstructorEntity> result = sportInstructorRepository.findBySportWithoutInstructor(sport.getId());

        assertTrue(result.isPresent());
    }

    @Test
    @Order(5)
    void testFindByActivity() {
        SportEntity sport = createSport(null);
        InstructorEntity instructor = createInstructor(null);
        SportInstructorEntity entity = createSportInstructor(null, sport, instructor);

        em.persist(sport);
        em.persist(instructor);
        em.persist(entity);
        em.flush();

        List<SportInstructorEntity> result = sportInstructorRepository.findByActivity("Activity-001");

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @Order(6)
    void testCountSportInstructors() {
        SportEntity sport = createSport(null);
        InstructorEntity instructor = createInstructor(null);
        SportInstructorEntity entity = createSportInstructor(null, sport, instructor);

        em.persist(sport);
        em.persist(instructor);
        em.persist(entity);
        em.flush();

        Long count = sportInstructorRepository.countSportInstructors(sport.getId());

        assertEquals(1, count);
    }
}
