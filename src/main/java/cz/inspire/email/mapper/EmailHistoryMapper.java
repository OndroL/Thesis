package cz.inspire.email.mapper;

import cz.inspire.email.dto.EmailHistoryDto;
import cz.inspire.email.entity.EmailHistoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "cdi")
public interface EmailHistoryMapper {

    // Map from DTO to Entity
    @Mapping(target = "attachments", source = "attachments", ignore = true) // attachments might need custom handling
    EmailHistoryEntity toEntity(EmailHistoryDto dto);

    // Map from Entity to DTO
    @Mapping(target = "attachments", source = "attachments") // Map attachments directly
    EmailHistoryDto toDto(EmailHistoryEntity entity);
}
