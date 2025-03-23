package cz.inspire.sport.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.inspire.exception.InvalidParameterException;
import cz.inspire.sport.dto.InstructorDto;
import cz.inspire.sport.dto.SportDto;
import cz.inspire.sport.entity.InstructorEntity;
import cz.inspire.sport.entity.SportEntity;
import cz.inspire.sport.entity.SportInstructorEntity;
import cz.inspire.sport.entity.SportLocEntity;
import cz.inspire.sport.service.ActivityService;
import cz.inspire.sport.service.InstructorService;
import cz.inspire.sport.service.SportInstructorService;
import cz.inspire.sport.service.SportKategorieService;
import cz.inspire.sport.service.SportService;
import cz.inspire.sport.utils.InstructorConstants;
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
import org.mapstruct.Named;

import java.awt.*;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI,
        uses = {SportLocMapper.class, InstructorMapper.class, SportKategorieMapper.class, ActivityMapper.class})
public abstract class SportMapper {

    @Inject
    InstructorMapper instructorMapper;

    @Inject
    SportService sportService;

    @Inject
    SportKategorieService sportKategorieService;

    @Inject
    ActivityService activityService;

    @Inject
    SportInstructorService sportInstructorService;

    @Inject
    InstructorService instructorService;

    @Inject
    SportLocMapper sportLocMapper;

    private static final Logger logger = LogManager.getLogger(SportMapper.class);

    ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    // Map DTO to Entity
    @Mapping(target = "sportKategorie", ignore = true)
    @Mapping(target = "nadrazenySport", ignore = true)
    @Mapping(target = "navazujiciSport", ignore = true)
    @Mapping(target = "activity", ignore = true)
    @Mapping(target = "instructorSet", ignore = true)
    @Mapping(target = "podrazeneSporty", ignore = true)
    @Mapping(target = "sportInstructors", ignore = true)
    @Mapping(target = "objekty", ignore = true)
    @Mapping(target = "barvaPopredi", source = "barvaPopredi", qualifiedByName = "colorToJson")
    @Mapping(target = "barvaPozadi",  source = "barvaPozadi",  qualifiedByName = "colorToJson")
    @Mapping(target = "localeData", ignore = true)
    public abstract SportEntity toEntity(SportDto dto) throws CreateException, InvalidParameterException, FinderException;

    @AfterMapping
    protected void mapLocaleData(SportDto dto, @MappingTarget SportEntity entity) {
        List<SportLocEntity> newLocaleData = sportLocMapper.mapLocaleDataToList(dto.getLocaleData());
        if (entity.getLocaleData() == null) {
            entity.setLocaleData(newLocaleData);
        } else {
            entity.getLocaleData().clear();
            if (newLocaleData != null) {
                entity.getLocaleData().addAll(newLocaleData);
            }
        }
    }

    @AfterMapping
    protected void mapNavazujiciSport(SportDto dto, @MappingTarget SportEntity entity) throws CreateException, InvalidParameterException {
        try {
            if (dto.getNavazujiciSportId() != null) {
                entity.setNavazujiciSport(sportService.findByPrimaryKey(dto.getNavazujiciSportId()));
            }
        } catch (Exception e) {
            if (dto.getId() != null) { // While updating SportEntity
                throw new InvalidParameterException("Could not set navazujici sport with id = "
                        + dto.getNavazujiciSportId() + ", while updating SportEntity", e);
            } else { // While creating SportEntity
                throw new CreateException("Sport entity could not be created, while setting NavazujiciSport with id = "
                        + dto.getNavazujiciSportId() + e);
            }
        }
    }

    @AfterMapping
    protected void mapActivity(SportDto dto, @MappingTarget SportEntity entity) throws CreateException, InvalidParameterException {
        try {
            if (dto.getActivityId() != null) {
                entity.setActivity(activityService.findByPrimaryKey(dto.getActivityId()));
            }
        } catch (Exception e) {
            if (dto.getId() != null) { // While updating SportEntity
                throw new InvalidParameterException("Could not set activity with id = "
                        + dto.getActivityId() + ", while updating SportEntity", e);
            } else { // While creating SportEntity
                throw new CreateException("Sport entity could not be created, while setting Activity with id = "
                        + dto.getActivityId() + e.getMessage());
            }
        }
    }

