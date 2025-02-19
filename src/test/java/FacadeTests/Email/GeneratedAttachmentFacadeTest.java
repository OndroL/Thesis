package FacadeTests.Email;

import cz.inspire.email.dto.GeneratedAttachmentDto;
import cz.inspire.email.entity.EmailHistoryEntity;
import cz.inspire.email.entity.GeneratedAttachmentEntity;

import cz.inspire.email.facade.GeneratedAttachmentFacade;
import cz.inspire.email.mapper.GeneratedAttachmentMapper;
import cz.inspire.email.service.EmailHistoryService;
import cz.inspire.email.service.GeneratedAttachmentService;
import cz.inspire.enterprise.exception.SystemException;
import cz.inspire.template.entity.PrintTemplateEntity;
import cz.inspire.template.service.PrintTemplateService;
import jakarta.ejb.CreateException;
import jakarta.ejb.FinderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
public class GeneratedAttachmentFacadeTest {

    @Mock
    private GeneratedAttachmentService generatedAttachmentService;

    @Mock
    private GeneratedAttachmentMapper generatedAttachmentMapper;

    @Mock
    private EmailHistoryService emailHistoryService;

    @Mock
    private PrintTemplateService printTemplateService;

    @InjectMocks
    private GeneratedAttachmentFacade generatedAttachmentFacade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate_Success() throws CreateException, SystemException, FinderException {
        GeneratedAttachmentDto dto = new GeneratedAttachmentDto();
        dto.setId("attachment123");
        dto.setEmailHistoryId("history123");

        GeneratedAttachmentEntity entity = new GeneratedAttachmentEntity();
        entity.setId("attachment123");

        EmailHistoryEntity emailHistory = new EmailHistoryEntity();
        emailHistory.setId("history123");

        when(generatedAttachmentMapper.toEntity(dto)).thenReturn(entity);
        when(emailHistoryService.findByPrimaryKey("history123")).thenReturn(emailHistory);

        when(generatedAttachmentService.create(any(GeneratedAttachmentEntity.class))).thenReturn(entity);
        doNothing().when(generatedAttachmentService).update(entity);

        String result = generatedAttachmentFacade.create(dto);

        assertEquals("attachment123", result);
        verify(generatedAttachmentMapper, times(1)).toEntity(dto);
        verify(emailHistoryService, times(1)).findByPrimaryKey("history123");
        verify(generatedAttachmentService, times(1)).create(entity);
        verify(generatedAttachmentService, times(1)).update(entity);
    }

    @Test
    void testCreate_Failure_EmailHistoryNotFound() throws CreateException, FinderException {
        GeneratedAttachmentDto dto = new GeneratedAttachmentDto();
        dto.setId("attachment123");
        dto.setEmailHistoryId("history123");

        when(emailHistoryService.findByPrimaryKey("history123")).thenReturn(null);

        assertThrows(CreateException.class, () -> generatedAttachmentFacade.create(dto));

        verify(emailHistoryService, times(1)).findByPrimaryKey("history123");
        verify(generatedAttachmentService, never()).create(any(GeneratedAttachmentEntity.class));
    }

    @Test
    void testSetPrintTemplate_Success() throws CreateException, FinderException {
        GeneratedAttachmentEntity entity = new GeneratedAttachmentEntity();
        entity.setId("attachment123");

        GeneratedAttachmentDto dto = new GeneratedAttachmentDto();
        dto.setPrintTemplateId("template123");

        PrintTemplateEntity printTemplateEntity = new PrintTemplateEntity();
        printTemplateEntity.setId("template123");

        when(printTemplateService.findByPrimaryKey("template123")).thenReturn(printTemplateEntity);

        generatedAttachmentFacade.setPrintTemplate(entity, dto);

        assertNotNull(entity.getPrintTemplate());
        assertEquals("template123", entity.getPrintTemplate().getId());

        verify(printTemplateService, times(1)).findByPrimaryKey("template123");
    }

