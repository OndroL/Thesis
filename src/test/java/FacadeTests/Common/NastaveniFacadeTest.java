package FacadeTests.Common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cz.inspire.common.dto.NastaveniDto;
import cz.inspire.common.facade.NastaveniFacade;
import cz.inspire.common.mapper.NastaveniMapper;
import cz.inspire.common.entity.NastaveniEntity;
import cz.inspire.common.service.NastaveniService;
import jakarta.ejb.CreateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.Serializable;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class NastaveniFacadeTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Mock
    private NastaveniService nastaveniService;

    @Mock
    private NastaveniMapper nastaveniMapper;

    @InjectMocks
    private NastaveniFacade nastaveniFacade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private JsonNode createJsonValue(String className, String value) {
        ObjectNode jsonNode = OBJECT_MAPPER.createObjectNode();
        jsonNode.put("ClassName", className);
        jsonNode.put("Value", value);
        return jsonNode;
    }

    private Serializable jsonToSerializable(JsonNode jsonNode) {
        return jsonNode.toString(); // ✅ Convert JsonNode to String
    }

    @Test
    void testCreate_Success() throws CreateException {
        String key = "testKey";
        JsonNode jsonValue = createJsonValue("java.lang.String", "testValue");
        Serializable serializableValue = jsonToSerializable(jsonValue);

        nastaveniFacade.create(key, serializableValue);

        verify(nastaveniService, times(1)).create(argThat(entity ->
                entity.getKey().equals(key) && entity.getValue().toString().equals(serializableValue)
        ));
    }

    @Test
    void testCreate_Failure() throws CreateException {
        String key = "testKey";
        JsonNode jsonValue = createJsonValue("java.lang.String", "testValue");
        Serializable serializableValue = jsonToSerializable(jsonValue);

        doThrow(RuntimeException.class).when(nastaveniService).create(argThat(entity ->
                entity.getKey().equals(key) && entity.getValue().toString().equals(serializableValue)
        ));

        assertThrows(CreateException.class, () -> nastaveniFacade.create(key, serializableValue));

        verify(nastaveniService, times(1)).create(argThat(entity ->
                entity.getKey().equals(key) && entity.getValue().toString().equals(serializableValue)
        ));
    }

    @Test
    void testMapToDto() {
        String key = "testKey";
        JsonNode jsonValue = createJsonValue("java.lang.String", "testValue");
        Serializable serializableValue = jsonToSerializable(jsonValue);

        NastaveniEntity entity = new NastaveniEntity(key, jsonValue);
        NastaveniDto dto = new NastaveniDto(key, serializableValue);

        when(nastaveniMapper.toDto(entity)).thenReturn(dto);

        NastaveniDto result = nastaveniFacade.mapToDto(entity);

        assertNotNull(result);
        assertEquals("testKey", result.getKey());
        assertEquals(serializableValue, result.getValue()); // ✅ Ensure string representation matches
        verify(nastaveniMapper, times(1)).toDto(entity);
    }
}