    @AfterMapping
    protected void mapNadrazenySport(SportDto dto, @MappingTarget SportEntity entity) throws CreateException, InvalidParameterException {
        try {
            if (dto.getNadrazenySportId() != null) {
                SportEntity nadrazenySport = sportService.findByPrimaryKey(dto.getNadrazenySportId());
                entity.setNadrazenySport(nadrazenySport);
                // This update for podSportyCount is subject to change
                nadrazenySport.setPodSportyCount(nadrazenySport.getPodSportyCount() + 1);
                // As something like this would be better, but not same as in old bean
                // nadrazenySport.setPodSportyCount(nadrazenySport.getPodrazeneSporty().size() + 1);
                // but we need to check if it's behaving correctly and when Hibernate updates podrazeneSporty list
                // so nadrazenySport.getPodrazeneSporty().size() + 1 will produce correct number as podrazeneSporty is mapped by nadrazenySport
                sportService.update(nadrazenySport);
            }
        } catch (Exception e) {
            if (dto.getId() != null) { // While updating SportEntity
                throw new InvalidParameterException("Could not set parent sport with id = "
                        + dto.getNadrazenySportId() + ", while updating SportEntity", e);
            } else { // While creating SportEntity
                throw new CreateException("Sport entity could not be created, while setting parent sport with id = "
                        + dto.getNadrazenySportId() + e.getMessage());
            }
        }
    }

    @AfterMapping
    protected void mapSportKategorie(SportDto dto, @MappingTarget SportEntity entity) throws InvalidParameterException, CreateException {
        if (dto.getSportKategorie() != null) {
            try {
                entity.setSportKategorie(sportKategorieService.findByPrimaryKey(dto.getSportKategorie().getId()));
            } catch (Exception e) {
                if (dto.getId() != null) { // While updating SportEntity
                    throw new InvalidParameterException("Could not set SportKategorie with id = "
                            + dto.getSportKategorie() + ", while updating SportEntity", e);
                } else { // While creating SportEntity
                    throw new CreateException("Sport entity could not be created, while setting SportKategorie with id = "
                            + dto.getSportKategorie().getId() + e.getMessage());
                }
            }
        }
    }

    @AfterMapping
    protected void mapSportInstructors(SportDto dto, @MappingTarget SportEntity entity) throws CreateException, FinderException {
        boolean isCreate = (dto.getId() == null);

        Set<String> newInstructorIds = extractNewInstructorIds(dto);

        if (isCreate) {
            createInstructors(newInstructorIds, dto, entity, /*throwOnError=*/ true);
        }
        else {
            Set<String> oldInstructorIds = extractOldInstructorIds(entity.getId());

            Set<String> toDelete = new HashSet<>(oldInstructorIds);
            toDelete.removeAll(newInstructorIds);

            Set<String> toAdd = new HashSet<>(newInstructorIds);
            toAdd.removeAll(oldInstructorIds);

            System.out.println("Call from mapSportInstructors in else branch before deleteInstructors with SportEntity with Id -> " + entity.getId());
            System.out.println("OldInstructors to be deleted -> " + toDelete);
            deleteInstructors(toDelete, entity);

            createInstructors(toAdd, dto, entity, /*throwOnError=*/ false);
        }
    }

    /**
     * Extracts instructor IDs from the DTO, converting
     * ZADNY_INSTRUKTOR_ID -> null.
     */
    private Set<String> extractNewInstructorIds(SportDto dto) {
        if (dto.getInstructors() == null || dto.getInstructors().isEmpty()) {
            return new HashSet<>();
        }
        System.out.println("Extracting Ids for Instructors, dto.getInstructors() -> " + dto.getInstructors());
        return dto.getInstructors().stream()
                .map(instrDto -> InstructorConstants.ZADNY_INSTRUKTOR_ID.equals(instrDto.getId())
                        ? null
                        : instrDto.getId())
                .collect(Collectors.toCollection(HashSet::new));
    }

    /**
     * Extracts the existing non-deleted instructor IDs from the SportEntity.
     * If an Instructor is null, we store ZADNY_INSTRUKTOR_ID.
     */
    private Set<String> extractOldInstructorIds(String pk) throws FinderException {
        Set<String> oldIds = new HashSet<>();
        List<SportInstructorEntity> oldSportInstructorsFromDB = sportService.findByPrimaryKey(pk).getSportInstructors();
        if (oldSportInstructorsFromDB != null) {
            for (SportInstructorEntity si : oldSportInstructorsFromDB) {
                if (!Boolean.TRUE.equals(si.getDeleted())) {
                    if (si.getInstructor() == null) {
                        oldIds.add(InstructorConstants.ZADNY_INSTRUKTOR_ID);
                    } else {
                        oldIds.add(si.getInstructor().getId());
                    }
                }
            }
        }
        return oldIds;
    }

