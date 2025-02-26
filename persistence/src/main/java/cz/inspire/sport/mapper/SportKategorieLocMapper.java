package cz.inspire.sport.mapper;

import cz.inspire.sport.dto.SportKategorieLocDto;
import cz.inspire.sport.entity.SportKategorieLocEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI)
public interface SportKategorieLocMapper {

    // Map DTO to Entity
    SportKategorieLocEntity toEntity(SportKategorieLocDto dto);

    // Map Entity to DTO
    SportKategorieLocDto toDto(SportKategorieLocEntity entity);

    @Named("mapLocaleDataToList")
    default List<SportKategorieLocEntity> mapLocaleDataToList(Map<String, SportKategorieLocDto> locData) {
        if (locData.isEmpty()) return Collections.emptyList();
        return new ArrayList<>(locData.values().stream().map(this::toEntity).toList());
    }

    @Named("mapLocaleDataToMap")
    default Map<String, SportKategorieLocDto> mapLocaleDataToMap(List<SportKategorieLocEntity> locData) {
        if (locData.isEmpty()) return Collections.emptyMap();
        return locData.stream()
                .map(this::toDto)
                .collect(Collectors.toMap(SportKategorieLocDto::getJazyk, Function.identity()));
    }
}
