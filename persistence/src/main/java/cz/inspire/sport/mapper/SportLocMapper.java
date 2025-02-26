package cz.inspire.sport.mapper;

import cz.inspire.sport.dto.SportLocDto;
import cz.inspire.sport.entity.SportLocEntity;
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
public interface SportLocMapper {

    // Map DTO to Entity
    SportLocEntity toEntity(SportLocDto dto);

    // Map Entity to DTO
    SportLocDto toDto(SportLocEntity entity);

    @Named("mapLocaleDataToList")
    default List<SportLocEntity> mapLocaleDataToList(Map<String, SportLocDto> locData) {
        if (locData == null || locData.isEmpty()) return Collections.emptyList();
        return new ArrayList<>(locData.values().stream().map(this::toEntity).toList());
    }

    @Named("mapLocaleDataToMap")
    default Map<String, SportLocDto> mapLocaleDataToMap(List<SportLocEntity> locData) {
        if (locData == null || locData.isEmpty()) return Collections.emptyMap();
        return locData.stream()
                .map(this::toDto)
                .collect(Collectors.toMap(SportLocDto::getJazyk, Function.identity()));
    }
}
