package MapperTests.Email;

import RepositoryTests.DatabaseCleaner;
import cz.inspire.email.dto.GeneratedAttachmentDto;
import cz.inspire.email.entity.GeneratedAttachmentEntity;
import cz.inspire.email.mapper.GeneratedAttachmentMapper;
import cz.inspire.email.service.GeneratedAttachmentService;
import cz.inspire.template.entity.PrintTemplateEntity;
import cz.inspire.template.service.PrintTemplateService;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ejb.CreateException;
import jakarta.ejb.FinderException;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GeneratedAttachmentMapperIT {

    @Inject
    GeneratedAttachmentMapper generatedAttachmentMapper;

    @Inject
    GeneratedAttachmentService generatedAttachmentService; // adapt to your actual service/repository

    @Inject
    PrintTemplateService printTemplateService; // used to create/find a PrintTemplateEntity

    @Inject
    DatabaseCleaner databaseCleaner;

    private String attachmentId;
    private String printTemplateId;

    @BeforeAll
    @Transactional
    public void setup() throws CreateException {
        databaseCleaner.clearTable(GeneratedAttachmentEntity.class, true);
        databaseCleaner.clearTable(PrintTemplateEntity.class, true);

        // Optionally create a PrintTemplate record for referencing in the test
        PrintTemplateEntity template = new PrintTemplateEntity();
        template.setContent("Sample template content");
        template.setType(1);
        template.setTemplateName("TestTemplate");
        template.setFileName("testfile.txt");
        printTemplateService.create(template);
        printTemplateId = template.getId();
    }

    @Order(1)
    @Test
    @Transactional
    public void testPersistGeneratedAttachment() throws CreateException {
        GeneratedAttachmentDto dto = new GeneratedAttachmentDto();
        dto.setEmail("test@example.com");
        dto.setAttributes(Map.of("key1", "value1"));
        dto.setPrintTemplateId(printTemplateId);

        GeneratedAttachmentEntity entity = generatedAttachmentMapper.toEntity(dto);
        generatedAttachmentService.create(entity);
        attachmentId = entity.getId();

        assertNotNull(attachmentId);
        assertEquals("test@example.com", entity.getEmail());
        assertNotNull(entity.getAttributes());
        assertEquals("value1", entity.getAttributes().get("key1"));

        // Verify that the mapper's @AfterMapping set the printTemplate if printTemplateId was provided
        assertNotNull(entity.getPrintTemplate());
        assertEquals(printTemplateId, entity.getPrintTemplate().getId());
    }

    @Order(2)
    @Test
    @Transactional
    public void testRetrieveGeneratedAttachment() throws FinderException {
        GeneratedAttachmentEntity fromDb = generatedAttachmentService.findByPrimaryKey(attachmentId);
        assertNotNull(fromDb);

        GeneratedAttachmentDto mappedDto = generatedAttachmentMapper.toDto(fromDb);
        assertEquals(attachmentId, mappedDto.getId());
        assertEquals("test@example.com", mappedDto.getEmail());
        assertEquals("value1", mappedDto.getAttributes().get("key1"));
        assertEquals(printTemplateId, mappedDto.getPrintTemplateId());
    }
}
