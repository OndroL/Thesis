package cz.inspire.sport.mapper;

import cz.inspire.sport.dto.ActivityFavouriteDto;
import cz.inspire.sport.entity.ActivityFavouriteEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI)
public interface ActivityFavouriteMapper {

    // Map DTO to Entity
    ActivityFavouriteEntity toEntity(ActivityFavouriteDto dto);

    // Map Entity to DTO
    ActivityFavouriteDto toDto(ActivityFavouriteEntity entity);
}
