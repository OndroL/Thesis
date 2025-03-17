package MapperTests.Sport;

import RepositoryTests.DatabaseCleaner;
import cz.inspire.exception.ApplicationException;
import cz.inspire.exception.SystemException;
import cz.inspire.sport.dto.SportInstructorDto;
import cz.inspire.sport.entity.ActivityEntity;
import cz.inspire.sport.entity.InstructorEntity;
import cz.inspire.sport.entity.SportEntity;
import cz.inspire.sport.entity.SportInstructorEntity;
import cz.inspire.sport.mapper.ActivityMapper;
import cz.inspire.sport.mapper.InstructorMapper;
import cz.inspire.sport.mapper.SportInstructorMapper;
import cz.inspire.sport.mapper.SportMapper;
import cz.inspire.sport.service.ActivityService;
import cz.inspire.sport.service.InstructorService;
import cz.inspire.sport.service.SportService;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ejb.CreateException;
import jakarta.ejb.FinderException;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SportInstructorMapperIT {

    @Inject
    SportInstructorMapper sportInstructorMapper;

    @Inject
    SportService sportService;

    @Inject
    InstructorService instructorService;

    @Inject
    ActivityService activityService;

    @Inject
    ActivityMapper activityMapper;

    @Inject
    SportMapper sportMapper;

    @Inject
    InstructorMapper instructorMapper;

    @Inject
    DatabaseCleaner databaseCleaner;

    private String sportId;
    private String instructorId;
    private String activityId;

    @Transactional
    @Test
    public void testSetup() throws CreateException, FinderException, ApplicationException, SystemException {
        // Clean tables.
        databaseCleaner.clearTable(ActivityEntity.class, true);
        databaseCleaner.clearTable(SportEntity.class, true);
        databaseCleaner.clearTable(InstructorEntity.class, true);

        // Create and persist an ActivityEntity so that any SportEntity will have a non-null activity.
        ActivityEntity actEntity = activityService.create(activityMapper.toEntity(DtoFactory.createActivityDto()));
        activityId = actEntity.getId();
        assertNotNull(activityId);

        // Create a SportEntity that uses the persisted Activity.
        SportEntity sportEntity = sportService.create(sportMapper.toEntity(DtoFactory.createSportDto()));
        // For testing, set the activity explicitly (if not already set in mapping)
        sportEntity.setActivity(actEntity);
        sportService.update(sportEntity);
        sportId = sportEntity.getId();
        assertNotNull(sportId);

        // Create an InstructorEntity.
        InstructorEntity instrEntity = instructorService.create(instructorMapper.toEntity(DtoFactory.createInstructorDto()));
        instructorId = instrEntity.getId();
        assertNotNull(instructorId);
    }

    @Transactional
    @Test
    public void testToEntityMapping() throws FinderException, ApplicationException {
        // Create a SportInstructorDto with valid sportId and instructorId.
        SportInstructorDto dto = new SportInstructorDto();
        dto.setSportId(sportId);
        dto.setInstructorId(instructorId);
        dto.setDeleted(false);

        SportInstructorEntity entity = sportInstructorMapper.toEntity(dto);
        assertNotNull(entity, "Mapped entity should not be null");

        // Check that the Sport reference is set (via the @AfterMapping logic).
        assertNotNull(entity.getSport(), "Sport should be set by the mapper");
        // Check that the ActivityId is set from the Sport's Activity.
        assertNotNull(entity.getSport().getActivity(), "Sport's Activity should not be null");
        assertEquals(entity.getSport().getActivity().getId(), entity.getActivityId(), "ActivityId should be taken from Sport's activity");
        // Check that the Instructor reference is set.
        assertNotNull(entity.getInstructor(), "Instructor should be set by the mapper");
        assertEquals(instructorId, entity.getInstructor().getId(), "Instructor id should match");
    }

    @Transactional
    @Test
    public void testToDtoMapping() throws FinderException, ApplicationException {
        // Create a SportInstructorDto, map to entity, then back to DTO.
        SportInstructorDto originalDto = new SportInstructorDto();
        originalDto.setSportId(sportId);
        originalDto.setInstructorId(instructorId);
        originalDto.setDeleted(false);

        SportInstructorEntity entity = sportInstructorMapper.toEntity(originalDto);
        SportInstructorDto mappedDto = sportInstructorMapper.toDto(entity);

        assertNotNull(mappedDto, "Mapped DTO should not be null");
        assertEquals(sportId, mappedDto.getSportId(), "SportId should match");
        assertEquals(instructorId, mappedDto.getInstructorId(), "InstructorId should match");
    }
}
