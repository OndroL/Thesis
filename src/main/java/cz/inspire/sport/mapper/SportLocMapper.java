package cz.inspire.sport.mapper;

import cz.inspire.sport.dto.SportLocDto;
import cz.inspire.sport.entity.SportLocEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI)
public interface SportLocMapper {

    // Map DTO to Entity
    SportLocEntity toEntity(SportLocDto dto);

    // Map Entity to DTO
    SportLocDto toDto(SportLocEntity entity);
}
