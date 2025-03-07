package cz.inspire.sport.mapper;

import cz.inspire.exception.ApplicationException;
import cz.inspire.sport.dto.ArealDto;
import cz.inspire.sport.entity.ArealEntity;
import cz.inspire.sport.service.ArealService;
import jakarta.inject.Inject;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI, uses = {ArealLocMapper.class})
public abstract class ArealMapper {

    @Inject
    ArealService arealService;

    // Map DTO to Entity
    @Mapping(target = "nadrazenyAreal", ignore = true)
    @Mapping(target = "podrazeneArealy", ignore = true)
    @Mapping(target = "objekty", ignore = true)
    @Mapping(target = "localeData", source = "localeData", qualifiedByName = "mapLocaleDataToList")
    public abstract ArealEntity toEntity(ArealDto dto);

    @AfterMapping
    protected void mapNadrazenyAreal(ArealDto dto, @MappingTarget ArealEntity entity) throws ApplicationException {
        if (dto.getNadrazenyArealId() != null) {
            try {
                ArealEntity nadrazeny = arealService.findByPrimaryKey(dto.getNadrazenyArealId());
                nadrazeny.getPodrazeneArealy().add(entity);
                arealService.update(nadrazeny);
            } catch (Exception e) {
                throw new ApplicationException("Unable to set nadrazeny areal during mapping", e);
            }
        }
    }


    // Map Entity to DTO
    @Mapping(target = "nadrazenyArealId", source = "nadrazenyAreal.id")
    @Mapping(target = "localeData", source = "localeData", qualifiedByName = "mapLocaleDataToMap")
    public abstract ArealDto toDto(ArealEntity entity);
}

