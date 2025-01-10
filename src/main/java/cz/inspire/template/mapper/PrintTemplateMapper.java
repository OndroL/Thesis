package cz.inspire.template.mapper;

import cz.inspire.template.dto.PrintTemplateDto;
import cz.inspire.template.entity.PrintTemplateEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface PrintTemplateMapper {
    PrintTemplateDto toDto(PrintTemplateEntity entity);

    PrintTemplateEntity toEntity(PrintTemplateDto dto);
}
