package cz.inspire.sport.mapper;

import cz.inspire.sport.dto.ArealDto;
import cz.inspire.sport.entity.ArealEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI, uses = ArealLocMapper.class)
public interface ArealMapper {

    // Map DTO to Entity
    @Mapping(target = "nadrazenyAreal", ignore = true)
    @Mapping(target = "podrazeneArealy", ignore = true)
    @Mapping(target = "objekty", ignore = true)
    ArealEntity toEntity(ArealDto dto);

    // Map Entity to DTO
    @Mapping(target = "nadrazenyArealId", source = "nadrazenyAreal.id")
    ArealDto toDto(ArealEntity entity);
}

