package MapperTests.Sport;

import RepositoryTests.DatabaseCleaner;
import cz.inspire.exception.ApplicationException;
import cz.inspire.exception.SystemException;
import cz.inspire.sport.dto.ActivityDto;
import cz.inspire.sport.dto.InstructorDto;
import cz.inspire.sport.dto.SportDto;
import cz.inspire.sport.entity.ActivityEntity;
import cz.inspire.sport.entity.InstructorEntity;
import cz.inspire.sport.entity.SportEntity;
import cz.inspire.sport.mapper.ActivityMapper;
import cz.inspire.sport.mapper.InstructorMapper;
import cz.inspire.sport.mapper.SportInstructorMapper;
import cz.inspire.sport.mapper.SportMapper;
import cz.inspire.sport.service.ActivityService;
import cz.inspire.sport.service.InstructorService;
import cz.inspire.sport.service.SportInstructorService;
import cz.inspire.sport.service.SportService;
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

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class InstructorMapperIT {

    @Inject
    ActivityMapper activityMapper;

    @Inject
    InstructorMapper instructorMapper;

    @Inject
    SportInstructorMapper sportInstructorMapper;

    @Inject
    ActivityService activityService;

    @Inject
    InstructorService instructorService;

    @Inject
    SportInstructorService sportInstructorService;

    @Inject
    SportService sportService;

    @Inject
    DatabaseCleaner databaseCleaner;

    @Inject
    SportMapper sportMapper;

    private String instructorId1;
    private String instructorId2;
    private String activityId;

    private String s1;
    private String s2;
    private String s3;

    @BeforeAll
    @Transactional
    public void setup() throws CreateException, FinderException, ApplicationException {
        databaseCleaner.clearTable(ActivityEntity.class, true);
        databaseCleaner.clearTable(InstructorEntity.class, true);

        // Create first Instructor
        InstructorDto instrDto1 = DtoFactory.createInstructorDto();
        InstructorEntity instrEntity1 = instructorMapper.toEntity(instrDto1);
        instructorService.create(instrEntity1);
        instructorId1 = instrEntity1.getId();

        // Create second Instructor
        InstructorDto instrDto2 = DtoFactory.createInstructorDto();
        InstructorEntity instrEntity2 = instructorMapper.toEntity(instrDto2);
        instructorService.create(instrEntity2);
        instructorId2 = instrEntity2.getId();
    }

    @Order(1)
    @Test
    @Transactional
    public void testCreateActivityWithInstructor() throws CreateException, FinderException {
        ActivityDto activityDto = DtoFactory.createActivityDto();
        // Set instructors to include only the first instructor
        InstructorDto mappedInstrDto1 = instructorMapper.toDto(instructorService.findByPrimaryKey(instructorId1));
        activityDto.setInstructors(List.of(mappedInstrDto1));

        ActivityEntity entity = activityMapper.toEntity(activityDto);
        activityService.create(entity);
        activityId = entity.getId();

        assertNotNull(activityId);
        ActivityEntity fromDb = activityService.findByPrimaryKey(activityId);
        assertNotNull(fromDb);
        assertEquals(1, fromDb.getInstructors().size());
        assertTrue(fromDb.getInstructors().stream()
                .anyMatch(i -> instructorId1.equals(i.getId())));
    }

    @Order(2)
    @Test
    @Transactional
    public void testUpdateActivityReplaceInstructor() throws CreateException, FinderException, SystemException {
        ActivityEntity existing = activityService.findByPrimaryKey(activityId);
        assertNotNull(existing);

        ActivityDto activityDto = new ActivityDto();
        activityDto.setId(activityId);
        activityDto.setName("Updated Activity");
        // Replace instructors: set instructors to include only the second instructor
        InstructorDto mappedInstrDto2 = instructorMapper.toDto(instructorService.findByPrimaryKey(instructorId2));
        activityDto.setInstructors(List.of(mappedInstrDto2));

        ActivityEntity updatedEntity = activityMapper.toEntity(activityDto);
        activityService.update(updatedEntity);

        ActivityEntity fromDb = activityService.findByPrimaryKey(activityId);
        assertNotNull(fromDb);
        assertEquals("Updated Activity", fromDb.getName());
        assertTrue(fromDb.getInstructors().stream()
                .anyMatch(i -> instructorId2.equals(i.getId())));
    }

    @Order(3)
    @Test
    @Transactional
    public void testMapSportInstructor_CreateBranch() throws FinderException, ApplicationException {
        // Create a new InstructorDto without an ID to simulate creation branch.
        InstructorDto createDto = DtoFactory.createInstructorDto();
        // Set activities and sports; these should not trigger update branch
        ActivityDto actDto = DtoFactory.createActivityDto();
        actDto.setId("A_NEW"); // dummy id
        createDto.setActivities(Collections.singleton(actDto));

        SportDto sportDto = DtoFactory.createSportDto();
        sportDto.setId("S_NEW"); // dummy id, not persisted
        sportDto.setActivityId("A_NEW");
        createDto.setSports(Collections.singleton(sportDto));

        // Map to entity; since createDto.getId() is null, update branch in mapSportInstructor is not triggered.
        InstructorEntity entity = instructorMapper.toEntity(createDto);
        // In create branch, sportInstructors should remain empty.
        assertTrue(entity.getSportInstructors() == null || entity.getSportInstructors().isEmpty(),
                "No sport instructors should be mapped in creation branch");
    }

    @Order(4)
    @Test
    @Transactional
    public void testMapSportInstructor_Update() throws CreateException, FinderException, SystemException, ApplicationException {
        // Prepopulate instructorId1 with two sport instructors.
        // First, create a valid ActivityEntity so that SportEntity has a non-null Activity.
        ActivityDto actDtoForSports = DtoFactory.createActivityDto();
        ActivityEntity actEntity = activityService.create(activityMapper.toEntity(actDtoForSports));
        String activityForSportsId = actEntity.getId();

        // Create two SportEntities for S1 and S2 with the persisted Activity.
        SportDto sportDto1 = DtoFactory.createSportDto();
        sportDto1.setActivityId(activityForSportsId);
        sportDto1.getInstructors().add(instructorMapper.toDto(instructorService.findByPrimaryKey(instructorId1)));
        SportEntity sportEntity1 = sportService.create(sportMapper.toEntity(sportDto1));
        s1 = sportEntity1.getId();

        SportDto sportDto2 = DtoFactory.createSportDto();
        sportDto2.setActivityId(activityForSportsId);
        sportDto2.getInstructors().add(instructorMapper.toDto(instructorService.findByPrimaryKey(instructorId1)));
        SportEntity sportEntity2 = sportService.create(sportMapper.toEntity(sportDto2));
        s2 = sportEntity2.getId();

        // Verify initial state: instructorId1 should have at least 2 active sport instructors.
        InstructorEntity beforeUpdate = instructorService.findByPrimaryKey(instructorId1);
        long activeBefore = beforeUpdate.getSportInstructors().stream()
                .filter(si -> !si.getDeleted())
                .count();
        assertTrue(activeBefore >= 2, "Expected at least 2 active sport instructors before update");

        // Now prepare an update DTO for instructorId1: remove S1 and S2 and add new sport S3.
        SportDto newSportDto = DtoFactory.createSportDto();
        newSportDto.setActivityId(activityForSportsId);
        newSportDto.getInstructors().add(instructorMapper.toDto(instructorService.findByPrimaryKey(instructorId1)));
        SportEntity sportEntity3 = sportService.create(sportMapper.toEntity(newSportDto));
        s3 = sportEntity3.getId();

        InstructorDto updateDto = DtoFactory.createInstructorDto();
        updateDto.setId(instructorId1);
        updateDto.setFirstName("UpdatedFirst");
        // Set activities: new Activity with id equal to activityForSportsId.
        ActivityDto updActDto = DtoFactory.createActivityDto();
        updActDto.setId(activityForSportsId);
        updateDto.setActivities(Collections.singleton(updActDto));
        // Set sports: new sport S3 only.
        updateDto.setSports(Collections.singleton(newSportDto));

        // Invoke mapping update branch.
        InstructorEntity updatedEntity = instructorMapper.toEntity(updateDto);
        instructorService.update(updatedEntity);

        // SportInstructorEntities for S1 and S2 should be marked as deleted.
        assertTrue(sportService.findByPrimaryKey(s1).getSportInstructors().getFirst().getDeleted().equals(true) ||
                sportService.findByPrimaryKey(s1).getSportInstructors().getLast().getDeleted().equals(true), "Sport instructors for S1 should be marked as deleted");
        assertTrue(sportService.findByPrimaryKey(s2).getSportInstructors().getFirst().getDeleted().equals(true) ||
                sportService.findByPrimaryKey(s2).getSportInstructors().getLast().getDeleted().equals(true), "Sport instructors for S2 should be marked as deleted");
    }

    @Order(5)
    @Test
    @Transactional
    public void testMapSportInstructor_Update_finalsChecks() throws FinderException {
        // SportInstructorEntities for S1 and S2 should be marked as deleted.
        assertEquals(2, sportService.findByPrimaryKey(s1).getSportInstructors().size(), "Sport should have two links to SportInstructors");
        assertEquals(2, sportService.findByPrimaryKey(s2).getSportInstructors().size(), "Sport should have two links to SportInstructors");

        InstructorEntity afterUpdate = instructorService.findByPrimaryKey(instructorId1);
        boolean hasS3 = afterUpdate.getSportInstructors().stream()
                .anyMatch(si -> si.getSport().getId().equals(s3) && !si.getDeleted());
        assertTrue(hasS3, "There should be an active sport instructor for sport S3");
    }
}
