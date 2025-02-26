package cz.inspire.email.mapper;

import cz.inspire.email.dto.GeneratedAttachmentDto;
import cz.inspire.email.entity.GeneratedAttachmentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI)
public interface GeneratedAttachmentMapper {

    // Map DTO to Entity
    @Mapping(target = "emailHistory", ignore = true)
    @Mapping(target = "printTemplate", ignore = true)
    GeneratedAttachmentEntity toEntity(GeneratedAttachmentDto dto);

    // Map Entity to DTO
    @Mapping(target = "emailHistoryId", source = "emailHistory.id")
    @Mapping(target = "printTemplateId", source = "printTemplate.id")
    GeneratedAttachmentDto toDto(GeneratedAttachmentEntity entity);
}
