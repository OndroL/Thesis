package RepositoryTests.Sport;

import RepositoryTests.DatabaseCleaner;
import cz.inspire.sport.entity.ActivityEntity;
import cz.inspire.sport.entity.InstructorEntity;
import cz.inspire.sport.repository.InstructorRepository;
import cz.inspire.sport.service.InstructorService;
import cz.inspire.utils.FileAttributes;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class InstructorPhotoIT {

    @Inject
    InstructorRepository instructorRepository;

    @Inject
    InstructorService instructorService;

    @Inject
    DatabaseCleaner databaseCleaner;

    private static final String DEFAULT_PHOTO_PATH = "FILE_SYSTEM/default-avatar.png";

    private byte[] defaultPhoto;

    @BeforeAll
    @ActivateRequestContext
    public void setUp() throws IOException {
        defaultPhoto = Files.readAllBytes(Paths.get(DEFAULT_PHOTO_PATH));
        databaseCleaner.clearTable(InstructorEntity.class, true);
        databaseCleaner.clearTable(ActivityEntity.class, true);
    }

    @Order(1)
    @Test
    public void testSaveInstructorPhoto() throws IOException {
        FileAttributes savedFile = instructorService.savePhoto(defaultPhoto, "John", "Doe");
        assertNotNull(savedFile, "Saved FileAttributes should not be null");

        InstructorEntity instructor = new InstructorEntity(
                "INS-100", "John", "Doe", 1, "john@example.com", "+1", "123456789",
                "john.internal@example.com", "+1", "987654321", "Trainer", "blue",
                savedFile, false, "calendar100", true, 15, new HashSet<>(), new HashSet<>(), null
        );

        instructorRepository.save(instructor);

        Optional<InstructorEntity> retrieved = instructorRepository.findById("INS-100");

        assertTrue(retrieved.isPresent(), "Instructor should be present in repository.");
        assertNotNull(retrieved.get().getPhoto(), "Photo should not be null in the retrieved entity.");
        assertEquals(savedFile.getFilePath(), retrieved.get().getPhoto().getFilePath(), "Photo path should match.");
    }

    @Order(2)
    @Test
    public void testRetrieveInstructorPhoto() throws IOException {
        Optional<InstructorEntity> retrieved = instructorRepository.findById("INS-100");

        assertTrue(retrieved.isPresent(), "Instructor should be present.");
        assertNotNull(retrieved.get().getPhoto(), "Instructor should have a photo.");

        byte[] retrievedPhoto = instructorService.readFile(retrieved.get().getPhoto().getFilePath());

        assertNotNull(retrievedPhoto, "Retrieved photo data should not be null.");
        assertArrayEquals(defaultPhoto, retrievedPhoto, "Retrieved photo data should match the original file.");
    }
}
