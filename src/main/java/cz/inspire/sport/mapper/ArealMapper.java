package cz.inspire.sport.mapper;

import cz.inspire.sport.dto.ArealDto;
import cz.inspire.sport.dto.ArealLocDto;
import cz.inspire.sport.entity.ArealEntity;
import cz.inspire.sport.entity.ArealLocEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.mapstruct.Context;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI, uses = ArealLocMapper.class)
public interface ArealMapper {

    // Map DTO to Entity
    @Mapping(target = "localeData", source = "localeData", qualifiedByName = "mapDtoToEntityLocaleData")
    @Mapping(target = "nadrazenyAreal", ignore = true)
    @Mapping(target = "podrazeneArealy", ignore = true)
    @Mapping(target = "objekty", ignore = true)
    ArealEntity toEntity(ArealDto dto);

    // Map Entity to DTO
    @Mapping(target = "localeData", source = "localeData", qualifiedByName = "mapEntityToDtoLocaleData")
    @Mapping(target = "nadrazenyArealId", source = "nadrazenyAreal.id")
    ArealDto toDto(ArealEntity entity);

    // Custom mapping: Convert List<ArealLocEntity> -> Map<String, ArealLocDto>
    @Named("mapEntityToDtoLocaleData")
    default Map<String, ArealLocDto> mapEntityToDtoLocaleData(List<ArealLocEntity> localeData, @Context ArealLocMapper arealLocMapper) {
        if (localeData == null) {
            return null;
        }
        return localeData.stream()
                .collect(Collectors.toMap(ArealLocEntity::getId, arealLocMapper::toDto));
    }

    // Custom mapping: Convert Map<String, ArealLocDto> -> List<ArealLocEntity>
    @Named("mapDtoToEntityLocaleData")
    default List<ArealLocEntity> mapDtoToEntityLocaleData(Map<String, ArealLocDto> localeData, @Context ArealLocMapper arealLocMapper) {
        if (localeData == null) {
            return null;
        }
        return localeData.values().stream()
                .map(arealLocMapper::toEntity)
                .collect(Collectors.toList());
    }
}
