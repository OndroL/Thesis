package cz.inspire.common.mapper;

import cz.inspire.common.entity.NastaveniJsonEntity;
import cz.inspire.common.dto.NastaveniJsonDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI)
public interface NastaveniJsonMapper {
    NastaveniJsonDto toDto(NastaveniJsonEntity entity);

    NastaveniJsonEntity toEntity(NastaveniJsonDto dto);
}
