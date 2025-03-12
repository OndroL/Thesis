package MapperTests.Sport;

import RepositoryTests.DatabaseCleaner;
import cz.inspire.exception.SystemException;
import cz.inspire.sport.dto.ActivityDto;
import cz.inspire.sport.dto.InstructorDto;
import cz.inspire.sport.entity.ActivityEntity;
import cz.inspire.sport.entity.InstructorEntity;
import cz.inspire.sport.mapper.ActivityMapper;
import cz.inspire.sport.mapper.InstructorMapper;
import cz.inspire.sport.service.ActivityService;
import cz.inspire.sport.service.InstructorService;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ActivityMapperIT {

    @Inject
    ActivityMapper activityMapper;

    @Inject
    InstructorMapper instructorMapper;

    @Inject
    ActivityService activityService;

    @Inject
    InstructorService instructorService;

    @Inject
    DatabaseCleaner databaseCleaner;

    private String instructorId1;
    private String instructorId2;
    private String activityId1;
    private String activityId2;

    @BeforeAll
    @Transactional
    public void setup() throws CreateException, FinderException {
        databaseCleaner.clearTable(ActivityEntity.class, true);
        databaseCleaner.clearTable(InstructorEntity.class, true);

        // 1) Create first Instructor via DtoFactory
        InstructorDto instrDto1 = DtoFactory.createInstructorDto();
        // No ID is set by DtoFactory
        InstructorEntity instrEntity1 = instructorMapper.toEntity(instrDto1);
        instructorService.create(instrEntity1);
        instructorId1 = instrEntity1.getId();

        // 2) Create second Instructor
        InstructorDto instrDto2 = DtoFactory.createInstructorDto();
        InstructorEntity instrEntity2 = instructorMapper.toEntity(instrDto2);
        instructorService.create(instrEntity2);
        instructorId2 = instrEntity2.getId();
    }


    @Order(1)
    @Test
    @Transactional
    public void testDefaultMappingFields() throws CreateException, FinderException {
        ActivityDto dto = DtoFactory.createActivityDto(); // no instructors
        dto.setName("Default Mapped Activity");
        dto.setDescription("Test Description");
        dto.setIndex(42);
        dto.setIconId("icon-test-123");

        ActivityEntity entity = activityMapper.toEntity(dto);
        activityService.create(entity);
        activityId1 = entity.getId();

        assertNotNull(activityId1);
        assertEquals("Default Mapped Activity", entity.getName());
        assertEquals("Test Description", entity.getDescription());
        assertEquals(42, entity.getIndex());
        assertEquals("icon-test-123", entity.getIconId());

        // reload from DB and verify
        ActivityEntity fromDb = activityService.findByPrimaryKey(activityId1);
        assertNotNull(fromDb);
        assertEquals("Default Mapped Activity", fromDb.getName());
        assertEquals("Test Description", fromDb.getDescription());
        assertEquals(42, fromDb.getIndex());
        assertEquals("icon-test-123", fromDb.getIconId());
    }

    @Order(2)
    @Test
    @Transactional
    public void testRetrieveDefaultMappingFields() throws FinderException {
        ActivityEntity entity = activityService.findByPrimaryKey(activityId1);
        assertNotNull(entity);

        ActivityDto mappedDto = activityMapper.toDto(entity);
        assertEquals(activityId1, mappedDto.getId());
        assertEquals("Default Mapped Activity", mappedDto.getName());
        assertEquals("Test Description", mappedDto.getDescription());
        assertEquals(42, mappedDto.getIndex());
        assertEquals("icon-test-123", mappedDto.getIconId());
    }


    @Order(3)
    @Test
    @Transactional
    public void testCreateActivityWithInstructor() throws CreateException, FinderException {
        // Create an ActivityDto referencing the first instructor
        ActivityDto activityDto = DtoFactory.createActivityDto();
        activityDto.setInstructors(List.of(instructorMapper.toDto(instructorService.findByPrimaryKey(instructorId1))));

        ActivityEntity entity = activityMapper.toEntity(activityDto);
        activityService.create(entity);
        activityId2 = entity.getId();

        assertNotNull(activityId2);

        // Verify the DB record
        ActivityEntity fromDb = activityService.findByPrimaryKey(activityId2);
        assertNotNull(fromDb);
        // Should have exactly one instructor: instructorId1
        assertEquals(1, fromDb.getInstructors().size());
        assertTrue(
            fromDb.getInstructors().stream().anyMatch(i -> instructorId1.equals(i.getId())),
            "Activity should have the first instructor"
        );
    }

    @Order(4)
    @Test
    @Transactional
    public void testUpdateActivityReplaceInstructor() throws CreateException, FinderException, SystemException {
        // Retrieve existing activity from DB (has instructorId1)
        ActivityEntity existing = activityService.findByPrimaryKey(activityId2);
        assertNotNull(existing);

        // Build an ActivityDto referencing that same activity, but only instructorId2
        ActivityDto activityDto = new ActivityDto();
        activityDto.setId(activityId2);
        activityDto.setName("Updated Activity");
        activityDto.setInstructors(List.of(instructorMapper.toDto(instructorService.findByPrimaryKey(instructorId2))));

        // toEntity calls our update logic
        ActivityEntity updatedEntity = activityMapper.toEntity(activityDto);
        activityService.update(updatedEntity);

        // Verify from DB
        ActivityEntity fromDb = activityService.findByPrimaryKey(activityId2);
        assertNotNull(fromDb);
        assertEquals("Updated Activity", fromDb.getName());
        assertTrue(
            fromDb.getInstructors().stream().anyMatch(i -> instructorId2.equals(i.getId())),
            "Activity should now have instructor #2"
        );
    }
}
