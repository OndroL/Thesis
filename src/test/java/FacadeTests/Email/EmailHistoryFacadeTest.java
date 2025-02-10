package FacadeTests.Email;

import cz.inspire.email.dto.EmailHistoryDto;
import cz.inspire.email.dto.GeneratedAttachmentDto;
import cz.inspire.email.entity.EmailHistoryEntity;
import cz.inspire.email.entity.GeneratedAttachmentEntity;
import cz.inspire.email.facade.EmailHistoryFacade;
import cz.inspire.email.mapper.EmailHistoryMapper;
import cz.inspire.email.mapper.GeneratedAttachmentMapper;
import cz.inspire.email.service.EmailHistoryService;
import cz.inspire.email.service.GeneratedAttachmentService;
import cz.inspire.enterprise.exception.SystemException;
import cz.inspire.template.entity.PrintTemplateEntity;
import cz.inspire.template.service.PrintTemplateService;
import cz.inspire.utils.File;
import jakarta.ejb.CreateException;
import jakarta.ejb.FinderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
public class EmailHistoryFacadeTest {

    @Mock
    private EmailHistoryService emailHistoryService;

    @Mock
    private EmailHistoryMapper emailHistoryMapper;

    @Mock
    private GeneratedAttachmentService generatedAttachmentService;

    @Mock
    private GeneratedAttachmentMapper generatedAttachmentMapper;

    @Mock
    private PrintTemplateService printTemplateService;

    @InjectMocks
    private EmailHistoryFacade emailHistoryFacade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate_Success() throws CreateException, SystemException {
        EmailHistoryDto dto = new EmailHistoryDto();
        dto.setId("email123");

        EmailHistoryEntity entity = new EmailHistoryEntity();
        entity.setId("email123");

        when(emailHistoryMapper.toEntity(dto)).thenReturn(entity);
        doNothing().when(emailHistoryService).create(entity);
        doNothing().when(emailHistoryService).update(entity);

        String result = emailHistoryFacade.create(dto);

        assertEquals("email123", result);
        verify(emailHistoryMapper, times(1)).toEntity(dto);
        verify(emailHistoryService, times(1)).create(entity);
        verify(emailHistoryService, times(1)).update(entity);
    }

    @Test
    void testCreate_Failure() throws CreateException {
        EmailHistoryDto dto = new EmailHistoryDto();
        dto.setId("email123");

        when(emailHistoryMapper.toEntity(dto)).thenThrow(new RuntimeException("Mapping error"));

        assertThrows(CreateException.class, () -> emailHistoryFacade.create(dto));

        verify(emailHistoryMapper, times(1)).toEntity(dto);
        verify(emailHistoryService, never()).create(any(EmailHistoryEntity.class));
    }

    @Test
    void testMapToDto() {
        EmailHistoryEntity entity = new EmailHistoryEntity();
        entity.setId("email123");
        entity.setSent(true);
        entity.setAttachments(List.of(new File("file1.pdf", "/path/file1.pdf")));

        EmailHistoryDto expectedDto = new EmailHistoryDto();
        expectedDto.setId("email123");
        expectedDto.setSent(true);
        expectedDto.setAttachments(Map.of("file1.pdf", new byte[]{1, 2, 3}));

        when(emailHistoryMapper.toDto(entity)).thenReturn(expectedDto);

        EmailHistoryDto result = emailHistoryFacade.mapToDto(entity);

        assertNotNull(result);
        assertEquals("email123", result.getId());
        assertEquals(true, result.getSent());
        assertEquals(1, result.getAttachments().size());
        assertTrue(result.getAttachments().containsKey("file1.pdf"));

        verify(emailHistoryMapper, times(1)).toDto(entity);
    }

