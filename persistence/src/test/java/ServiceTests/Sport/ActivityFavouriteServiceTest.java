package ServiceTests.Sport;

import cz.inspire.exception.SystemException;
import cz.inspire.sport.entity.ActivityFavouriteEntity;
import cz.inspire.sport.repository.ActivityFavouriteRepository;
import cz.inspire.sport.service.ActivityFavouriteService;
import jakarta.ejb.FinderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ActivityFavouriteServiceTest {

    @Mock
    private ActivityFavouriteRepository activityFavouriteRepository;

    private ActivityFavouriteService activityFavouriteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        activityFavouriteService = new ActivityFavouriteService(activityFavouriteRepository);
    }

    @Test
    void testFindByZakaznik_Success() throws FinderException {
        String zakaznikId = "customer123";
        int offset = 0, count = 10;
        List<ActivityFavouriteEntity> expectedEntities = List.of(
                new ActivityFavouriteEntity("1", zakaznikId, "activity1", 5, LocalDateTime.now()),
                new ActivityFavouriteEntity("2", zakaznikId, "activity2", 3, LocalDateTime.now())
        );
        when(activityFavouriteRepository.findByZakaznik(eq(zakaznikId), eq(count), eq(offset))).thenReturn(expectedEntities);

        List<ActivityFavouriteEntity> result = activityFavouriteService.findByZakaznik(zakaznikId, offset, count);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(activityFavouriteRepository, times(1)).findByZakaznik(eq(zakaznikId), eq(count), eq(offset));
    }

    @Test
    void testFindByZakaznik_Failure() {
        String zakaznikId = "customer123";
        when(activityFavouriteRepository.findByZakaznik(eq(zakaznikId), anyInt(), anyInt()))
                .thenThrow(new RuntimeException("Database error"));

        FinderException exception = assertThrows(FinderException.class,
                () -> activityFavouriteService.findByZakaznik(zakaznikId, 0, 10));
        assertTrue(exception.getMessage().contains("Error retrieving ActivityFavouriteEntity records"));

        verify(activityFavouriteRepository, times(1)).findByZakaznik(eq(zakaznikId), anyInt(), anyInt());
    }

    @Test
    void testFindByZakaznikAktivita_Success() throws FinderException {
        String zakaznikId = "customer123";
        String activityId = "activity456";
        ActivityFavouriteEntity entity = new ActivityFavouriteEntity("1", zakaznikId, activityId, 2, LocalDateTime.now());

        when(activityFavouriteRepository.findByZakaznikAktivita(zakaznikId, activityId)).thenReturn(Optional.of(entity));

        ActivityFavouriteEntity result = activityFavouriteService.findByZakaznikAktivita(zakaznikId, activityId);

        assertNotNull(result);
        assertEquals(activityId, result.getActivityId());
        verify(activityFavouriteRepository, times(1)).findByZakaznikAktivita(zakaznikId, activityId);
    }

    @Test
    void testFindByZakaznikAktivita_NotFound() {
        String zakaznikId = "customer123";
        String activityId = "activity456";

        when(activityFavouriteRepository.findByZakaznikAktivita(zakaznikId, activityId)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> activityFavouriteService.findByZakaznikAktivita(zakaznikId, activityId));
        verify(activityFavouriteRepository, times(1)).findByZakaznikAktivita(zakaznikId, activityId);
    }

    @Test
    void testCreate_Success() throws Exception {
        ActivityFavouriteEntity entity = new ActivityFavouriteEntity("1", "customer123", "activity456", 2, LocalDateTime.now());
        when(activityFavouriteRepository.create(entity)).thenReturn(entity);

        ActivityFavouriteEntity result = activityFavouriteService.create(entity);

        assertEquals(entity, result);
        verify(activityFavouriteRepository, times(1)).create(entity);
    }

    @Test
    void testCreate_Failure() {
        ActivityFavouriteEntity entity = new ActivityFavouriteEntity("1", "customer123", "activity456", 2, LocalDateTime.now());
        when(activityFavouriteRepository.create(entity)).thenThrow(new RuntimeException("DB error"));

        assertThrows(Exception.class, () -> activityFavouriteService.create(entity));
        verify(activityFavouriteRepository, times(1)).create(entity);
    }

    @Test
    void testUpdate_Success() throws SystemException {
        ActivityFavouriteEntity entity = new ActivityFavouriteEntity("1", "customer123", "activity456", 2, LocalDateTime.now());
        when(activityFavouriteRepository.update(entity)).thenReturn(entity);

        ActivityFavouriteEntity result = activityFavouriteService.update(entity);

        assertEquals(entity, result);
        verify(activityFavouriteRepository, times(1)).update(entity);
    }

    @Test
    void testUpdate_Failure() {
        ActivityFavouriteEntity entity = new ActivityFavouriteEntity("1", "customer123", "activity456", 2, LocalDateTime.now());
        when(activityFavouriteRepository.update(entity)).thenThrow(new RuntimeException("DB error"));

        assertThrows(Exception.class, () -> activityFavouriteService.update(entity));
        verify(activityFavouriteRepository, times(1)).update(entity);
    }

    @Test
    void testRemove_Success() {
        ActivityFavouriteEntity entity = new ActivityFavouriteEntity("1", "customer123", "activity456", 2, LocalDateTime.now());
        doNothing().when(activityFavouriteRepository).delete(entity);

        assertDoesNotThrow(() -> activityFavouriteService.delete(entity));
        verify(activityFavouriteRepository, times(1)).delete(entity);
    }

    @Test
    void testRemove_Failure() {
        ActivityFavouriteEntity entity = new ActivityFavouriteEntity("1", "customer123", "activity456", 2, LocalDateTime.now());
        doThrow(new RuntimeException("DB error")).when(activityFavouriteRepository).delete(entity);

        assertThrows(Exception.class, () -> activityFavouriteService.delete(entity));
        verify(activityFavouriteRepository, times(1)).delete(entity);
    }
}
