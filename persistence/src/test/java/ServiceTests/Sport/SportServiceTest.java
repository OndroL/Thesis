package ServiceTests.Sport;

import cz.inspire.sport.entity.SportEntity;
import cz.inspire.sport.repository.SportRepository;
import cz.inspire.sport.service.SportService;
import jakarta.ejb.FinderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SportServiceTest {

    @Mock
    private SportRepository repository;

    @InjectMocks
    private SportService service;

    @BeforeEach
    void setUp() {
        // The service is automatically injected with the repository mock.
    }

    @Test
    void testFindAll_Success() throws FinderException {
        List<SportEntity> expectedEntities = List.of(
                new SportEntity("sport1", 1, null, null, 10, false, null, false, 1, null, null, null, false, 0, null, null, null, false, 15, 0, 0, 0, 0, null, null, null, null, null, null, null, null, null),
                new SportEntity("sport2", 2, null, null, 20, true, null, true, 2, null, null, null, true, 1, null, null, null, true, 30, 5, 5, 5, 5, null, null, null, null, null, null, null, null, null)
        );
        when(repository.findAll()).thenReturn(expectedEntities);

        List<SportEntity> result = service.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testFindAll_Failure() {
        when(repository.findAll()).thenThrow(new RuntimeException("Database error"));

        FinderException exception = assertThrows(FinderException.class, () -> service.findAll());
        assertTrue(exception.getMessage().contains("Error retrieving all SportEntity records"));

        verify(repository, times(1)).findAll();
    }

    @Test
    void testFindByParent_Success() throws FinderException {
        String parentId = "parent1";
        String jazyk = "cz";
        List<SportEntity> expectedEntities = List.of(
                new SportEntity("sport1", 1, null, null, 10, false, null, false, 1, null, null, null, false, 0, null, null, null, false, 15, 0, 0, 0, 0, null, null, null, null, null, null, null, null, null)
        );
        when(repository.findByParent(eq(parentId), eq(jazyk))).thenReturn(expectedEntities);

        List<SportEntity> result = service.findByParent(parentId, jazyk);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository, times(1)).findByParent(eq(parentId), eq(jazyk));
    }

    @Test
    void testFindByParent_Failure() {
        String parentId = "parent1";
        String jazyk = "cz";
        when(repository.findByParent(eq(parentId), eq(jazyk))).thenThrow(new RuntimeException("Database error"));

        FinderException exception = assertThrows(FinderException.class, () -> service.findByParent(parentId, jazyk));
        assertTrue(exception.getMessage().contains("Error retrieving SportEntity records"));

        verify(repository, times(1)).findByParent(eq(parentId), eq(jazyk));
    }

    @Test
    void testFindByParentWithPagination_Success() throws FinderException {
        String parentId = "parent1";
        String jazyk = "cz";
        int offset = 0, count = 10;
        List<SportEntity> expectedEntities = List.of(
                new SportEntity("sport1", 1, null, null, 10, false, null, false, 1, null, null, null, false, 0, null, null, null, false, 15, 0, 0, 0, 0, null, null, null, null, null, null, null, null, null)
        );
        when(repository.findByParent(eq(parentId), eq(jazyk), eq(count), eq(offset))).thenReturn(expectedEntities);

        List<SportEntity> result = service.findByParent(parentId, jazyk, offset, count);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository, times(1)).findByParent(eq(parentId), eq(jazyk), eq(count), eq(offset));
    }

    @Test
    void testFindByCategory_Success() throws FinderException {
        String categoryId = "category1";
        int offset = 0, count = 10;
        List<SportEntity> expectedEntities = List.of(
                new SportEntity("sport1", 1, null, null, 10, false, null, false, 1, null, null, null, false, 0, null, null, null, false, 15, 0, 0, 0, 0, null, null, null, null, null, null, null, null, null)
        );
        when(repository.findByCategory(eq(categoryId), eq(count), eq(offset))).thenReturn(expectedEntities);

        List<SportEntity> result = service.findByCategory(categoryId, offset, count);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository, times(1)).findByCategory(eq(categoryId), eq(count), eq(offset));
    }

    @Test
    void testFindByCategory_Failure() {
        String categoryId = "category1";
        when(repository.findByCategory(eq(categoryId), anyInt(), anyInt())).thenThrow(new RuntimeException("Database error"));

        FinderException exception = assertThrows(FinderException.class, () -> service.findByCategory(categoryId, 0, 10));
        assertTrue(exception.getMessage().contains("Error retrieving SportEntity records"));

        verify(repository, times(1)).findByCategory(eq(categoryId), anyInt(), anyInt());
    }

    @Test
    void testFindByZbozi_Success() throws FinderException {
        String zboziId = "zbozi1";
        int offset = 0, count = 10;
        List<SportEntity> expectedEntities = List.of(
                new SportEntity("sport1", 1, null, null, 10, false, null, false, 1, null, null, null, false, 0, null, null, null, false, 15, 0, 0, 0, 0, null, null, null, null, null, null, null, null, null)
        );
        when(repository.findByZbozi(eq(zboziId), eq(count), eq(offset))).thenReturn(expectedEntities);

        List<SportEntity> result = service.findByZbozi(zboziId, offset, count);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository, times(1)).findByZbozi(eq(zboziId), eq(count), eq(offset));
    }

    @Test
    void testFindByZbozi_Failure() {
        String zboziId = "zbozi1";
        when(repository.findByZbozi(eq(zboziId), anyInt(), anyInt())).thenThrow(new RuntimeException("Database error"));

        FinderException exception = assertThrows(FinderException.class, () -> service.findByZbozi(zboziId, 0, 10));
        assertTrue(exception.getMessage().contains("Error retrieving SportEntity records"));

        verify(repository, times(1)).findByZbozi(eq(zboziId), anyInt(), anyInt());
    }

    @Test
    void testFindRoot_Success() throws FinderException {
        String jazyk = "cz";
        List<SportEntity> expectedEntities = List.of(
                new SportEntity("sport1", 1, null, null, 10, false, null, false, 1, null, null, null, false, 0, null, null, null, false, 15, 0, 0, 0, 0, null, null, null, null, null, null, null, null, null)
        );
        when(repository.findRoot(eq(jazyk))).thenReturn(expectedEntities);

        List<SportEntity> result = service.findRoot(jazyk);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository, times(1)).findRoot(eq(jazyk));
    }

    @Test
    void testFindRoot_Failure() {
        String jazyk = "cz";
        when(repository.findRoot(eq(jazyk))).thenThrow(new RuntimeException("Database error"));

        FinderException exception = assertThrows(FinderException.class, () -> service.findRoot(jazyk));
        assertTrue(exception.getMessage().contains("Error retrieving root SportEntity records"));

        verify(repository, times(1)).findRoot(eq(jazyk));
    }

    @Test
    void testFindRootWithPagination_Success() throws FinderException {
        String jazyk = "cz";
        int offset = 0, count = 10;
        List<SportEntity> expectedEntities = List.of(
                new SportEntity("sport1", 1, null, null, 10, false, null, false, 1, null, null, null, false, 0, null, null, null, false, 15, 0, 0, 0, 0, null, null, null, null, null, null, null, null, null)
        );
        when(repository.findRoot(eq(jazyk), eq(count), eq(offset))).thenReturn(expectedEntities);

        List<SportEntity> result = service.findRoot(jazyk, offset, count);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository, times(1)).findRoot(eq(jazyk), eq(count), eq(offset));
    }

    @Test
    void testFindCategoryRoot_Success() throws FinderException {
        int offset = 0, count = 10;
        List<SportEntity> expectedEntities = List.of(
                new SportEntity("sport1", 1, null, null, 10, false, null, false, 1, null, null, null, false, 0, null, null, null, false, 15, 0, 0, 0, 0, null, null, null, null, null, null, null, null, null)
        );
        when(repository.findCategoryRoot(eq(count), eq(offset))).thenReturn(expectedEntities);

        List<SportEntity> result = service.findCategoryRoot(offset, count);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository, times(1)).findCategoryRoot(eq(count), eq(offset));
    }

    @Test
    void testFindCategoryRoot_Failure() {
        when(repository.findCategoryRoot(anyInt(), anyInt())).thenThrow(new RuntimeException("Database error"));

        FinderException exception = assertThrows(FinderException.class, () -> service.findCategoryRoot(0, 10));
        assertTrue(exception.getMessage().contains("Error retrieving root category SportEntity records"));

        verify(repository, times(1)).findCategoryRoot(anyInt(), anyInt());
    }

    @Test
    void testCountCategoryRoot_Success() throws FinderException {
        when(repository.countCategoryRoot()).thenReturn(5L);

        Long result = service.countCategoryRoot();

        assertNotNull(result);
        assertEquals(5L, result);
        verify(repository, times(1)).countCategoryRoot();
    }

    @Test
    void testCountCategoryRoot_Failure() {
        when(repository.countCategoryRoot()).thenThrow(new RuntimeException("Database error"));

        FinderException exception = assertThrows(FinderException.class, () -> service.countCategoryRoot());
        assertTrue(exception.getMessage().contains("Error retrieving count of root category SportEntity records"));

        verify(repository, times(1)).countCategoryRoot();
    }

    @Test
    void testCountAllByCategory_Success() throws FinderException {
        String categoryId = "category1";
        when(repository.countAllByCategory(eq(categoryId))).thenReturn(15L);

        Long result = service.countAllByCategory(categoryId);

        assertNotNull(result);
        assertEquals(15L, result);
        verify(repository, times(1)).countAllByCategory(eq(categoryId));
    }

    @Test
    void testCountAllByCategory_Failure() {
        String categoryId = "category1";
        when(repository.countAllByCategory(eq(categoryId))).thenThrow(new RuntimeException("Database error"));

        FinderException exception = assertThrows(FinderException.class, () -> service.countAllByCategory(categoryId));
        assertTrue(exception.getMessage().contains("Error retrieving count of SportEntity records"));

        verify(repository, times(1)).countAllByCategory(eq(categoryId));
    }

    @Test
    void testCountAllByParentAndLanguage_Success() throws FinderException {
        String parentId = "parent1";
        String language = "cz";
        when(repository.countAllByParentAndLanguage(eq(parentId), eq(language))).thenReturn(25L);

        Long result = service.countAllByParentAndLanguage(parentId, language);

        assertNotNull(result);
        assertEquals(25L, result);
        verify(repository, times(1)).countAllByParentAndLanguage(eq(parentId), eq(language));
    }

    @Test
    void testCountAllByParentAndLanguage_Failure() {
        String parentId = "parent1";
        String language = "cz";
        when(repository.countAllByParentAndLanguage(eq(parentId), eq(language))).thenThrow(new RuntimeException("Database error"));

        FinderException exception = assertThrows(FinderException.class, () -> service.countAllByParentAndLanguage(parentId, language));
        assertTrue(exception.getMessage().contains("Error retrieving count of SportEntity records"));

        verify(repository, times(1)).countAllByParentAndLanguage(eq(parentId), eq(language));
    }

    @Test
    void testGetAllIdsByParentAndLanguage_Success() throws FinderException {
        String parentId = "parent1";
        String language = "cz";
        List<String> expectedIds = List.of("sport1", "sport2");
        when(repository.getAllIdsByParentAndLanguage(eq(parentId), eq(language))).thenReturn(expectedIds);

        List<String> result = service.getAllIdsByParentAndLanguage(parentId, language);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(repository, times(1)).getAllIdsByParentAndLanguage(eq(parentId), eq(language));
    }

    @Test
    void testGetAllIdsByParentAndLanguage_Failure() {
        String parentId = "parent1";
        String language = "cz";
        when(repository.getAllIdsByParentAndLanguage(eq(parentId), eq(language))).thenThrow(new RuntimeException("Database error"));

        FinderException exception = assertThrows(FinderException.class, () -> service.getAllIdsByParentAndLanguage(parentId, language));
        assertTrue(exception.getMessage().contains("Error retrieving SportEntity IDs"));

        verify(repository, times(1)).getAllIdsByParentAndLanguage(eq(parentId), eq(language));
    }

    @Test
    void testCountRootByLanguage_Success() throws FinderException {
        String language = "cz";
        when(repository.countRootByLanguage(eq(language))).thenReturn(8L);

        Long result = service.countRootByLanguage(language);

        assertNotNull(result);
        assertEquals(8L, result);
        verify(repository, times(1)).countRootByLanguage(eq(language));
    }

    @Test
    void testCountRootByLanguage_Failure() {
        String language = "cz";
        when(repository.countRootByLanguage(eq(language))).thenThrow(new RuntimeException("Database error"));

        FinderException exception = assertThrows(FinderException.class, () -> service.countRootByLanguage(language));
        assertTrue(exception.getMessage().contains("Error retrieving count of root SportEntity records"));

        verify(repository, times(1)).countRootByLanguage(eq(language));
    }

    @Test
    void testGetRootIdsByLanguage_Success() throws FinderException {
        String language = "cz";
        List<String> expectedIds = List.of("sport1", "sport2");
        when(repository.getRootIdsByLanguage(eq(language))).thenReturn(expectedIds);

        List<String> result = service.getRootIdsByLanguage(language);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(repository, times(1)).getRootIdsByLanguage(eq(language));
    }

    @Test
    void testGetRootIdsByLanguage_Failure() {
        String language = "cz";
        when(repository.getRootIdsByLanguage(eq(language))).thenThrow(new RuntimeException("Database error"));

        FinderException exception = assertThrows(FinderException.class, () -> service.getRootIdsByLanguage(language));
        assertTrue(exception.getMessage().contains("Error retrieving root SportEntity IDs"));

        verify(repository, times(1)).getRootIdsByLanguage(eq(language));
    }
}
