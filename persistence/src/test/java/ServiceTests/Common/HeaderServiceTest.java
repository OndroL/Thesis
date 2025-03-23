package ServiceTests.Common;

import cz.inspire.common.entity.HeaderEntity;
import cz.inspire.common.repository.HeaderRepository;
import cz.inspire.common.service.HeaderService;
import cz.inspire.exception.SystemException;
import jakarta.ejb.CreateException;
import jakarta.ejb.FinderException;
import jakarta.ejb.RemoveException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HeaderServiceTest {

    @Mock
    private HeaderRepository headerRepository;

    private HeaderService headerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        headerService = new HeaderService(headerRepository);
    }

    @Test
    void testFindValidAttributes_Success() throws FinderException {
        List<HeaderEntity> expectedList = Arrays.asList(
                new HeaderEntity("1", 42, 7),
                new HeaderEntity("2", 24, 9)
        );
        when(headerRepository.findValidAttributes()).thenReturn(expectedList);

        List<HeaderEntity> result = headerService.findValidAttributes();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(headerRepository, times(1)).findValidAttributes();
    }

    @Test
    void testFindValidAttributes_Failure() {
        when(headerRepository.findValidAttributes()).thenThrow(new RuntimeException("DB error"));

        FinderException ex = assertThrows(FinderException.class, () -> headerService.findValidAttributes());
        assertEquals("Unexpected error (Operation: Error retrieving valid attributes from HeaderEntity) - Cause: DB error", ex.getMessage());
        verify(headerRepository, times(1)).findValidAttributes();
    }

    @Test
    void testCreate_Success() throws CreateException {
        HeaderEntity entity = new HeaderEntity("1", 42, 7);
        when(headerRepository.create(entity)).thenReturn(entity);

        HeaderEntity result = headerService.create(entity);

        assertEquals(entity, result);
        verify(headerRepository, times(1)).create(entity);
    }

    @Test
    void testCreate_Failure() {
        HeaderEntity entity = new HeaderEntity("1", 42, 7);
        when(headerRepository.create(entity)).thenThrow(new RuntimeException("DB error"));

        CreateException ex = assertThrows(CreateException.class, () -> headerService.create(entity));
        assertEquals("Failed to create HeaderEntity", ex.getMessage());
        verify(headerRepository, times(1)).create(entity);
    }

    @Test
    void testUpdate_Success() throws SystemException {
        HeaderEntity entity = new HeaderEntity("1", 42, 7);
        when(headerRepository.update(entity)).thenReturn(entity);

        HeaderEntity result = headerService.update(entity);

        assertEquals(entity, result);
        verify(headerRepository, times(1)).update(entity);
    }

    @Test
    void testUpdate_Failure() {
        HeaderEntity entity = new HeaderEntity("1", 42, 7);
        when(headerRepository.update(entity)).thenThrow(new RuntimeException("DB error"));

        SystemException ex = assertThrows(SystemException.class, () -> headerService.update(entity));
        assertEquals("Failed to update HeaderEntity", ex.getMessage());
        verify(headerRepository, times(1)).update(entity);
    }

    @Test
    void testDelete_Success() throws RemoveException {
        HeaderEntity entity = new HeaderEntity("1", 42, 7);
        doNothing().when(headerRepository).delete(entity);

        headerService.delete(entity);

        verify(headerRepository, times(1)).delete(entity);
    }

    @Test
    void testDelete_Failure() {
        HeaderEntity entity = new HeaderEntity("1", 42, 7);
        doThrow(new RuntimeException("DB error")).when(headerRepository).delete(entity);

        RemoveException ex = assertThrows(RemoveException.class, () -> headerService.delete(entity));
        assertEquals("Failed to remove HeaderEntity", ex.getMessage());
        verify(headerRepository, times(1)).delete(entity);
    }

    @Test
    void testDelete_NullEntity() {
        RemoveException ex = assertThrows(RemoveException.class, () -> headerService.delete(null));
        assertEquals("Cannot delete null as HeaderEntity", ex.getMessage());
    }

    @Test
    void testFindByPrimaryKey_Found() throws FinderException {
        String id = "1";
        HeaderEntity entity = new HeaderEntity(id, 42, 7);
        when(headerRepository.findByPrimaryKey(id)).thenReturn(entity);

        HeaderEntity result = headerService.findByPrimaryKey(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(headerRepository, times(1)).findByPrimaryKey(id);
    }

    @Test
    void testFindByPrimaryKey_NullPK() throws FinderException {
        HeaderEntity result = headerService.findByPrimaryKey(null);
        assertNull(result);
    }

    @Test
    void testDeleteByPrimaryKey_Success() throws RemoveException {
        String id = "1";
        doNothing().when(headerRepository).deleteByPrimaryKey(id);

        headerService.deleteByPrimaryKey(id);

        verify(headerRepository, times(1)).deleteByPrimaryKey(id);
    }

    @Test
    void testDeleteByPrimaryKey_Null() {
        RemoveException ex = assertThrows(RemoveException.class, () -> headerService.deleteByPrimaryKey(null));
        assertEquals("Cannot delete HeaderEntity with null primary key", ex.getMessage());
    }

    @Test
    void testDeleteByPrimaryKey_Failure() {
        String id = "1";
        doThrow(new RuntimeException("DB error")).when(headerRepository).deleteByPrimaryKey(id);

        RemoveException ex = assertThrows(RemoveException.class, () -> headerService.deleteByPrimaryKey(id));
        assertEquals("Failed to remove HeaderEntity", ex.getMessage());
        verify(headerRepository, times(1)).deleteByPrimaryKey(id);
    }
}
