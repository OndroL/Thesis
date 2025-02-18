package ServiceTests.Sport;

import cz.inspire.sport.entity.OtviraciDobaObjektuEntity;
import cz.inspire.sport.entity.OtviraciDobaObjektuPK;
import cz.inspire.sport.repository.OtviraciDobaObjektuRepository;
import cz.inspire.sport.service.OtviraciDobaObjektuService;
import jakarta.ejb.FinderException;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OtviraciDobaObjektuServiceTest {

    @Mock
    private OtviraciDobaObjektuRepository repository;

    @Mock
    private EntityManager em;

    private OtviraciDobaObjektuService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new OtviraciDobaObjektuService(repository);
        service.setEntityManager(em);
    }

    @Test
    void testFindAll_Success() throws FinderException {
        List<OtviraciDobaObjektuEntity> expectedEntities = List.of(
                new OtviraciDobaObjektuEntity(new OtviraciDobaObjektuPK("objekt1", LocalDateTime.now()), null),
                new OtviraciDobaObjektuEntity(new OtviraciDobaObjektuPK("objekt2", LocalDateTime.now()), null)
        );

        when(repository.findAllOrdered()).thenReturn(expectedEntities);

        List<OtviraciDobaObjektuEntity> result = service.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(repository, times(1)).findAllOrdered();
    }

    @Test
    void testFindAll_Failure() {
        when(repository.findAllOrdered()).thenThrow(new RuntimeException("Database error"));

        FinderException exception = assertThrows(FinderException.class, () -> service.findAll());
        assertTrue(exception.getMessage().contains("Error retrieving all OtviraciDobaObjektuEntity records"));

        verify(repository, times(1)).findAllOrdered();
    }

    @Test
    void testFindByObjekt_Success() throws FinderException {
        String objektId = "objekt123";
        List<OtviraciDobaObjektuEntity> expectedEntities = List.of(
                new OtviraciDobaObjektuEntity(new OtviraciDobaObjektuPK(objektId, LocalDateTime.now()), null)
        );

        when(repository.findByObjekt(eq(objektId))).thenReturn(expectedEntities);

        List<OtviraciDobaObjektuEntity> result = service.findByObjekt(objektId);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository, times(1)).findByObjekt(eq(objektId));
    }

    @Test
    void testFindByObjekt_Failure() {
        String objektId = "objekt123";

        when(repository.findByObjekt(eq(objektId))).thenThrow(new RuntimeException("Database error"));

        FinderException exception = assertThrows(FinderException.class, () -> service.findByObjekt(objektId));
        assertTrue(exception.getMessage().contains("Error retrieving OtviraciDobaObjektuEntity records"));

        verify(repository, times(1)).findByObjekt(eq(objektId));
    }

    @Test
    void testFindCurrent_Success() throws FinderException {
        String objektId = "objekt123";
        Date day = new Date();
        OtviraciDobaObjektuPK pk = new OtviraciDobaObjektuPK(objektId, LocalDateTime.now());
        OtviraciDobaObjektuEntity expectedEntity = new OtviraciDobaObjektuEntity(pk, null);

        when(repository.findCurrent(eq(objektId), any(), any())).thenReturn(Optional.of(expectedEntity));

        Optional<OtviraciDobaObjektuEntity> result = service.findCurrent(objektId, day);

        assertTrue(result.isPresent());
        verify(repository, times(1)).findCurrent(eq(objektId), any(), any());
    }

    @Test
    void testFindCurrent_Failure() {
        String objektId = "objekt123";
        Date day = new Date();

        when(repository.findCurrent(eq(objektId), any(), any())).thenThrow(new RuntimeException("Database error"));

        FinderException exception = assertThrows(FinderException.class, () -> service.findCurrent(objektId, day));
        assertTrue(exception.getMessage().contains("Error retrieving current OtviraciDobaObjektuEntity record"));

        verify(repository, times(1)).findCurrent(eq(objektId), any(), any());
    }

    @Test
    void testFindAfter_Success() throws FinderException {
        String objektId = "objekt123";
        Date day = new Date();
        List<OtviraciDobaObjektuEntity> expectedEntities = List.of(
                new OtviraciDobaObjektuEntity(new OtviraciDobaObjektuPK(objektId, LocalDateTime.now().plusDays(1)), null)
        );

        when(repository.findAfter(eq(objektId), any())).thenReturn(expectedEntities);

        List<OtviraciDobaObjektuEntity> result = service.findAfter(objektId, day);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository, times(1)).findAfter(eq(objektId), any());
    }

    @Test
    void testFindAfter_Failure() {
        String objektId = "objekt123";
        Date day = new Date();

        when(repository.findAfter(eq(objektId), any())).thenThrow(new RuntimeException("Database error"));

        FinderException exception = assertThrows(FinderException.class, () -> service.findAfter(objektId, day));
        assertTrue(exception.getMessage().contains("Error retrieving OtviraciDobaObjektuEntity records after date"));

        verify(repository, times(1)).findAfter(eq(objektId), any());
    }

    @Test
    void testFindById_Success() throws FinderException {
        OtviraciDobaObjektuPK pk = new OtviraciDobaObjektuPK("objekt123", LocalDateTime.now());
        OtviraciDobaObjektuEntity expectedEntity = new OtviraciDobaObjektuEntity(pk, null);

        when(repository.findById(eq(pk))).thenReturn(Optional.of(expectedEntity));

        Optional<OtviraciDobaObjektuEntity> result = service.findById(pk);

        assertTrue(result.isPresent());
        verify(repository, times(1)).findById(eq(pk));
    }

    @Test
    void testDeleteById_Success() {
        OtviraciDobaObjektuPK pk = new OtviraciDobaObjektuPK("objekt123", LocalDateTime.now());

        doNothing().when(repository).deleteById(pk);

        assertDoesNotThrow(() -> service.deleteById(pk));

        verify(repository, times(1)).deleteById(eq(pk));
    }

    @Test
    void testDeleteById_Failure() {
        OtviraciDobaObjektuPK pk = new OtviraciDobaObjektuPK("objekt123", LocalDateTime.now());

        doThrow(new RuntimeException("Database error")).when(repository).deleteById(eq(pk));

        FinderException exception = assertThrows(FinderException.class, () -> service.deleteById(pk));
        assertTrue(exception.getMessage().contains("Error deleting OtviraciDobaObjektuEntity record"));

        verify(repository, times(1)).deleteById(eq(pk));
    }

    @Test
    void testGetCurrentIdsByObjectAndDay_Success() throws FinderException {
        String objektId = "objekt123";
        Date day = new Date();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(day.toInstant(), ZoneOffset.UTC);
        List<LocalDateTime> expectedIds = List.of(localDateTime.plusHours(1), localDateTime.plusHours(2));

        when(repository.getCurrentIdsByObjectAndDay(eq(objektId), eq(localDateTime))).thenReturn(expectedIds);

        List<LocalDateTime> result = service.getCurrentIdsByObjectAndDay(objektId, day);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(repository, times(1)).getCurrentIdsByObjectAndDay(eq(objektId), eq(localDateTime));
    }

    @Test
    void testGetCurrentIdsByObjectAndDay_Failure() {
        String objektId = "objekt123";
        Date day = new Date();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(day.toInstant(), ZoneOffset.UTC);

        when(repository.getCurrentIdsByObjectAndDay(eq(objektId), eq(localDateTime))).thenThrow(new RuntimeException("Database error"));

        FinderException exception = assertThrows(FinderException.class, () -> service.getCurrentIdsByObjectAndDay(objektId, day));
        assertTrue(exception.getMessage().contains("Error retrieving current OtviraciDobaObjektuEntity IDs"));

        verify(repository, times(1)).getCurrentIdsByObjectAndDay(eq(objektId), eq(localDateTime));
    }

}
