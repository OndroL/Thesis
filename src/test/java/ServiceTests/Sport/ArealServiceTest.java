package ServiceTests.Sport;

import cz.inspire.sport.entity.ArealEntity;
import cz.inspire.sport.repository.ArealRepository;
import cz.inspire.sport.service.ArealService;
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
public class ArealServiceTest {

    @Mock
    private ArealRepository arealRepository;

    @Mock
    private EntityManager em;

    private ArealService arealService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        arealService = new ArealService(arealRepository);
        arealService.setEntityManager(em);
    }

    @Test
    void testFindAll_Success() throws FinderException {
        List<ArealEntity> expectedEntities = List.of(
                new ArealEntity("1", 10, null, null, null, null),
                new ArealEntity("2", 20, null, null, null, null)
        );

        when(arealRepository.findAllOrdered()).thenReturn(expectedEntities);

        List<ArealEntity> result = arealService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(arealRepository, times(1)).findAllOrdered();
    }

    @Test
    void testFindAll_Failure() {
        when(arealRepository.findAllOrdered()).thenThrow(new RuntimeException("Database error"));

        FinderException exception = assertThrows(FinderException.class, () -> arealService.findAll());
        assertTrue(exception.getMessage().contains("Error retrieving all ArealEntity records"));

        verify(arealRepository, times(1)).findAllOrdered();
    }

    @Test
    void testFindByParent_Success() throws FinderException {
        String parentId = "parent123";
        String jazyk = "cz";
        List<ArealEntity> expectedEntities = List.of(
                new ArealEntity("1", 10, null, null, null, null)
        );

        when(arealRepository.findByParent(eq(parentId), eq(jazyk))).thenReturn(expectedEntities);

        List<ArealEntity> result = arealService.findByParent(parentId, jazyk);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(arealRepository, times(1)).findByParent(eq(parentId), eq(jazyk));
    }

    @Test
    void testFindByParent_Failure() {
        String parentId = "parent123";
        String jazyk = "cz";

        when(arealRepository.findByParent(eq(parentId), eq(jazyk))).thenThrow(new RuntimeException("Database error"));

        FinderException exception = assertThrows(FinderException.class, () -> arealService.findByParent(parentId, jazyk));
        assertTrue(exception.getMessage().contains("Error retrieving ArealEntity records"));

        verify(arealRepository, times(1)).findByParent(eq(parentId), eq(jazyk));
    }

    @Test
    void testFindRoot_Success() throws FinderException {
        String jazyk = "cz";
        List<ArealEntity> expectedEntities = List.of(
                new ArealEntity("1", 10, null, null, null, null)
        );

        when(arealRepository.findRoot(eq(jazyk))).thenReturn(expectedEntities);

        List<ArealEntity> result = arealService.findRoot(jazyk);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(arealRepository, times(1)).findRoot(eq(jazyk));
    }

    @Test
    void testFindRoot_Failure() {
        String jazyk = "cz";

        when(arealRepository.findRoot(eq(jazyk))).thenThrow(new RuntimeException("Database error"));

        FinderException exception = assertThrows(FinderException.class, () -> arealService.findRoot(jazyk));
        assertTrue(exception.getMessage().contains("Error retrieving root ArealEntity records"));

        verify(arealRepository, times(1)).findRoot(eq(jazyk));
    }

    @Test
    void testFindIfChild_Success() throws FinderException {
        String childId = "child123";
        String parentId = "parent123";
        Optional<ArealEntity> expectedEntity = Optional.of(new ArealEntity("1", 10, null, null, null, null));

        when(arealRepository.findIfChild(eq(childId), eq(parentId))).thenReturn(expectedEntity);

        Optional<ArealEntity> result = arealService.findIfChild(childId, parentId);

        assertTrue(result.isPresent());
        verify(arealRepository, times(1)).findIfChild(eq(childId), eq(parentId));
    }

    @Test
    void testFindIfChild_NotFound() throws FinderException {
        String childId = "child123";
        String parentId = "parent123";

        when(arealRepository.findIfChild(eq(childId), eq(parentId))).thenReturn(Optional.empty());

        Optional<ArealEntity> result = arealService.findIfChild(childId, parentId);

        assertFalse(result.isPresent());
        verify(arealRepository, times(1)).findIfChild(eq(childId), eq(parentId));
    }

    @Test
    void testFindIfChild_Failure() {
        String childId = "child123";
        String parentId = "parent123";

        when(arealRepository.findIfChild(eq(childId), eq(parentId))).thenThrow(new RuntimeException("Database error"));

        FinderException exception = assertThrows(FinderException.class, () -> arealService.findIfChild(childId, parentId));
        assertTrue(exception.getMessage().contains("Error checking if ArealEntity"));

        verify(arealRepository, times(1)).findIfChild(eq(childId), eq(parentId));
    }

    @Test
    void testGetArealIdsByParent_Success() throws FinderException {
        String arealId = "areal123";
        List<String> expectedIds = List.of("1", "2", "3");

        when(arealRepository.getArealIdsByParent(eq(arealId))).thenReturn(expectedIds);

        List<String> result = arealService.getArealIdsByParent(arealId);

        assertNotNull(result);
        assertEquals(3, result.size());
        verify(arealRepository, times(1)).getArealIdsByParent(eq(arealId));
    }

    @Test
    void testGetArealIdsByParent_Failure() {
        String arealId = "areal123";

        when(arealRepository.getArealIdsByParent(eq(arealId))).thenThrow(new RuntimeException("Database error"));

        FinderException exception = assertThrows(FinderException.class, () -> arealService.getArealIdsByParent(arealId));
        assertTrue(exception.getMessage().contains("Error retrieving ArealEntity IDs"));

        verify(arealRepository, times(1)).getArealIdsByParent(eq(arealId));
    }
}
