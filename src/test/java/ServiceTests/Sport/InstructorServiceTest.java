package ServiceTests.Sport;

import cz.inspire.sport.entity.InstructorEntity;
import cz.inspire.sport.repository.InstructorRepository;
import cz.inspire.sport.service.InstructorService;
import cz.inspire.utils.FileAttributes;
import jakarta.ejb.FinderException;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InstructorServiceTest {

    @Mock
    private InstructorRepository instructorRepository;

    @Mock
    private EntityManager em;

    private InstructorService instructorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        instructorService = new InstructorService(instructorRepository);
        instructorService.setEntityManager(em);
    }

    @Test
    void testFindAll_Success() throws FinderException {
        List<InstructorEntity> expectedEntities = List.of(
                new InstructorEntity("1", "John", "Doe", 1, "john@example.com", "420", "123456789",
                        "john.internal@example.com", "420", "987654321", "Info", "Blue", null,
                        false, "googleId", true, 15, null, null, null),
                new InstructorEntity("2", "Jane", "Smith", 2, "jane@example.com", "420", "987654321",
                        "jane.internal@example.com", "420", "123456789", "More Info", "Red", null,
                        false, "googleId2", true, 10, null, null, null)
        );

        when(instructorRepository.findAllOrdered()).thenReturn(expectedEntities);

        List<InstructorEntity> result = instructorService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(instructorRepository, times(1)).findAllOrdered();
    }

    @Test
    void testFindAll_Failure() {
        when(instructorRepository.findAllOrdered()).thenThrow(new RuntimeException("Database error"));

        FinderException exception = assertThrows(FinderException.class, () -> instructorService.findAll());
        assertTrue(exception.getMessage().contains("Error retrieving all InstructorEntity records"));

        verify(instructorRepository, times(1)).findAllOrdered();
    }

    @Test
    void testFindAllByActivity_Success() throws FinderException {
        String activityId = "activity123";
        List<InstructorEntity> expectedEntities = List.of(
                new InstructorEntity("1", "John", "Doe", 1, "john@example.com", "420", "123456789",
                        "john.internal@example.com", "420", "987654321", "Info", "Blue", null,
                        false, "googleId", true, 15, null, null, null)
        );

        when(instructorRepository.findAllByActivity(eq(activityId), any(), eq(false))).thenReturn(expectedEntities);

        List<InstructorEntity> result = instructorService.findAllByActivity(activityId, 0, 10, false);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(instructorRepository, times(1)).findAllByActivity(eq(activityId), any(), eq(false));
    }

    @Test
    void testFindAllByActivity_Failure() {
        String activityId = "activity123";

        when(instructorRepository.findAllByActivity(eq(activityId), any(), eq(false))).thenThrow(new RuntimeException("Database error"));

        FinderException exception = assertThrows(FinderException.class, () -> instructorService.findAllByActivity(activityId, 0, 10, false));
        assertTrue(exception.getMessage().contains("Error retrieving InstructorEntity records"));

        verify(instructorRepository, times(1)).findAllByActivity(eq(activityId), any(), eq(false));
    }

    @Test
    void testCountInstructors_Success() throws FinderException {
        when(instructorRepository.countInstructors(false)).thenReturn(42L);

        Long result = instructorService.countInstructors(false);

        assertNotNull(result);
        assertEquals(42L, result);
        verify(instructorRepository, times(1)).countInstructors(false);
    }

    @Test
    void testCountInstructors_Failure() {
        when(instructorRepository.countInstructors(false)).thenThrow(new RuntimeException("Database error"));

        FinderException exception = assertThrows(FinderException.class, () -> instructorService.countInstructors(false));
        assertTrue(exception.getMessage().contains("Error retrieving total count of InstructorEntity records"));

        verify(instructorRepository, times(1)).countInstructors(false);
    }

    @Test
    void testCountInstructorsByActivity_Success() throws FinderException {
        String activityId = "activity123";

        when(instructorRepository.countInstructorsByActivity(activityId, false)).thenReturn(10L);

        Long result = instructorService.countInstructorsByActivity(activityId, false);

        assertNotNull(result);
        assertEquals(10L, result);
        verify(instructorRepository, times(1)).countInstructorsByActivity(activityId, false);
    }

    @Test
    void testCountInstructorsByActivity_Failure() {
        String activityId = "activity123";

        when(instructorRepository.countInstructorsByActivity(activityId, false)).thenThrow(new RuntimeException("Database error"));

        FinderException exception = assertThrows(FinderException.class, () -> instructorService.countInstructorsByActivity(activityId, false));
        assertTrue(exception.getMessage().contains("Error retrieving count of InstructorEntity records"));

        verify(instructorRepository, times(1)).countInstructorsByActivity(activityId, false);
    }

    @Test
    void testSavePhoto_Success() throws IOException {
        byte[] photoData = new byte[]{1, 2, 3};
        String id = "1";

        FileAttributes expectedFile = new FileAttributes(id + ".png", "FILE_SYSTEM/photos/" + id + ".png");

        InstructorService spyService = spy(instructorService);
        doReturn(expectedFile).when(spyService).savePhoto(photoData, id);

        FileAttributes result = spyService.savePhoto(photoData, id);

        assertNotNull(result);
        assertEquals(expectedFile.getFilePath(), result.getFilePath());
    }

    @Test
    void testSavePhoto_Failure() {
        byte[] photoData = new byte[]{1, 2, 3};
        String id = "1";

        InstructorService spyService = spy(instructorService);
        try {
            doThrow(new IOException("File save error")).when(spyService).savePhoto(photoData, id);

            IOException exception = assertThrows(IOException.class, () -> spyService.savePhoto(photoData, id));
            assertTrue(exception.getMessage().contains("File save error"));
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }

    @Test
    void testReadFile_Success() throws IOException {
        String filePath = "FILE_SYSTEM/photos/sample.png";
        byte[] fileContent = new byte[]{1, 2, 3};

        InstructorService spyService = spy(instructorService);
        doReturn(fileContent).when(spyService).readFile(filePath);

        byte[] result = spyService.readFile(filePath);

        assertNotNull(result);
        assertEquals(3, result.length);
    }

    @Test
    void testReadFile_Failure() {
        String filePath = "FILE_SYSTEM/photos/sample.png";

        InstructorService spyService = spy(instructorService);
        try {
            doThrow(new IOException("File not found")).when(spyService).readFile(filePath);

            IOException exception = assertThrows(IOException.class, () -> spyService.readFile(filePath));
            assertEquals("File not found", exception.getMessage());
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }
}
