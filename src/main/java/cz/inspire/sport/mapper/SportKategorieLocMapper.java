package cz.inspire.sport.mapper;

import cz.inspire.sport.dto.SportKategorieLocDto;
import cz.inspire.sport.entity.SportKategorieLocEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI)
public interface SportKategorieLocMapper {

    // Map DTO to Entity
    SportKategorieLocEntity toEntity(SportKategorieLocDto dto);

    // Map Entity to DTO
    SportKategorieLocDto toDto(SportKategorieLocEntity entity);
}
