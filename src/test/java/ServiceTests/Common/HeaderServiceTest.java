package ServiceTests.Common;

import cz.inspire.common.entity.HeaderEntity;
import cz.inspire.common.repository.HeaderRepository;
import cz.inspire.common.service.HeaderService;
import cz.inspire.enterprise.exception.SystemException;
import jakarta.ejb.CreateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HeaderServiceTest {

    @Mock
    private HeaderRepository headerRepository;

    @Spy
    private HeaderService headerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        headerService = spy(new HeaderService(headerRepository));
    }

    @Test
    void testCreate_Success() throws CreateException {
        HeaderEntity entity = new HeaderEntity("1", 42, 7);

        headerService.create(entity);

        verify(headerService, times(1)).create(entity);
        verify(headerRepository, times(1)).save(entity);
    }

    @Test
    void testCreate_Failure() throws CreateException {
        HeaderEntity entity = new HeaderEntity("1", 42, 7);
        doThrow(new RuntimeException("Database failure")).when(headerRepository).save(entity);

        assertThrows(CreateException.class, () -> headerService.create(entity));

        verify(headerService, times(1)).create(entity);
    }

    @Test
    void testUpdate_Success() throws SystemException {
        HeaderEntity entity = new HeaderEntity("1", 42, 7);

        headerService.update(entity);

        verify(headerService, times(1)).update(entity);
        verify(headerRepository, times(1)).save(entity);
    }

    @Test
    void testUpdate_Failure() throws SystemException {
        HeaderEntity entity = new HeaderEntity("1", 42, 7);
        doThrow(new RuntimeException("Database failure")).when(headerRepository).save(entity);

        assertThrows(SystemException.class, () -> headerService.update(entity));

        verify(headerService, times(1)).update(entity);
    }

    @Test
    void testRemove_Success() throws SystemException {
        HeaderEntity entity = new HeaderEntity("1", 42, 7);

        headerService.delete(entity);

        verify(headerService, times(1)).delete(entity);
        verify(headerRepository, times(1)).delete(entity);
    }

    @Test
    void testRemove_Failure() throws SystemException {
        HeaderEntity entity = new HeaderEntity("1", 42, 7);
        doThrow(new RuntimeException("Database failure")).when(headerRepository).delete(entity);

        assertThrows(SystemException.class, () -> headerService.delete(entity));

        verify(headerService, times(1)).delete(entity);
    }

    @Test
    void testFindValidAttributes() {
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
}
