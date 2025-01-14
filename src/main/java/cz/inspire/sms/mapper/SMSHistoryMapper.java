package cz.inspire.sms.mapper;

import cz.inspire.sms.dto.SMSHistoryDto;
import cz.inspire.sms.entity.SMSHistoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI)
public interface SMSHistoryMapper {

    SMSHistoryDto toDto(SMSHistoryEntity entity);

    SMSHistoryEntity toEntity(SMSHistoryDto dto);
}
