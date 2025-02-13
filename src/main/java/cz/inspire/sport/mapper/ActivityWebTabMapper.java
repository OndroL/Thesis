package cz.inspire.sport.mapper;

import cz.inspire.sport.dto.ActivityWebTabDto;
import cz.inspire.sport.entity.ActivityWebTabEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI)
public interface ActivityWebTabMapper {

    // Map DTO to Entity
    ActivityWebTabEntity toEntity(ActivityWebTabDto dto);

    // Map Entity to DTO
    ActivityWebTabDto toDto(ActivityWebTabEntity entity);
}
