package cz.inspire.sport.mapper;

import cz.inspire.sport.dto.OvladacObjektuDto;
import cz.inspire.sport.entity.OvladacObjektuEntity;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI)
public interface OvladacObjektuMapper {

    // Map DTO to Entity
    @Mapping(target = "cislaZapojeni", source = "cislaZapojeniList") // Maps differently named fields
    OvladacObjektuEntity toEntity(OvladacObjektuDto dto);

    // Map Entity to DTO
    @Mapping(target = "cislaZapojeniList", source = "cislaZapojeni") // Maps differently named fields
    OvladacObjektuDto toDto(OvladacObjektuEntity entity);
}
