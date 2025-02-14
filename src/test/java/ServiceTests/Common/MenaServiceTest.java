package ServiceTests.Common;

import cz.inspire.common.entity.MenaEntity;
import cz.inspire.common.repository.MenaRepository;
import cz.inspire.common.service.MenaService;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MenaServiceTest {

    @Mock
    private MenaRepository menaRepository;

    @Mock
    private EntityManager em;

    private MenaService menaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        menaService = new MenaService(menaRepository);
        menaService.setEntityManager(em);
    }

    @Test
    void testCreate_Success() throws CreateException {
        MenaEntity entity = new MenaEntity("1", "USD", "123,456", 840, 1, 1);

        doNothing().when(em).persist(entity);
        doNothing().when(em).flush();

        menaService.create(entity);

        verify(em, times(1)).persist(entity);
        verify(em, times(1)).flush();
    }

    @Test
    void testCreate_Failure() {
        MenaEntity entity = new MenaEntity("1", "USD", "123,456", 840, 1, 1);

        doThrow(new RuntimeException("Database failure")).when(em).persist(entity);

        CreateException exception = assertThrows(CreateException.class, () -> menaService.create(entity));
        assertEquals("Failed to create MenaEntity", exception.getMessage());

        verify(em, times(1)).persist(entity);
    }

    @Test
    void testUpdate_Success() throws SystemException {
        MenaEntity entity = new MenaEntity("1", "USD", "123,456", 840, 1, 1);

        when(em.merge(entity)).thenReturn(entity);
        doNothing().when(em).flush();

        menaService.update(entity);

        verify(em, times(1)).merge(entity);
        verify(em, times(1)).flush();
    }

    @Test
    void testUpdate_Failure() {
        MenaEntity entity = new MenaEntity("1", "USD", "123,456", 840, 1, 1);

        doThrow(new RuntimeException("Database failure")).when(em).merge(entity);

        SystemException exception = assertThrows(SystemException.class, () -> menaService.update(entity));
        assertEquals("Failed to update MenaEntity", exception.getMessage());

        verify(em, times(1)).merge(entity);
    }

    @Test
    void testRemove_Success() throws RemoveException {
        MenaEntity entity = new MenaEntity("1", "USD", "123,456", 840, 1, 1);

        when(em.merge(entity)).thenReturn(entity);
        doNothing().when(em).remove(entity);
        doNothing().when(em).flush();

        menaService.delete(entity);

        verify(em, times(1)).merge(entity);
        verify(em, times(1)).remove(entity);
        verify(em, times(1)).flush();
    }

    @Test
    void testRemove_Failure() {
        MenaEntity entity = new MenaEntity("1", "USD", "123,456", 840, 1, 1);

        when(em.merge(entity)).thenReturn(entity);
        doThrow(new RuntimeException("Database failure")).when(em).remove(entity);

        RemoveException exception = assertThrows(RemoveException.class, () -> menaService.delete(entity));
        assertEquals("Failed to remove MenaEntity", exception.getMessage());

        verify(em, times(1)).merge(entity);
        verify(em, times(1)).remove(entity);
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
    void testFindByPrimaryKey_Found() throws FinderException {
        String entityId = "1";
        MenaEntity entity = new MenaEntity(entityId, "USD", "123,456", 840, 1, 1);

        when(menaRepository.findById(entityId)).thenReturn(Optional.of(entity));

        MenaEntity result = menaService.findByPrimaryKey(entityId);

        assertNotNull(result);
        assertEquals(entityId, result.getId());
        verify(menaRepository, times(1)).findById(entityId);
    }

    @Test
    void testFindByPrimaryKey_NotFound() {
        String entityId = "1";

        when(menaRepository.findById(entityId)).thenReturn(Optional.empty());

        FinderException exception = assertThrows(FinderException.class, () -> menaService.findByPrimaryKey(entityId));
        assertEquals("Failed to find MenaEntity with primary key: " + entityId, exception.getMessage());

        verify(menaRepository, times(1)).findById(entityId);
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

    @Test
    void testDelete_NullEntity() {
        RemoveException exception = assertThrows(RemoveException.class, () -> menaService.delete(null));
        assertEquals("Cannot delete null entity in MenaEntity", exception.getMessage());
    }
}
