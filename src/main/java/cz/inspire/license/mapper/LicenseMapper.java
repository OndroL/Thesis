package cz.inspire.license.mapper;

import cz.inspire.license.dto.LicenseDto;
import cz.inspire.license.entity.LicenseEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI)
public interface LicenseMapper {
    LicenseDto toDto(LicenseEntity entity);
    LicenseEntity toEntity(LicenseDto dto);
}
