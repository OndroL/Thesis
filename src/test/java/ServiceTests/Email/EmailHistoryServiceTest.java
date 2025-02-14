package ServiceTests.Email;

import cz.inspire.email.entity.EmailHistoryEntity;
import cz.inspire.email.repository.EmailHistoryRepository;
import cz.inspire.email.service.EmailHistoryService;
import cz.inspire.enterprise.exception.SystemException;
import cz.inspire.utils.FileAttributes;
import cz.inspire.utils.FileStorageUtil;
import jakarta.ejb.CreateException;
import jakarta.ejb.FinderException;
import jakarta.ejb.RemoveException;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailHistoryServiceTest {

    @Mock
    private EmailHistoryRepository emailHistoryRepository;

    @Mock
    private FileStorageUtil fileStorageUtil;

    @Mock
    private EntityManager em;

    private EmailHistoryService emailHistoryService;

    @BeforeEach
    void setUp() throws IllegalAccessException, NoSuchFieldException {
        MockitoAnnotations.openMocks(this);
        emailHistoryService = new EmailHistoryService(emailHistoryRepository);
        emailHistoryService.setEntityManager(em);
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

        doNothing().when(em).persist(entity);
        doNothing().when(em).flush();

        emailHistoryService.create(entity);

        verify(em, times(1)).persist(entity);
        verify(em, times(1)).flush();
    }

    @Test
    void testCreate_Failure() {
        EmailHistoryEntity entity = new EmailHistoryEntity(
                "1", new Date(), "Test Email", "Test Subject",
                List.of("Group1"), List.of("Recipient1"), List.of("MoreRecipient1"),
                true, false, new ArrayList<>(), false, new ArrayList<>()
        );

        doThrow(new RuntimeException("Database failure")).when(em).persist(entity);

        CreateException exception = assertThrows(CreateException.class, () -> emailHistoryService.create(entity));
        assertEquals("Failed to create EmailHistoryEntity", exception.getMessage());

        verify(em, times(1)).persist(entity);
    }

    @Test
    void testUpdate_Success() throws SystemException {
        EmailHistoryEntity entity = new EmailHistoryEntity(
                "1", new Date(), "Updated Email", "Updated Subject",
                List.of("Group1"), List.of("Recipient1"), List.of("MoreRecipient1"),
                true, false, new ArrayList<>(), false, new ArrayList<>()
        );

        when(em.merge(entity)).thenReturn(entity);
        doNothing().when(em).flush();

        emailHistoryService.update(entity);

        verify(em, times(1)).merge(entity);
        verify(em, times(1)).flush();
    }

    @Test
    void testUpdate_Failure() {
        EmailHistoryEntity entity = new EmailHistoryEntity(
                "1", new Date(), "Updated Email", "Updated Subject",
                List.of("Group1"), List.of("Recipient1"), List.of("MoreRecipient1"),
                true, false, new ArrayList<>(), false, new ArrayList<>()
        );

        doThrow(new RuntimeException("Database failure")).when(em).merge(entity);

        SystemException exception = assertThrows(SystemException.class, () -> emailHistoryService.update(entity));
        assertEquals("Failed to update EmailHistoryEntity", exception.getMessage());

        verify(em, times(1)).merge(entity);
    }

    @Test
    void testRemove_Success() throws RemoveException {
        EmailHistoryEntity entity = new EmailHistoryEntity(
                "1", new Date(), "Test Email", "Test Subject",
                List.of("Group1"), List.of("Recipient1"), List.of("MoreRecipient1"),
                true, false, new ArrayList<>(), false, new ArrayList<>()
        );

        when(em.merge(entity)).thenReturn(entity);
        doNothing().when(em).remove(entity);
        doNothing().when(em).flush();

        emailHistoryService.delete(entity);

        verify(em, times(1)).merge(entity);
        verify(em, times(1)).remove(entity);
        verify(em, times(1)).flush();
    }

    @Test
    void testRemove_Failure() {
        EmailHistoryEntity entity = new EmailHistoryEntity(
                "1", new Date(), "Test Email", "Test Subject",
                List.of("Group1"), List.of("Recipient1"), List.of("MoreRecipient1"),
                true, false, new ArrayList<>(), false, new ArrayList<>()
        );

        when(em.merge(entity)).thenReturn(entity);
        doThrow(new RuntimeException("Database failure")).when(em).remove(entity);

        RemoveException exception = assertThrows(RemoveException.class, () -> emailHistoryService.delete(entity));
        assertEquals("Failed to remove EmailHistoryEntity", exception.getMessage());

        verify(em, times(1)).merge(entity);
        verify(em, times(1)).remove(entity);
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
    void testFindById_Success() throws FinderException {
        EmailHistoryEntity entity = new EmailHistoryEntity("1", new Date(), "Email 1", "Subject 1", List.of(), List.of(), List.of(), true, false, List.of(), false, List.of());
        when(emailHistoryRepository.findById("1")).thenReturn(Optional.of(entity));

        Optional<EmailHistoryEntity> result = emailHistoryService.findById("1");

        assertTrue(result.isPresent());
        assertEquals("1", result.get().getId());
        verify(emailHistoryRepository, times(1)).findById("1");
    }

    @Test
    void testDelete_NullEntity() {
        RemoveException exception = assertThrows(RemoveException.class, () -> emailHistoryService.delete(null));
        assertEquals("Cannot delete null entity in EmailHistoryEntity", exception.getMessage());
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
    void testReadFile_Failure() throws IOException {
        String filePath = "FILE_SYSTEM/attachments/file1.pdf";

        doThrow(new IOException("File not found")).when(fileStorageUtil).readFile(eq(filePath));

        IOException exception = assertThrows(IOException.class, () -> emailHistoryService.readFile(filePath));
        assertEquals("File not found", exception.getMessage());

        verify(fileStorageUtil, times(1)).readFile(eq(filePath));
    }


    @Test
    void testGenerateFileName() {
        String fileName = EmailHistoryService.generateFileName();

        assertNotNull(fileName);
        assertTrue(fileName.matches("voucher-\\d{1,2}-\\d{1,2}-\\d{4}\\.pdf"));
    }

    @Test
    void testSaveAttachments_Success() throws IOException {
        Map<String, byte[]> attachments = new HashMap<>();
        attachments.put("file1.pdf", new byte[]{1, 2, 3});
        attachments.put("file2.pdf", new byte[]{4, 5, 6});

        String rootDirectory = "FILE_SYSTEM";
        String subDirectory = "attachments";

        doAnswer(invocation -> {
            String fileName = invocation.getArgument(1);
            String subDir = invocation.getArgument(2);

            assertEquals(subDirectory, subDir);

            String savedPath = rootDirectory + "/" + subDir + "/" + UUID.randomUUID();

            return new FileAttributes(fileName, savedPath);
        }).when(fileStorageUtil).saveFile(any(), any(), eq(subDirectory));

        List<FileAttributes> result = emailHistoryService.saveAttachments(attachments);

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(fileStorageUtil, times(2)).saveFile(any(), any(), eq(subDirectory));
    }


    @Test
    void testReadFile_Success() throws IOException {
        byte[] fileContent = new byte[]{1, 2, 3};

        String rootDirectory = "FILE_SYSTEM";
        String subDirectory = "attachments";
        String generatedFilePath = rootDirectory + "/" + subDirectory + "/" + UUID.randomUUID();

        when(fileStorageUtil.readFile(anyString()))
                .thenAnswer(invocation -> {
                    String requestedPath = invocation.getArgument(0);
                    assertTrue(requestedPath.startsWith(rootDirectory + "/" + subDirectory),
                            "Path should start with: " + rootDirectory + "/" + subDirectory);
                    return fileContent;
                });

        byte[] result = emailHistoryService.readFile(generatedFilePath);

        assertNotNull(result);
        assertEquals(3, result.length);
        verify(fileStorageUtil, times(1)).readFile(anyString());
    }
}
