package cz.inspire.sport.mapper;

import cz.inspire.sport.dto.OtviraciDobaObjektuDto;
import cz.inspire.sport.entity.OtviraciDobaObjektuEntity;
import cz.inspire.sport.entity.OtviraciDobaObjektuPK;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI)
public interface OtviraciDobaObjektuMapper {

    // Map DTO to Entity
    @Mapping(target = "id", source = ".", qualifiedByName = "mapDtoToPK")
    OtviraciDobaObjektuEntity toEntity(OtviraciDobaObjektuDto dto);

    // Map Entity to DTO
    @Mapping(target = "objektId", source = "id.objektId")
    @Mapping(target = "platnostOd", source = "id.platnostOd")
    OtviraciDobaObjektuDto toDto(OtviraciDobaObjektuEntity entity);

    // Convert DTO fields to EmbeddedId
    @Named("mapDtoToPK")
    default OtviraciDobaObjektuPK mapDtoToPK(OtviraciDobaObjektuDto dto) {
        return new OtviraciDobaObjektuPK(dto.getObjektId(), dto.getPlatnostOd());
    }
}
