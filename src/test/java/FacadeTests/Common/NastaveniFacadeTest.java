package FacadeTests.Common;

import cz.inspire.common.dto.NastaveniDto;
import cz.inspire.common.entity.NastaveniEntity;
import cz.inspire.common.facade.NastaveniFacade;
import cz.inspire.common.mapper.NastaveniMapper;
import cz.inspire.common.service.NastaveniService;
import jakarta.ejb.CreateException;
import jakarta.ejb.FinderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.io.Serializable;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
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
        Serializable value = "testValue";
        NastaveniDto dto = new NastaveniDto(key, value);
        NastaveniEntity entity = new NastaveniEntity(key, null);

        when(nastaveniMapper.toEntity(dto)).thenReturn(entity);
        doNothing().when(nastaveniService).create(entity);

        String result = nastaveniFacade.create(key, value);

        assertNull(result, "Expected create method to return null");
        verify(nastaveniMapper, times(1)).toEntity(dto);
        verify(nastaveniService, times(1)).create(entity);
    }

    @Test
    void testCreate_Failure() throws CreateException {
        String key = "testKey";
        Serializable value = "testValue";
        NastaveniDto dto = new NastaveniDto(key, value);

        when(nastaveniMapper.toEntity(dto)).thenThrow(new RuntimeException("Mapping error"));

        assertThrows(CreateException.class, () -> nastaveniFacade.create(key, value));

        verify(nastaveniMapper, times(1)).toEntity(dto);
        verify(nastaveniService, never()).create(any(NastaveniEntity.class));
    }

    @Test
    void testMapToDto() {
        String key = "testKey";
        NastaveniEntity entity = new NastaveniEntity(key, null);
        NastaveniDto dto = new NastaveniDto(key, "testValue");

        when(nastaveniMapper.toDto(entity)).thenReturn(dto);

        NastaveniDto result = nastaveniFacade.mapToDto(entity);

        assertNotNull(result, "Result should not be null");
        assertEquals(dto.getKey(), result.getKey());
        assertEquals(dto.getValue(), result.getValue());

        verify(nastaveniMapper, times(1)).toDto(entity);
    }

    @Test
    void testFindByPrimaryKey_Found() throws FinderException {
        String key = "testKey";
        NastaveniEntity entity = new NastaveniEntity(key, null);
        NastaveniDto dto = new NastaveniDto(key, "testValue");

        when(nastaveniService.findByPrimaryKey(key)).thenReturn(entity);
        when(nastaveniMapper.toDto(entity)).thenReturn(dto);

        NastaveniDto result = nastaveniFacade.findByPrimaryKey(key);

        assertNotNull(result, "Result should not be null");
        assertEquals(dto.getKey(), result.getKey());
        assertEquals(dto.getValue(), result.getValue());

        verify(nastaveniService, times(1)).findByPrimaryKey(key);
        verify(nastaveniMapper, times(1)).toDto(entity);
    }

    @Test
    void testFindByPrimaryKey_NotFound() throws FinderException {
        String key = "testKey";

        when(nastaveniService.findByPrimaryKey(key)).thenReturn(null);

        NastaveniDto result = nastaveniFacade.findByPrimaryKey(key);

        assertNull(result, "Expected null when entity is not found");

        verify(nastaveniService, times(1)).findByPrimaryKey(key);
        verify(nastaveniMapper, never()).toDto(any(NastaveniEntity.class));
    }
}
