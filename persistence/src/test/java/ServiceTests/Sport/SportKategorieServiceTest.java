package ServiceTests.Sport;

import cz.inspire.sport.entity.SportKategorieEntity;
import cz.inspire.sport.repository.SportKategorieRepository;
import cz.inspire.sport.service.SportKategorieService;
import jakarta.ejb.FinderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SportKategorieServiceTest {

    @Mock
    private SportKategorieRepository repository;

    @InjectMocks
    private SportKategorieService service;

    @BeforeEach
    void setUp() {
        // service is injected with repository mock automatically.
    }

    @Test
    void testFindAll_Success() throws FinderException {
        List<SportKategorieEntity> expectedEntities = List.of(
                new SportKategorieEntity("1", "multi1", "uuid1", null, null, null, null),
                new SportKategorieEntity("2", "multi2", "uuid2", null, null, null, null)
        );
        when(repository.findAll()).thenReturn(expectedEntities);

        List<SportKategorieEntity> result = service.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testFindAll_Failure() {
        when(repository.findAll()).thenThrow(new RuntimeException("Database error"));

        FinderException exception = assertThrows(FinderException.class, () -> service.findAll());
        assertTrue(exception.getMessage().contains("Error retrieving all SportKategorieEntity records"));

        verify(repository, times(1)).findAll();
    }

    @Test
    void testFindRoot_Success() throws FinderException {
        List<SportKategorieEntity> expectedEntities = List.of(
                new SportKategorieEntity("1", "multi1", "uuid1", null, null, null, null)
        );
        when(repository.findRoot()).thenReturn(expectedEntities);

        List<SportKategorieEntity> result = service.findRoot();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository, times(1)).findRoot();
    }

    @Test
    void testFindRoot_Failure() {
        when(repository.findRoot()).thenThrow(new RuntimeException("Database error"));

        FinderException exception = assertThrows(FinderException.class, () -> service.findRoot());
        assertTrue(exception.getMessage().contains("Error retrieving root SportKategorieEntity records"));

        verify(repository, times(1)).findRoot();
    }

    @Test
    void testFindAllByNadrazenaKategorie_Success() throws FinderException {
        String nadrazenaKategorieId = "category1";
        List<SportKategorieEntity> expectedEntities = List.of(
                new SportKategorieEntity("2", "multi2", "uuid2", null, null, null, null)
        );
        when(repository.findAllByNadrazenaKategorie(eq(nadrazenaKategorieId))).thenReturn(expectedEntities);

        List<SportKategorieEntity> result = service.findAllByNadrazenaKategorie(nadrazenaKategorieId);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository, times(1)).findAllByNadrazenaKategorie(eq(nadrazenaKategorieId));
    }

    @Test
    void testFindAllByNadrazenaKategorie_Failure() {
        String nadrazenaKategorieId = "category1";
        when(repository.findAllByNadrazenaKategorie(eq(nadrazenaKategorieId))).thenThrow(new RuntimeException("Database error"));

        FinderException exception = assertThrows(FinderException.class, () -> service.findAllByNadrazenaKategorie(nadrazenaKategorieId));
        assertTrue(exception.getMessage().contains("Error retrieving SportKategorieEntity records"));

        verify(repository, times(1)).findAllByNadrazenaKategorie(eq(nadrazenaKategorieId));
    }

    @Test
    void testFindAllPaginated_Success() throws FinderException {
        int offset = 0, count = 10;
        List<SportKategorieEntity> expectedEntities = List.of(
                new SportKategorieEntity("1", "multi1", "uuid1", null, null, null, null)
        );
        when(repository.findAll(eq(count), eq(offset))).thenReturn(expectedEntities);

        List<SportKategorieEntity> result = service.findAll(offset, count);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository, times(1)).findAll(eq(count), eq(offset));
    }

    @Test
    void testCount_Success() throws FinderException {
        when(repository.count()).thenReturn(10L);

        Long result = service.count();

        assertNotNull(result);
        assertEquals(10L, result);
        verify(repository, times(1)).count();
    }

    @Test
    void testCount_Failure() {
        when(repository.count()).thenThrow(new RuntimeException("Database error"));

        FinderException exception = assertThrows(FinderException.class, () -> service.count());
        assertTrue(exception.getMessage().contains("Error retrieving total count"));

        verify(repository, times(1)).count();
    }

    @Test
    void testCountRoot_Success() throws FinderException {
        when(repository.countRoot()).thenReturn(5L);

        Long result = service.countRoot();

        assertNotNull(result);
        assertEquals(5L, result);
        verify(repository, times(1)).countRoot();
    }

    @Test
    void testCountRoot_Failure() {
        when(repository.countRoot()).thenThrow(new RuntimeException("Database error"));

        FinderException exception = assertThrows(FinderException.class, () -> service.countRoot());
        assertTrue(exception.getMessage().contains("Error retrieving count of root SportKategorieEntity records"));

        verify(repository, times(1)).countRoot();
    }

    @Test
    void testCountByNadrazenaKategorie_Success() throws FinderException {
        String nadrazenaKategorieId = "category1";
        when(repository.countByNadrazenaKategorie(eq(nadrazenaKategorieId))).thenReturn(3L);

        Long result = service.countByNadrazenaKategorie(nadrazenaKategorieId);

        assertNotNull(result);
        assertEquals(3L, result);
        verify(repository, times(1)).countByNadrazenaKategorie(eq(nadrazenaKategorieId));
    }

    @Test
    void testCountByNadrazenaKategorie_Failure() {
        String nadrazenaKategorieId = "category1";
        when(repository.countByNadrazenaKategorie(eq(nadrazenaKategorieId))).thenThrow(new RuntimeException("Database error"));

        FinderException exception = assertThrows(FinderException.class, () -> service.countByNadrazenaKategorie(nadrazenaKategorieId));
        assertTrue(exception.getMessage().contains("Error retrieving count"));

        verify(repository, times(1)).countByNadrazenaKategorie(eq(nadrazenaKategorieId));
    }

    @Test
    void testFindAllByMultisportFacilityId_Success() throws FinderException {
        String multisportFacilityId = "multi1";
        List<SportKategorieEntity> expectedEntities = List.of(
                new SportKategorieEntity("1", multisportFacilityId, "uuid1", null, null, null, null)
        );
        when(repository.findAllByMultisportFacilityId(eq(multisportFacilityId))).thenReturn(expectedEntities);

        List<SportKategorieEntity> result = service.findAllByMultisportFacilityId(multisportFacilityId);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository, times(1)).findAllByMultisportFacilityId(eq(multisportFacilityId));
    }

    @Test
    void testFindAllByMultisportFacilityId_Failure() {
        String multisportFacilityId = "multi1";
        when(repository.findAllByMultisportFacilityId(eq(multisportFacilityId))).thenThrow(new RuntimeException("Database error"));

        FinderException exception = assertThrows(FinderException.class, () -> service.findAllByMultisportFacilityId(multisportFacilityId));
        assertTrue(exception.getMessage().contains("Error retrieving SportKategorieEntity records"));

        verify(repository, times(1)).findAllByMultisportFacilityId(eq(multisportFacilityId));
    }
}
