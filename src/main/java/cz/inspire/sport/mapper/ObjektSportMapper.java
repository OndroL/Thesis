package cz.inspire.sport.mapper;

import cz.inspire.sport.dto.ObjektSportDto;
import cz.inspire.sport.entity.ObjektSportEntity;
import cz.inspire.sport.entity.ObjektSportPK;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI)
public interface ObjektSportMapper {

    // Map DTO to Entity
    @Mapping(target = "id", source = ".", qualifiedByName = "mapDtoToPK")
    @Mapping(target = "sport", ignore = true)
    @Mapping(target = "objekt", ignore = true)
    ObjektSportEntity toEntity(ObjektSportDto dto);

    // Map Entity to DTO
    @Mapping(target = "id", source = "id.id")
    @Mapping(target = "index", source = "id.index")
    @Mapping(target = "sportId", source = "sport.id")
    @Mapping(target = "objektId", source = "objekt.id")
    ObjektSportDto toDto(ObjektSportEntity entity);

    // Custom mapping: Convert DTO to Embedded ID
    @Named("mapDtoToPK")
    default ObjektSportPK mapDtoToPK(ObjektSportDto dto) {
        return new ObjektSportPK(dto.getId(), dto.getIndex());
    }
}
