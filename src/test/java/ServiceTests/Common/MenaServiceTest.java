package ServiceTests.Common;

import cz.inspire.common.entity.MenaEntity;
import cz.inspire.common.repository.MenaRepository;
import cz.inspire.common.service.MenaService;
import cz.inspire.enterprise.exception.SystemException;
import jakarta.ejb.CreateException;
import jakarta.ejb.FinderException;
import jakarta.ejb.RemoveException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MenaServiceTest {

    @Mock
    private MenaRepository menaRepository;

    @Spy
    private MenaService menaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        menaService = spy(new MenaService(menaRepository));
    }

    @Test
    void testCreate_Success() throws CreateException {
        MenaEntity entity = new MenaEntity("1", "USD", "123,456", 840, 1, 1);

        menaService.create(entity);

        verify(menaService, times(1)).create(entity);
        verify(menaRepository, times(1)).insert(entity);
    }

    @Test
    void testCreate_Failure() throws CreateException {
        MenaEntity entity = new MenaEntity("1", "USD", "123,456", 840, 1, 1);
        doThrow(new RuntimeException("Database failure")).when(menaRepository).insert(entity);

        assertThrows(CreateException.class, () -> menaService.create(entity));

        verify(menaService, times(1)).create(entity);
    }

    @Test
    void testUpdate_Success() throws SystemException {
        MenaEntity entity = new MenaEntity("1", "USD", "123,456", 840, 1, 1);

        menaService.update(entity);

        verify(menaService, times(1)).update(entity);
        verify(menaRepository, times(1)).save(entity);
    }

    @Test
    void testUpdate_Failure() throws SystemException {
        MenaEntity entity = new MenaEntity("1", "USD", "123,456", 840, 1, 1);
        doThrow(new RuntimeException("Database failure")).when(menaRepository).save(entity);

        assertThrows(SystemException.class, () -> menaService.update(entity));

        verify(menaService, times(1)).update(entity);
    }

    @Test
    void testRemove_Success() throws RemoveException {
        MenaEntity entity = new MenaEntity("1", "USD", "123,456", 840, 1, 1);

        menaService.delete(entity);

        verify(menaService, times(1)).delete(entity);
        verify(menaRepository, times(1)).delete(entity);
    }

    @Test
    void testRemove_Failure() throws RemoveException {
        MenaEntity entity = new MenaEntity("1", "USD", "123,456", 840, 1, 1);
        doThrow(new RuntimeException("Database failure")).when(menaRepository).delete(entity);

        assertThrows(RemoveException.class, () -> menaService.delete(entity));

        verify(menaService, times(1)).delete(entity);
    }

    @Test
    void testFindAll() throws FinderException {
        Stream<MenaEntity> stream = Stream.of(
                new MenaEntity("1", "USD", "123,456", 840, 1, 1),
                new MenaEntity("2", "EUR", "789", 978, 0, 0)
        );

        when(menaRepository.findAll()).thenReturn(stream);

        List<MenaEntity> result = menaService.findAll().stream().toList();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("USD", result.getFirst().getKod());
        verify(menaRepository, times(1)).findAll();
    }


    @Test
    void testFindByCode() throws FinderException {
        List<MenaEntity> expectedList = List.of(new MenaEntity("1", "USD", "123,456", 840, 1, 1));
        when(menaRepository.findByCode("USD")).thenReturn(expectedList);

        List<MenaEntity> result = menaService.findByCode("USD");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("USD", result.getFirst().getKod());
        verify(menaRepository, times(1)).findByCode("USD");
    }

    @Test
    void testFindByCodeNum() throws FinderException {
        List<MenaEntity> expectedList = List.of(new MenaEntity("1", "USD", "123,456", 840, 1, 1));
        when(menaRepository.findByCodeNum(840)).thenReturn(expectedList);

        List<MenaEntity> result = menaService.findByCodeNum(840);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(840, result.getFirst().getKodNum());
        verify(menaRepository, times(1)).findByCodeNum(840);
    }
}
