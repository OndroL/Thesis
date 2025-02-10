package ServiceTests.Email;

import cz.inspire.email.entity.EmailHistoryEntity;
import cz.inspire.email.repository.EmailHistoryRepository;
import cz.inspire.email.service.EmailHistoryService;
import cz.inspire.enterprise.exception.SystemException;
import cz.inspire.utils.File;
import cz.inspire.utils.FileStorageUtil;
import jakarta.ejb.CreateException;
import jakarta.ejb.FinderException;
import jakarta.ejb.RemoveException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailHistoryServiceTest {

    @Mock
    private EmailHistoryRepository emailHistoryRepository;

    @Mock
    private FileStorageUtil fileStorageUtil;

    @Spy
    private EmailHistoryService emailHistoryService;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        emailHistoryService = spy(new EmailHistoryService(emailHistoryRepository));
        Field fileStorageUtilField = EmailHistoryService.class.getDeclaredField("fileStorageUtil");
        fileStorageUtilField.setAccessible(true);
        fileStorageUtilField.set(emailHistoryService, fileStorageUtil);
    }


    @Test
    void testCreate_Success() throws CreateException {
        EmailHistoryEntity entity = new EmailHistoryEntity(
                "1", new Date(), "Test Email", "Test Subject",
                List.of("Group1"), List.of("Recipient1"), List.of("MoreRecipient1"),
                true, false, new ArrayList<>(), false, new ArrayList<>()
        );

        emailHistoryService.create(entity);

        verify(emailHistoryService, times(1)).create(entity);
        verify(emailHistoryRepository, times(1)).insert(entity);
    }

    @Test
    void testCreate_Failure() throws CreateException {
        EmailHistoryEntity entity = new EmailHistoryEntity(
                "1", new Date(), "Test Email", "Test Subject",
                List.of("Group1"), List.of("Recipient1"), List.of("MoreRecipient1"),
                true, false, new ArrayList<>(), false, new ArrayList<>()
        );
        doThrow(new RuntimeException("Database failure")).when(emailHistoryRepository).insert(entity);

        assertThrows(CreateException.class, () -> emailHistoryService.create(entity));

        verify(emailHistoryService, times(1)).create(entity);
    }

    @Test
    void testUpdate_Success() throws SystemException {
        EmailHistoryEntity entity = new EmailHistoryEntity(
                "1", new Date(), "Updated Email", "Updated Subject",
                List.of("Group1"), List.of("Recipient1"), List.of("MoreRecipient1"),
                true, false, new ArrayList<>(), false, new ArrayList<>()
        );

        emailHistoryService.update(entity);

        verify(emailHistoryService, times(1)).update(entity);
        verify(emailHistoryRepository, times(1)).save(entity);
    }

    @Test
    void testUpdate_Failure() throws SystemException {
        EmailHistoryEntity entity = new EmailHistoryEntity(
                "1", new Date(), "Updated Email", "Updated Subject",
                List.of("Group1"), List.of("Recipient1"), List.of("MoreRecipient1"),
                true, false, new ArrayList<>(), false, new ArrayList<>()
        );
        doThrow(new RuntimeException("Database failure")).when(emailHistoryRepository).save(entity);

        assertThrows(SystemException.class, () -> emailHistoryService.update(entity));

        verify(emailHistoryService, times(1)).update(entity);
    }

    @Test
    void testRemove_Success() throws RemoveException {
        EmailHistoryEntity entity = new EmailHistoryEntity(
                "1", new Date(), "Test Email", "Test Subject",
                List.of("Group1"), List.of("Recipient1"), List.of("MoreRecipient1"),
                true, false, new ArrayList<>(), false, new ArrayList<>()
        );

        emailHistoryService.delete(entity);

        verify(emailHistoryService, times(1)).delete(entity);
        verify(emailHistoryRepository, times(1)).delete(entity);
    }

    @Test
    void testRemove_Failure() throws RemoveException {
        EmailHistoryEntity entity = new EmailHistoryEntity(
                "1", new Date(), "Test Email", "Test Subject",
                List.of("Group1"), List.of("Recipient1"), List.of("MoreRecipient1"),
                true, false, new ArrayList<>(), false, new ArrayList<>()
        );
        doThrow(new RuntimeException("Database failure")).when(emailHistoryRepository).delete(entity);

        assertThrows(RemoveException.class, () -> emailHistoryService.delete(entity));

        verify(emailHistoryService, times(1)).delete(entity);
    }

    @Test
    void testFindAll_Success() throws FinderException {
        List<EmailHistoryEntity> entities = List.of(
                new EmailHistoryEntity("1", new Date(), "Email 1", "Subject 1", List.of(), List.of(), List.of(), true, false, List.of(), false, List.of()),
                new EmailHistoryEntity("2", new Date(), "Email 2", "Subject 2", List.of(), List.of(), List.of(), false, true, List.of(), true, List.of())
        );

        when(emailHistoryRepository.findAllOrdered()).thenReturn(entities);

        List<EmailHistoryEntity> result = emailHistoryService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(emailHistoryRepository, times(1)).findAllOrdered();
    }

    @Test
    void testFindByDate_Success() throws FinderException {
        Timestamp from = new Timestamp(System.currentTimeMillis() - 10000);
        Timestamp to = new Timestamp(System.currentTimeMillis());
        List<EmailHistoryEntity> expected = List.of(
                new EmailHistoryEntity("1", new Date(), "Email 1", "Subject 1", List.of(), List.of(), List.of(), true, false, List.of(), false, List.of())
        );

        when(emailHistoryRepository.findByDate(from, to, new jakarta.data.Limit(10, 1))).thenReturn(expected);

        List<EmailHistoryEntity> result = emailHistoryService.findByDate(new Date(from.getTime()), new Date(to.getTime()), 0, 10);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(emailHistoryRepository, times(1)).findByDate(from, to, new jakarta.data.Limit(10, 1));
    }

    @Test
    void testFindById_Success() {
        EmailHistoryEntity entity = new EmailHistoryEntity("1", new Date(), "Email 1", "Subject 1", List.of(), List.of(), List.of(), true, false, List.of(), false, List.of());
        when(emailHistoryRepository.findById("1")).thenReturn(Optional.of(entity));

        Optional<EmailHistoryEntity> result = emailHistoryService.findById("1");

        assertTrue(result.isPresent());
        assertEquals("1", result.get().getId());
        verify(emailHistoryRepository, times(1)).findById("1");
    }

    @Test
    void testSaveAttachments_Success() throws IOException {
        Map<String, byte[]> attachments = new HashMap<>();
        attachments.put("file1.pdf", new byte[]{1, 2, 3});
        attachments.put("file2.pdf", new byte[]{4, 5, 6});

        String rootDirectory = "FILE_SYSTEM";
        String subDirectory = "attachments";


        doAnswer(invocation -> {
            invocation.getArgument(0);
            String fileName = invocation.getArgument(1);
            String subDir = invocation.getArgument(2);

            // Generate a UUID-based filename dynamically (as the service does)
            String savedPath = rootDirectory + "/" + subDir + "/" + UUID.randomUUID();

            return new File(fileName, savedPath);
        }).when(fileStorageUtil).saveFile(any(), any(), eq(subDirectory));

        List<File> result = emailHistoryService.saveAttachments(attachments);

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(fileStorageUtil, times(2)).saveFile(any(), any(), eq(subDirectory));
    }

    @Test
    void testReadFile_Success() throws IOException {
        byte[] fileContent = new byte[]{1, 2, 3};  // Mocked file content

        String rootDirectory = "FILE_SYSTEM";
        String subDirectory = "attachments";

        when(fileStorageUtil.readFile(argThat(path -> path.startsWith(rootDirectory + "/" + subDirectory))))
                .thenReturn(fileContent);

        String generatedFilePath = rootDirectory + "/" + subDirectory + "/" + UUID.randomUUID();

        byte[] result = emailHistoryService.readFile(generatedFilePath);

        assertNotNull(result);
        assertEquals(3, result.length);
        verify(fileStorageUtil, times(1)).readFile(anyString());
    }
}
