package cz.inspire.common.mapper;

import cz.inspire.common.entity.NastaveniJsonEntity;
import cz.inspire.common.dto.NastaveniJsonDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface NastaveniJsonMapper {
    NastaveniJsonDto toDto(NastaveniJsonEntity entity);

    NastaveniJsonEntity toEntity(NastaveniJsonDto dto);
}
