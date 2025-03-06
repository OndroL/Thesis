package ServiceTests.Sport;

import cz.inspire.sport.entity.ActivityWebTabEntity;
import cz.inspire.sport.repository.ActivityWebTabRepository;
import cz.inspire.sport.service.ActivityWebTabService;
import jakarta.ejb.FinderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ActivityWebTabServiceTest {

    @Mock
    private ActivityWebTabRepository activityWebTabRepository;

    private ActivityWebTabService activityWebTabService;

    @BeforeEach
    void setUp() {
        activityWebTabService = new ActivityWebTabService(activityWebTabRepository);
    }

    @Test
    void testFindBySport_Success() throws FinderException {
        String sportId = "sport123";
        List<ActivityWebTabEntity> expectedEntities = List.of(
                new ActivityWebTabEntity("1", sportId, "activity1", "object1", 1),
                new ActivityWebTabEntity("2", sportId, "activity2", "object2", 2)
        );
        when(activityWebTabRepository.findBySport(eq(sportId))).thenReturn(expectedEntities);

        List<ActivityWebTabEntity> result = activityWebTabService.findBySport(sportId);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(activityWebTabRepository, times(1)).findBySport(eq(sportId));
    }

    @Test
    void testFindBySport_Failure() {
        String sportId = "sport123";
        when(activityWebTabRepository.findBySport(eq(sportId))).thenThrow(new RuntimeException("Database error"));

        FinderException exception = assertThrows(FinderException.class, () -> activityWebTabService.findBySport(sportId));
        assertTrue(exception.getMessage().contains("Error retrieving ActivityWebTabEntity records"));

        verify(activityWebTabRepository, times(1)).findBySport(eq(sportId));
    }

    @Test
    void testFindByActivity_Success() throws FinderException {
        String activityId = "activity123";
        List<ActivityWebTabEntity> expectedEntities = List.of(
                new ActivityWebTabEntity("1", "sport1", activityId, "object1", 1)
        );
        when(activityWebTabRepository.findByActivity(eq(activityId))).thenReturn(expectedEntities);

        List<ActivityWebTabEntity> result = activityWebTabService.findByActivity(activityId);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(activityWebTabRepository, times(1)).findByActivity(eq(activityId));
    }

    @Test
    void testFindByActivity_Failure() {
        String activityId = "activity123";
        when(activityWebTabRepository.findByActivity(eq(activityId))).thenThrow(new RuntimeException("Database error"));

        FinderException exception = assertThrows(FinderException.class, () -> activityWebTabService.findByActivity(activityId));
        assertTrue(exception.getMessage().contains("Error retrieving ActivityWebTabEntity records"));

        verify(activityWebTabRepository, times(1)).findByActivity(eq(activityId));
    }

    @Test
    void testFindByObject_Success() throws FinderException {
        String objectId = "object123";
        List<ActivityWebTabEntity> expectedEntities = List.of(
                new ActivityWebTabEntity("1", "sport1", "activity1", objectId, 1)
        );
        when(activityWebTabRepository.findByObject(eq(objectId))).thenReturn(expectedEntities);

        List<ActivityWebTabEntity> result = activityWebTabService.findByObject(objectId);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(activityWebTabRepository, times(1)).findByObject(eq(objectId));
    }

    @Test
    void testFindByObject_Failure() {
        String objectId = "object123";
        when(activityWebTabRepository.findByObject(eq(objectId))).thenThrow(new RuntimeException("Database error"));

        FinderException exception = assertThrows(FinderException.class, () -> activityWebTabService.findByObject(objectId));
        assertTrue(exception.getMessage().contains("Error retrieving ActivityWebTabEntity records"));

        verify(activityWebTabRepository, times(1)).findByObject(eq(objectId));
    }
}
