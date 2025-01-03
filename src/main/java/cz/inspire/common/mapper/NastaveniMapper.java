package cz.inspire.common.mapper;

import cz.inspire.common.entity.NastaveniEntity;
import cz.inspire.common.dto.NastaveniDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface NastaveniMapper {
    NastaveniDto toDto(NastaveniEntity entity);

    NastaveniEntity toEntity(NastaveniDto dto);
}
