package ServiceTests.Sport;

import cz.inspire.sport.entity.ObjektEntity;
import cz.inspire.sport.repository.ObjektRepository;
import cz.inspire.sport.service.ObjektService;
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
public class ObjektServiceTest {

    @Mock
    private ObjektRepository objektRepository;

    @Mock
    private EntityManager em;

    private ObjektService objektService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objektService = new ObjektService(objektRepository);
        objektService.setEntityManager(em);
    }

    @Test
    void testFindAll_Success() throws FinderException {
        List<ObjektEntity> expectedEntities = List.of(
                new ObjektEntity("1", 50, 30, 1, true, 10, 60, 5, 5, 15, 10, false, 1, null, null,
                        false, false, false, false, false, false, false, false, false, false,
                        15, 30, 60, "calendar1", true, 10, null, null, null, null, null, null, null),
                new ObjektEntity("2", 40, 20, 2, false, 5, 45, 3, 3, 10, 8, true, 2, null, null,
                        true, true, true, true, true, true, true, true, true, true,
                        10, 20, 30, "calendar2", false, 15, null, null, null, null, null, null, null)
        );

        when(objektRepository.findAllOrdered()).thenReturn(expectedEntities);

        List<ObjektEntity> result = objektService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(objektRepository, times(1)).findAllOrdered();
    }

    @Test
    void testFindAll_Failure() {
        when(objektRepository.findAllOrdered()).thenThrow(new RuntimeException("Database error"));

        FinderException exception = assertThrows(FinderException.class, () -> objektService.findAll());
        assertTrue(exception.getMessage().contains("Error retrieving all ObjektEntity records"));

        verify(objektRepository, times(1)).findAllOrdered();
    }

    @Test
    void testFindByAreal_Success() throws FinderException {
        String arealId = "areal123";
        String jazyk = "cz";
        List<ObjektEntity> expectedEntities = List.of(
                new ObjektEntity("1", 50, 30, 1, true, 10, 60, 5, 5, 15, 10, false, 1, null, null,
                        false, false, false, false, false, false, false, false, false, false,
                        15, 30, 60, "calendar1", true, 10, null, null, null, null, null, null, null)
        );

        when(objektRepository.findByAreal(eq(arealId), eq(jazyk))).thenReturn(expectedEntities);

        List<ObjektEntity> result = objektService.findByAreal(arealId, jazyk);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(objektRepository, times(1)).findByAreal(eq(arealId), eq(jazyk));
    }

    @Test
    void testFindByAreal_Failure() {
        String arealId = "areal123";
        String jazyk = "cz";

        when(objektRepository.findByAreal(eq(arealId), eq(jazyk))).thenThrow(new RuntimeException("Database error"));

        FinderException exception = assertThrows(FinderException.class, () -> objektService.findByAreal(arealId, jazyk));
        assertTrue(exception.getMessage().contains("Error retrieving ObjektEntity records"));

        verify(objektRepository, times(1)).findByAreal(eq(arealId), eq(jazyk));
    }

    @Test
    void testFindByTypRezervace_Success() throws FinderException {
        Integer typRezervace = 1;
        String jazyk = "cz";
        List<ObjektEntity> expectedEntities = List.of(
                new ObjektEntity("1", 50, 30, 1, true, 10, 60, 5, 5, 15, 10, false, 1, null, null,
                        false, false, false, false, false, false, false, false, false, false,
                        15, 30, 60, "calendar1", true, 10, null, null, null, null, null, null, null)
        );

        when(objektRepository.findByTypRezervace(eq(typRezervace), eq(jazyk))).thenReturn(expectedEntities);

        List<ObjektEntity> result = objektService.findByTypRezervace(typRezervace, jazyk);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(objektRepository, times(1)).findByTypRezervace(eq(typRezervace), eq(jazyk));
    }

    @Test
    void testFindByTypRezervace_Failure() {
        Integer typRezervace = 1;
        String jazyk = "cz";

        when(objektRepository.findByTypRezervace(eq(typRezervace), eq(jazyk))).thenThrow(new RuntimeException("Database error"));

        FinderException exception = assertThrows(FinderException.class, () -> objektService.findByTypRezervace(typRezervace, jazyk));
        assertTrue(exception.getMessage().contains("Error retrieving ObjektEntity records"));

        verify(objektRepository, times(1)).findByTypRezervace(eq(typRezervace), eq(jazyk));
    }

    @Test
    void testFindByPrimyVstup_Success() throws FinderException {
        String jazyk = "cz";
        boolean primyVstup = true;
        List<ObjektEntity> expectedEntities = List.of(
                new ObjektEntity("1", 50, 30, 1, true, 10, 60, 5, 5, 15, 10, false, 1, null, null,
                        false, false, false, false, false, false, false, false, false, false,
                        15, 30, 60, "calendar1", true, 10, null, null, null, null, null, null, null)
        );

        when(objektRepository.findByPrimyVstup(eq(jazyk), eq(primyVstup))).thenReturn(expectedEntities);

        List<ObjektEntity> result = objektService.findByPrimyVstup(jazyk, primyVstup);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(objektRepository, times(1)).findByPrimyVstup(eq(jazyk), eq(primyVstup));
    }

    @Test
    void testFindByPrimyVstup_Failure() {
        String jazyk = "cz";
        boolean primyVstup = true;

        when(objektRepository.findByPrimyVstup(eq(jazyk), eq(primyVstup))).thenThrow(new RuntimeException("Database error"));

        FinderException exception = assertThrows(FinderException.class, () -> objektService.findByPrimyVstup(jazyk, primyVstup));
        assertTrue(exception.getMessage().contains("Error retrieving ObjektEntity records"));

        verify(objektRepository, times(1)).findByPrimyVstup(eq(jazyk), eq(primyVstup));
    }

    @Test
    void testFindBaseByAreal_Success() throws FinderException {
        String arealId = "areal123";
        String jazyk = "cz";
        List<ObjektEntity> expectedEntities = List.of(
                new ObjektEntity("1", 50, 30, 1, true, 10, 60, 5, 5, 15, 10, false, 1, null, null,
                        false, false, false, false, false, false, false, false, false, false,
                        15, 30, 60, "calendar1", true, 10, null, null, null, null, null, null, null)
        );

        when(objektRepository.findBaseByAreal(eq(arealId), eq(jazyk))).thenReturn(expectedEntities);

        List<ObjektEntity> result = objektService.findBaseByAreal(arealId, jazyk);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(objektRepository, times(1)).findBaseByAreal(eq(arealId), eq(jazyk));
    }

    @Test
    void testFindBaseByAreal_Failure() {
        String arealId = "areal123";
        String jazyk = "cz";

        when(objektRepository.findBaseByAreal(eq(arealId), eq(jazyk))).thenThrow(new RuntimeException("Database error"));

        FinderException exception = assertThrows(FinderException.class, () -> objektService.findBaseByAreal(arealId, jazyk));
        assertTrue(exception.getMessage().contains("Error retrieving base ObjektEntity records"));

        verify(objektRepository, times(1)).findBaseByAreal(eq(arealId), eq(jazyk));
    }

    @Test
    void testFindByArealWithPagination_Success() throws FinderException {
        String arealId = "areal123";
        String jazyk = "cz";
        int offset = 0, count = 10;
        List<ObjektEntity> expectedEntities = List.of(
                new ObjektEntity("1", 50, 30, 1, true, 10, 60, 5, 5, 15, 10, false, 1, null, null,
                        false, false, false, false, false, false, false, false, false, false,
                        15, 30, 60, "calendar1", true, 10, null, null, null, null, null, null, null)
        );

        when(objektRepository.findByArealWithLimit(eq(arealId), eq(jazyk), any())).thenReturn(expectedEntities);

        List<ObjektEntity> result = objektService.findByAreal(arealId, jazyk, offset, count);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(objektRepository, times(1)).findByArealWithLimit(eq(arealId), eq(jazyk), any());
    }

    @Test
    void testFindBaseByArealWithPagination_Success() throws FinderException {
        String arealId = "areal123";
        String jazyk = "cz";
        int offset = 0, count = 10;
        List<ObjektEntity> expectedEntities = List.of(
                new ObjektEntity("1", 50, 30, 1, true, 10, 60, 5, 5, 15, 10, false, 1, null, null,
                        false, false, false, false, false, false, false, false, false, false,
                        15, 30, 60, "calendar1", true, 10, null, null, null, null, null, null, null)
        );

        when(objektRepository.findBaseByArealWithLimit(eq(arealId), eq(jazyk), any())).thenReturn(expectedEntities);

        List<ObjektEntity> result = objektService.findBaseByAreal(arealId, jazyk, offset, count);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(objektRepository, times(1)).findBaseByArealWithLimit(eq(arealId), eq(jazyk), any());
    }

    @Test
    void testFindByPrimyVstupWithPagination_Success() throws FinderException {
        String jazyk = "cz";
        boolean primyVstup = true;
        int offset = 0, count = 10;
        List<ObjektEntity> expectedEntities = List.of(
                new ObjektEntity("1", 50, 30, 1, true, 10, 60, 5, 5, 15, 10, false, 1, null, null,
                        false, false, false, false, false, false, false, false, false, false,
                        15, 30, 60, "calendar1", true, 10, null, null, null, null, null, null, null)
        );

        when(objektRepository.findByPrimyVstupWithLimit(eq(jazyk), any(), eq(primyVstup))).thenReturn(expectedEntities);

        List<ObjektEntity> result = objektService.findByPrimyVstup(jazyk, offset, count, primyVstup);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(objektRepository, times(1)).findByPrimyVstupWithLimit(eq(jazyk), any(), eq(primyVstup));
    }

}
