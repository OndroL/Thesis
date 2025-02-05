package ServiceTests.Common;

import cz.inspire.common.service.BaseService;
import cz.inspire.enterprise.exception.SystemException;
import jakarta.ejb.CreateException;
import jakarta.ejb.FinderException;
import jakarta.data.repository.CrudRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BaseServiceTest {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    private static class TestEntity implements Serializable {
        private String id;
    }

    private interface TestRepository extends CrudRepository<TestEntity, String> {
    }

    private static class TestService extends BaseService<TestEntity, String, TestRepository> {
        public TestService(TestRepository repository) {
            super(repository);
        }
    }

    @Mock
    private TestRepository repository;

    private TestService baseService;  // Manually initialized

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        baseService = new TestService(repository);  // Manually initialize with mocked repository
    }

    @Test
    void testFindAll() {
        List<TestEntity> entities = Arrays.asList(new TestEntity("1"), new TestEntity("2"));
        when(repository.findAll()).thenReturn(entities.stream());

        List<TestEntity> result = baseService.findAll();

        assertEquals(2, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testFindByPrimaryKey_Found() throws FinderException {
        String entityId = "123";
        TestEntity entity = new TestEntity(entityId);

        when(repository.findById(entityId)).thenReturn(Optional.of(entity));

        TestEntity result = baseService.findByPrimaryKey(entityId);

        assertNotNull(result);
        assertEquals(entityId, result.getId());
        verify(repository, times(1)).findById(entityId);
    }

    @Test
    void testFindByPrimaryKey_NotFound() {
        String entityId = "123";

        when(repository.findById(entityId)).thenReturn(Optional.empty());

        FinderException exception = assertThrows(FinderException.class, () -> baseService.findByPrimaryKey(entityId));
        assertEquals("Failed to find TestEntity with primary key: " + entityId, exception.getMessage());

        verify(repository, times(1)).findById(entityId);
    }

    @Test
    void testCreate_Success() throws CreateException {
        TestEntity entity = new TestEntity("123");

        baseService.create(entity);

        verify(repository, times(1)).save(entity);
    }

    @Test
    void testCreate_Failure() {
        TestEntity entity = new TestEntity("123");

        doThrow(new RuntimeException("DB error")).when(repository).save(entity);

        CreateException exception = assertThrows(CreateException.class, () -> baseService.create(entity));
        assertEquals("Failed to create TestEntity", exception.getMessage());

        verify(repository, times(1)).save(entity);
    }

    @Test
    void testUpdate_Success() throws SystemException {
        TestEntity entity = new TestEntity("123");

        baseService.update(entity);

        verify(repository, times(1)).save(entity);
    }

    @Test
    void testUpdate_Failure() {
        TestEntity entity = new TestEntity("123");

        doThrow(new RuntimeException("DB error")).when(repository).save(entity);

        SystemException exception = assertThrows(SystemException.class, () -> baseService.update(entity));
        assertEquals("Failed to update TestEntity", exception.getMessage());

        verify(repository, times(1)).save(entity);
    }

    @Test
    void testDelete_Success() throws SystemException {
        TestEntity entity = new TestEntity("123");

        baseService.delete(entity);

        verify(repository, times(1)).delete(entity);
    }

    @Test
    void testDelete_Failure() {
        TestEntity entity = new TestEntity("123");

        doThrow(new RuntimeException("DB error")).when(repository).delete(entity);

        SystemException exception = assertThrows(SystemException.class, () -> baseService.delete(entity));
        assertEquals("Failed to remove TestEntity", exception.getMessage());

        verify(repository, times(1)).delete(entity);
    }
}
