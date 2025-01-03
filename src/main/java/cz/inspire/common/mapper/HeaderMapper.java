package cz.inspire.common.mapper;

import cz.inspire.common.dto.HeaderDto;
import cz.inspire.common.entity.HeaderEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface HeaderMapper {
    HeaderDto toDto(HeaderEntity entity);
    HeaderEntity toEntity(HeaderDto dto);
}
