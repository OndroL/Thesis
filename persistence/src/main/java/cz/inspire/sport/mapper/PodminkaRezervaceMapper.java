package cz.inspire.sport.mapper;

import cz.inspire.sport.dto.PodminkaRezervaceDto;
import cz.inspire.sport.entity.PodminkaRezervaceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI)
public interface PodminkaRezervaceMapper {

    // Map DTO to Entity
    @Mapping(target = "objekt", source = "objektId", ignore = true)
    PodminkaRezervaceEntity toEntity(PodminkaRezervaceDto dto);

    // Map Entity to DTO
    @Mapping(target = "objektId", source = "objekt.id")
    PodminkaRezervaceDto toDto(PodminkaRezervaceEntity entity);
}
