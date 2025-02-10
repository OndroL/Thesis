package cz.inspire.sport.mapper;

import cz.inspire.sport.dto.OmezeniRezervaciDto;
import cz.inspire.sport.entity.OmezeniRezervaciEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI)
public interface OmezeniRezervaciMapper {

    // Map DTO to Entity
    OmezeniRezervaciEntity toEntity(OmezeniRezervaciDto dto);

    // Map Entity to DTO
    OmezeniRezervaciDto toDto(OmezeniRezervaciEntity entity);
}
