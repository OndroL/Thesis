package ServiceTests.Common;

import cz.inspire.common.entity.NastaveniJsonEntity;
import cz.inspire.common.repository.NastaveniJsonRepository;
import cz.inspire.common.service.NastaveniJsonService;
import cz.inspire.enterprise.exception.SystemException;
import jakarta.ejb.CreateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NastaveniJsonServiceTest {

    @Mock
    private NastaveniJsonRepository nastaveniJsonRepository;

    @Spy
    private NastaveniJsonService nastaveniJsonService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        nastaveniJsonService = spy(new NastaveniJsonService(nastaveniJsonRepository));
    }

    @Test
    void testCreate_Success() throws CreateException {
        NastaveniJsonEntity entity = new NastaveniJsonEntity("key1", "value1");

        nastaveniJsonService.create(entity);

        verify(nastaveniJsonService, times(1)).create(entity);
        verify(nastaveniJsonRepository, times(1)).save(entity);
    }

    @Test
    void testCreate_Failure() throws CreateException {
        NastaveniJsonEntity entity = new NastaveniJsonEntity("key1", "value1");
        doThrow(new RuntimeException("Database failure")).when(nastaveniJsonRepository).save(entity);

        assertThrows(CreateException.class, () -> nastaveniJsonService.create(entity));

        verify(nastaveniJsonService, times(1)).create(entity);
    }

    @Test
    void testUpdate_Success() throws SystemException {
        NastaveniJsonEntity entity = new NastaveniJsonEntity("key1", "value1");

        nastaveniJsonService.update(entity);

        verify(nastaveniJsonService, times(1)).update(entity);
        verify(nastaveniJsonRepository, times(1)).save(entity);
    }

    @Test
    void testUpdate_Failure() throws SystemException {
        NastaveniJsonEntity entity = new NastaveniJsonEntity("key1", "value1");
        doThrow(new RuntimeException("Database failure")).when(nastaveniJsonRepository).save(entity);

        assertThrows(SystemException.class, () -> nastaveniJsonService.update(entity));

        verify(nastaveniJsonService, times(1)).update(entity);
    }

    @Test
    void testRemove_Success() throws SystemException {
        NastaveniJsonEntity entity = new NastaveniJsonEntity("key1", "value1");

        nastaveniJsonService.delete(entity);

        verify(nastaveniJsonService, times(1)).delete(entity);
        verify(nastaveniJsonRepository, times(1)).delete(entity);
    }

    @Test
    void testRemove_Failure() throws SystemException {
        NastaveniJsonEntity entity = new NastaveniJsonEntity("key1", "value1");
        doThrow(new RuntimeException("Database failure")).when(nastaveniJsonRepository).delete(entity);

        assertThrows(SystemException.class, () -> nastaveniJsonService.delete(entity));

        verify(nastaveniJsonService, times(1)).delete(entity);
    }
}
