package ServiceTests.CommonServer;

import cz.inspire.common.entity.NastaveniJsonEntity;
import cz.inspire.common.repository.NastaveniJsonRepository;
import cz.inspire.common.service.NastaveniJsonService;
import cz.inspire.exception.SystemException;
import jakarta.ejb.CreateException;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class NastaveniJsonServiceTest {

    @Mock
    private NastaveniJsonRepository nastaveniJsonRepository;

    @Mock
    private Logger logger;

    @InjectMocks
    private NastaveniJsonService nastaveniJsonService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate_Success() throws CreateException {
        NastaveniJsonEntity entity = new NastaveniJsonEntity("key1", "value1");

        nastaveniJsonService.create(entity);

        verify(nastaveniJsonRepository, times(1)).save(entity);
    }

    @Test
    void testCreate_Failure() {
        NastaveniJsonEntity entity = new NastaveniJsonEntity("key1", "value1");
        doThrow(RuntimeException.class).when(nastaveniJsonRepository).save(entity);

        assertThrows(CreateException.class, () -> nastaveniJsonService.create(entity));
        verify(logger, times(1)).error(eq("Failed to create NastaveniJsonEntity"), any(RuntimeException.class));
    }

    @Test
    void testUpdate_Success() throws SystemException {
        NastaveniJsonEntity entity = new NastaveniJsonEntity("key1", "value1");

        nastaveniJsonService.update(entity);

        verify(nastaveniJsonRepository, times(1)).save(entity);
    }

    @Test
    void testUpdate_Failure() {
        NastaveniJsonEntity entity = new NastaveniJsonEntity("key1", "value1");
        doThrow(RuntimeException.class).when(nastaveniJsonRepository).save(entity);

        SystemException exception = assertThrows(SystemException.class, () -> nastaveniJsonService.update(entity));
        assertEquals("Failed to update NastaveniJsonEntity", exception.getMessage());
        verify(logger, times(1)).error(eq("Failed to update NastaveniJsonEntity"), any(RuntimeException.class));
    }

    @Test
    void testRemove_Success() throws SystemException {
        NastaveniJsonEntity entity = new NastaveniJsonEntity("key1", "value1");

        nastaveniJsonService.remove(entity);

        verify(nastaveniJsonRepository, times(1)).remove(entity);
    }

    @Test
    void testRemove_Failure() {
        NastaveniJsonEntity entity = new NastaveniJsonEntity("key1", "value1");
        doThrow(RuntimeException.class).when(nastaveniJsonRepository).remove(entity);

        SystemException exception = assertThrows(SystemException.class, () -> nastaveniJsonService.remove(entity));
        assertEquals("Failed to remove NastaveniJsonEntity", exception.getMessage());
        verify(logger, times(1)).error(eq("Failed to remove NastaveniJsonEntity"), any(RuntimeException.class));
    }
}

