package FacadeTests.Common;

import cz.inspire.common.dto.HeaderDto;
import cz.inspire.common.facade.HeaderFacade;
import cz.inspire.common.mapper.HeaderMapper;
import cz.inspire.common.entity.HeaderEntity;
import cz.inspire.common.service.HeaderService;
import jakarta.ejb.CreateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class HeaderFacadeTest {

    @Mock
    private HeaderService headerService;

    @Mock
    private HeaderMapper headerMapper;

    @InjectMocks
    private HeaderFacade headerFacade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate_Success() throws CreateException {
        String id = "1";
        int field = 42;
        int location = 7;

        HeaderDto dto = new HeaderDto(id, field, location);
        HeaderEntity entity = new HeaderEntity(id, field, location);

        when(headerMapper.toEntity(argThat(argument ->
                argument != null && argument.getId().equals(dto.getId()) &&
                        argument.getField() == dto.getField() &&
                        argument.getLocation() == dto.getLocation()))).thenReturn(entity);

        // No exception expected
        String result = headerFacade.create(id, field, location);

        assertEquals(id, result);
        verify(headerMapper, times(1)).toEntity(any(HeaderDto.class));
        verify(headerService, times(1)).create(entity);
    }

    @Test
    void testCreate_Failure() throws CreateException {
        String id = "1";
        int field = 42;
        int location = 7;

        HeaderEntity entity = new HeaderEntity(id, field, location);

        when(headerMapper.toEntity(any(HeaderDto.class))).thenReturn(entity);
        doThrow(new RuntimeException("Simulated failure")).when(headerService).create(entity);

        // Expecting CreateException
        CreateException exception = assertThrows(CreateException.class, () -> headerFacade.create(id, field, location));
        assertTrue(exception.getMessage().contains("Failed to create HeaderEntity"));
    }

    @Test
    void testFindValidAtributes() {
        List<HeaderEntity> entities = Arrays.asList(
                new HeaderEntity("1", 42, 7),
                new HeaderEntity("2", 24, 9)
        );

        List<HeaderDto> dtos = Arrays.asList(
                new HeaderDto("1", 42, 7),
                new HeaderDto("2", 24, 9)
        );

        when(headerService.findValidAttributes()).thenReturn(entities);
        when(headerMapper.toDto(entities.get(0))).thenReturn(dtos.get(0));
        when(headerMapper.toDto(entities.get(1))).thenReturn(dtos.get(1));

        Collection<HeaderDto> result = headerFacade.findValidAtributes();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.containsAll(dtos));

        verify(headerService, times(1)).findValidAttributes();
        verify(headerMapper, times(1)).toDto(entities.get(0));
        verify(headerMapper, times(1)).toDto(entities.get(1));
    }
}
