package FacadeTests.CommonServer;

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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class NastaveniFacadeTest {

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

    @Test
    void testCreate_Success() throws CreateException {
        String key = "testKey";
        String value = "testValue";

        NastaveniEntity entity = new NastaveniEntity(key, value);

        // No need to stub anything for a successful void method call
        nastaveniFacade.create(key, value);

        verify(nastaveniService, times(1)).create(entity);
    }

    @Test
    void testCreate_Failure() throws CreateException {
        String key = "testKey";
        String value = "testValue";

        NastaveniEntity entity = new NastaveniEntity(key, value);
        doThrow(RuntimeException.class).when(nastaveniService).create(entity);

        assertThrows(CreateException.class, () -> nastaveniFacade.create(key, value));
        verify(nastaveniService, times(1)).create(entity);
    }

    @Test
    void testMapToDto() {
        NastaveniEntity entity = new NastaveniEntity("testKey", "testValue");
        NastaveniDto dto = new NastaveniDto("testKey", "testValue");

        when(nastaveniMapper.toDto(entity)).thenReturn(dto);

        NastaveniDto result = nastaveniFacade.mapToDto(entity);

        assertNotNull(result);
        assertEquals("testKey", result.getKey());
        assertEquals("testValue", result.getValue());
        verify(nastaveniMapper, times(1)).toDto(entity);
    }
}

