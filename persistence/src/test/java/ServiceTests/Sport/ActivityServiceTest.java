package ServiceTests.Sport;

import cz.inspire.sport.entity.ActivityEntity;
import cz.inspire.sport.repository.ActivityRepository;
import cz.inspire.sport.service.ActivityService;
import cz.inspire.exception.SystemException;
import jakarta.ejb.CreateException;
import jakarta.ejb.FinderException;
import jakarta.ejb.RemoveException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ActivityServiceTest {

    @Mock
    private ActivityRepository activityRepository;

    private ActivityService activityService;

    @BeforeEach
    void setUp() {
        activityService = new ActivityService(activityRepository);
    }

    @Test
    void testCreate_Success() throws CreateException {
        ActivityEntity entity = new ActivityEntity("1", "Yoga", "Relaxing activity", 1, "icon1", null, null);
        when(activityRepository.create(entity)).thenReturn(entity);

        ActivityEntity result = activityService.create(entity);

        assertEquals(entity, result);
        verify(activityRepository, times(1)).create(entity);
    }

    @Test
    void testCreate_Failure() {
        ActivityEntity entity = new ActivityEntity("1", "Yoga", "Relaxing activity", 1, "icon1", null, null);
        when(activityRepository.create(entity)).thenThrow(new RuntimeException("DB error"));

        CreateException exception = assertThrows(CreateException.class, () -> activityService.create(entity));
        assertEquals("Failed to create ActivityEntity", exception.getMessage());
        verify(activityRepository, times(1)).create(entity);
    }

    @Test
    void testUpdate_Success() throws SystemException {
        ActivityEntity entity = new ActivityEntity("1", "Yoga", "Relaxing activity", 1, "icon1", null, null);
        when(activityRepository.update(entity)).thenReturn(entity);

        ActivityEntity result = activityService.update(entity);

        assertEquals(entity, result);
        verify(activityRepository, times(1)).update(entity);
    }

    @Test
    void testUpdate_Failure() {
        ActivityEntity entity = new ActivityEntity("1", "Yoga", "Relaxing activity", 1, "icon1", null, null);
        when(activityRepository.update(entity)).thenThrow(new RuntimeException("DB error"));

        SystemException exception = assertThrows(SystemException.class, () -> activityService.update(entity));
        assertEquals("Failed to update ActivityEntity", exception.getMessage());
        verify(activityRepository, times(1)).update(entity);
    }

    @Test
    void testDelete_Success() throws RemoveException {
        ActivityEntity entity = new ActivityEntity("1", "Yoga", "Relaxing activity", 1, "icon1", null, null);
        doNothing().when(activityRepository).delete(entity);

        activityService.delete(entity);

        verify(activityRepository, times(1)).delete(entity);
    }

    @Test
    void testDelete_Failure() {
        ActivityEntity entity = new ActivityEntity("1", "Yoga", "Relaxing activity", 1, "icon1", null, null);
        doThrow(new RuntimeException("DB error")).when(activityRepository).delete(entity);

        RemoveException exception = assertThrows(RemoveException.class, () -> activityService.delete(entity));
        assertEquals("Failed to remove ActivityEntity", exception.getMessage());
        verify(activityRepository, times(1)).delete(entity);
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
        assertTrue(exception.getMessage().contains("Error retrieving all ActivityEntity, Ordered by index"));

        verify(activityRepository, times(1)).findAllOrdered();
    }

    @Test
    void testFindAll_Paginated_Success() throws FinderException {
        List<ActivityEntity> expectedEntities = List.of(
                new ActivityEntity("1", "Yoga", "Relaxing activity", 1, "icon1", null, null)
        );
        when(activityRepository.findAll(anyInt(), anyInt())).thenReturn(expectedEntities);

        List<ActivityEntity> result = activityService.findAll(0, 10);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(activityRepository, times(1)).findAll(anyInt(), anyInt());
    }

    @Test
    void testFindAllByInstructor_Success() throws FinderException {
        String instructorId = "instructor123";
        List<ActivityEntity> expectedEntities = List.of(
                new ActivityEntity("1", "Yoga", "Relaxing activity", 1, "icon1", null, null)
        );
        when(activityRepository.findAllByInstructor(eq(instructorId), anyInt(), anyInt())).thenReturn(expectedEntities);

        List<ActivityEntity> result = activityService.findAllByInstructor(instructorId, 0, 10);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(activityRepository, times(1)).findAllByInstructor(eq(instructorId), anyInt(), anyInt());
    }

    @Test
    void testFindAllByInstructor_Failure() {
        String instructorId = "instructor123";
        when(activityRepository.findAllByInstructor(eq(instructorId), anyInt(), anyInt()))
                .thenThrow(new RuntimeException("Database error"));

        FinderException exception = assertThrows(FinderException.class, () -> activityService.findAllByInstructor(instructorId, 0, 10));
        assertTrue(exception.getMessage().contains("Error retrieving all paginated ActivityEntity records"));

        verify(activityRepository, times(1)).findAllByInstructor(eq(instructorId), anyInt(), anyInt());
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
