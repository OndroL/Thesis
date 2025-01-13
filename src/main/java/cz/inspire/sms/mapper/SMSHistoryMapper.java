package cz.inspire.sms.mapper;

import cz.inspire.sms.dto.SMSHistoryDto;
import cz.inspire.sms.entity.SMSHistoryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface SMSHistoryMapper {

    SMSHistoryDto toDto(SMSHistoryEntity entity);

    SMSHistoryEntity toEntity(SMSHistoryDto dto);
}
