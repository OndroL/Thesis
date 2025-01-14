package FacadeTests.CommonServer;

import cz.inspire.common.dto.NastaveniJsonDto;
import cz.inspire.common.facade.NastaveniJsonFacade;
import cz.inspire.common.mapper.NastaveniJsonMapper;
import cz.inspire.common.entity.NastaveniJsonEntity;
import cz.inspire.common.service.NastaveniJsonService;
import jakarta.ejb.CreateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class NastaveniJsonFacadeTest {

    @Mock
    private NastaveniJsonService nastaveniJsonService;

    @Mock
    private NastaveniJsonMapper nastaveniJsonMapper;

    @InjectMocks
    private NastaveniJsonFacade nastaveniJsonFacade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateWithKeyAndValue_Success() throws CreateException {
        String key = "testKey";
        String value = "testValue";

        String result = nastaveniJsonFacade.create(key, value);

        assertNull(result);
        verify(nastaveniJsonService, times(1)).create(new NastaveniJsonEntity(key, value));
    }

    @Test
    void testCreateWithKeyAndValue_Failure() throws CreateException {
        String key = "testKey";
        String value = "testValue";

        doThrow(RuntimeException.class).when(nastaveniJsonService).create(any(NastaveniJsonEntity.class));

        assertThrows(CreateException.class, () -> nastaveniJsonFacade.create(key, value));
        verify(nastaveniJsonService, times(1)).create(any(NastaveniJsonEntity.class));
    }

    @Test
    void testCreateWithDto_Success() throws CreateException {
        NastaveniJsonDto dto = new NastaveniJsonDto("testKey", "testValue");
        NastaveniJsonEntity entity = new NastaveniJsonEntity("testKey", "testValue");

        when(nastaveniJsonMapper.toEntity(dto)).thenReturn(entity);

        String result = nastaveniJsonFacade.create(dto);

        assertNull(result);
        verify(nastaveniJsonMapper, times(1)).toEntity(dto);
        verify(nastaveniJsonService, times(1)).create(entity);
    }

    @Test
    void testCreateWithDto_Failure() throws CreateException {
        NastaveniJsonDto dto = new NastaveniJsonDto("testKey", "testValue");
        NastaveniJsonEntity entity = new NastaveniJsonEntity("testKey", "testValue");

        when(nastaveniJsonMapper.toEntity(dto)).thenReturn(entity);
        doThrow(RuntimeException.class).when(nastaveniJsonService).create(entity);

        assertThrows(CreateException.class, () -> nastaveniJsonFacade.create(dto));
        verify(nastaveniJsonMapper, times(1)).toEntity(dto);
        verify(nastaveniJsonService, times(1)).create(entity);
    }

    @Test
    void testMapToDto() {
        NastaveniJsonEntity entity = new NastaveniJsonEntity("testKey", "testValue");
        NastaveniJsonDto dto = new NastaveniJsonDto("testKey", "testValue");

        when(nastaveniJsonMapper.toDto(entity)).thenReturn(dto);

        NastaveniJsonDto result = nastaveniJsonFacade.mapToDto(entity);

        assertNotNull(result);
        assertEquals("testKey", result.getKey());
        assertEquals("testValue", result.getValue());
        verify(nastaveniJsonMapper, times(1)).toDto(entity);
    }
}
