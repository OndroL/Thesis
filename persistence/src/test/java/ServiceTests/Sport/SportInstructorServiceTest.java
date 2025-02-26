package ServiceTests.Sport;

import cz.inspire.sport.entity.InstructorEntity;
import cz.inspire.sport.entity.SportEntity;
import cz.inspire.sport.entity.SportInstructorEntity;
import cz.inspire.sport.repository.SportInstructorRepository;
import cz.inspire.sport.service.SportInstructorService;
import jakarta.ejb.FinderException;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SportInstructorServiceTest {

    @Mock
    private SportInstructorRepository repository;

    @Mock
    private EntityManager em;

    private SportInstructorService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new SportInstructorService(repository);
        service.setEntityManager(em);
    }

    @Test
    void testFindBySport_Success() throws FinderException {
        String sportId = "sport1";
        List<SportInstructorEntity> expectedEntities = List.of(
                new SportInstructorEntity("1", "activity1", null, false,
                        new SportEntity("sport1", 1, null, null, 10, false, null, false, 1, null, null, null, false, 0, null, null, null, false, 15, 0, 0, 0, 0, null, null, null, null, null, null, null, null, null),
                        null)
        );

        when(repository.findBySport(eq(sportId))).thenReturn(expectedEntities);

        List<SportInstructorEntity> result = service.findBySport(sportId);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository, times(1)).findBySport(eq(sportId));
    }

    @Test
    void testFindBySport_Failure() {
        String sportId = "sport1";

        when(repository.findBySport(eq(sportId))).thenThrow(new RuntimeException("Database error"));

        FinderException exception = assertThrows(FinderException.class, () -> service.findBySport(sportId));
        assertTrue(exception.getMessage().contains("Error retrieving SportInstructorEntity records"));

        verify(repository, times(1)).findBySport(eq(sportId));
    }

    @Test
    void testFindByInstructor_Success() throws FinderException {
        String instructorId = "instructor1";
        List<SportInstructorEntity> expectedEntities = List.of(
                new SportInstructorEntity("1", "activity1", null, false, null,
                        new InstructorEntity("instructor1", "John", "Doe", 1, "john@example.com", "+1", "123456789",
                                "john.internal@example.com", "+1", "987654321", "Info", "Blue", null,
                                false, "calendar100", true, 15, null, null, null))
        );

        when(repository.findByInstructor(eq(instructorId))).thenReturn(expectedEntities);

        List<SportInstructorEntity> result = service.findByInstructor(instructorId);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository, times(1)).findByInstructor(eq(instructorId));
    }

    @Test
    void testFindByInstructor_Failure() {
        String instructorId = "instructor1";

        when(repository.findByInstructor(eq(instructorId))).thenThrow(new RuntimeException("Database error"));

        FinderException exception = assertThrows(FinderException.class, () -> service.findByInstructor(instructorId));
        assertTrue(exception.getMessage().contains("Error retrieving SportInstructorEntity records"));

        verify(repository, times(1)).findByInstructor(eq(instructorId));
    }

    @Test
    void testFindBySportAndInstructor_Success() throws FinderException {
        String sportId = "sport1";
        String instructorId = "instructor1";
        SportInstructorEntity expectedEntity = new SportInstructorEntity("1", "activity1", null, false,
                new SportEntity("sport1", 1, null, null, 10, false, null, false, 1, null, null, null, false, 0, null, null, null, false, 15, 0, 0, 0, 0, null, null, null, null, null, null, null, null, null),
                new InstructorEntity("instructor1", "John", "Doe", 1, "john@example.com", "+1", "123456789",
                        "john.internal@example.com", "+1", "987654321", "Info", "Blue", null,
                        false, "calendar100", true, 15, null, null, null));

        when(repository.findBySportAndInstructor(eq(sportId), eq(instructorId))).thenReturn(Optional.of(expectedEntity));

        SportInstructorEntity result = service.findBySportAndInstructor(sportId, instructorId);

        assertNotNull(result);
        verify(repository, times(1)).findBySportAndInstructor(eq(sportId), eq(instructorId));
    }

    @Test
    void testFindBySportAndInstructor_Failure() {
        String sportId = "sport1";
        String instructorId = "instructor1";

        when(repository.findBySportAndInstructor(eq(sportId), eq(instructorId))).thenThrow(new RuntimeException("Database error"));

        FinderException exception = assertThrows(FinderException.class, () -> service.findBySportAndInstructor(sportId, instructorId));
        assertTrue(exception.getMessage().contains("Error retrieving SportInstructorEntity record"));

        verify(repository, times(1)).findBySportAndInstructor(eq(sportId), eq(instructorId));
    }

    @Test
    void testCountSportInstructors_Success() throws FinderException {
        String sportId = "sport1";
        when(repository.countSportInstructors(eq(sportId))).thenReturn(10L);

        Long result = service.countSportInstructors(sportId);

        assertNotNull(result);
        assertEquals(10L, result);
        verify(repository, times(1)).countSportInstructors(eq(sportId));
    }

    @Test
    void testCountSportInstructors_Failure() {
        String sportId = "sport1";

        when(repository.countSportInstructors(eq(sportId))).thenThrow(new RuntimeException("Database error"));

        FinderException exception = assertThrows(FinderException.class, () -> service.countSportInstructors(sportId));
        assertTrue(exception.getMessage().contains("Error retrieving count of SportInstructorEntity records"));

        verify(repository, times(1)).countSportInstructors(eq(sportId));
    }

    @Test
    void testFindBySportWithoutInstructor_Success() throws FinderException {
        String sportId = "sport1";
        SportInstructorEntity expectedEntity = new SportInstructorEntity("1", "activity1", null, false,
                new SportEntity("sport1", 1, null, null, 10, false, null, false, 1, null, null, null, false, 0, null, null, null, false, 15, 0, 0, 0, 0, null, null, null, null, null, null, null, null, null),
                null);

        when(repository.findBySportWithoutInstructor(eq(sportId))).thenReturn(Optional.of(expectedEntity));

        SportInstructorEntity result = service.findBySportWithoutInstructor(sportId);

        assertNotNull(result);
        verify(repository, times(1)).findBySportWithoutInstructor(eq(sportId));
    }

    @Test
    void testFindBySportWithoutInstructor_Failure() {
        String sportId = "sport1";

        when(repository.findBySportWithoutInstructor(eq(sportId))).thenThrow(new RuntimeException("Database error"));

        FinderException exception = assertThrows(FinderException.class, () -> service.findBySportWithoutInstructor(sportId));
        assertTrue(exception.getMessage().contains("Error retrieving SportInstructorEntity record"));

        verify(repository, times(1)).findBySportWithoutInstructor(eq(sportId));
    }

    @Test
    void testFindByActivity_Success() throws FinderException {
        String activityId = "activity1";
        List<SportInstructorEntity> expectedEntities = List.of(
                new SportInstructorEntity("1", activityId, null, false,
                        new SportEntity("sport1", 1, null, null, 10, false, null, false, 1, null, null, null, false, 0, null, null, null, false, 15, 0, 0, 0, 0, null, null, null, null, null, null, null, null, null),
                        null)
        );

        when(repository.findByActivity(eq(activityId))).thenReturn(expectedEntities);

        List<SportInstructorEntity> result = service.findByActivity(activityId);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository, times(1)).findByActivity(eq(activityId));
    }

    @Test
    void testFindByActivity_Failure() {
        String activityId = "activity1";

        when(repository.findByActivity(eq(activityId))).thenThrow(new RuntimeException("Database error"));

        FinderException exception = assertThrows(FinderException.class, () -> service.findByActivity(activityId));
        assertTrue(exception.getMessage().contains("Error retrieving SportInstructorEntity records"));

        verify(repository, times(1)).findByActivity(eq(activityId));
    }

}
