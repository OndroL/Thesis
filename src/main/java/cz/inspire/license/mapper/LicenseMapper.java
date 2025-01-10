package cz.inspire.license.mapper;

import cz.inspire.license.dto.LicenseDto;
import cz.inspire.license.entity.LicenseEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface LicenseMapper {
    LicenseDto toDto(LicenseEntity entity);
    LicenseEntity toEntity(LicenseDto dto);
}
