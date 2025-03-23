package cz.inspire.sport.mapper;

import cz.inspire.sport.dto.ActivityFavouriteDto;
import cz.inspire.sport.entity.ActivityFavouriteEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI)
public interface ActivityFavouriteMapper {

    // Map DTO to Entity
    @Mapping(target = "datumPosledniZmeny", source = "datumPosledniZmeny", qualifiedByName = "dateToLocalDateTime")
    ActivityFavouriteEntity toEntity(ActivityFavouriteDto dto);
    // Map Entity to DTO
    @Mapping(target = "datumPosledniZmeny", source = "datumPosledniZmeny", qualifiedByName = "localDateTimeToDate")
    ActivityFavouriteDto toDto(ActivityFavouriteEntity entity);


    @Named("localDateTimeToDate")
    default Date localDateTimeToDate(LocalDateTime ldt) {
        if (ldt == null) {
            return null;
        }
        return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
    }

    @Named("dateToLocalDateTime")
    default LocalDateTime dateToLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
}
