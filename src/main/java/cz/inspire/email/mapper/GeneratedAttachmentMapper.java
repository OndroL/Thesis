package cz.inspire.email.mapper;

import cz.inspire.email.dto.GeneratedAttachmentDto;
import cz.inspire.email.entity.GeneratedAttachmentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "cdi")
public interface GeneratedAttachmentMapper {

    // Map DTO to Entity
    @Mapping(target = "emailHistory", ignore = true) // Handle emailHistory manually if needed
    @Mapping(target = "printTemplate", ignore = true) // Handle printTemplate manually if needed
    GeneratedAttachmentEntity toEntity(GeneratedAttachmentDto dto);

    // Map Entity to DTO
    @Mapping(target = "emailHistoryId", source = "emailHistory.id") // Map EmailHistoryEntity to emailHistoryId
    @Mapping(target = "printTemplateId", source = "printTemplate.id") // Map PrintTemplateEntity to printTemplateId
    GeneratedAttachmentDto toDto(GeneratedAttachmentEntity entity);
}
