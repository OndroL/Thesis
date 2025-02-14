package ServiceTests.Common;

import cz.inspire.common.entity.HeaderEntity;
import cz.inspire.common.repository.HeaderRepository;
import cz.inspire.common.service.HeaderService;
import cz.inspire.enterprise.exception.SystemException;
import jakarta.ejb.CreateException;
import jakarta.ejb.FinderException;
import jakarta.ejb.RemoveException;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HeaderServiceTest {

    @Mock
    private HeaderRepository headerRepository;

    @Mock
    private EntityManager em;

    private HeaderService headerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        headerService = new HeaderService(headerRepository);
        headerService.setEntityManager(em);
    }

    @Test
    void testCreate_Success() throws CreateException {
        HeaderEntity entity = new HeaderEntity("1", 42, 7);

        doNothing().when(em).persist(entity);
        doNothing().when(em).flush();

        headerService.create(entity);

        verify(em, times(1)).persist(entity);
        verify(em, times(1)).flush();
    }

    @Test
    void testCreate_Failure() {
        HeaderEntity entity = new HeaderEntity("1", 42, 7);

        doThrow(new RuntimeException("Database failure")).when(em).persist(entity);

        CreateException exception = assertThrows(CreateException.class, () -> headerService.create(entity));
        assertEquals("Failed to create HeaderEntity", exception.getMessage());

        verify(em, times(1)).persist(entity);
    }

    @Test
    void testUpdate_Success() throws SystemException {
        HeaderEntity entity = new HeaderEntity("1", 42, 7);

        when(em.merge(entity)).thenReturn(entity);
        doNothing().when(em).flush();

        headerService.update(entity);

        verify(em, times(1)).merge(entity);
        verify(em, times(1)).flush();
    }

    @Test
    void testUpdate_Failure() {
        HeaderEntity entity = new HeaderEntity("1", 42, 7);

        doThrow(new RuntimeException("Database failure")).when(em).merge(entity);

        SystemException exception = assertThrows(SystemException.class, () -> headerService.update(entity));
        assertEquals("Failed to update HeaderEntity", exception.getMessage());

        verify(em, times(1)).merge(entity);
    }

    @Test
    void testRemove_Success() throws RemoveException {
        HeaderEntity entity = new HeaderEntity("1", 42, 7);

        when(em.merge(entity)).thenReturn(entity);
        doNothing().when(em).remove(entity);
        doNothing().when(em).flush();

        headerService.delete(entity);

        verify(em, times(1)).merge(entity);
        verify(em, times(1)).remove(entity);
        verify(em, times(1)).flush();
    }

    @Test
    void testRemove_Failure() {
        HeaderEntity entity = new HeaderEntity("1", 42, 7);

        when(em.merge(entity)).thenReturn(entity);
        doThrow(new RuntimeException("Database failure")).when(em).remove(entity);

        RemoveException exception = assertThrows(RemoveException.class, () -> headerService.delete(entity));
        assertEquals("Failed to remove HeaderEntity", exception.getMessage());

        verify(em, times(1)).merge(entity);
        verify(em, times(1)).remove(entity);
    }

    @Test
    void testFindValidAttributes() throws FinderException {
        List<HeaderEntity> expectedList = Arrays.asList(
                new HeaderEntity("1", 42, 7),
                new HeaderEntity("2", 24, 9)
        );
        when(headerRepository.findValidAttributes()).thenReturn(expectedList);

        List<HeaderEntity> result = headerService.findValidAttributes();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("1", result.getFirst().getId());
        verify(headerRepository, times(1)).findValidAttributes();
    }

    @Test
    void testFindByPrimaryKey_Found() throws FinderException {
        String entityId = "1";
        HeaderEntity entity = new HeaderEntity(entityId, 42, 7);

        when(headerRepository.findById(entityId)).thenReturn(Optional.of(entity));

        HeaderEntity result = headerService.findByPrimaryKey(entityId);

        assertNotNull(result);
        assertEquals(entityId, result.getId());
        verify(headerRepository, times(1)).findById(entityId);
    }

    @Test
    void testFindByPrimaryKey_NotFound() {
        String entityId = "1";

        when(headerRepository.findById(entityId)).thenReturn(Optional.empty());

        FinderException exception = assertThrows(FinderException.class, () -> headerService.findByPrimaryKey(entityId));
        assertEquals("Failed to find HeaderEntity with primary key: " + entityId, exception.getMessage());

        verify(headerRepository, times(1)).findById(entityId);
    }

    @Test
    void testFindById_Found() throws FinderException {
        String entityId = "1";
        HeaderEntity entity = new HeaderEntity(entityId, 42, 7);

        when(headerRepository.findById(entityId)).thenReturn(Optional.of(entity));

        Optional<HeaderEntity> result = headerService.findById(entityId);

        assertTrue(result.isPresent());
        assertEquals(entityId, result.get().getId());
        verify(headerRepository, times(1)).findById(entityId);
    }

    @Test
    void testFindById_NotFound() throws FinderException {
        String entityId = "1";

        when(headerRepository.findById(entityId)).thenReturn(Optional.empty());

        Optional<HeaderEntity> result = headerService.findById(entityId);

        assertFalse(result.isPresent());
        verify(headerRepository, times(1)).findById(entityId);
    }

    @Test
    void testDelete_NullEntity() {
        RemoveException exception = assertThrows(RemoveException.class, () -> headerService.delete(null));
        assertEquals("Cannot delete null entity in HeaderEntity", exception.getMessage());
    }
}
