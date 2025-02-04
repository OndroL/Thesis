package ServiceTests.Common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.inspire.common.entity.NastaveniEntity;
import cz.inspire.common.repository.NastaveniRepository;
import cz.inspire.common.service.NastaveniService;
import cz.inspire.enterprise.exception.SystemException;
import jakarta.ejb.CreateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NastaveniServiceTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private NastaveniRepository nastaveniRepository;

    @Spy
    private NastaveniService nastaveniService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        nastaveniService = spy(new NastaveniService(nastaveniRepository));
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

        nastaveniService.create(entity);

        verify(nastaveniService, times(1)).create(entity);
        verify(nastaveniRepository, times(1)).save(entity);
    }

    @Test
    void testCreate_Failure() throws CreateException {
        JsonNode value = createJsonValue("{\"key\":\"value1\"}");
        NastaveniEntity entity = new NastaveniEntity("key1", value);
        doThrow(new RuntimeException("Database failure")).when(nastaveniRepository).save(entity);

        assertThrows(CreateException.class, () -> nastaveniService.create(entity));

        verify(nastaveniService, times(1)).create(entity);
    }

    @Test
    void testUpdate_Success() throws SystemException {
        JsonNode value = createJsonValue("{\"key\":\"value1\"}");
        NastaveniEntity entity = new NastaveniEntity("key1", value);

        nastaveniService.update(entity);

        verify(nastaveniService, times(1)).update(entity);
        verify(nastaveniRepository, times(1)).save(entity);
    }

    @Test
    void testUpdate_Failure() throws SystemException {
        JsonNode value = createJsonValue("{\"key\":\"value1\"}");
        NastaveniEntity entity = new NastaveniEntity("key1", value);
        doThrow(new RuntimeException("Database failure")).when(nastaveniRepository).save(entity);

        assertThrows(SystemException.class, () -> nastaveniService.update(entity));

        verify(nastaveniService, times(1)).update(entity);
    }

    @Test
    void testRemove_Success() throws SystemException {
        JsonNode value = createJsonValue("{\"key\":\"value1\"}");
        NastaveniEntity entity = new NastaveniEntity("key1", value);

        nastaveniService.delete(entity);

        verify(nastaveniService, times(1)).delete(entity);
        verify(nastaveniRepository, times(1)).delete(entity);
    }

    @Test
    void testRemove_Failure() throws SystemException {
        JsonNode value = createJsonValue("{\"key\":\"value1\"}");
        NastaveniEntity entity = new NastaveniEntity("key1", value);
        doThrow(new RuntimeException("Database failure")).when(nastaveniRepository).delete(entity);

        assertThrows(SystemException.class, () -> nastaveniService.delete(entity));

        verify(nastaveniService, times(1)).delete(entity);
    }
}
