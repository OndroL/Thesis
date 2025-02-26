package ServiceTests.Sport;

import cz.inspire.sport.entity.PodminkaRezervaceEntity;
import cz.inspire.sport.repository.PodminkaRezervaceRepository;
import cz.inspire.sport.service.PodminkaRezervaceService;
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
public class PodminkaRezervaceServiceTest {

    @Mock
    private PodminkaRezervaceRepository repository;

    @Mock
    private EntityManager em;

    private PodminkaRezervaceService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new PodminkaRezervaceService(repository);
        service.setEntityManager(em);
    }

    @Test
    void testFindAll_Success() throws FinderException {
        List<PodminkaRezervaceEntity> expectedEntities = List.of(
                new PodminkaRezervaceEntity("1", "Condition A", 1, "obj1", true, null),
                new PodminkaRezervaceEntity("2", "Condition B", 2, "obj2", false, null)
        );

        when(repository.findAllOrdered()).thenReturn(expectedEntities);

        List<PodminkaRezervaceEntity> result = service.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(repository, times(1)).findAllOrdered();
    }

    @Test
    void testFindAll_Failure() {
        when(repository.findAllOrdered()).thenThrow(new RuntimeException("Database error"));

        FinderException exception = assertThrows(FinderException.class, () -> service.findAll());
        assertTrue(exception.getMessage().contains("Error retrieving all PodminkaRezervaceEntity records"));

        verify(repository, times(1)).findAllOrdered();
    }

    @Test
    void testFindByObjekt_Success() throws FinderException {
        String objektId = "obj1";
        List<PodminkaRezervaceEntity> expectedEntities = List.of(
                new PodminkaRezervaceEntity("1", "Condition A", 1, objektId, true, null)
        );

        when(repository.findByObjekt(eq(objektId), any())).thenReturn(expectedEntities);

        List<PodminkaRezervaceEntity> result = service.findByObjekt(objektId, 0, 10);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository, times(1)).findByObjekt(eq(objektId), any());
    }

    @Test
    void testFindByObjekt_Failure() {
        String objektId = "obj1";

        when(repository.findByObjekt(eq(objektId), any())).thenThrow(new RuntimeException("Database error"));

        FinderException exception = assertThrows(FinderException.class, () -> service.findByObjekt(objektId, 0, 10));
        assertTrue(exception.getMessage().contains("Error retrieving PodminkaRezervaceEntity records"));

        verify(repository, times(1)).findByObjekt(eq(objektId), any());
    }

    @Test
    void testCountAllByObject_Success() throws FinderException {
        String objektId = "obj1";
        when(repository.countAllByObject(eq(objektId))).thenReturn(5L);

        Long result = service.countAllByObject(objektId);

        assertNotNull(result);
        assertEquals(5L, result);
        verify(repository, times(1)).countAllByObject(eq(objektId));
    }

    @Test
    void testCountAllByObject_Failure() {
        String objektId = "obj1";

        when(repository.countAllByObject(eq(objektId))).thenThrow(new RuntimeException("Database error"));

        FinderException exception = assertThrows(FinderException.class, () -> service.countAllByObject(objektId));
        assertTrue(exception.getMessage().contains("Error retrieving count of PodminkaRezervaceEntity records"));

        verify(repository, times(1)).countAllByObject(eq(objektId));
    }

    @Test
    void testCountAll_Success() throws FinderException {
        when(repository.countAll()).thenReturn(20L);

        Long result = service.countAll();

        assertNotNull(result);
        assertEquals(20L, result);
        verify(repository, times(1)).countAll();
    }

    @Test
    void testCountAll_Failure() {
        when(repository.countAll()).thenThrow(new RuntimeException("Database error"));

        FinderException exception = assertThrows(FinderException.class, () -> service.countAll());
        assertTrue(exception.getMessage().contains("Error retrieving total count of PodminkaRezervaceEntity records"));

        verify(repository, times(1)).countAll();
    }

    @Test
    void testGetObjectIdsByReservationConditionObject_Success() throws FinderException {
        String objektRezervaceId = "obj1";
        List<String> expectedIds = List.of("obj1", "obj2", "obj3");

        when(repository.getObjectIdsByReservationConditionObject(eq(objektRezervaceId))).thenReturn(expectedIds);

        List<String> result = service.getObjectIdsByReservationConditionObject(objektRezervaceId);

        assertNotNull(result);
        assertEquals(3, result.size());
        verify(repository, times(1)).getObjectIdsByReservationConditionObject(eq(objektRezervaceId));
    }

    @Test
    void testGetObjectIdsByReservationConditionObject_Failure() {
        String objektRezervaceId = "obj1";

        when(repository.getObjectIdsByReservationConditionObject(eq(objektRezervaceId))).thenThrow(new RuntimeException("Database error"));

        FinderException exception = assertThrows(FinderException.class, () -> service.getObjectIdsByReservationConditionObject(objektRezervaceId));
        assertTrue(exception.getMessage().contains("Error retrieving object IDs"));

        verify(repository, times(1)).getObjectIdsByReservationConditionObject(eq(objektRezervaceId));
    }

    @Test
    void testGetMaxPriority_Success() throws FinderException {
        when(repository.getMaxPriority()).thenReturn(99L);

        Long result = service.getMaxPriority();

        assertNotNull(result);
        assertEquals(99L, result);
        verify(repository, times(1)).getMaxPriority();
    }

    @Test
    void testGetMaxPriority_Failure() {
        when(repository.getMaxPriority()).thenThrow(new RuntimeException("Database error"));

        FinderException exception = assertThrows(FinderException.class, () -> service.getMaxPriority());
        assertTrue(exception.getMessage().contains("Error retrieving max priority"));

        verify(repository, times(1)).getMaxPriority();
    }
}
