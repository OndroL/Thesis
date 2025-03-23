package cz.inspire.sport.mapper;

import cz.inspire.exception.ApplicationException;
import cz.inspire.sport.dto.ActivityDto;
import cz.inspire.sport.dto.InstructorDto;
import cz.inspire.sport.dto.SportDto;
import cz.inspire.sport.dto.SportInstructorDto;
import cz.inspire.sport.entity.InstructorEntity;
import cz.inspire.sport.entity.SportInstructorEntity;
import cz.inspire.sport.service.ActivityService;
import cz.inspire.sport.service.InstructorService;
import cz.inspire.sport.service.SportInstructorService;
import jakarta.ejb.FinderException;
import jakarta.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI, uses = {ActivityMapper.class, SportMapper.class})
public abstract class InstructorMapper {

    @Inject
    InstructorService instructorService;

    @Inject
    ActivityService activityService;

    @Inject
    SportInstructorService sportInstructorService;

    @Inject
    SportInstructorMapper sportInstructorMapper;

    private static final Logger logger = LogManager.getLogger(InstructorMapper.class);

    // Map DTO to Entity
    @Mapping(target = "photo", ignore = true)
    @Mapping(target = "sportInstructors", ignore = true)
    @Mapping(target = "activities", ignore = true)
    @Mapping(target = "sportSet", ignore = true)
    public abstract InstructorEntity toEntity(InstructorDto dto) throws FinderException, ApplicationException;

    @AfterMapping
    protected void mapActivities(InstructorDto dto, @MappingTarget InstructorEntity entity) {
        if (dto.getActivities() != null) {
            dto.getActivities().forEach(activityDto -> {
                try {
                    entity.getActivities().add(activityService.findByPrimaryKey(activityDto.getId()));
                } catch (FinderException e) {
                    logger.error("Unable to set activities for instructor [activityId = {}] !", activityDto.getId(), e);
                }
            });
        }
    }

    @AfterMapping
    protected void mapSportInstructor(InstructorDto dto, @MappingTarget InstructorEntity entity) throws FinderException {
        Set<String> oldSportIds = new HashSet<>();
        if (dto.getId() != null) {
            oldSportIds = instructorService.findByPrimaryKey(dto.getId())
                    .getSportInstructors().stream()
                    .filter(si -> Boolean.FALSE.equals(si.getDeleted()))
                    .map(si -> {
                        if (si.getSport() == null) {
                            logger.info("SportInstructor has no associated Sport");
                            return null;
                        }
                        String sportId = si.getSport().getId();
                        logger.info("old sportId: {}", sportId);
                        return sportId;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
        }

        //get new sports, each sport's activity has to be presented in instructor's activities
        Set<String> activityIds = dto.getActivities() == null || dto.getActivities().isEmpty()
                ? Collections.emptySet()
                : dto.getActivities().stream()
                .map(ActivityDto::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Set<String> newSportIds = new HashSet<>(dto.getSports() == null || dto.getSports().isEmpty()
                ? Collections.emptySet()
                : dto.getSports().stream()
                .filter(sportDetails -> activityIds.contains(sportDetails.getActivityId()))
                .map(SportDto::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet()));

        Set<String> deleted = new HashSet<>(oldSportIds);
        deleted.removeAll(newSportIds);
        deleted.forEach(sportIdToDelete -> {
            try {
                SportInstructorEntity sportInstructor = sportInstructorService
                        .findBySportAndInstructor(sportIdToDelete, dto.getId());
                sportInstructor.setDeleted(true);
                sportInstructorService.update(sportInstructor);
                sportInstructorService.checkSportWithoutInstructor(sportInstructor.getSport());
            } catch (Exception ex) {
                logger.error("Unable to set SportInstructorEntity " +
                        "[sportId={}, instructorId={}] as Deleted!", sportIdToDelete, dto.getId(), ex);
            }
        });

        newSportIds.removeAll(oldSportIds);
        newSportIds.forEach(sportId -> {
            try {
                SportInstructorDto sid = new SportInstructorDto();
                sid.setDeleted(false);
                sid.setSportId(sportId);
                sid.setInstructorId(dto.getId());
                sportInstructorService.create(sportInstructorMapper.toEntity(sid));
            } catch (Exception ex) {
                logger.error("Unable to create SportInstructorEntity " +
                        "[sportId= {}, instructorId= {}]!", sportId, dto.getId(), ex);
            }
        });
    }

    // Map Entity to DTO
    @Mapping(target = "photo", ignore = true)
    @Mapping(target = "sports", ignore = true)
    public abstract InstructorDto toDto(InstructorEntity entity);
}
