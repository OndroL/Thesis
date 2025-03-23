package MapperTests.Sport;

import RepositoryTests.DatabaseCleaner;
import cz.inspire.exception.ApplicationException;
import cz.inspire.exception.InvalidParameterException;
import cz.inspire.exception.SystemException;
import cz.inspire.sport.dto.InstructorDto;
import cz.inspire.sport.dto.SportDto;
import cz.inspire.sport.dto.SportKategorieDto;
import cz.inspire.sport.dto.SportLocDto;
import cz.inspire.sport.entity.ActivityEntity;
import cz.inspire.sport.entity.InstructorEntity;
import cz.inspire.sport.entity.SportEntity;
import cz.inspire.sport.entity.SportInstructorEntity;
import cz.inspire.sport.entity.SportKategorieEntity;
import cz.inspire.sport.mapper.ActivityMapper;
import cz.inspire.sport.mapper.InstructorMapper;
import cz.inspire.sport.mapper.SportKategorieMapper;
import cz.inspire.sport.mapper.SportLocMapper;
import cz.inspire.sport.mapper.SportMapper;
import cz.inspire.sport.service.ActivityService;
import cz.inspire.sport.service.InstructorService;
import cz.inspire.sport.service.SportInstructorService;
import cz.inspire.sport.service.SportKategorieService;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SportMapperIT {

    @Inject
    SportMapper sportMapper;

    @Inject
    SportService sportService;

    @Inject
    ActivityService activityService;

    @Inject
    SportKategorieService sportKategorieService;

    @Inject
    InstructorService instructorService;

    @Inject
    SportKategorieMapper sportKategorieMapper;

    @Inject
    ActivityMapper activityMapper;

    @Inject
    InstructorMapper instructorMapper;

    @Inject
    SportLocMapper sportLocMapper;

    @Inject
    DatabaseCleaner databaseCleaner;
    @Inject
    SportInstructorService sportInstructorService;

    private String activityId;
    private String parentSportId; // for nadrazenySport

    @BeforeAll
    @Transactional
    public void setup() throws CreateException, FinderException, InvalidParameterException {
        // Clear tables for a clean state.
        databaseCleaner.clearTable(SportEntity.class, true);
        databaseCleaner.clearTable(SportKategorieEntity.class, true);
        databaseCleaner.clearTable(InstructorEntity.class, true);
        databaseCleaner.clearTable(ActivityEntity.class, true);

        // Create an ActivityEntity so that SportEntity's activity is not null.
        ActivityEntity actEntity = activityService.create(activityMapper.toEntity(DtoFactory.createActivityDto()));
        activityId = actEntity.getId();
        assertNotNull(activityId);

        // Create a parent SportEntity for nadrazenySport mapping.
        SportDto parentSportDto = DtoFactory.createSportDto();
        parentSportDto.setActivityId(activityId);
        SportEntity parentSport = sportService.create(sportMapper.toEntity(parentSportDto));
        parentSportId = parentSport.getId();
        assertNotNull(parentSportId);
    }

    @Test
    @Transactional
    @Order(1)
    public void testMapLocaleData() throws CreateException, InvalidParameterException, FinderException {
        // Create a SportDto with locale data.
        SportDto dto = DtoFactory.createSportDto();
        dto.setActivityId(activityId);
        Map<String, SportLocDto> localeData = new HashMap<>();
        SportLocDto locDto = new SportLocDto();
        locDto.setJazyk("cs");
        locDto.setNazev("Test Location");
        locDto.setPopis("Test Popis");
        localeData.put("cs", locDto);
        dto.setLocaleData(localeData);

        SportEntity entity = sportMapper.toEntity(dto);
        // AfterMapping, mapLocaleData should update entity.localeData.
        assertNotNull(entity.getLocaleData());
        assertEquals(1, entity.getLocaleData().size());
        SportLocDto mappedLoc = sportLocMapper.toDto(entity.getLocaleData().getFirst());
        assertEquals("cs", mappedLoc.getJazyk());
        assertEquals("Test Location", mappedLoc.getNazev());
        assertEquals("Test Popis", mappedLoc.getPopis());
    }

    @Test
    @Transactional
    @Order(2)
    public void testMapNavazujiciSport() throws CreateException, InvalidParameterException, FinderException {
        // Pre-create a SportEntity to serve as navazujiciSport.
        SportDto navSportDto = DtoFactory.createSportDto();
        navSportDto.setActivityId(activityId);
        SportEntity navSport = sportService.create(sportMapper.toEntity(navSportDto));
        String navSportId = navSport.getId();

        SportDto dto = DtoFactory.createSportDto();
        dto.setActivityId(activityId);
        dto.setNavazujiciSportId(navSportId);

        SportEntity entity = sportMapper.toEntity(dto);
        // Verify that navazujiciSport is set.
        assertNotNull(entity.getNavazujiciSport());
        assertEquals(navSportId, entity.getNavazujiciSport().getId());
    }

    @Test
    @Transactional
    @Order(3)
    public void testMapActivity() throws CreateException, InvalidParameterException, FinderException {
        SportDto dto = DtoFactory.createSportDto();
        dto.setActivityId(activityId);
        SportEntity entity = sportMapper.toEntity(dto);
        // Verify that activity is set.
        assertNotNull(entity.getActivity());
        assertEquals(activityId, entity.getActivity().getId());
    }

    @Test
    @Transactional
    @Order(4)
    public void testMapNadrazenySport() throws CreateException, InvalidParameterException, FinderException {
        SportDto dto = DtoFactory.createSportDto();
        dto.setActivityId(activityId);
        dto.setNadrazenySportId(parentSportId);
        SportEntity entity = sportMapper.toEntity(dto);
        // Verify that nadrazenySport is set.
        assertNotNull(entity.getNadrazenySport());
        assertEquals(parentSportId, entity.getNadrazenySport().getId());
        // Verify that parent's podSportyCount is incremented.
        SportEntity parent = sportService.findByPrimaryKey(parentSportId);
        assertTrue(parent.getPodSportyCount() >= 1, "Parent sport's podSportyCount should be incremented");
    }

    @Test
    @Transactional
    @Order(5)
    public void testMapSportKategorie() throws CreateException, InvalidParameterException, FinderException {
        // Pre-create a SportKategorieEntity.
        SportKategorieDto kategorieDto = DtoFactory.createSportKategorieDto();
        SportKategorieEntity kategorieEntity = sportKategorieService.create(sportKategorieMapper.toEntity(kategorieDto));
        String kategorieId = kategorieEntity.getId();
        // Set sportKategorie in SportDto.
        kategorieDto.setId(kategorieId);
        SportDto dto = DtoFactory.createSportDto();
        dto.setActivityId(activityId);
        dto.setSportKategorie(kategorieDto);

        SportEntity entity = sportMapper.toEntity(dto);
        // Verify that sportKategorie is set.
        assertNotNull(entity.getSportKategorie());
        assertEquals(kategorieId, entity.getSportKategorie().getId());
    }

    @Test
    @Transactional
    @Order(6)
    public void testMapSportInstructors_CreateBranch() throws CreateException, ApplicationException, FinderException {
        // For creation branch, dto.getId() is null.
        SportDto dto = DtoFactory.createSportDto();
        dto.setActivityId(activityId);
        // Set instructors: add a persisted instructor.
        InstructorDto instrDto = DtoFactory.createInstructorDto();
        InstructorEntity instrEntity = instructorService.create(instructorMapper.toEntity(instrDto));
        instrDto = instructorMapper.toDto(instrEntity);
        String instrId = instrEntity.getId();
        dto.getInstructors().add(instrDto);

        SportEntity entity = sportService.create(sportMapper.toEntity(dto));

        databaseCleaner.clearHibernateCacheAndRefresh(entity);

        // In creation branch, createInstructors() adds SportInstructorEntity records via cascade.
        assertNotNull(entity.getSportInstructors(), "Sport instructors collection should not be null");
        assertEquals(1, entity.getSportInstructors().size(), "Should have 1 sport instructor created");
        SportInstructorEntity si = entity.getSportInstructors().getFirst();
        assertNotNull(si.getInstructor(), "Instructor should be set in SportInstructorEntity");
        assertEquals(instrId, si.getInstructor().getId(), "Instructor id should match");
    }

    @Test
    @Transactional
    @Order(7)
    public void testMapSportInstructors_UpdateBranch() throws CreateException, ApplicationException, FinderException, SystemException {
        // For update branch, dto.getId() is not null.
        // Prepopulate a SportEntity with two SportInstructorEntity records.
        SportDto dto = DtoFactory.createSportDto();
        dto.setActivityId(activityId);
        // Create a persisted instructor.
        InstructorDto instrDto = DtoFactory.createInstructorDto();
        InstructorEntity instrEntity = instructorService.create(instructorMapper.toEntity(instrDto));
        instrDto = instructorMapper.toDto(instrEntity);
        String instrId = instrEntity.getId();
        dto.getInstructors().add(instrDto);

        SportEntity sportEntity = sportService.create(sportMapper.toEntity(dto));

        String sportId = sportEntity.getId();

        // Verify initial state: count active sport instructors.
        SportEntity beforeUpdate = sportService.findByPrimaryKey(sportId);
        long activeBefore = beforeUpdate.getSportInstructors().stream()
                .filter(si -> Boolean.FALSE.equals(si.getDeleted()))
                .count();
        assertEquals(1, activeBefore, "Expected 1 active sport instructors before update");

        // Now update: remove the old instructor and add a new one.
        InstructorDto newInstrDto = DtoFactory.createInstructorDto();
        InstructorEntity newInstrEntity = instructorService.create(instructorMapper.toEntity(newInstrDto));
        newInstrDto = instructorMapper.toDto(newInstrEntity);
        String newInstrId = newInstrEntity.getId();

        SportDto parentSport = sportMapper.toDto(sportService.findByPrimaryKey(parentSportId));

        parentSport.getInstructors().add(newInstrDto);

        sportService.update(sportMapper.toEntity(parentSport));

        SportDto updateDto = DtoFactory.createSportDto();
        updateDto.setId(sportId);
        updateDto.setActivityId(activityId);
        // Set instructors to new instructor only.
        updateDto.setInstructors(Collections.singleton(newInstrDto));

        SportEntity updatedEntity = sportMapper.toEntity(updateDto);
        sportService.update(updatedEntity);

        SportEntity afterUpdate = sportService.findByPrimaryKey(sportId);

        databaseCleaner.clearHibernateCacheAndRefresh(afterUpdate);
        afterUpdate.getSportInstructors().forEach(s-> databaseCleaner.clearHibernateCacheAndRefresh(s));
        // Verify that old associations are marked as deleted.
        long deletedCount = afterUpdate.getSportInstructors().stream()
                .filter(si -> (si.getInstructor() != null && instrId.equals(si.getInstructor().getId()) && Boolean.TRUE.equals(si.getDeleted())))
                .count();
        assertEquals(1, deletedCount, "Old sport instructors should be marked as deleted");
        // Verify that a new active record exists.
        boolean hasNew = afterUpdate.getSportInstructors().stream()
                .anyMatch(si -> si.getInstructor() != null && newInstrId.equals(si.getInstructor().getId()) && Boolean.FALSE.equals(si.getDeleted()));
        assertTrue(hasNew, "There should be an active sport instructor for the new instructor");
    }

    @Test
    @Transactional
    @Order(8)
    public void testMapSportInstructorsWithoutDuplicits() throws ApplicationException, FinderException, CreateException, SystemException {
        SportDto sportDto = DtoFactory.createSportDto();
        SportEntity sportEntity = sportService.create(sportMapper.toEntity(sportDto));
        String sportId = sportEntity.getId();

        assertNotNull(sportService.findByPrimaryKey(sportId), "SportEntity should exists in DB");
        assertEquals(1, sportInstructorService.findBySport(sportId).size(), "Only one SportInstructorEntity should exists in DB");
        assertNotNull(sportInstructorService.findBySport(sportId), "SportInstructorEntity should exists in DB");
        assertFalse(sportInstructorService.findBySport(sportId).getFirst().getDeleted(), "SportInstructorEntity shouldn't be deleted");

        System.out.println("Before of creation of Instructor Entity");

        InstructorDto instructorDto = DtoFactory.createInstructorDto();

        System.out.println("InstructorDTO");

        InstructorEntity instructor = instructorService.create(instructorMapper.toEntity(instructorDto));


        System.out.println("InstructorEntity saved in DB");


        sportDto = sportMapper.toDto(sportService.findByPrimaryKey(sportId));
        sportDto.setInstructors(Set.of(instructorMapper.toDto(instructorService.findByPrimaryKey(instructor.getId()))));

        System.out.println("Before of update of SportEntity");

        sportService.update(sportMapper.toEntity(sportDto));

        System.out.println("After of update of SportEntity");

        assertNotNull(sportService.findByPrimaryKey(sportId), "SportEntity should exists in DB");

        System.out.println(sportInstructorService.findBySport(sportId));

        assertNotNull(sportInstructorService.findBySport(sportId), "SportInstructor should exists with sport_id and not be deleted");
        assertEquals(1, sportInstructorService.findBySport(sportId).size(), "There should be only one not deleted SportInstructorEntity in DB ");
        assertNotNull(sportInstructorService.findBySport(sportId).getFirst().getInstructor(), "SportInstructorEntity should have set Instructor");


    }


}
