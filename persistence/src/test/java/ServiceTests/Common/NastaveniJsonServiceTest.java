package ServiceTests.Common;

import cz.inspire.common.entity.NastaveniJsonEntity;
import cz.inspire.common.repository.NastaveniJsonRepository;
import cz.inspire.common.service.NastaveniJsonService;
import cz.inspire.exception.SystemException;
import jakarta.ejb.CreateException;
import jakarta.ejb.RemoveException;
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

    private NastaveniJsonService nastaveniJsonService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        nastaveniJsonService = new NastaveniJsonService(nastaveniJsonRepository);
    }

    @Test
    void testCreate_Success() throws CreateException {
        NastaveniJsonEntity entity = new NastaveniJsonEntity("key1", "value1");
        when(nastaveniJsonRepository.create(entity)).thenReturn(entity);

        NastaveniJsonEntity result = nastaveniJsonService.create(entity);

        assertEquals(entity, result);
        verify(nastaveniJsonRepository, times(1)).create(entity);
    }

    @Test
    void testCreate_Failure() {
        NastaveniJsonEntity entity = new NastaveniJsonEntity("key1", "value1");
        when(nastaveniJsonRepository.create(entity)).thenThrow(new RuntimeException("DB error"));

        CreateException ex = assertThrows(CreateException.class, () -> nastaveniJsonService.create(entity));
        assertEquals("Failed to create NastaveniJsonEntity", ex.getMessage());
        verify(nastaveniJsonRepository, times(1)).create(entity);
    }

    @Test
    void testUpdate_Success() throws SystemException {
        NastaveniJsonEntity entity = new NastaveniJsonEntity("key1", "value1");
        when(nastaveniJsonRepository.update(entity)).thenReturn(entity);

        NastaveniJsonEntity result = nastaveniJsonService.update(entity);

        assertEquals(entity, result);
        verify(nastaveniJsonRepository, times(1)).update(entity);
    }

    @Test
    void testUpdate_Failure() {
        NastaveniJsonEntity entity = new NastaveniJsonEntity("key1", "value1");
        when(nastaveniJsonRepository.update(entity)).thenThrow(new RuntimeException("DB error"));

        SystemException ex = assertThrows(SystemException.class, () -> nastaveniJsonService.update(entity));
        assertEquals("Failed to update NastaveniJsonEntity", ex.getMessage());
        verify(nastaveniJsonRepository, times(1)).update(entity);
    }

    @Test
    void testDelete_Success() throws RemoveException {
        NastaveniJsonEntity entity = new NastaveniJsonEntity("key1", "value1");
        doNothing().when(nastaveniJsonRepository).delete(entity);

        nastaveniJsonService.delete(entity);

        verify(nastaveniJsonRepository, times(1)).delete(entity);
    }

    @Test
    void testDelete_Failure() {
        NastaveniJsonEntity entity = new NastaveniJsonEntity("key1", "value1");
        doThrow(new RuntimeException("DB error")).when(nastaveniJsonRepository).delete(entity);

        RemoveException ex = assertThrows(RemoveException.class, () -> nastaveniJsonService.delete(entity));
        assertEquals("Failed to remove NastaveniJsonEntity", ex.getMessage());
        verify(nastaveniJsonRepository, times(1)).delete(entity);
    }

    @Test
    void testDelete_NullEntity() {
        RemoveException ex = assertThrows(RemoveException.class, () -> nastaveniJsonService.delete(null));
        assertEquals("Cannot delete null as NastaveniJsonEntity", ex.getMessage());
    }
}
