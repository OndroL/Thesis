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
import cz.inspire.template.dto.PrintTemplateDto;
import cz.inspire.template.entity.PrintTemplateEntity;
import cz.inspire.template.mapper.PrintTemplateMapper;
import cz.inspire.template.service.PrintTemplateService;
import cz.inspire.utils.File;
import jakarta.ejb.CreateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
public class EmailHistoryFacadeTest {

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
    void testSetGeneratedAttachments() throws CreateException {
        String emailHistoryId = "email123";
        EmailHistoryEntity entity = new EmailHistoryEntity();
        entity.setId(emailHistoryId);

        List<GeneratedAttachmentEntity> generatedAttachments = List.of(
                new GeneratedAttachmentEntity("1", "user@example.com", Map.of("key", "value"), entity, null),
                new GeneratedAttachmentEntity("2", "user@example.com", Map.of("key2", "value2"), entity, null)
        );

        List<GeneratedAttachmentDto> generatedAttachmentDtos = List.of(
                new GeneratedAttachmentDto("1", "user@example.com", Map.of("key", "value"), emailHistoryId, null),
                new GeneratedAttachmentDto("2", "user@example.com", Map.of("key2", "value2"), emailHistoryId, null)
        );

        when(generatedAttachmentService.findByHistory(emailHistoryId)).thenReturn(generatedAttachments);
        when(generatedAttachmentMapper.toDto(any(GeneratedAttachmentEntity.class)))
                .thenAnswer(invocation -> {
                    GeneratedAttachmentEntity arg = invocation.getArgument(0);
                    return new GeneratedAttachmentDto(
                            arg.getId(),
                            arg.getEmail(),
                            arg.getAttributes(),
                            emailHistoryId,
                            (arg.getPrintTemplate() != null) ? arg.getPrintTemplate().getId() : null
                    );
                });

        EmailHistoryDto dto = new EmailHistoryDto();
        emailHistoryFacade.setGeneratedAttachments(entity, dto);

        assertNotNull(dto.getGeneratedAttachments());
        assertEquals(2, dto.getGeneratedAttachments().size());
        assertEquals("1", dto.getGeneratedAttachments().get(0).getId());
        assertEquals("2", dto.getGeneratedAttachments().get(1).getId());
        verify(generatedAttachmentService, times(1)).findByHistory(emailHistoryId);
    }

    @Test
    void testSetGeneratedAttachments_WithPrintedTemplate() throws CreateException, SystemException {
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
        when(printTemplateService.findById(templateId)).thenReturn(Optional.of(printTemplateEntity));

        EmailHistoryDto emailHistoryDto = new EmailHistoryDto();
        emailHistoryDto.setGeneratedAttachments(generatedAttachmentDtos);

        emailHistoryFacade.setGeneratedAttachments(emailHistoryEntity, emailHistoryDto);

        assertNotNull(generatedAttachmentEntity.getPrintTemplate());
        assertEquals(templateId, generatedAttachmentEntity.getPrintTemplate().getId());

        verify(printTemplateService, times(1)).findById(templateId);
        verify(generatedAttachmentService, times(1)).create(any(GeneratedAttachmentEntity.class));
        verify(generatedAttachmentService, times(1)).update(any(GeneratedAttachmentEntity.class));
    }

}
