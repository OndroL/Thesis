package ServiceTests.CommonServer;

import cz.inspire.common.entity.MenaEntity;
import cz.inspire.common.repository.MenaRepository;
import cz.inspire.common.service.MenaService;
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

public class MenaServiceTest {

    @Mock
    private MenaRepository menaRepository;

    @Mock
    private Logger logger;

    @InjectMocks
    private MenaService menaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate_Success() throws CreateException {
        MenaEntity entity = new MenaEntity("1", "USD", "123,456", 840, 1, 1);

        menaService.create(entity);

        verify(menaRepository, times(1)).save(entity);
    }

    @Test
    void testCreate_Failure() {
        MenaEntity entity = new MenaEntity("1", "USD", "123,456", 840, 1, 1);
        doThrow(RuntimeException.class).when(menaRepository).save(entity);

        assertThrows(CreateException.class, () -> menaService.create(entity));
        verify(logger, times(1)).error(eq("Failed to create MenaEntity"), any(RuntimeException.class));
    }

    @Test
    void testUpdate_Success() throws SystemException {
        MenaEntity entity = new MenaEntity("1", "USD", "123,456", 840, 1, 1);

        menaService.update(entity);

        verify(menaRepository, times(1)).save(entity);
    }

    @Test
    void testUpdate_Failure() {
        MenaEntity entity = new MenaEntity("1", "USD", "123,456", 840, 1, 1);
        doThrow(RuntimeException.class).when(menaRepository).save(entity);

        SystemException exception = assertThrows(SystemException.class, () -> menaService.update(entity));
        assertEquals("Failed to update MenaEntity", exception.getMessage());
        verify(logger, times(1)).error(eq("Failed to update MenaEntity"), any(RuntimeException.class));
    }

    @Test
    void testRemove_Success() throws SystemException {
        MenaEntity entity = new MenaEntity("1", "USD", "123,456", 840, 1, 1);

        menaService.remove(entity);

        verify(menaRepository, times(1)).remove(entity);
    }

    @Test
    void testRemove_Failure() {
        MenaEntity entity = new MenaEntity("1", "USD", "123,456", 840, 1, 1);
        doThrow(RuntimeException.class).when(menaRepository).remove(entity);

        SystemException exception = assertThrows(SystemException.class, () -> menaService.remove(entity));
        assertEquals("Failed to remove MenaEntity", exception.getMessage());
        verify(logger, times(1)).error(eq("Failed to remove MenaEntity"), any(RuntimeException.class));
    }

    @Test
    void testFindAll() {
        List<MenaEntity> expectedList = Arrays.asList(
                new MenaEntity("1", "USD", "123,456", 840, 1, 1),
                new MenaEntity("2", "EUR", "789", 978, 0, 0)
        );
        when(menaRepository.findAll()).thenReturn(expectedList);

        List<MenaEntity> result = menaService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("USD", result.getFirst().getKod());
        verify(menaRepository, times(1)).findAll();
    }

    @Test
    void testFindByCode() {
        List<MenaEntity> expectedList = List.of(new MenaEntity("1", "USD", "123,456", 840, 1, 1));
        when(menaRepository.findByCode("USD")).thenReturn(expectedList);

        List<MenaEntity> result = menaService.findByCode("USD");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("USD", result.getFirst().getKod());
        verify(menaRepository, times(1)).findByCode("USD");
    }

    @Test
    void testFindByCodeNum() {
        List<MenaEntity> expectedList = List.of(new MenaEntity("1", "USD", "123,456", 840, 1, 1));
        when(menaRepository.findByCodeNum(840)).thenReturn(expectedList);

        List<MenaEntity> result = menaService.findByCodeNum(840);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(840, result.getFirst().getKodnum());
        verify(menaRepository, times(1)).findByCodeNum(840);
    }
}
