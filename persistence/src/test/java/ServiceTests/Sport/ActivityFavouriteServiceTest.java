package ServiceTests.Sport;

import cz.inspire.sport.entity.ActivityFavouriteEntity;
import cz.inspire.sport.repository.ActivityFavouriteRepository;
import cz.inspire.sport.service.ActivityFavouriteService;
import jakarta.ejb.FinderException;
import jakarta.persistence.EntityManager;
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

    @Mock
    private EntityManager em;

    private ActivityFavouriteService activityFavouriteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        activityFavouriteService = new ActivityFavouriteService(activityFavouriteRepository);
        activityFavouriteService.setEntityManager(em);
    }

    @Test
    void testFindByZakaznik_Success() throws FinderException {
        String zakaznikId = "customer123";
        int offset = 0, count = 10;
        List<ActivityFavouriteEntity> expectedEntities = List.of(
                new ActivityFavouriteEntity("1", zakaznikId, "activity1", 5, LocalDateTime.now()),
                new ActivityFavouriteEntity("2", zakaznikId, "activity2", 3, LocalDateTime.now())
        );

        when(activityFavouriteRepository.findByZakaznik(eq(zakaznikId), any())).thenReturn(expectedEntities);

        List<ActivityFavouriteEntity> result = activityFavouriteService.findByZakaznik(zakaznikId, offset, count);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(activityFavouriteRepository, times(1)).findByZakaznik(eq(zakaznikId), any());
    }

    @Test
    void testFindByZakaznik_Failure() {
        String zakaznikId = "customer123";

        when(activityFavouriteRepository.findByZakaznik(eq(zakaznikId), any())).thenThrow(new RuntimeException("Database error"));

        FinderException exception = assertThrows(FinderException.class, () -> activityFavouriteService.findByZakaznik(zakaznikId, 0, 10));
        assertTrue(exception.getMessage().contains("Error retrieving ActivityFavouriteEntity records"));

        verify(activityFavouriteRepository, times(1)).findByZakaznik(eq(zakaznikId), any());
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

        assertThrows(FinderException.class, () -> activityFavouriteService.findByZakaznikAktivita(zakaznikId, activityId));

        verify(activityFavouriteRepository, times(1)).findByZakaznikAktivita(zakaznikId, activityId);
    }

    @Test
    void testCreate_Success() {
        ActivityFavouriteEntity entity = new ActivityFavouriteEntity("1", "customer123", "activity456", 2, LocalDateTime.now());

        doNothing().when(em).persist(entity);
        doNothing().when(em).flush();

        assertDoesNotThrow(() -> activityFavouriteService.create(entity));

        verify(em, times(1)).persist(entity);
        verify(em, times(1)).flush();
    }

    @Test
    void testCreate_Failure() {
        ActivityFavouriteEntity entity = new ActivityFavouriteEntity("1", "customer123", "activity456", 2, LocalDateTime.now());

        doThrow(new RuntimeException("DB error")).when(em).persist(entity);

        assertThrows(Exception.class, () -> activityFavouriteService.create(entity));

        verify(em, times(1)).persist(entity);
    }

    @Test
    void testUpdate_Success() {
        ActivityFavouriteEntity entity = new ActivityFavouriteEntity("1", "customer123", "activity456", 2, LocalDateTime.now());

        when(em.merge(entity)).thenReturn(entity);
        doNothing().when(em).flush();

        assertDoesNotThrow(() -> activityFavouriteService.update(entity));

        verify(em, times(1)).merge(entity);
        verify(em, times(1)).flush();
    }

    @Test
    void testUpdate_Failure() {
        ActivityFavouriteEntity entity = new ActivityFavouriteEntity("1", "customer123", "activity456", 2, LocalDateTime.now());

        doThrow(new RuntimeException("DB error")).when(em).merge(entity);

        assertThrows(Exception.class, () -> activityFavouriteService.update(entity));

        verify(em, times(1)).merge(entity);
    }

    @Test
    void testRemove_Success() {
        ActivityFavouriteEntity entity = new ActivityFavouriteEntity("1", "customer123", "activity456", 2, LocalDateTime.now());

        when(em.merge(entity)).thenReturn(entity);
        doNothing().when(em).remove(entity);
        doNothing().when(em).flush();

        assertDoesNotThrow(() -> activityFavouriteService.delete(entity));

        verify(em, times(1)).merge(entity);
        verify(em, times(1)).remove(entity);
        verify(em, times(1)).flush();
    }

    @Test
    void testRemove_Failure() {
        ActivityFavouriteEntity entity = new ActivityFavouriteEntity("1", "customer123", "activity456", 2, LocalDateTime.now());

        when(em.merge(entity)).thenReturn(entity);
        doThrow(new RuntimeException("DB error")).when(em).remove(entity);

        assertThrows(Exception.class, () -> activityFavouriteService.delete(entity));

        verify(em, times(1)).merge(entity);
        verify(em, times(1)).remove(entity);
    }
}
