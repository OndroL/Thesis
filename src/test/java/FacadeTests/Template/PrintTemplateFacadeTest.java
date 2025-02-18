package FacadeTests.Template;

import cz.inspire.template.dto.PrintTemplateDto;
import cz.inspire.template.entity.PrintTemplateEntity;
import cz.inspire.template.facade.PrintTemplateFacade;
import cz.inspire.template.mapper.PrintTemplateMapper;
import cz.inspire.template.service.PrintTemplateService;
import jakarta.ejb.CreateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PrintTemplateFacadeTest {

    @Mock
    private PrintTemplateService printTemplateService;

    @Mock
    private PrintTemplateMapper printTemplateMapper;

    @InjectMocks
    private PrintTemplateFacade printTemplateFacade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate_Success() throws CreateException {
        // Input DTO
        PrintTemplateDto dto = new PrintTemplateDto(null, "Content", 1, "TemplateName", "FileName");

        // Mapped Entity
        PrintTemplateEntity entity = new PrintTemplateEntity(null, "Content", 1, "TemplateName", "FileName");
        PrintTemplateEntity savedEntity = new PrintTemplateEntity("generated-id", "Content", 1, "TemplateName", "FileName");

        when(printTemplateMapper.toEntity(dto)).thenReturn(entity);
        doAnswer(invocation -> {
            entity.setId(PrintTemplateUtil.generateGUID(entity)); // Mimic GUID generation
            return null;
        }).when(printTemplateService).create(entity);

        // Invoke and verify
        String result = printTemplateFacade.create(dto);
        assertNotNull(result);
        verify(printTemplateMapper, times(1)).toEntity(dto);
        verify(printTemplateService, times(1)).create(entity);
    }

    @Test
    void testCreate_Failure() throws CreateException {
        PrintTemplateDto dto = new PrintTemplateDto(null, "Content", 1, "TemplateName", "FileName");
        PrintTemplateEntity entity = new PrintTemplateEntity(null, "Content", 1, "TemplateName", "FileName");

        when(printTemplateMapper.toEntity(dto)).thenReturn(entity);
        doThrow(new RuntimeException("Simulated failure")).when(printTemplateService).create(entity);

        // Expecting CreateException
        CreateException exception = assertThrows(CreateException.class, () -> printTemplateFacade.create(dto));
        assertNotNull(exception);
        verify(printTemplateMapper, times(1)).toEntity(dto);
        verify(printTemplateService, times(1)).create(entity);
    }

    @Test
    void testMapToDto() {
        PrintTemplateEntity entity = new PrintTemplateEntity("1", "Content", 1, "TemplateName", "FileName");
        PrintTemplateDto dto = new PrintTemplateDto("1", "Content", 1, "TemplateName", "FileName");

        when(printTemplateMapper.toDto(entity)).thenReturn(dto);

        PrintTemplateDto result = printTemplateFacade.mapToDto(entity);

        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals("Content", result.getContent());
        assertEquals(1, result.getType());
        verify(printTemplateMapper, times(1)).toDto(entity);
    }
}
