package ServiceTests.Common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.inspire.common.entity.NastaveniEntity;
import cz.inspire.common.repository.NastaveniRepository;
import cz.inspire.common.service.NastaveniService;
import cz.inspire.exception.SystemException;
import jakarta.ejb.CreateException;
import jakarta.ejb.RemoveException;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NastaveniServiceTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private NastaveniRepository nastaveniRepository;

    @Mock
    private EntityManager em;

    private NastaveniService nastaveniService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        nastaveniService = new NastaveniService(nastaveniRepository);
        nastaveniService.setEntityManager(em);
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

        doNothing().when(em).persist(entity);
        doNothing().when(em).flush();

        nastaveniService.create(entity);

        verify(em, times(1)).persist(entity);
        verify(em, times(1)).flush();
    }

    @Test
    void testCreate_Failure() {
        JsonNode value = createJsonValue("{\"key\":\"value1\"}");
        NastaveniEntity entity = new NastaveniEntity("key1", value);

        doThrow(new RuntimeException("Database failure")).when(em).persist(entity);

        CreateException exception = assertThrows(CreateException.class, () -> nastaveniService.create(entity));
        assertEquals("Failed to create NastaveniEntity", exception.getMessage());

        verify(em, times(1)).persist(entity);
    }

    @Test
    void testUpdate_Success() throws SystemException {
        JsonNode value = createJsonValue("{\"key\":\"value1\"}");
        NastaveniEntity entity = new NastaveniEntity("key1", value);

        when(em.merge(entity)).thenReturn(entity);
        doNothing().when(em).flush();

        nastaveniService.update(entity);

        verify(em, times(1)).merge(entity);
        verify(em, times(1)).flush();
    }

    @Test
    void testUpdate_Failure() {
        JsonNode value = createJsonValue("{\"key\":\"value1\"}");
        NastaveniEntity entity = new NastaveniEntity("key1", value);

        doThrow(new RuntimeException("Database failure")).when(em).merge(entity);

        SystemException exception = assertThrows(SystemException.class, () -> nastaveniService.update(entity));
        assertEquals("Failed to update NastaveniEntity", exception.getMessage());

        verify(em, times(1)).merge(entity);
    }

    @Test
    void testRemove_Success() throws RemoveException {
        JsonNode value = createJsonValue("{\"key\":\"value1\"}");
        NastaveniEntity entity = new NastaveniEntity("key1", value);

        when(em.merge(entity)).thenReturn(entity);
        doNothing().when(em).remove(entity);
        doNothing().when(em).flush();

        nastaveniService.delete(entity);

        verify(em, times(1)).merge(entity);
        verify(em, times(1)).remove(entity);
        verify(em, times(1)).flush();
    }

    @Test
    void testRemove_Failure() {
        JsonNode value = createJsonValue("{\"key\":\"value1\"}");
        NastaveniEntity entity = new NastaveniEntity("key1", value);

        when(em.merge(entity)).thenReturn(entity);
        doThrow(new RuntimeException("Database failure")).when(em).remove(entity);

        RemoveException exception = assertThrows(RemoveException.class, () -> nastaveniService.delete(entity));
        assertEquals("Failed to remove NastaveniEntity", exception.getMessage());

        verify(em, times(1)).merge(entity);
        verify(em, times(1)).remove(entity);
    }

    @Test
    void testDelete_NullEntity() {
        RemoveException exception = assertThrows(RemoveException.class, () -> nastaveniService.delete(null));
        assertEquals("Cannot delete null entity in NastaveniEntity", exception.getMessage());
    }
}
