package cz.inspire.email.mapper;

import cz.inspire.email.dto.EmailQueueDto;
import cz.inspire.email.entity.EmailQueueEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI)
public interface EmailQueueMapper {

    // Map DTO to Entity
    EmailQueueEntity toEntity(EmailQueueDto dto);

    // Map Entity to DTO
    EmailQueueDto toDto(EmailQueueEntity entity);
}
