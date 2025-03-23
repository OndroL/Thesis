package ServiceTests.Sport;

import cz.inspire.sport.entity.OvladacObjektuEntity;
import cz.inspire.sport.repository.OvladacObjektuRepository;
import cz.inspire.sport.service.OvladacObjektuService;
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
public class OvladacObjektuServiceTest {

    @Mock
    private OvladacObjektuRepository repository;

    private OvladacObjektuService service;

    @BeforeEach
    void setUp() {
        service = new OvladacObjektuService(repository);
    }

    @Test
    void testFindWithOvladacObjektu_Success() throws FinderException {
        String idOvladace = "ovladac123";
        List<OvladacObjektuEntity> expectedEntities = List.of(
                new OvladacObjektuEntity("1", idOvladace, List.of(1, 2), true, false, 10, 5, "objekt1"),
                new OvladacObjektuEntity("2", idOvladace, List.of(3, 4), false, true, 20, 10, "objekt2")
        );

        when(repository.findWithOvladacObjektu(eq(idOvladace))).thenReturn(expectedEntities);

        List<OvladacObjektuEntity> result = service.findWithOvladacObjektu(idOvladace);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(repository, times(1)).findWithOvladacObjektu(eq(idOvladace));
    }

    @Test
    void testFindWithOvladacObjektu_Failure() {
        String idOvladace = "ovladac123";

        when(repository.findWithOvladacObjektu(eq(idOvladace))).thenThrow(new RuntimeException("Database error"));

        FinderException exception = assertThrows(FinderException.class, () -> service.findWithOvladacObjektu(idOvladace));
        assertTrue(exception.getMessage().contains("Error retrieving OvladacObjektuEntity records"));

        verify(repository, times(1)).findWithOvladacObjektu(eq(idOvladace));
    }

    @Test
    void testFindByObjekt_Success() throws FinderException {
        String objektId = "objekt123";
        List<OvladacObjektuEntity> expectedEntities = List.of(
                new OvladacObjektuEntity("1", "ovladac1", List.of(1, 2), true, false, 10, 5, objektId),
                new OvladacObjektuEntity("2", "ovladac2", List.of(3, 4), false, true, 20, 10, objektId)
        );

        when(repository.findByObjekt(eq(objektId))).thenReturn(expectedEntities);

        List<OvladacObjektuEntity> result = service.findByObjekt(objektId);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(repository, times(1)).findByObjekt(eq(objektId));
    }

    @Test
    void testFindByObjekt_Failure() {
        String objektId = "objekt123";

        when(repository.findByObjekt(eq(objektId))).thenThrow(new RuntimeException("Database error"));

        FinderException exception = assertThrows(FinderException.class, () -> service.findByObjekt(objektId));
        assertTrue(exception.getMessage().contains("Error retrieving OvladacObjektuEntity records"));

        verify(repository, times(1)).findByObjekt(eq(objektId));
    }
}
