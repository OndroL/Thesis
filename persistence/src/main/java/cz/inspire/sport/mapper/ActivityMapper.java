package cz.inspire.sport.mapper;

import cz.inspire.sport.dto.ActivityDto;
import cz.inspire.sport.entity.ActivityEntity;
import cz.inspire.sport.service.InstructorService;
import jakarta.ejb.FinderException;
import jakarta.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI,
        uses = {InstructorMapper.class, SportMapper.class})
public abstract class ActivityMapper {

    @Inject
    InstructorService instructorService;

    Logger logger = LogManager.getLogger(ActivityMapper.class);

    // Map DTO to Entity
    @Mapping(target = "instructors", ignore = true)
    @Mapping(target = "sports", source = "sports")
    public abstract ActivityEntity toEntity(ActivityDto dto);

    @AfterMapping
    protected void setInstructors(ActivityDto dto, @MappingTarget ActivityEntity entity) {
        if (dto.getInstructors() != null) {
            try {
                dto.getInstructors().forEach(instructorDto -> {
                    try {
                        entity.getInstructors().add(instructorService.findByPrimaryKey(instructorDto.getId()));
                    } catch (FinderException e) {
                        throw new RuntimeException("Unable to find instructor with id : " + instructorDto.getId() +
                                " while mapping ActivityEntity");
                    }
                });
            } catch (Exception e) {
                logger.error("Unable to set instructors", e);
            }
        }
    }

    // Map Entity to DTO
    public abstract ActivityDto toDto(ActivityEntity entity);
}
