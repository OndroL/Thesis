package MapperTests.Email;

import cz.inspire.email.dto.EmailHistoryDto;
import cz.inspire.email.dto.GeneratedAttachmentDto;
import cz.inspire.email.entity.EmailHistoryEntity;
import cz.inspire.email.entity.GeneratedAttachmentEntity;
import cz.inspire.email.mapper.EmailHistoryMapper;
import cz.inspire.email.service.EmailHistoryService;
import RepositoryTests.DatabaseCleaner;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ejb.CreateException;
import jakarta.ejb.FinderException;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
public class EmailHistoryMapperIT {

    @Inject
    EmailHistoryMapper emailHistoryMapper;

    @Inject
    EmailHistoryService emailHistoryService;

    @Inject
    DatabaseCleaner databaseCleaner;

    private String emailHistoryId;

    @BeforeAll
    @Transactional
    public void setup() {
        databaseCleaner.clearTable(EmailHistoryEntity.class, true);
    }

    @Order(1)
    @Test
    @Transactional
    public void testPersistEmailHistoryWithGeneratedAttachments() throws CreateException {
        EmailHistoryDto emailHistoryDto = new EmailHistoryDto();
        emailHistoryDto.setDate(new Date());
        emailHistoryDto.setText("Test email text");
        emailHistoryDto.setSubject("Test subject");

        GeneratedAttachmentDto attachmentDto = new GeneratedAttachmentDto();
        attachmentDto.setEmail("attachment@example.com");
        attachmentDto.setAttributes(null);


        List<GeneratedAttachmentDto> attachments = new ArrayList<>();
        attachments.add(attachmentDto);
        emailHistoryDto.setGeneratedAttachments(attachments);

        EmailHistoryEntity emailHistoryEntity = emailHistoryMapper.toEntity(emailHistoryDto);

        emailHistoryService.create(emailHistoryEntity);
        emailHistoryId = emailHistoryEntity.getId();

        assertNotNull(emailHistoryEntity.getId(), "EmailHistoryEntity id should be generated");

        assertNotNull(emailHistoryEntity.getGeneratedAttachments(), "Generated attachments list should not be null");
        assertEquals(1, emailHistoryEntity.getGeneratedAttachments().size(), "Should have one attachment");

        GeneratedAttachmentEntity attachmentEntity = emailHistoryEntity.getGeneratedAttachments().getFirst();
        assertNotNull(attachmentEntity.getEmailHistory(), "Attachment's emailHistory should be set");
        assertEquals(emailHistoryEntity.getId(), attachmentEntity.getEmailHistory().getId(),
                "Attachment should reference the parent's id");
    }

    @Order(2)
    @Test
    @Transactional
    public void testRetrieveEmailHistoryWithGeneratedAttachments() throws FinderException {
        EmailHistoryEntity fromDb = emailHistoryService.findByPrimaryKey(emailHistoryId);
        assertNotNull(fromDb, "Retrieved EmailHistoryEntity should not be null");

        List<GeneratedAttachmentEntity> attachments = fromDb.getGeneratedAttachments();
        assertNotNull(attachments, "Generated attachments list should not be null");
        assertEquals(1, attachments.size(), "Should have one generated attachment");

        GeneratedAttachmentEntity attachmentEntity = attachments.getFirst();
        assertNotNull(attachmentEntity.getEmailHistory(), "Attachment's emailHistory should be set");
        assertEquals(emailHistoryId, attachmentEntity.getEmailHistory().getId(), "Attachment should reference the parent's id");
    }
}
