package cz.inspire.sport.mapper;

import cz.inspire.sport.dto.OtviraciDobaObjektuDto;
import cz.inspire.sport.entity.OtviraciDobaObjektuEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI)
public interface OtviraciDobaObjektuMapper {

    // Map DTO to Entity
    @Mapping(target = "embeddedId.objektId",   source = "objektId")
    @Mapping(target = "embeddedId.platnostOd", source = "platnostOd", qualifiedByName = "dateToLocalDateTime")
    @Mapping(target = "otviraciDoba",  source = "otviraciDoba")
    OtviraciDobaObjektuEntity toEntity(OtviraciDobaObjektuDto dto);

    // Map Entity to DTO
    @Mapping(target = "objektId",   source = "embeddedId.objektId")
    @Mapping(target = "platnostOd", source = "embeddedId.platnostOd", qualifiedByName = "localDateTimeToDate")
    @Mapping(target = "otviraciDoba", source = "otviraciDoba")
    OtviraciDobaObjektuDto toDto(OtviraciDobaObjektuEntity entity);

    @Named("localDateTimeToDate")
    default Date localDateTimeToDate(LocalDateTime ldt) {
        if (ldt == null) {
            return null;
        }
        // Convert LocalDateTime -> Date using system default zone (customize if needed)
        return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
    }

    @Named("dateToLocalDateTime")
    default LocalDateTime dateToLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        // Convert Date -> LocalDateTime using system default zone
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
}