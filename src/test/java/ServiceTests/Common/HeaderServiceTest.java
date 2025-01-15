package ServiceTests.Common;

import cz.inspire.common.entity.HeaderEntity;
import cz.inspire.common.repository.HeaderRepository;
import cz.inspire.common.service.HeaderService;
import cz.inspire.exception.SystemException;
import jakarta.ejb.CreateException;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class HeaderServiceTest {

    @Mock
    private HeaderRepository headerRepository;

    @Mock
    private Logger logger;

    @InjectMocks
    private HeaderService headerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate_Success() throws CreateException {
        HeaderEntity entity = new HeaderEntity("1", 42, 7);

        headerService.create(entity);

        verify(headerRepository, times(1)).save(entity);
    }

    @Test
    void testCreate_Failure() {
        HeaderEntity entity = new HeaderEntity("1", 42, 7);
        doThrow(RuntimeException.class).when(headerRepository).save(entity);

        assertThrows(CreateException.class, () -> headerService.create(entity));
        verify(logger, times(1)).error(eq("Failed to create HeaderEntity"), any(RuntimeException.class));
    }

    @Test
    void testUpdate_Success() throws SystemException {
        HeaderEntity entity = new HeaderEntity("1", 42, 7);

        headerService.update(entity);

        verify(headerRepository, times(1)).save(entity);
    }

    @Test
    void testUpdate_Failure() {
        HeaderEntity entity = new HeaderEntity("1", 42, 7);
        doThrow(RuntimeException.class).when(headerRepository).save(entity);

        SystemException exception = assertThrows(SystemException.class, () -> headerService.update(entity));
        assertEquals("Failed to update HeaderEntity", exception.getMessage());
        verify(logger, times(1)).error(eq("Failed to update HeaderEntity"), any(RuntimeException.class));
    }

    @Test
    void testRemove_Success() throws SystemException {
        HeaderEntity entity = new HeaderEntity("1", 42, 7);

        headerService.remove(entity);

        verify(headerRepository, times(1)).remove(entity);
    }

    @Test
    void testRemove_Failure() {
        HeaderEntity entity = new HeaderEntity("1", 42, 7);

        doThrow(RuntimeException.class).when(headerRepository).remove(entity);

        SystemException exception = assertThrows(SystemException.class, () -> headerService.remove(entity));

        assertEquals("Failed to remove HeaderEntity", exception.getMessage());

        verify(logger, times(1)).error(eq("Failed to remove HeaderEntity"), any(RuntimeException.class));
    }

    @Test
    void testFindValidAttributes() {
        List<HeaderEntity> expectedList = Arrays.asList(
                new HeaderEntity("1", 42, 7),
                new HeaderEntity("2", 24, 9)
        );
        when(headerRepository.findValidAtributes()).thenReturn(expectedList);

        List<HeaderEntity> result = headerService.findValidAttributes();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("1", result.getFirst().getId());
        verify(headerRepository, times(1)).findValidAtributes();
    }
}
