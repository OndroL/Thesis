package ServiceTests.Common;

import cz.inspire.common.service.BaseService;
import cz.inspire.enterprise.exception.SystemException;
import jakarta.data.repository.Repository;
import jakarta.ejb.CreateException;
import jakarta.ejb.DuplicateKeyException;
import jakarta.ejb.FinderException;
import jakarta.data.repository.CrudRepository;
import jakarta.ejb.RemoveException;
import jakarta.persistence.EntityManager;
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
    @Repository
    private interface TestRepository extends CrudRepository<TestEntity, String> {
    }

    private static class TestService extends BaseService<TestEntity, String, TestRepository> {
        public TestService(TestRepository repository) {
            super(repository);
        }
    }

    @Mock
    private TestRepository repository;

    @Mock
    private EntityManager em;

    private TestService baseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        baseService = new TestService(repository);
        baseService.setEntityManager(em);
    }

    @Test
    void testFindAll() throws FinderException {
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

        doNothing().when(em).persist(entity);
        doNothing().when(em).flush();

        baseService.create(entity);

        verify(em, times(1)).persist(entity);
        verify(em, times(1)).flush();
    }

    @Test
    void testCreate_Failure() {
        TestEntity entity = new TestEntity("123");

        doThrow(new RuntimeException("DB error")).when(em).persist(entity);

        CreateException exception = assertThrows(CreateException.class, () -> baseService.create(entity));
        assertEquals("Failed to create TestEntity", exception.getMessage());

        verify(em, times(1)).persist(entity);
    }


    @Test
    void testUpdate_Success() throws SystemException {
        TestEntity entity = new TestEntity("123");

        when(em.merge(entity)).thenReturn(entity);
        doNothing().when(em).flush();

        baseService.update(entity);

        verify(em, times(1)).merge(entity);
        verify(em, times(1)).flush();
    }

    @Test
    void testUpdate_Failure() {
        TestEntity entity = new TestEntity("123");

        doThrow(new RuntimeException("DB error")).when(em).merge(entity);

        SystemException exception = assertThrows(SystemException.class, () -> baseService.update(entity));
        assertEquals("Failed to update TestEntity", exception.getMessage());

        verify(em, times(1)).merge(entity);
    }


    @Test
    void testDelete_Success() throws RemoveException {
        TestEntity entity = new TestEntity("123");

        when(em.merge(entity)).thenReturn(entity);
        doNothing().when(em).remove(entity);
        doNothing().when(em).flush();

        baseService.delete(entity);

        verify(em, times(1)).merge(entity);
        verify(em, times(1)).remove(entity);
        verify(em, times(1)).flush();
    }

    @Test
    void testDelete_Failure() {
        TestEntity entity = new TestEntity("123");

        when(em.merge(entity)).thenReturn(entity);
        doThrow(new RuntimeException("DB error")).when(em).remove(entity);

        RemoveException exception = assertThrows(RemoveException.class, () -> baseService.delete(entity));
        assertEquals("Failed to remove TestEntity", exception.getMessage());

        verify(em, times(1)).merge(entity);
        verify(em, times(1)).remove(entity);
    }

    @Test
    void testFindById_Found() throws FinderException {
        String entityId = "123";
        TestEntity entity = new TestEntity(entityId);

        when(repository.findById(entityId)).thenReturn(Optional.of(entity));

        Optional<TestEntity> result = baseService.findById(entityId);

        assertTrue(result.isPresent());
        assertEquals(entityId, result.get().getId());
        verify(repository, times(1)).findById(entityId);
    }

    @Test
    void testFindById_NotFound() throws FinderException {
        String entityId = "123";

        when(repository.findById(entityId)).thenReturn(Optional.empty());

        Optional<TestEntity> result = baseService.findById(entityId);

        assertFalse(result.isPresent());
        verify(repository, times(1)).findById(entityId);
    }

    @Test
    void testFindByPrimaryKey_NullPK() {
        FinderException exception = assertThrows(FinderException.class, () -> baseService.findByPrimaryKey(null));
        assertEquals("Primary key cannot be null for TestEntity", exception.getMessage());
    }

    @Test
    void testCreate_EntityAlreadyExists() {
        TestEntity entity = new TestEntity("123");

        doThrow(new jakarta.persistence.EntityExistsException("Already exists")).when(em).persist(entity);

        DuplicateKeyException exception = assertThrows(DuplicateKeyException.class, () -> baseService.create(entity));
        assertEquals("Failed to create TestEntity already exists.", exception.getMessage());

        verify(em, times(1)).persist(entity);
    }


    @Test
    void testUpdate_ThrowsSystemException() {
        TestEntity entity = new TestEntity("123");

        doThrow(new RuntimeException("DB error")).when(em).merge(entity);

        SystemException exception = assertThrows(SystemException.class, () -> baseService.update(entity));
        assertEquals("Failed to update TestEntity", exception.getMessage());

        verify(em, times(1)).merge(entity);
    }


    @Test
    void testDelete_NullEntity() {
        RemoveException exception = assertThrows(RemoveException.class, () -> baseService.delete(null));
        assertEquals("Cannot delete null entity in TestEntity", exception.getMessage());
    }

}
