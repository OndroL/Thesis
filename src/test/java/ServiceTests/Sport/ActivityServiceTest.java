package ServiceTests.Sport;

import cz.inspire.sport.entity.ActivityEntity;
import cz.inspire.sport.repository.ActivityRepository;
import cz.inspire.sport.service.ActivityService;
import jakarta.ejb.FinderException;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ActivityServiceTest {

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private EntityManager em;

    private ActivityService activityService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        activityService = new ActivityService(activityRepository);
        activityService.setEntityManager(em);
    }

    @Test
    void testFindAll_Success() throws FinderException {
        List<ActivityEntity> expectedEntities = List.of(
                new ActivityEntity("1", "Yoga", "Relaxing activity", 1, "icon1", null, null),
                new ActivityEntity("2", "Pilates", "Core training", 2, "icon2", null, null)
        );

        when(activityRepository.findAllOrdered()).thenReturn(expectedEntities);

        List<ActivityEntity> result = activityService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(activityRepository, times(1)).findAllOrdered();
    }

    @Test
    void testFindAll_Failure() {
        when(activityRepository.findAllOrdered()).thenThrow(new RuntimeException("Database error"));

        FinderException exception = assertThrows(FinderException.class, () -> activityService.findAll());
        assertTrue(exception.getMessage().contains("Error retrieving all ActivityEntity records"));

        verify(activityRepository, times(1)).findAllOrdered();
    }

    @Test
    void testFindAll_Paginated_Success() throws FinderException {
        List<ActivityEntity> expectedEntities = List.of(
                new ActivityEntity("1", "Yoga", "Relaxing activity", 1, "icon1", null, null)
        );

        when(activityRepository.findAll(any())).thenReturn(expectedEntities);

        List<ActivityEntity> result = activityService.findAll(0, 10);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(activityRepository, times(1)).findAll(any());
    }

    @Test
    void testFindAllByInstructor_Success() throws FinderException {
        String instructorId = "instructor123";
        List<ActivityEntity> expectedEntities = List.of(
                new ActivityEntity("1", "Yoga", "Relaxing activity", 1, "icon1", null, null)
        );

        when(activityRepository.findAllByInstructor(eq(instructorId), any())).thenReturn(expectedEntities);

        List<ActivityEntity> result = activityService.findAllByInstructor(instructorId, 0, 10);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(activityRepository, times(1)).findAllByInstructor(eq(instructorId), any());
    }

    @Test
    void testFindAllByInstructor_Failure() {
        String instructorId = "instructor123";

        when(activityRepository.findAllByInstructor(eq(instructorId), any())).thenThrow(new RuntimeException("Database error"));

        FinderException exception = assertThrows(FinderException.class, () -> activityService.findAllByInstructor(instructorId, 0, 10));
        assertTrue(exception.getMessage().contains("Error retrieving all paginated ActivityEntity records"));

        verify(activityRepository, times(1)).findAllByInstructor(eq(instructorId), any());
    }

    @Test
    void testCountActivities_Success() throws FinderException {
        when(activityRepository.countActivities()).thenReturn(42L);

        Long result = activityService.countActivities();

        assertNotNull(result);
        assertEquals(42L, result);
        verify(activityRepository, times(1)).countActivities();
    }

    @Test
    void testCountActivities_Failure() {
        when(activityRepository.countActivities()).thenThrow(new RuntimeException("Database error"));

        FinderException exception = assertThrows(FinderException.class, () -> activityService.countActivities());
        assertTrue(exception.getMessage().contains("Error retrieving total count of ActivityEntity records"));

        verify(activityRepository, times(1)).countActivities();
    }

    @Test
    void testCountActivitiesByInstructor_Success() throws FinderException {
        String instructorId = "instructor123";

        when(activityRepository.countActivitiesByInstructor(instructorId)).thenReturn(10L);

        Long result = activityService.countActivitiesByInstructor(instructorId);

        assertNotNull(result);
        assertEquals(10L, result);
        verify(activityRepository, times(1)).countActivitiesByInstructor(instructorId);
    }

    @Test
    void testCountActivitiesByInstructor_Failure() {
        String instructorId = "instructor123";

        when(activityRepository.countActivitiesByInstructor(instructorId)).thenThrow(new RuntimeException("Database error"));

        FinderException exception = assertThrows(FinderException.class, () -> activityService.countActivitiesByInstructor(instructorId));
        assertTrue(exception.getMessage().contains("Error retrieving count of ActivityEntity records"));

        verify(activityRepository, times(1)).countActivitiesByInstructor(instructorId);
    }
}
