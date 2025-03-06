package ServiceTests.Email;

import cz.inspire.email.entity.EmailHistoryEntity;
import cz.inspire.email.repository.EmailHistoryRepository;
import cz.inspire.email.service.EmailHistoryService;
import cz.inspire.exception.SystemException;
import cz.inspire.utils.FileAttributes;
import cz.inspire.utils.FileStorageUtil;
import jakarta.ejb.CreateException;
import jakarta.ejb.FinderException;
import jakarta.ejb.RemoveException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailHistoryServiceTest {

    @Mock
    private EmailHistoryRepository emailHistoryRepository;

    @Mock
    private FileStorageUtil fileStorageUtil;

    private EmailHistoryService emailHistoryService;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        MockitoAnnotations.openMocks(this);
        emailHistoryService = new EmailHistoryService(emailHistoryRepository);
        // Inject fileStorageUtil via reflection
        java.lang.reflect.Field field = EmailHistoryService.class.getDeclaredField("fileStorageUtil");
        field.setAccessible(true);
        field.set(emailHistoryService, fileStorageUtil);
    }

    @Test
    void testCreate_Success() throws CreateException {
        EmailHistoryEntity entity = new EmailHistoryEntity(
                "1", new Date(), "Test Email", "Test Subject",
                List.of("Group1"), List.of("Recipient1"), List.of("MoreRecipient1"),
                true, false, new ArrayList<>(), false, new ArrayList<>()
        );
        when(emailHistoryRepository.create(entity)).thenReturn(entity);

        EmailHistoryEntity result = emailHistoryService.create(entity);

        assertEquals(entity, result);
        verify(emailHistoryRepository, times(1)).create(entity);
    }

    @Test
    void testCreate_Failure() {
        EmailHistoryEntity entity = new EmailHistoryEntity(
                "1", new Date(), "Test Email", "Test Subject",
                List.of("Group1"), List.of("Recipient1"), List.of("MoreRecipient1"),
                true, false, new ArrayList<>(), false, new ArrayList<>()
        );
        when(emailHistoryRepository.create(entity)).thenThrow(new RuntimeException("Database failure"));

        CreateException exception = assertThrows(CreateException.class, () -> emailHistoryService.create(entity));
        assertEquals("Failed to create EmailHistoryEntity", exception.getMessage());
        verify(emailHistoryRepository, times(1)).create(entity);
    }

    @Test
    void testUpdate_Success() throws SystemException {
        EmailHistoryEntity entity = new EmailHistoryEntity(
                "1", new Date(), "Updated Email", "Updated Subject",
                List.of("Group1"), List.of("Recipient1"), List.of("MoreRecipient1"),
                true, false, new ArrayList<>(), false, new ArrayList<>()
        );
        when(emailHistoryRepository.update(entity)).thenReturn(entity);

        EmailHistoryEntity result = emailHistoryService.update(entity);

        assertEquals(entity, result);
        verify(emailHistoryRepository, times(1)).update(entity);
    }

    @Test
    void testUpdate_Failure() {
        EmailHistoryEntity entity = new EmailHistoryEntity(
                "1", new Date(), "Updated Email", "Updated Subject",
                List.of("Group1"), List.of("Recipient1"), List.of("MoreRecipient1"),
                true, false, new ArrayList<>(), false, new ArrayList<>()
        );
        when(emailHistoryRepository.update(entity)).thenThrow(new RuntimeException("Database failure"));

        SystemException exception = assertThrows(SystemException.class, () -> emailHistoryService.update(entity));
        assertEquals("Failed to update EmailHistoryEntity", exception.getMessage());
        verify(emailHistoryRepository, times(1)).update(entity);
    }

    @Test
    void testRemove_Success() throws RemoveException {
        EmailHistoryEntity entity = new EmailHistoryEntity(
                "1", new Date(), "Test Email", "Test Subject",
                List.of("Group1"), List.of("Recipient1"), List.of("MoreRecipient1"),
                true, false, new ArrayList<>(), false, new ArrayList<>()
        );
        doNothing().when(emailHistoryRepository).delete(entity);

        emailHistoryService.delete(entity);

        verify(emailHistoryRepository, times(1)).delete(entity);
    }

    @Test
    void testRemove_Failure() {
        EmailHistoryEntity entity = new EmailHistoryEntity(
                "1", new Date(), "Test Email", "Test Subject",
                List.of("Group1"), List.of("Recipient1"), List.of("MoreRecipient1"),
                true, false, new ArrayList<>(), false, new ArrayList<>()
        );
        doThrow(new RuntimeException("Database failure")).when(emailHistoryRepository).delete(entity);

        RemoveException exception = assertThrows(RemoveException.class, () -> emailHistoryService.delete(entity));
        assertEquals("Failed to remove EmailHistoryEntity", exception.getMessage());
        verify(emailHistoryRepository, times(1)).delete(entity);
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
        when(emailHistoryRepository.findByDate(from, to, 10, 0)).thenReturn(expected);

        List<EmailHistoryEntity> result = emailHistoryService.findByDate(new Date(from.getTime()), new Date(to.getTime()), 0, 10);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(emailHistoryRepository, times(1)).findByDate(from, to, 10, 0);
    }

    @Test
    void testFindById_Success() throws FinderException {
        EmailHistoryEntity entity = new EmailHistoryEntity("1", new Date(), "Email 1", "Subject 1", List.of(), List.of(), List.of(), true, false, List.of(), false, List.of());
        when(emailHistoryRepository.findByPrimaryKey("1")).thenReturn(entity);

        EmailHistoryEntity result = emailHistoryService.findByPrimaryKey("1");

        assertNotNull(result);
        assertEquals("1", result.getId());
        verify(emailHistoryRepository, times(1)).findByPrimaryKey("1");
    }

    @Test
    void testDelete_NullEntity() {
        RemoveException exception = assertThrows(RemoveException.class, () -> emailHistoryService.delete(null));
        assertEquals("Cannot delete null as EmailHistoryEntity", exception.getMessage());
    }

    @Test
    void testSaveAttachments_EmptyInput() throws IOException {
        Map<String, byte[]> attachments = new HashMap<>();

        List<FileAttributes> savedAttachments = emailHistoryService.saveAttachments(attachments);

        assertNotNull(savedAttachments);
        assertTrue(savedAttachments.isEmpty());
        verify(fileStorageUtil, never()).saveFile(any(), any(), any());
    }

    @Test
    void testSaveAttachments_Failure() throws IOException {
        Map<String, byte[]> attachments = new HashMap<>();
        attachments.put("file1.pdf", new byte[]{1, 2, 3});

        doThrow(new IOException("File system error")).when(fileStorageUtil).saveFile(any(), any(), any());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> emailHistoryService.saveAttachments(attachments));
        assertTrue(exception.getMessage().contains("Failed to save attachment: file1.pdf"));
        verify(fileStorageUtil, times(1)).saveFile(any(), any(), any());
    }

    @Test
    void testSaveAttachments_Success() throws IOException {
        Map<String, byte[]> attachments = new HashMap<>();
        attachments.put("file1.pdf", new byte[]{1, 2, 3});
        attachments.put("file2.pdf", new byte[]{4, 5, 6});

        String subDirectory = "attachments";
        doAnswer(invocation -> {
            String fileName = invocation.getArgument(1);
            String dir = invocation.getArgument(2);
            assertEquals(subDirectory, dir);
            return new FileAttributes(fileName, "FILE_SYSTEM/" + dir + "/" + UUID.randomUUID());
        }).when(fileStorageUtil).saveFile(any(), any(), eq(subDirectory));

        List<FileAttributes> result = emailHistoryService.saveAttachments(attachments);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(fileStorageUtil, times(2)).saveFile(any(), any(), eq(subDirectory));
    }

    @Test
    void testReadFile_Failure() throws IOException {
        String filePath = "FILE_SYSTEM/attachments/file1.pdf";
        doThrow(new IOException("File not found")).when(fileStorageUtil).readFile(eq(filePath));

        IOException exception = assertThrows(IOException.class, () -> emailHistoryService.readFile(filePath));
        assertEquals("File not found", exception.getMessage());
        verify(fileStorageUtil, times(1)).readFile(eq(filePath));
    }

    @Test
    void testReadFile_Success() throws IOException {
        byte[] fileContent = new byte[]{1, 2, 3};
        String rootDirectory = "FILE_SYSTEM";
        String subDirectory = "attachments";
        String generatedFilePath = rootDirectory + "/" + subDirectory + "/" + UUID.randomUUID();

        when(fileStorageUtil.readFile(anyString())).thenAnswer(invocation -> {
            String requestedPath = invocation.getArgument(0);
            assertTrue(requestedPath.startsWith(rootDirectory + "/" + subDirectory));
            return fileContent;
        });

        byte[] result = emailHistoryService.readFile(generatedFilePath);

        assertNotNull(result);
        assertEquals(3, result.length);
        verify(fileStorageUtil, times(1)).readFile(anyString());
    }

    @Test
    void testGenerateFileName() {
        String fileName = EmailHistoryService.generateFileName();
        assertNotNull(fileName);
        assertTrue(fileName.matches("voucher-\\d{1,2}-\\d{1,2}-\\d{4}\\.pdf"));
    }
}
