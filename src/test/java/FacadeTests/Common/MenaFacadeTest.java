package FacadeTests.Common;

import cz.inspire.common.dto.MenaDto;
import cz.inspire.common.facade.MenaFacade;
import cz.inspire.common.mapper.MenaMapper;
import cz.inspire.common.entity.MenaEntity;
import cz.inspire.common.service.MenaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MenaFacadeTest {

    @Mock
    private MenaService menaService;

    @Mock
    private MenaMapper menaMapper;

    @InjectMocks
    private MenaFacade menaFacade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testMapToDto() {
        MenaEntity entity = new MenaEntity("1", "USD", "123.45;67.89", 840, 1, 1);
        MenaDto dto = new MenaDto("1", "USD", "123.45;67.89", 840, 1, 1, List.of(new BigDecimal("123.45"), new BigDecimal("67.89")));

        when(menaMapper.toDto(entity)).thenReturn(dto);

        MenaDto result = menaFacade.mapToDto(entity);

        assertNotNull(result);
        assertEquals("USD", result.getKod());
        assertEquals(2, result.getVycetkaList().size());
        verify(menaMapper, times(1)).toDto(entity);
    }

    @Test
    void testFindByCode() {
        String code = "USD";
        List<MenaEntity> entities = Arrays.asList(
                new MenaEntity("1", "USD", "123.45;67.89", 840, 1, 1),
                new MenaEntity("2", "USD", "987.65;43.21", 840, 0, 1)
        );

        List<MenaDto> dtos = Arrays.asList(
                new MenaDto("1", "USD", "123.45;67.89", 840, 1, 1, List.of(new BigDecimal("123.45"), new BigDecimal("67.89"))),
                new MenaDto("2", "USD", "987.65;43.21", 840, 0, 1, List.of(new BigDecimal("987.65"), new BigDecimal("43.21")))
        );

        when(menaService.findByCode(code)).thenReturn(entities);
        when(menaMapper.toDto(entities.get(0))).thenReturn(dtos.get(0));
        when(menaMapper.toDto(entities.get(1))).thenReturn(dtos.get(1));

        Collection<MenaDto> result = menaFacade.findByCode(code);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.containsAll(dtos));
        verify(menaService, times(1)).findByCode(code);
        verify(menaMapper, times(2)).toDto(any(MenaEntity.class));
    }

    @Test
    void testFindByCodeNum() {
        int codeNum = 840;
        List<MenaEntity> entities = Arrays.asList(
                new MenaEntity("1", "USD", "123.45;67.89", 840, 1, 1),
                new MenaEntity("2", "EUR", "987.65;43.21", 978, 0, 1)
        );

        List<MenaDto> dtos = Arrays.asList(
                new MenaDto("1", "USD", "123.45;67.89", 840, 1, 1, List.of(new BigDecimal("123.45"), new BigDecimal("67.89"))),
                new MenaDto("2", "EUR", "987.65;43.21", 978, 0, 1, List.of(new BigDecimal("987.65"), new BigDecimal("43.21")))
        );

        when(menaService.findByCodeNum(codeNum)).thenReturn(entities);
        when(menaMapper.toDto(entities.get(0))).thenReturn(dtos.get(0));
        when(menaMapper.toDto(entities.get(1))).thenReturn(dtos.get(1));

        Collection<MenaDto> result = menaFacade.findByCodeNum(codeNum);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.containsAll(dtos));
        verify(menaService, times(1)).findByCodeNum(codeNum);
        verify(menaMapper, times(2)).toDto(any(MenaEntity.class));
    }

    @Test
    void testFindAll() {
        List<MenaEntity> entities = Arrays.asList(
                new MenaEntity("1", "USD", "123.45;67.89", 840, 1, 1),
                new MenaEntity("2", "EUR", "987.65;43.21", 978, 0, 1)
        );

        List<MenaDto> dtos = Arrays.asList(
                new MenaDto("1", "USD", "123.45;67.89", 840, 1, 1, List.of(new BigDecimal("123.45"), new BigDecimal("67.89"))),
                new MenaDto("2", "EUR", "987.65;43.21", 978, 0, 1, List.of(new BigDecimal("987.65"), new BigDecimal("43.21")))
        );

        when(menaService.findAll()).thenReturn(entities);
        when(menaMapper.toDto(entities.get(0))).thenReturn(dtos.get(0));
        when(menaMapper.toDto(entities.get(1))).thenReturn(dtos.get(1));

        Collection<MenaDto> result = menaFacade.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.containsAll(dtos));
        verify(menaService, times(1)).findAll();
        verify(menaMapper, times(2)).toDto(any(MenaEntity.class));
    }
}

