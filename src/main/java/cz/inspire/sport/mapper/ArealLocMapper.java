package cz.inspire.sport.mapper;

import cz.inspire.sport.dto.ArealLocDto;
import cz.inspire.sport.entity.ArealLocEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI)
public interface ArealLocMapper {

    // Map DTO to Entity
    ArealLocEntity toEntity(ArealLocDto dto);

    // Map Entity to DTO
    ArealLocDto toDto(ArealLocEntity entity);
}
