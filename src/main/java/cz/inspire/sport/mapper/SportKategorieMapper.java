package cz.inspire.sport.mapper;

import cz.inspire.sport.dto.SportKategorieDto;
import cz.inspire.sport.dto.SportKategorieLocDto;
import cz.inspire.sport.entity.SportKategorieEntity;
import cz.inspire.sport.entity.SportKategorieLocEntity;
import org.mapstruct.*;

import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI, uses = {SportKategorieLocMapper.class})
public interface SportKategorieMapper {

    // Map DTO to Entity
    @Mapping(target = "nadrazenaKategorie", ignore = true)
    @Mapping(target = "cinnosti", ignore = true)
    @Mapping(target = "podrazeneKategorie", ignore = true)
    SportKategorieEntity toEntity(SportKategorieDto dto);

    // Map Entity to DTO
    @Mapping(target = "nadrazenaKategorieId", source = "nadrazenaKategorie.id")
    SportKategorieDto toDto(SportKategorieEntity entity);
}
