package cz.inspire.sport.mapper;

import cz.inspire.sport.dto.SportDto;
import cz.inspire.sport.entity.SportEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI,
        uses = {SportLocMapper.class, InstructorMapper.class, SportKategorieMapper.class, ActivityMapper.class})
public interface SportMapper {

    // Map DTO to Entity
    @Mapping(target = "sportKategorie", ignore = true)
    @Mapping(target = "nadrazenySport", ignore = true)
    @Mapping(target = "navazujiciSport", ignore = true)
    @Mapping(target = "activity", ignore = true)
    SportEntity toEntity(SportDto dto);

    // Map Entity to DTO
    @Mapping(target = "sportKategorie", source = "sportKategorie")
    @Mapping(target = "nadrazenySportId", source = "nadrazenySport.id")
    @Mapping(target = "navazujiciSportId", source = "navazujiciSport.id")
    @Mapping(target = "activityId", source = "activity.id")
    SportDto toDto(SportEntity entity);
}
