package cz.inspire.sport.mapper;

import cz.inspire.sport.dto.ArealDto;
import cz.inspire.sport.entity.ArealEntity;
import cz.inspire.sport.entity.ArealLocEntity;
import cz.inspire.sport.service.ArealService;
import jakarta.ejb.FinderException;
import jakarta.inject.Inject;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI, uses = {ArealLocMapper.class})
public abstract class ArealMapper {

    @Inject
    ArealService arealService;

    @Inject
    ArealLocMapper arealLocMapper;

    // Map DTO to Entity
    @Mapping(target = "nadrazenyAreal", ignore = true)
    @Mapping(target = "podrazeneArealy", ignore = true)
    @Mapping(target = "objekty", ignore = true)
    @Mapping(target = "localeData", ignore = true)
    public abstract ArealEntity toEntity(ArealDto dto) throws FinderException;

    @AfterMapping
    protected void mapLocaleData(ArealDto dto, @MappingTarget ArealEntity entity) {
        List<ArealLocEntity> newLocaleData = arealLocMapper.mapLocaleDataToList(dto.getLocaleData());
        if (entity.getLocaleData() == null) {
            entity.setLocaleData(newLocaleData);
        } else {
            entity.getLocaleData().clear();
            if (newLocaleData != null) {
                entity.getLocaleData().addAll(newLocaleData);
            }
        }
    }

    @AfterMapping
    protected void mapNadrazenyAreal(ArealDto dto, @MappingTarget ArealEntity entity) throws FinderException {
        if (dto.getNadrazenyArealId() != null) {
            try {
                ArealEntity nadrazeny = arealService.findByPrimaryKey(dto.getNadrazenyArealId());
                entity.setNadrazenyAreal(nadrazeny);
            } catch (Exception e) {
                throw new FinderException("Unable to set nadrazeny areal during mapping with NadrazenyArealId = " + dto.getNadrazenyArealId());
            }
        }
    }


    // Map Entity to DTO
    @Mapping(target = "nadrazenyArealId", source = "nadrazenyAreal.id")
    @Mapping(target = "localeData", source = "localeData", qualifiedByName = "mapLocaleDataToMap")
    public abstract ArealDto toDto(ArealEntity entity);
}

