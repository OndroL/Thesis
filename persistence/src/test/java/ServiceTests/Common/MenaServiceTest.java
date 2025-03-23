package ServiceTests.Common;

import cz.inspire.common.entity.MenaEntity;
import cz.inspire.common.repository.MenaRepository;
import cz.inspire.common.service.MenaService;
import cz.inspire.exception.SystemException;
import jakarta.ejb.CreateException;
import jakarta.ejb.FinderException;
import jakarta.ejb.RemoveException;
import jakarta.persistence.NoResultException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MenaServiceTest {

    @Mock
    private MenaRepository menaRepository;

    private MenaService menaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        menaService = new MenaService(menaRepository);
    }

    @Test
    void testCreate_Success() throws CreateException {
        MenaEntity entity = new MenaEntity("1", "USD", "123,456", 840, 1, 1);
        when(menaRepository.create(entity)).thenReturn(entity);

        MenaEntity result = menaService.create(entity);

        assertEquals(entity, result);
        verify(menaRepository, times(1)).create(entity);
    }

    @Test
    void testCreate_Failure() {
        MenaEntity entity = new MenaEntity("1", "USD", "123,456", 840, 1, 1);
        when(menaRepository.create(entity)).thenThrow(new RuntimeException("DB error"));

        CreateException ex = assertThrows(CreateException.class, () -> menaService.create(entity));
        assertEquals("Failed to create MenaEntity", ex.getMessage());
        verify(menaRepository, times(1)).create(entity);
    }

    @Test
    void testUpdate_Success() throws SystemException {
        MenaEntity entity = new MenaEntity("1", "USD", "123,456", 840, 1, 1);
        when(menaRepository.update(entity)).thenReturn(entity);

        MenaEntity result = menaService.update(entity);

        assertEquals(entity, result);
        verify(menaRepository, times(1)).update(entity);
    }

    @Test
    void testUpdate_Failure() {
        MenaEntity entity = new MenaEntity("1", "USD", "123,456", 840, 1, 1);
        when(menaRepository.update(entity)).thenThrow(new RuntimeException("DB error"));

        SystemException ex = assertThrows(SystemException.class, () -> menaService.update(entity));
        assertEquals("Failed to update MenaEntity", ex.getMessage());
        verify(menaRepository, times(1)).update(entity);
    }

    @Test
    void testDelete_Success() throws RemoveException {
        MenaEntity entity = new MenaEntity("1", "USD", "123,456", 840, 1, 1);
        doNothing().when(menaRepository).delete(entity);

        menaService.delete(entity);

        verify(menaRepository, times(1)).delete(entity);
    }

    @Test
    void testDelete_Failure() {
        MenaEntity entity = new MenaEntity("1", "USD", "123,456", 840, 1, 1);
        doThrow(new RuntimeException("DB error")).when(menaRepository).delete(entity);

        RemoveException ex = assertThrows(RemoveException.class, () -> menaService.delete(entity));
        assertEquals("Failed to remove MenaEntity", ex.getMessage());
        verify(menaRepository, times(1)).delete(entity);
    }

    @Test
    void testFindAll() throws FinderException {
        List<MenaEntity> list = List.of(
                new MenaEntity("1", "USD", "123,456", 840, 1, 1),
                new MenaEntity("2", "EUR", "789", 978, 0, 0)
        );
        when(menaRepository.findAll()).thenReturn(list);

        List<MenaEntity> result = menaService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(menaRepository, times(1)).findAll();
    }

    @Test
    void testFindByPrimaryKey_Found() throws FinderException {
        String entityId = "1";
        MenaEntity entity = new MenaEntity(entityId, "USD", "123,456", 840, 1, 1);
        when(menaRepository.findByPrimaryKey(entityId)).thenReturn(entity);

        MenaEntity result = menaService.findByPrimaryKey(entityId);

        assertNotNull(result);
        assertEquals(entityId, result.getId());
        verify(menaRepository, times(1)).findByPrimaryKey(entityId);
    }

    @Test
    void testFindByPrimaryKey_NotFound() {
        String entityId = "1";
        when(menaRepository.findByPrimaryKey(entityId)).thenThrow(new NoResultException("No entity found"));

        FinderException ex = assertThrows(FinderException.class, () -> menaService.findByPrimaryKey(entityId));
        assertEquals("No result found (Operation: Failed to find MenaEntity with primary key: 1) - Cause: No entity found", ex.getMessage());
        verify(menaRepository, times(1)).findByPrimaryKey(entityId);
    }

    @Test
    void testFindByCode_Success() throws FinderException {
        List<MenaEntity> expectedList = List.of(new MenaEntity("1", "USD", "123,456", 840, 1, 1));
        when(menaRepository.findByCode("USD")).thenReturn(expectedList);

        List<MenaEntity> result = menaService.findByCode("USD");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("USD", result.getFirst().getKod());
        verify(menaRepository, times(1)).findByCode("USD");
    }

    @Test
    void testFindByCode_Failure() {
        when(menaRepository.findByCode("USD")).thenThrow(new RuntimeException("DB error"));

        FinderException ex = assertThrows(FinderException.class, () -> menaService.findByCode("USD"));
        assertEquals("Unexpected error (Operation: Failed to find MenaEntity by Code = USD) - Cause: DB error", ex.getMessage());
        verify(menaRepository, times(1)).findByCode("USD");
    }

    @Test
    void testFindByCodeNum_Success() throws FinderException {
        List<MenaEntity> expectedList = List.of(new MenaEntity("1", "USD", "123,456", 840, 1, 1));
        when(menaRepository.findByCodeNum(840)).thenReturn(expectedList);

        List<MenaEntity> result = menaService.findByCodeNum(840);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(840, result.getFirst().getKodNum());
        verify(menaRepository, times(1)).findByCodeNum(840);
    }

    @Test
    void testFindByCodeNum_Failure() {
        when(menaRepository.findByCodeNum(840)).thenThrow(new RuntimeException("DB error"));

        FinderException ex = assertThrows(FinderException.class, () -> menaService.findByCodeNum(840));
        assertEquals("Unexpected error (Operation: Failed to find MenaEntity by CodeNum = 840) - Cause: DB error", ex.getMessage());
        verify(menaRepository, times(1)).findByCodeNum(840);
    }

    @Test
    void testDelete_NullEntity() {
        RemoveException ex = assertThrows(RemoveException.class, () -> menaService.delete(null));
        assertEquals("Cannot delete null as MenaEntity", ex.getMessage());
    }
}
