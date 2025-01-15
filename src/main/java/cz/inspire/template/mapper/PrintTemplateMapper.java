package cz.inspire.template.mapper;

import cz.inspire.template.dto.PrintTemplateDto;
import cz.inspire.template.entity.PrintTemplateEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI)
public interface PrintTemplateMapper {
    PrintTemplateDto toDto(PrintTemplateEntity entity);

    PrintTemplateEntity toEntity(PrintTemplateDto dto);
}
