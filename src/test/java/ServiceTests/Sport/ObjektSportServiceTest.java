package ServiceTests.Sport;

import cz.inspire.sport.entity.ObjektEntity;
import cz.inspire.sport.entity.ObjektSportEntity;
import cz.inspire.sport.entity.ObjektSportPK;
import cz.inspire.sport.entity.SportEntity;
import cz.inspire.sport.repository.ObjektSportRepository;
import cz.inspire.sport.service.ObjektSportService;
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
public class ObjektSportServiceTest {

    @Mock
    private ObjektSportRepository objektSportRepository;

    @Mock
    private EntityManager em;

    private ObjektSportService objektSportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objektSportService = new ObjektSportService(objektSportRepository);
        objektSportService.setEntityManager(em);
    }

    @Test
    void testFindByObjekt_Success() throws FinderException {
        String objektId = "objekt123";
        List<ObjektSportEntity> expectedEntities = List.of(
                new ObjektSportEntity(new ObjektSportPK("sport1", 1), new SportEntity("sport1", 1, null, null, 15, false, null, false, 1, null, null, null, false, 0, null, null, false, false, 0, 0, 0, 0, 0, null, null, null, null, null, null, null, null, null),
                        new ObjektEntity("objekt123", 50, 30, 1, true, 10, 60, 5, 5, 15, 10, false, 1, null, null, false, false, false, false, false, false, false, false, false, false, 15, 30, 60, "calendar1", true, 10, null, null, null, null, null, null, null))
        );

        when(objektSportRepository.findByObjekt(eq(objektId))).thenReturn(expectedEntities);

        List<ObjektSportEntity> result = objektSportService.findByObjekt(objektId);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(objektSportRepository, times(1)).findByObjekt(eq(objektId));
    }

    @Test
    void testFindByObjekt_Failure() {
        String objektId = "objekt123";

        when(objektSportRepository.findByObjekt(eq(objektId))).thenThrow(new RuntimeException("Database error"));

        FinderException exception = assertThrows(FinderException.class, () -> objektSportService.findByObjekt(objektId));
        assertTrue(exception.getMessage().contains("Error retrieving valid attributes from ObjektSportEntity"));

        verify(objektSportRepository, times(1)).findByObjekt(eq(objektId));
    }

    @Test
    void testFindById_Success() throws FinderException {
        ObjektSportPK pk = new ObjektSportPK("sport1", 1);
        ObjektSportEntity expectedEntity = new ObjektSportEntity(pk, new SportEntity("sport1", 1, null, null, 10, false, null, false, 1, null, null, null, false, 0, null, null, null, false, 15, 0, 0, 0, 0, null, null, null, null, null, null, null, null, null ),
                new ObjektEntity("objekt123", 50, 30, 1, true, 10, 60, 5, 5, 15, 10, false, 1, null, null, false, false, false, false, false, false, false, false, false, false, 15, 30, 60, "calendar1", true, 10, null, null, null, null, null, null, null));

        when(objektSportRepository.findById(eq(pk))).thenReturn(Optional.of(expectedEntity));

        Optional<ObjektSportEntity> result = objektSportService.findById(pk);

        assertTrue(result.isPresent());
        assertEquals(pk, result.get().getEmbeddedPK());
        verify(objektSportRepository, times(1)).findById(eq(pk));
    }

    @Test
    void testFindById_NotFound() throws FinderException {
        ObjektSportPK pk = new ObjektSportPK("sport1", 1);

        when(objektSportRepository.findById(eq(pk))).thenReturn(Optional.empty());

        Optional<ObjektSportEntity> result = objektSportService.findById(pk);

        assertFalse(result.isPresent());
        verify(objektSportRepository, times(1)).findById(eq(pk));
    }

    @Test
    void testFindById_Failure() {
        ObjektSportPK pk = new ObjektSportPK("sport1", 1);

        when(objektSportRepository.findById(eq(pk))).thenThrow(new RuntimeException("Database error"));

        FinderException exception = assertThrows(FinderException.class, () -> objektSportService.findById(pk));
        assertTrue(exception.getMessage().contains("Error retrieving valid attributes from ObjektSportEntity"));

        verify(objektSportRepository, times(1)).findById(eq(pk));
    }

    @Test
    void testDeleteById_Success() {
        ObjektSportPK pk = new ObjektSportPK("sport1", 1);

        doNothing().when(objektSportRepository).deleteById(pk);

        assertDoesNotThrow(() -> objektSportService.deleteById(pk));

        verify(objektSportRepository, times(1)).deleteById(eq(pk));
    }

    @Test
    void testDeleteById_Failure() {
        ObjektSportPK pk = new ObjektSportPK("sport1", 1);

        doThrow(new RuntimeException("Database error")).when(objektSportRepository).deleteById(eq(pk));

        FinderException exception = assertThrows(FinderException.class, () -> objektSportService.deleteById(pk));
        assertTrue(exception.getMessage().contains("Error deleting ObjektSportEntity record"));

        verify(objektSportRepository, times(1)).deleteById(eq(pk));
    }
}
