package cz.inspire.sport.mapper;

import cz.inspire.sport.dto.SportKategorieDto;
import cz.inspire.sport.entity.SportKategorieEntity;
import cz.inspire.sport.entity.SportKategorieLocEntity;
import cz.inspire.sport.service.SportKategorieService;
import jakarta.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

import java.util.List;


@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI, uses = {SportKategorieLocMapper.class})
public abstract class SportKategorieMapper {

    @Inject
    SportKategorieService sportKategorieService;

    @Inject
    SportKategorieLocMapper sportKategorieLocMapper;

    private static final Logger logger = LogManager.getLogger(SportKategorieMapper.class);

    // Map DTO to Entity
    @Mapping(target = "nadrazenaKategorie", ignore = true)
    @Mapping(target = "cinnosti", ignore = true)
    @Mapping(target = "podrazeneKategorie", ignore = true)
    @Mapping(target = "localeData", ignore = true)
    public abstract SportKategorieEntity toEntity(SportKategorieDto dto);

    @AfterMapping
    protected void mapLocaleData(SportKategorieDto dto, @MappingTarget SportKategorieEntity entity) {
        List<SportKategorieLocEntity> newLocaleData = sportKategorieLocMapper.mapLocaleDataToList(dto.getLocaleData());
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
    protected void mapNadrazenaKategorie(SportKategorieDto dto, @MappingTarget SportKategorieEntity entity) {
        if (dto.getNadrazenaKategorieId() != null) {
            try {
                SportKategorieEntity nadrazenaKategorie = sportKategorieService.findByPrimaryKey(dto.getNadrazenaKategorieId());
                entity.setNadrazenaKategorie(nadrazenaKategorie);
            } catch (Exception ex) {
                logger.error("Unable set NadrazenaKategorie with id = {}", dto.getNadrazenaKategorieId(), ex);
            }
        }
    }


    // Map Entity to DTO
    @Mapping(target = "nadrazenaKategorieId", source = "nadrazenaKategorie.id")
    @Mapping(target = "localeData", source = "localeData", qualifiedByName = "mapLocaleDataToMap")
    public abstract SportKategorieDto toDto(SportKategorieEntity entity);
}
