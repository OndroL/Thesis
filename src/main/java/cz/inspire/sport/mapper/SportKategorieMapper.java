package cz.inspire.sport.mapper;

import cz.inspire.sport.dto.SportKategorieDto;
import cz.inspire.sport.entity.SportKategorieEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mapping;


@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI, uses = {SportKategorieLocMapper.class})
public interface SportKategorieMapper {

    // Map DTO to Entity
    @Mapping(target = "nadrazenaKategorie", ignore = true)
    @Mapping(target = "cinnosti", ignore = true)
    @Mapping(target = "podrazeneKategorie", ignore = true)
    @Mapping(target = "localeData", source = "localeData", qualifiedByName = "mapLocaleDataToList")
    SportKategorieEntity toEntity(SportKategorieDto dto);

    // Map Entity to DTO
    @Mapping(target = "nadrazenaKategorieId", source = "nadrazenaKategorie.id")
    @Mapping(target = "localeData", source = "localeData", qualifiedByName = "mapLocaleDataToMap")
    SportKategorieDto toDto(SportKategorieEntity entity);
}