    @Test
    void testSetGeneratedAttachments() throws CreateException, SystemException {
        String emailHistoryId = "email123";
        EmailHistoryEntity entity = new EmailHistoryEntity();
        entity.setId(emailHistoryId);

        List<GeneratedAttachmentDto> generatedAttachmentDtos = List.of(
                new GeneratedAttachmentDto("1", "user@example.com", Map.of("key", "value"), emailHistoryId, null),
                new GeneratedAttachmentDto("2", "user@example.com", Map.of("key2", "value2"), emailHistoryId, null)
        );

        when(generatedAttachmentMapper.toEntity(any(GeneratedAttachmentDto.class)))
                .thenAnswer(invocation -> {
                    GeneratedAttachmentDto arg = invocation.getArgument(0);
                    return new GeneratedAttachmentEntity(arg.getId(), arg.getEmail(), arg.getAttributes(), entity, null);
                });

        EmailHistoryDto dto = new EmailHistoryDto();
        dto.setGeneratedAttachments(generatedAttachmentDtos);

        emailHistoryFacade.setGeneratedAttachments(entity, dto);

        assertNotNull(dto.getGeneratedAttachments(), "Generated attachments list is null");
        assertFalse(dto.getGeneratedAttachments().isEmpty(), "Generated attachments list is empty");
        assertEquals(2, dto.getGeneratedAttachments().size(), "Expected 2 generated attachments");

        verify(generatedAttachmentMapper, times(2)).toEntity(any(GeneratedAttachmentDto.class));
        verify(generatedAttachmentService, times(2)).create(any(GeneratedAttachmentEntity.class));
        verify(generatedAttachmentService, times(2)).update(any(GeneratedAttachmentEntity.class));
    }

    @Test
    void testSetGeneratedAttachments_WithPrintedTemplate() throws CreateException, SystemException, FinderException {
        String emailHistoryId = "email123";
        String templateId = "template123";

        EmailHistoryEntity emailHistoryEntity = new EmailHistoryEntity();
        emailHistoryEntity.setId(emailHistoryId);

        PrintTemplateEntity printTemplateEntity = new PrintTemplateEntity();
        printTemplateEntity.setId(templateId);

        GeneratedAttachmentDto generatedAttachmentDto = new GeneratedAttachmentDto();
        generatedAttachmentDto.setId("attachment1");
        generatedAttachmentDto.setPrintTemplateId(templateId);

        List<GeneratedAttachmentDto> generatedAttachmentDtos = List.of(generatedAttachmentDto);

        GeneratedAttachmentEntity generatedAttachmentEntity = new GeneratedAttachmentEntity();
        generatedAttachmentEntity.setId("attachment1");

        when(generatedAttachmentMapper.toEntity(generatedAttachmentDto)).thenReturn(generatedAttachmentEntity);
        when(printTemplateService.findByPrimaryKey(templateId)).thenReturn(printTemplateEntity);

        EmailHistoryDto emailHistoryDto = new EmailHistoryDto();
        emailHistoryDto.setGeneratedAttachments(generatedAttachmentDtos);

        emailHistoryFacade.setGeneratedAttachments(emailHistoryEntity, emailHistoryDto);

        assertNotNull(generatedAttachmentEntity.getPrintTemplate());
        assertEquals(templateId, generatedAttachmentEntity.getPrintTemplate().getId());

        verify(printTemplateService, times(1)).findByPrimaryKey(templateId);
        verify(generatedAttachmentService, times(1)).create(any(GeneratedAttachmentEntity.class));
        verify(generatedAttachmentService, times(1)).update(any(GeneratedAttachmentEntity.class));
    }

    @Test
    void testFindByPrimaryKey_Found() throws FinderException {
        String emailHistoryId = "email123";
        EmailHistoryEntity entity = new EmailHistoryEntity();
        entity.setId(emailHistoryId);

        EmailHistoryDto dto = new EmailHistoryDto();
        dto.setId(emailHistoryId);

        when(emailHistoryService.findByPrimaryKey(emailHistoryId)).thenReturn(entity);
        when(emailHistoryMapper.toDto(entity)).thenReturn(dto);

        EmailHistoryDto result = emailHistoryFacade.findByPrimaryKey(emailHistoryId);

        assertEquals(emailHistoryId, result.getId());

        verify(emailHistoryService, times(1)).findByPrimaryKey(emailHistoryId);
        verify(emailHistoryMapper, times(1)).toDto(entity);
    }

    @Test
    void testFindByPrimaryKey_NotFound() throws FinderException {
        String emailHistoryId = "email123";

        when(emailHistoryService.findByPrimaryKey(emailHistoryId)).thenThrow(new FinderException("Failed to find EmailHistoryEntity with primary key: email123"));

        assertThrows(FinderException.class, () -> emailHistoryFacade.findByPrimaryKey(emailHistoryId));

        verify(emailHistoryService, times(1)).findByPrimaryKey(emailHistoryId);
        verify(emailHistoryMapper, never()).toDto(any());
    }

}
