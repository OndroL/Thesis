package MapperTests.Template;

import RepositoryTests.DatabaseCleaner;
import cz.inspire.template.dto.PrintTemplateDto;
import cz.inspire.template.entity.PrintTemplateEntity;
import cz.inspire.template.mapper.PrintTemplateMapper;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PrintTemplateMapperIT {

    @Inject
    PrintTemplateMapper printTemplateMapper;

    @Inject
    PrintTemplateService printTemplateService;

    @Inject
    DatabaseCleaner databaseCleaner;

    private String templateId;

    @BeforeAll
    @Transactional
    public void setup() {
        databaseCleaner.clearTable(PrintTemplateEntity.class, true);
    }

    @Order(1)
    @Test
    @Transactional
    public void testPersistPrintTemplate() throws CreateException {
        PrintTemplateDto dto = new PrintTemplateDto();
        dto.setContent("Example content");
        dto.setType(1);
        dto.setTemplateName("Test Template");
        dto.setFileName("example.txt");

        PrintTemplateEntity entity = printTemplateMapper.toEntity(dto);
        printTemplateService.create(entity);
        templateId = entity.getId();

        assertNotNull(templateId);
        assertEquals("Example content", entity.getContent());
        assertEquals(1, entity.getType());
        assertEquals("Test Template", entity.getTemplateName());
        assertEquals("example.txt", entity.getFileName());
    }

    @Order(2)
    @Test
    @Transactional
    public void testRetrievePrintTemplate() throws FinderException {
        PrintTemplateEntity entity = printTemplateService.findByPrimaryKey(templateId);
        assertNotNull(entity);

        PrintTemplateDto dto = printTemplateMapper.toDto(entity);
        assertEquals(templateId, dto.getId());
        assertEquals("Example content", dto.getContent());
        assertEquals(1, dto.getType());
        assertEquals("Test Template", dto.getTemplateName());
        assertEquals("example.txt", dto.getFileName());
    }
}
