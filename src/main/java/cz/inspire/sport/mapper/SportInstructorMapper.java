package cz.inspire.sport.mapper;

import cz.inspire.sport.dto.SportInstructorDto;
import cz.inspire.sport.entity.SportInstructorEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;


@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI)
public interface SportInstructorMapper {

    // Map DTO to Entity
    @Mapping(target = "sport", source = "sport", ignore = true)
    @Mapping(target = "instructor", source = "instructor", ignore = true)
    SportInstructorEntity toEntity(SportInstructorDto dto);

    // Map Entity to DTO
    @Mapping(target = "sportId", source = "sport.id")
    @Mapping(target = "instructorId", source = "instructor.id")
    SportInstructorDto toDto(SportInstructorEntity entity);
}
