package ServiceTests.Common;

import cz.inspire.common.entity.NastaveniJsonEntity;
import cz.inspire.common.repository.NastaveniJsonRepository;
import cz.inspire.common.service.NastaveniJsonService;
import cz.inspire.enterprise.exception.SystemException;
import jakarta.ejb.CreateException;
import jakarta.ejb.RemoveException;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NastaveniJsonServiceTest {

    @Mock
    private NastaveniJsonRepository nastaveniJsonRepository;

    @Mock
    private EntityManager em;

    private NastaveniJsonService nastaveniJsonService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        nastaveniJsonService = new NastaveniJsonService(nastaveniJsonRepository);
        nastaveniJsonService.setEntityManager(em);
    }

    @Test
    void testCreate_Success() throws CreateException {
        NastaveniJsonEntity entity = new NastaveniJsonEntity("key1", "value1");

        doNothing().when(em).persist(entity);
        doNothing().when(em).flush();

        nastaveniJsonService.create(entity);

        verify(em, times(1)).persist(entity);
        verify(em, times(1)).flush();
    }

    @Test
    void testCreate_Failure() {
        NastaveniJsonEntity entity = new NastaveniJsonEntity("key1", "value1");

        doThrow(new RuntimeException("Database failure")).when(em).persist(entity);

        CreateException exception = assertThrows(CreateException.class, () -> nastaveniJsonService.create(entity));
        assertEquals("Failed to create NastaveniJsonEntity", exception.getMessage());

        verify(em, times(1)).persist(entity);
    }

    @Test
    void testUpdate_Success() throws SystemException {
        NastaveniJsonEntity entity = new NastaveniJsonEntity("key1", "value1");

        when(em.merge(entity)).thenReturn(entity);
        doNothing().when(em).flush();

        nastaveniJsonService.update(entity);

        verify(em, times(1)).merge(entity);
        verify(em, times(1)).flush();
    }

    @Test
    void testUpdate_Failure() {
        NastaveniJsonEntity entity = new NastaveniJsonEntity("key1", "value1");

        doThrow(new RuntimeException("Database failure")).when(em).merge(entity);

        SystemException exception = assertThrows(SystemException.class, () -> nastaveniJsonService.update(entity));
        assertEquals("Failed to update NastaveniJsonEntity", exception.getMessage());

        verify(em, times(1)).merge(entity);
    }

    @Test
    void testRemove_Success() throws RemoveException {
        NastaveniJsonEntity entity = new NastaveniJsonEntity("key1", "value1");

        when(em.merge(entity)).thenReturn(entity);
        doNothing().when(em).remove(entity);
        doNothing().when(em).flush();

        nastaveniJsonService.delete(entity);

        verify(em, times(1)).merge(entity);
        verify(em, times(1)).remove(entity);
        verify(em, times(1)).flush();
    }

    @Test
    void testRemove_Failure() {
        NastaveniJsonEntity entity = new NastaveniJsonEntity("key1", "value1");

        when(em.merge(entity)).thenReturn(entity);
        doThrow(new RuntimeException("Database failure")).when(em).remove(entity);

        RemoveException exception = assertThrows(RemoveException.class, () -> nastaveniJsonService.delete(entity));
        assertEquals("Failed to remove NastaveniJsonEntity", exception.getMessage());

        verify(em, times(1)).merge(entity);
        verify(em, times(1)).remove(entity);
    }

    @Test
    void testDelete_NullEntity() {
        RemoveException exception = assertThrows(RemoveException.class, () -> nastaveniJsonService.delete(null));
        assertEquals("Cannot delete null entity in NastaveniJsonEntity", exception.getMessage());
    }
}
