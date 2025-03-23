package ServiceTests.Common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.inspire.common.entity.NastaveniEntity;
import cz.inspire.common.repository.NastaveniRepository;
import cz.inspire.common.service.NastaveniService;
import cz.inspire.exception.SystemException;
import jakarta.ejb.CreateException;
import jakarta.ejb.RemoveException;
import jakarta.ejb.FinderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NastaveniServiceTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private NastaveniRepository nastaveniRepository;

    private NastaveniService nastaveniService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        nastaveniService = new NastaveniService(nastaveniRepository);
    }

    private JsonNode createJsonValue(String json) {
        try {
            return objectMapper.readTree(json);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create JSON value", e);
        }
    }

    @Test
    void testCreate_Success() throws CreateException {
        JsonNode value = createJsonValue("{\"key\":\"value1\"}");
        NastaveniEntity entity = new NastaveniEntity("key1", value);
        when(nastaveniRepository.create(entity)).thenReturn(entity);

        NastaveniEntity result = nastaveniService.create(entity);

        assertEquals(entity, result);
        verify(nastaveniRepository, times(1)).create(entity);
    }

    @Test
    void testCreate_Failure() {
        JsonNode value = createJsonValue("{\"key\":\"value1\"}");
        NastaveniEntity entity = new NastaveniEntity("key1", value);
        when(nastaveniRepository.create(entity)).thenThrow(new RuntimeException("DB error"));

        CreateException exception = assertThrows(CreateException.class, () -> nastaveniService.create(entity));
        assertEquals("Failed to create NastaveniEntity", exception.getMessage());
        verify(nastaveniRepository, times(1)).create(entity);
    }

    @Test
    void testUpdate_Success() throws SystemException {
        JsonNode value = createJsonValue("{\"key\":\"value1\"}");
        NastaveniEntity entity = new NastaveniEntity("key1", value);
        when(nastaveniRepository.update(entity)).thenReturn(entity);

        NastaveniEntity result = nastaveniService.update(entity);

        assertEquals(entity, result);
        verify(nastaveniRepository, times(1)).update(entity);
    }

    @Test
    void testUpdate_Failure() {
        JsonNode value = createJsonValue("{\"key\":\"value1\"}");
        NastaveniEntity entity = new NastaveniEntity("key1", value);
        when(nastaveniRepository.update(entity)).thenThrow(new RuntimeException("DB error"));

        SystemException exception = assertThrows(SystemException.class, () -> nastaveniService.update(entity));
        assertEquals("Failed to update NastaveniEntity", exception.getMessage());
        verify(nastaveniRepository, times(1)).update(entity);
    }

    @Test
    void testDelete_Success() throws RemoveException {
        JsonNode value = createJsonValue("{\"key\":\"value1\"}");
        NastaveniEntity entity = new NastaveniEntity("key1", value);
        doNothing().when(nastaveniRepository).delete(entity);

        nastaveniService.delete(entity);

        verify(nastaveniRepository, times(1)).delete(entity);
    }

    @Test
    void testDelete_Failure() {
        JsonNode value = createJsonValue("{\"key\":\"value1\"}");
        NastaveniEntity entity = new NastaveniEntity("key1", value);
        doThrow(new RuntimeException("DB error")).when(nastaveniRepository).delete(entity);

        RemoveException exception = assertThrows(RemoveException.class, () -> nastaveniService.delete(entity));
        assertEquals("Failed to remove NastaveniEntity", exception.getMessage());
        verify(nastaveniRepository, times(1)).delete(entity);
    }

    @Test
    void testDelete_NullEntity() {
        RemoveException exception = assertThrows(RemoveException.class, () -> nastaveniService.delete(null));
        assertEquals("Cannot delete null as NastaveniEntity", exception.getMessage());
    }

    @Test
    void testFindAll_Success() throws FinderException {
        List<NastaveniEntity> list = List.of(
                new NastaveniEntity("key1", createJsonValue("{\"key\":\"value1\"}")),
                new NastaveniEntity("key2", createJsonValue("{\"key\":\"value2\"}"))
        );
        when(nastaveniRepository.findAll()).thenReturn(list);

        List<NastaveniEntity> result = nastaveniService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(nastaveniRepository, times(1)).findAll();
    }

    @Test
    void testFindByPrimaryKey_Found() throws FinderException {
        JsonNode value = createJsonValue("{\"key\":\"value1\"}");
        NastaveniEntity entity = new NastaveniEntity("key1", value);
        when(nastaveniRepository.findByPrimaryKey("key1")).thenReturn(entity);

        NastaveniEntity result = nastaveniService.findByPrimaryKey("key1");

        assertNotNull(result);
        assertEquals("key1", result.getKey());
        verify(nastaveniRepository, times(1)).findByPrimaryKey("key1");
    }

    @Test
    void testFindByPrimaryKey_Null() throws FinderException {
        NastaveniEntity result = nastaveniService.findByPrimaryKey(null);
        assertNull(result);
    }
}
