package ServiceTests.Common;

import cz.inspire.common.service.BaseService;
import cz.inspire.exception.SystemException;
import cz.inspire.repository.BaseRepository;
import jakarta.ejb.CreateException;
import jakarta.ejb.DuplicateKeyException;
import jakarta.ejb.FinderException;
import jakarta.ejb.RemoveException;
import jakarta.persistence.EntityExistsException;
import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BaseServiceTest {

    // A simple test entity.
    @Getter
    private static class TestEntity implements Serializable {
        private final String id;
        public TestEntity(String id) { this.id = id; }
    }

    // Test repository interface extending BaseRepository.
    private interface TestRepository extends BaseRepository<TestEntity, String> {
    }

    // Test service extending BaseService.
    private static class TestService extends BaseService<TestEntity, String, TestRepository> {
        public TestService(TestRepository repository) {
            super(repository);
        }
    }

    @Mock
    private TestRepository repository;

    private TestService testService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testService = new TestService(repository);
    }

    @Test
    void testFindAll_Success() throws FinderException {
        List<TestEntity> entities = Arrays.asList(new TestEntity("1"), new TestEntity("2"));
        when(repository.findAll()).thenReturn(entities);

        List<TestEntity> result = testService.findAll();

        assertEquals(2, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testFindByPrimaryKey_Found() throws FinderException {
        String id = "123";
        TestEntity entity = new TestEntity(id);
        when(repository.findByPrimaryKey(id)).thenReturn(entity);

        TestEntity result = testService.findByPrimaryKey(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(repository, times(1)).findByPrimaryKey(id);
    }

    @Test
    void testFindByPrimaryKey_NullPK() throws FinderException {
        TestEntity result = testService.findByPrimaryKey(null);
        assertNull(result);
    }

    @Test
    void testCreate_Success() throws CreateException {
        TestEntity entity = new TestEntity("123");
        when(repository.create(entity)).thenReturn(entity);

        TestEntity result = testService.create(entity);

        assertEquals(entity, result);
        verify(repository, times(1)).create(entity);
    }

    @Test
    void testCreate_EntityExists() {
        TestEntity entity = new TestEntity("123");
        when(repository.create(entity)).thenThrow(new EntityExistsException("exists"));

        DuplicateKeyException ex = assertThrows(DuplicateKeyException.class, () -> testService.create(entity));
        assertEquals("Failed to create TestEntity already exists.", ex.getMessage());
        verify(repository, times(1)).create(entity);
    }

    @Test
    void testCreate_Failure() {
        TestEntity entity = new TestEntity("123");
        when(repository.create(entity)).thenThrow(new RuntimeException("DB error"));

        CreateException ex = assertThrows(CreateException.class, () -> testService.create(entity));
        assertEquals("Failed to create TestEntity", ex.getMessage());
        verify(repository, times(1)).create(entity);
    }

    @Test
    void testUpdate_Success() throws SystemException {
        TestEntity entity = new TestEntity("123");
        when(repository.update(entity)).thenReturn(entity);

        TestEntity result = testService.update(entity);

        assertEquals(entity, result);
        verify(repository, times(1)).update(entity);
    }

    @Test
    void testUpdate_Failure() {
        TestEntity entity = new TestEntity("123");
        when(repository.update(entity)).thenThrow(new RuntimeException("DB error"));

        SystemException ex = assertThrows(SystemException.class, () -> testService.update(entity));
        assertEquals("Failed to update TestEntity", ex.getMessage());
        verify(repository, times(1)).update(entity);
    }

    @Test
    void testDelete_Success() throws RemoveException {
        TestEntity entity = new TestEntity("123");
        doNothing().when(repository).delete(entity);

        testService.delete(entity);

        verify(repository, times(1)).delete(entity);
    }

    @Test
    void testDelete_Failure() {
        TestEntity entity = new TestEntity("123");
        doThrow(new RuntimeException("DB error")).when(repository).delete(entity);

        RemoveException ex = assertThrows(RemoveException.class, () -> testService.delete(entity));
        assertEquals("Failed to remove TestEntity", ex.getMessage());
        verify(repository, times(1)).delete(entity);
    }

    @Test
    void testDelete_NullEntity() {
        RemoveException ex = assertThrows(RemoveException.class, () -> testService.delete(null));
        assertEquals("Cannot delete null as TestEntity", ex.getMessage());
    }

    @Test
    void testDeleteByPrimaryKey_Success() throws RemoveException {
        String id = "123";
        doNothing().when(repository).deleteByPrimaryKey(id);

        testService.deleteByPrimaryKey(id);

        verify(repository, times(1)).deleteByPrimaryKey(id);
    }

    @Test
    void testDeleteByPrimaryKey_Null() {
        RemoveException ex = assertThrows(RemoveException.class, () -> testService.deleteByPrimaryKey(null));
        assertEquals("Cannot delete TestEntity with null primary key", ex.getMessage());
    }

    @Test
    void testDeleteByPrimaryKey_Failure() {
        String id = "123";
        doThrow(new RuntimeException("DB error")).when(repository).deleteByPrimaryKey(id);

        RemoveException ex = assertThrows(RemoveException.class, () -> testService.deleteByPrimaryKey(id));
        assertEquals("Failed to remove TestEntity", ex.getMessage());
        verify(repository, times(1)).deleteByPrimaryKey(id);
    }
}
