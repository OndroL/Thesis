package cz.inspire.email.mapper;

import cz.inspire.email.dto.EmailQueueDto;
import cz.inspire.email.entity.EmailQueueEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface EmailQueueMapper {

    // Map DTO to Entity
    EmailQueueEntity toEntity(EmailQueueDto dto);

    // Map Entity to DTO
    EmailQueueDto toDto(EmailQueueEntity entity);
}