    @Test
    void testSetPrintTemplate_Failure_TemplateNotFound() throws FinderException {
        GeneratedAttachmentEntity entity = new GeneratedAttachmentEntity();
        entity.setId("attachment123");

        GeneratedAttachmentDto dto = new GeneratedAttachmentDto();
        dto.setPrintTemplateId("template123");

        when(printTemplateService.findByPrimaryKey("template123")).thenThrow(new FinderException("Failed to find PrintTemplateEntity with primary key: template123"));

        assertThrows(CreateException.class, () -> generatedAttachmentFacade.setPrintTemplate(entity, dto));

        verify(printTemplateService, times(1)).findByPrimaryKey("template123");
    }

    @Test
    void testMapToDto() {
        GeneratedAttachmentEntity entity = new GeneratedAttachmentEntity();
        entity.setId("attachment123");

        GeneratedAttachmentDto expectedDto = new GeneratedAttachmentDto();
        expectedDto.setId("attachment123");

        when(generatedAttachmentMapper.toDto(entity)).thenReturn(expectedDto);

        GeneratedAttachmentDto result = generatedAttachmentFacade.mapToDto(entity);

        assertNotNull(result);
        assertEquals("attachment123", result.getId());

        verify(generatedAttachmentMapper, times(1)).toDto(entity);
    }

    @Test
    void testFindByEmailAndHistory() throws FinderException {
        String historyId = "history123";
        String email = "user@example.com";
        GeneratedAttachmentEntity entity = new GeneratedAttachmentEntity();
        entity.setId("attachment123");

        GeneratedAttachmentDto dto = new GeneratedAttachmentDto();
        dto.setId("attachment123");

        when(generatedAttachmentService.findByEmailAndHistory(historyId, email)).thenReturn(List.of(entity));
        when(generatedAttachmentMapper.toDto(entity)).thenReturn(dto);

        List<GeneratedAttachmentDto> result = generatedAttachmentFacade.findByEmailAndHistory(historyId, email);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("attachment123", result.getFirst().getId());

        verify(generatedAttachmentService, times(1)).findByEmailAndHistory(historyId, email);
        verify(generatedAttachmentMapper, times(1)).toDto(entity);
    }

    @Test
    void testFindByHistory() throws FinderException {
        String historyId = "history123";
        GeneratedAttachmentEntity entity = new GeneratedAttachmentEntity();
        entity.setId("attachment123");

        GeneratedAttachmentDto dto = new GeneratedAttachmentDto();
        dto.setId("attachment123");

        when(generatedAttachmentService.findByHistory(historyId)).thenReturn(List.of(entity));
        when(generatedAttachmentMapper.toDto(entity)).thenReturn(dto);

        List<GeneratedAttachmentDto> result = generatedAttachmentFacade.findByHistory(historyId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("attachment123", result.getFirst().getId());

        verify(generatedAttachmentService, times(1)).findByHistory(historyId);
        verify(generatedAttachmentMapper, times(1)).toDto(entity);
    }

    @Test
    void testFindByEmailAndHistoryAndTemplate() throws FinderException {
        String historyId = "history123";
        String email = "user@example.com";
        String templateId = "template123";
        GeneratedAttachmentEntity entity = new GeneratedAttachmentEntity();
        entity.setId("attachment123");

        GeneratedAttachmentDto dto = new GeneratedAttachmentDto();
        dto.setId("attachment123");

        when(generatedAttachmentService.findByEmailAndHistoryAndTemplate(historyId, email, templateId)).thenReturn(List.of(entity));
        when(generatedAttachmentMapper.toDto(entity)).thenReturn(dto);

        List<GeneratedAttachmentDto> result = generatedAttachmentFacade.findByEmailAndHistoryAndTemplate(historyId, email, templateId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("attachment123", result.getFirst().getId());

        verify(generatedAttachmentService, times(1)).findByEmailAndHistoryAndTemplate(historyId, email, templateId);
        verify(generatedAttachmentMapper, times(1)).toDto(entity);
    }
}