    /**
     * Creates instructor entries for the given set of instructor IDs.
     * If empty, creates exactly one default record (with no instructor).
     * If throwOnError=true, wraps failures in a CreateException.
     * Otherwise, only logs them.
     */
    private void createInstructors(Set<String> instructorIds, SportDto dto, SportEntity entity, boolean throwOnError)
            throws CreateException {

        System.out.println("Creating SportInstructors from sportMapper!");
        System.out.println("Set of instructorsIds include : " + instructorIds);
        if (instructorIds != null && instructorIds.isEmpty()) {
            System.out.println("SportMapper in Creating SportInstructors - instructorsIds was empty");
            SportInstructorEntity sid = new SportInstructorEntity();
            sid.setActivityId(dto.getActivityId());
            sid.setDeleted(false);
            sid.setSport(entity);

            // This will create SportInstructorEntity when SportEntity is persisted in DB thanks to cascade
            entity.getSportInstructors().add(sid);
        }
        else {
            System.out.println("SportMapper in Creating SportInstructors - instructorsIds wasn't empty");
            for (String instructorId : instructorIds) {
                System.out.println("SportMapper in Creating SportInstructors - creating SportInstructor with instructorId -> " + instructorId);
                SportInstructorEntity sid = new SportInstructorEntity();
                sid.setActivityId(dto.getActivityId());
                sid.setDeleted(false);
                sid.setSport(entity);

                if (instructorId != null) {
                    try {
                        sid.setInstructor(instructorService.findByPrimaryKey(instructorId));

                    } catch (Exception e) {
                        handleCreateException("Failed to find Instructor for ID=" + instructorId, e, throwOnError);
                        // If we do not throw, skip creation and continue
                        continue;
                    }
                }
                // This will create SportInstructorEntity when SportEntity is persisted in DB thanks to cascade
                entity.getSportInstructors().add(sid);
            }
        }
    }

    /**
     * Marks any instructor from 'instructorIds' as deleted (only logs exceptions).
     */
    private void deleteInstructors(Set<String> instructorIds, SportEntity entity) {
        for (String instructorId : instructorIds) {
            try {
                SportInstructorEntity sportInstructor;
                if (instructorId == null || InstructorConstants.ZADNY_INSTRUKTOR_ID.equals(instructorId)) {
                    sportInstructor = sportInstructorService.findBySportWithoutInstructor(entity.getId());
                } else {
                    sportInstructor = sportInstructorService.findBySportAndInstructor(entity.getId(), instructorId);
                }
                System.out.println("Setting SportInstructor as deleted -> " + sportInstructor.getId());
                sportInstructor.setDeleted(true);
                sportInstructorService.update(sportInstructor);
            } catch (Exception ex) {
                logger.error("Failed to mark SportInstructor as deleted! ID=" + instructorId, ex);
            }
        }
    }

    /**
     * If throwOnError=true, wrap and throw the exception as CreateException.
     * Otherwise, just log it.
     */
    private void handleCreateException(String message, Exception e, boolean throwOnError) throws CreateException {
        if (throwOnError) {
            throw new CreateException(message + " " + e.getMessage());
        } else {
            logger.error(message, e);
        }
    }



    // Map Entity to DTO
    @Mapping(target = "sportKategorie", source = "sportKategorie")
    @Mapping(target = "nadrazenySportId", source = "nadrazenySport.id")
    @Mapping(target = "navazujiciSportId", source = "navazujiciSport.id")
    @Mapping(target = "activityId", source = "activity.id")
    @Mapping(target = "barvaPopredi", source = "barvaPopredi", qualifiedByName = "jsonToColor")
    @Mapping(target = "barvaPozadi",  source = "barvaPozadi",  qualifiedByName = "jsonToColor")
    @Mapping(target = "instructors", ignore = true)
    @Mapping(target = "localeData", source = "localeData", qualifiedByName = "mapLocaleDataToMap")
    public abstract SportDto toDto(SportEntity entity);

    @AfterMapping
    protected void mapInstructors(@MappingTarget SportDto dto, SportEntity entity) {
        List<SportInstructorEntity> sportInstructors = entity.getSportInstructors();
        Set<InstructorDto> instructors = sportInstructors.stream()
                .filter(sportInstructor -> !Boolean.TRUE.equals(sportInstructor.getDeleted()))
                .map(sportInstructor -> {
                    InstructorEntity instructor = sportInstructor.getInstructor();
                    if (instructor == null) {
                        InstructorDto noneInstructor = new InstructorDto();
                        noneInstructor.setId(InstructorConstants.ZADNY_INSTRUKTOR_ID);
                        return noneInstructor;
                    } else {
                        return instructorMapper.toDto(instructor);
                    }
                })
                .collect(Collectors.toSet());
        dto.setInstructors(instructors);
    }

    @Named("jsonToColor")
    Color jsonToColor(String json) {
        if (json == null || json.trim().isEmpty()) {
            return null;
        }
        try {
            JsonNode root = OBJECT_MAPPER.readTree(json);
            int r = root.path("red").asInt(0);
            int g = root.path("green").asInt(0);
            int b = root.path("blue").asInt(0);
            int a = root.path("alpha").asInt(255);
            return new Color(r, g, b, a);
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid JSON for color: " + json, e);
        }
    }
    @Named("colorToJson")
    String colorToJson(Color color) {
        if (color == null) {
            return null;
        }
        JsonNode node = OBJECT_MAPPER.createObjectNode()
                .put("red", color.getRed())
                .put("green", color.getGreen())
                .put("blue", color.getBlue())
                .put("alpha", color.getAlpha());
        return node.toString();
    }
}
