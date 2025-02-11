package cz.inspire.sport.mapper;

import cz.inspire.sport.dto.InstructorDto;
import cz.inspire.sport.entity.InstructorEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI, uses = {ActivityMapper.class, SportMapper.class})
public interface InstructorMapper {

    // Map DTO to Entity
    @Mapping(target = "photo", ignore = true)
    @Mapping(target = "sportInstructors", ignore = true)
    @Mapping(target = "activities", ignore = true)
    @Mapping(target = "sportSet", ignore = true)
    InstructorEntity toEntity(InstructorDto dto);

    // Map Entity to DTO
    @Mapping(target = "photo", ignore = true)
    @Mapping(target = "sports", ignore = true)
    InstructorDto toDto(InstructorEntity entity);
}
