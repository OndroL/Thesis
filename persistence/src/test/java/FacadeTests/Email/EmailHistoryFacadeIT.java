package FacadeTests.Email;

import RepositoryTests.DatabaseCleaner;
import cz.inspire.email.dto.EmailHistoryDto;
import cz.inspire.email.dto.GeneratedAttachmentDto;
import cz.inspire.email.entity.EmailHistoryEntity;
import cz.inspire.email.facade.EmailHistoryFacade;
import cz.inspire.template.dto.PrintTemplateDto;
import cz.inspire.template.entity.PrintTemplateEntity;
import cz.inspire.template.mapper.PrintTemplateMapper;
import cz.inspire.template.service.PrintTemplateService;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EmailHistoryFacadeIT {

    @Inject
    EmailHistoryFacade emailHistoryFacade;

    @Inject
    PrintTemplateService printTemplateService;

    @Inject
    DatabaseCleaner databaseCleaner;

    @Inject
    PrintTemplateMapper printTemplateMapper;

    private String printTemplateId;

    @BeforeAll
    @Transactional
    public void setup() throws Exception {
        // Clean the email_history table (and any related tables if necessary)
        databaseCleaner.clearTable(EmailHistoryEntity.class, true);
        databaseCleaner.clearTable(PrintTemplateEntity.class, true);
        // Create a full PrintTemplateDto (all fields except id)
        PrintTemplateDto printTemplateDto = new PrintTemplateDto();
        printTemplateDto.setContent("Test template content");
        printTemplateDto.setType(1);
        printTemplateDto.setTemplateName("Test Template");
        printTemplateDto.setFileName("template.pdf");
        // Persist the print template (assume the service returns the generated id)
        printTemplateId = printTemplateService.create(printTemplateMapper.toEntity(printTemplateDto)).getId();
        assertNotNull(printTemplateId, "PrintTemplate id should be generated");
    }

    @Test
    @Order(1)
    @Transactional
    public void testCreateEmailHistoryWithFullDtos() throws Exception {
        // Build a full EmailHistoryDto with all fields (except id)
        EmailHistoryDto emailHistoryDto = new EmailHistoryDto();
        emailHistoryDto.setDate(new Date());
        emailHistoryDto.setText("Integration test email text");
        emailHistoryDto.setSubject("Integration Test Subject");
        emailHistoryDto.setGroups(List.of("group1", "group2"));
        emailHistoryDto.setRecipients(List.of("recipient1@example.com", "recipient2@example.com"));
        emailHistoryDto.setMoreRecipients(List.of("more1@example.com"));
        // Set file attachments as a map (for example, file name to content bytes)
        emailHistoryDto.setAttachments(Map.of("file1.txt", "Test file content".getBytes()));
        emailHistoryDto.setAutomatic(true);
        emailHistoryDto.setHtml(true);
        emailHistoryDto.setSent(false);

        // Create a GeneratedAttachmentDto with all fields (except id and emailHistoryId)
        GeneratedAttachmentDto attachmentDto = new GeneratedAttachmentDto();
        attachmentDto.setEmail("attachment@example.com");
        attachmentDto.setAttributes(Map.of("key", "value"));
        // Set the printTemplateId to the one we created
        attachmentDto.setPrintTemplateId(printTemplateId);
        // Do not set emailHistoryId – it will be set by the mapping (via the EmailHistoryMapper's @AfterMapping)

        // Add the attachment DTO to the EmailHistoryDto
        emailHistoryDto.setGeneratedAttachments(List.of(attachmentDto));

        // Create the EmailHistory record via the facade
        String createdId = emailHistoryFacade.create(emailHistoryDto);
        assertNotNull(createdId, "Created EmailHistory id should not be null");

        // Retrieve the persisted EmailHistoryDto via the facade
        EmailHistoryDto createdDto = emailHistoryFacade.findByPrimaryKey(createdId);
        assertNotNull(createdDto, "Retrieved EmailHistoryDto should not be null");

        // Verify that the basic properties match
        assertEquals("Integration test email text", createdDto.getText());
        assertEquals("Integration Test Subject", createdDto.getSubject());
        assertEquals(2, createdDto.getGroups().size());
        assertEquals(2, createdDto.getRecipients().size());
        assertEquals(1, createdDto.getMoreRecipients().size());
        assertNotNull(createdDto.getAttachments(), "Attachments should not be null");
        assertTrue(createdDto.getAutomatic());
        assertTrue(createdDto.getHtml());
        assertFalse(createdDto.getSent());

        // Verify the generated attachments mapping
        assertNotNull(createdDto.getGeneratedAttachments(), "Generated attachments should not be null");
        assertEquals(1, createdDto.getGeneratedAttachments().size());
        GeneratedAttachmentDto createdAttachment = createdDto.getGeneratedAttachments().getFirst();
        // Although the dto may not include the parent's id, the mapping of the back-reference should have taken place.
        // We verify that the print template id was correctly set.
        assertEquals(printTemplateId, createdAttachment.getPrintTemplateId(), "PrintTemplateId should match");
    }

    @Test
    @Order(2)
    @Transactional
    public void testFindAllEmailHistories() throws Exception {
        // Retrieve all EmailHistoryDto records via the facade
        List<EmailHistoryDto> allEmailHistories = emailHistoryFacade.findAll();
        assertNotNull(allEmailHistories, "List of EmailHistoryDto should not be null");
        // We expect at least one record from the previous test
        assertFalse(allEmailHistories.isEmpty(), "There should be at least one EmailHistory record");
    }
}
