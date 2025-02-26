package cz.inspire.sport.mapper;

import cz.inspire.sport.dto.ObjektLocDto;
import cz.inspire.sport.entity.ObjektLocEntity;
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
public interface ObjektLocMapper {

    // Map DTO to Entity
    ObjektLocEntity toEntity(ObjektLocDto dto);

    // Map Entity to DTO
    ObjektLocDto toDto(ObjektLocEntity entity);

    @Named("mapLocaleDataToList")
    default List<ObjektLocEntity> mapLocaleDataToList(Map<String, ObjektLocDto> locData) {
        if (locData == null || locData.isEmpty()) return Collections.emptyList();
        return new ArrayList<>(locData.values().stream().map(this::toEntity).toList());
    }

    @Named("mapLocaleDataToMap")
    default Map<String, ObjektLocDto> mapLocaleDataToMap(List<ObjektLocEntity> locData) {
        if (locData == null || locData.isEmpty()) return Collections.emptyMap();
        return locData.stream()
                .map(this::toDto)
                .collect(Collectors.toMap(ObjektLocDto::getJazyk, Function.identity()));
    }
}
