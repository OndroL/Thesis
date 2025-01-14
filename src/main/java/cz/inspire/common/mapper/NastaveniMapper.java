package cz.inspire.common.mapper;

import cz.inspire.common.entity.NastaveniEntity;
import cz.inspire.common.dto.NastaveniDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI)
public interface NastaveniMapper {
    NastaveniDto toDto(NastaveniEntity entity);

    NastaveniEntity toEntity(NastaveniDto dto);
}
