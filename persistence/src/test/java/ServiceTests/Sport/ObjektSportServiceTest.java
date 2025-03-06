package ServiceTests.Sport;

import cz.inspire.sport.entity.ObjektSportEntity;
import cz.inspire.sport.entity.ObjektSportPK;
import cz.inspire.sport.repository.ObjektSportRepository;
import cz.inspire.sport.service.ObjektSportService;
import jakarta.ejb.FinderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ObjektSportServiceTest {

    @Mock
    private ObjektSportRepository objektSportRepository;

    private ObjektSportService objektSportService;

    @BeforeEach
    void setUp() {
        objektSportService = new ObjektSportService(objektSportRepository);
    }

    @Test
    void testFindByObjekt_Success() throws FinderException {
        String objektId = "objekt123";
        List<ObjektSportEntity> expectedEntities = List.of(
                new ObjektSportEntity(new ObjektSportPK("sport1", 1),
                        null, null)
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
        ObjektSportEntity expectedEntity = new ObjektSportEntity(pk, null, null);
        when(objektSportRepository.findByPrimaryKey(eq(pk))).thenReturn(expectedEntity);

        ObjektSportEntity result = objektSportService.findByPrimaryKey(pk);

        assertNotNull(result);
        assertEquals(pk, result.getEmbeddedId());
        verify(objektSportRepository, times(1)).findByPrimaryKey(eq(pk));
    }

    @Test
    void testFindById_NotFound() throws FinderException {
        ObjektSportPK pk = new ObjektSportPK("sport1", 1);
        when(objektSportRepository.findByPrimaryKey(eq(pk))).thenReturn(null);

        ObjektSportEntity result = objektSportService.findByPrimaryKey(pk);

        assertNull(result);
        verify(objektSportRepository, times(1)).findByPrimaryKey(eq(pk));
    }
}
