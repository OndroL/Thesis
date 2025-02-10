package cz.inspire.sport.mapper;

import cz.inspire.sport.dto.ObjektLocDto;
import cz.inspire.sport.entity.ObjektLocEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI)
public interface ObjektLocMapper {

    // Map DTO to Entity
    ObjektLocEntity toEntity(ObjektLocDto dto);

    // Map Entity to DTO
    ObjektLocDto toDto(ObjektLocEntity entity);
}
