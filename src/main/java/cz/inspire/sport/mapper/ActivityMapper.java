package cz.inspire.sport.mapper;

import cz.inspire.sport.dto.ActivityDto;
import cz.inspire.sport.entity.ActivityEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI)
public interface ActivityMapper {

    // Map DTO to Entity
    @Mapping(target = "instructors", source = "instructors", ignore = true)
    @Mapping(target = "sports", source = "sports", ignore = true)
    ActivityEntity toEntity(ActivityDto dto);

    // Map Entity to DTO
    ActivityDto toDto(ActivityEntity entity);
}
