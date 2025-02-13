package cz.inspire.sport.mapper;

import cz.inspire.sport.dto.ArealLocDto;
import cz.inspire.sport.entity.ArealLocEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI)
public interface ArealLocMapper {

    // Map DTO to Entity
    ArealLocEntity toEntity(ArealLocDto dto);

    // Map Entity to DTO
    ArealLocDto toDto(ArealLocEntity entity);

    @Named("mapLocaleDataToList")
    default List<ArealLocEntity> mapLocaleDataToList(Map<String, ArealLocDto> locData) {
        if (locData.isEmpty()) return Collections.emptyList();
        return locData.values().stream().map(this::toEntity).toList();
    }

    @Named("mapLocaleDataToMap")
    default Map<String, ArealLocDto> mapLocaleDataToMap(List<ArealLocEntity> locData) {
        if (locData.isEmpty()) return Collections.emptyMap();
        return locData.stream()
                .map(this::toDto)
                .collect(Collectors.toMap(ArealLocDto::getJazyk, Function.identity()));
    }
}
