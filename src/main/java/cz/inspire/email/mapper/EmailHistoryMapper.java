package cz.inspire.email.mapper;

import cz.inspire.email.dto.EmailHistoryDto;
import cz.inspire.email.entity.EmailHistoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "cdi")
public interface EmailHistoryMapper {

    // Map from DTO to Entity
    @Mapping(target = "attachments", source = "attachments", ignore = true)
    @Mapping(target = "generatedAttachments", source = "generatedAttachments", ignore = true)
    EmailHistoryEntity toEntity(EmailHistoryDto dto);

    // Map from Entity to DTO
    @Mapping(target = "attachments", source = "attachments", ignore = true)
    @Mapping(target = "generatedAttachments", source = "generatedAttachments")
    EmailHistoryDto toDto(EmailHistoryEntity entity);
}
