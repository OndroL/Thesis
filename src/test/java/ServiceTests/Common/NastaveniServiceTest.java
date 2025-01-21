package ServiceTests.Common;

import cz.inspire.common.entity.NastaveniEntity;
import cz.inspire.common.repository.NastaveniRepository;
import cz.inspire.common.service.NastaveniService;
import cz.inspire.enterprise.exception.SystemException;
import jakarta.ejb.CreateException;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class NastaveniServiceTest {

    @Mock
    private NastaveniRepository nastaveniRepository;

    @Mock
    private Logger logger;

    @InjectMocks
    private NastaveniService nastaveniService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate_Success() throws CreateException {
        NastaveniEntity entity = new NastaveniEntity("key1", "value1");

        nastaveniService.create(entity);

        verify(nastaveniRepository, times(1)).save(entity);
    }

    @Test
    void testCreate_Failure() {
        NastaveniEntity entity = new NastaveniEntity("key1", "value1");
        doThrow(RuntimeException.class).when(nastaveniRepository).save(entity);

        assertThrows(CreateException.class, () -> nastaveniService.create(entity));
        verify(logger, times(1)).error(eq("Failed to create NastaveniEntity"), any(RuntimeException.class));
    }

    @Test
    void testUpdate_Success() throws SystemException {
        NastaveniEntity entity = new NastaveniEntity("key1", "value1");

        nastaveniService.update(entity);

        verify(nastaveniRepository, times(1)).save(entity);
    }

    @Test
    void testUpdate_Failure() {
        NastaveniEntity entity = new NastaveniEntity("key1", "value1");
        doThrow(RuntimeException.class).when(nastaveniRepository).save(entity);

        SystemException exception = assertThrows(SystemException.class, () -> nastaveniService.update(entity));
        assertEquals("Failed to update NastaveniEntity", exception.getMessage());
        verify(logger, times(1)).error(eq("Failed to update NastaveniEntity"), any(RuntimeException.class));
    }

    @Test
    void testRemove_Success() throws SystemException {
        NastaveniEntity entity = new NastaveniEntity("key1", "value1");

        nastaveniService.delete(entity);

        verify(nastaveniRepository, times(1)).delete(entity);
    }

    @Test
    void testRemove_Failure() {
        NastaveniEntity entity = new NastaveniEntity("key1", "value1");
        doThrow(RuntimeException.class).when(nastaveniRepository).delete(entity);

        SystemException exception = assertThrows(SystemException.class, () -> nastaveniService.delete(entity));
        assertEquals("Failed to remove NastaveniEntity", exception.getMessage());
        verify(logger, times(1)).error(eq("Failed to remove NastaveniEntity"), any(RuntimeException.class));
    }
}

