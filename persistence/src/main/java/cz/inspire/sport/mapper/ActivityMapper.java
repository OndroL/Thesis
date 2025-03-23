package cz.inspire.sport.mapper;

import cz.inspire.exception.SystemException;
import cz.inspire.sport.dto.ActivityDto;
import cz.inspire.sport.entity.ActivityEntity;
import cz.inspire.sport.entity.InstructorEntity;
import cz.inspire.sport.entity.SportEntity;
import cz.inspire.sport.entity.SportInstructorEntity;
import cz.inspire.sport.service.ActivityService;
import cz.inspire.sport.service.InstructorService;
import cz.inspire.sport.service.SportInstructorService;
import jakarta.ejb.CreateException;
import jakarta.ejb.FinderException;
import jakarta.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI,
        uses = {InstructorMapper.class, SportMapper.class})
public abstract class ActivityMapper {

    @Inject
    InstructorService instructorService;

    @Inject
    ActivityService activityService;

    @Inject
    SportInstructorService sportInstructorService;

    Logger logger = LogManager.getLogger(ActivityMapper.class);

    // Map DTO to Entity
    @Mapping(target = "instructors", ignore = true)
    @Mapping(target = "sports", source = "sports")
    public abstract ActivityEntity toEntity(ActivityDto dto) throws CreateException;

    @AfterMapping
    protected void mapInstructors(ActivityDto dto, @MappingTarget ActivityEntity entity) throws CreateException {
        if (dto.getInstructors() != null) {
            try {
                Set<InstructorEntity> oldInstructors = null;
                if (dto.getId() != null) { // Update branch
                    oldInstructors = new HashSet<>(activityService.findByPrimaryKey(dto.getId()).getInstructors());
                }

                dto.getInstructors().forEach(instructorDto -> {
                    try {
                        // This means that InstructorEntity needs to be in DB before mapping ActivityEntity
                        entity.getInstructors().add(instructorService.findByPrimaryKey(instructorDto.getId()));
                    } catch (FinderException e) {
                        throw new RuntimeException("Unable to find instructor with id : " + instructorDto.getId() +
                                " while mapping ActivityEntity");
                    }
                });
                if (oldInstructors != null) { // Continuation of update branch - setting old Instructors as deleted
                    oldInstructors.removeAll(entity.getInstructors());
                    updateSportInstructor(activityService.findByPrimaryKey(dto.getId()).getSports(), oldInstructors);
                }
            } catch (Exception e) {
                if (dto.getId() != null) { //Update branch
                    logger.error("Unable to set instructors", e);
                } else { // Create branch
                    throw new CreateException("Unable to create ActivityEntity, error during mapping. " + e.getMessage());
                }
            }
        }
    }

    private void updateSportInstructor(List<SportEntity> sports, Set<InstructorEntity> oldInstructors)
            throws SystemException {
        for (SportEntity sport : sports) {
            for (SportInstructorEntity sportInstructor :  sport.getSportInstructors()) {
                if (sportInstructor.getInstructor() != null
                        && oldInstructors.contains(sportInstructor.getInstructor())) {
                    sportInstructor.setDeleted(true);
                    sportInstructorService.update(sportInstructor);
                }
            }
            sportInstructorService.checkSportWithoutInstructor(sport);
        }
    }

    // Map Entity to DTO
    public abstract ActivityDto toDto(ActivityEntity entity);
}
