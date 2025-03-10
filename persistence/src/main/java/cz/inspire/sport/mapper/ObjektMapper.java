package cz.inspire.sport.mapper;

import cz.inspire.sport.dto.ObjektDto;
import cz.inspire.sport.dto.PodminkaRezervaceDto;
import cz.inspire.sport.dto.SportDto;
import cz.inspire.sport.entity.ObjektEntity;
import cz.inspire.sport.entity.ObjektSportEntity;
import cz.inspire.sport.entity.ObjektSportPK;
import cz.inspire.sport.entity.PodminkaRezervaceEntity;
import cz.inspire.sport.entity.SportEntity;
import cz.inspire.sport.repository.ObjektRepository;
import jakarta.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI, uses = {
        ArealMapper.class,
        ObjektLocMapper.class,
        ObjektSportMapper.class,
        OvladacObjektuMapper.class,
        PodminkaRezervaceMapper.class,
        SportMapper.class
})
public abstract class ObjektMapper {

    @Inject
    protected SportMapper sportMapper;

    @Inject
    protected ObjektRepository objektRepository;

    @Inject
    protected PodminkaRezervaceMapper podminkaRezervaceMapper;

    private static final Logger logger = LogManager.getLogger(ObjektMapper.class);

    // ======================================
    //          DTO -> ENTITY
    // ======================================

    @Mapping(target = "nadObjekty", ignore = true)
    @Mapping(target = "podObjekty", ignore = true)
    @Mapping(target = "objektSports", ignore = true)
    @Mapping(target = "podminkyRezervaci", ignore = true)
    @Mapping(target = "localeData", source = "localeData", qualifiedByName = "mapLocaleDataToList")
    public abstract ObjektEntity toEntity(ObjektDto dto);


    @AfterMapping
    protected void mapSports(ObjektDto dto,
                                        @MappingTarget ObjektEntity entity)
    {
        if (dto.getSports() == null) {
            entity.setObjektSports(null);
            return;
        }

        List<ObjektSportEntity> objektSports = new ArrayList<>();
        int index = 0;
        for (SportDto sportDto : dto.getSports()) {
            SportEntity sportEntity = sportMapper.toEntity(sportDto);

            ObjektSportEntity objektSport = new ObjektSportEntity();
            objektSport.setSport(sportEntity);
            objektSport.setObjekt(entity);

            ObjektSportPK pk = new ObjektSportPK();
            pk.setIndex(index++);
            objektSport.setEmbeddedId(pk);

            objektSports.add(objektSport);
        }
        entity.setObjektSports(objektSports);
    }

    @AfterMapping
    protected void mapNadPod(ObjektDto dto,
                                        @MappingTarget ObjektEntity entity) {
        // =========== NADOBJEKTY ===========
        if (dto.getNadObjekty() != null) {
            Set<ObjektEntity> nadObjekty = new HashSet<>();
            for (String objektId : dto.getNadObjekty()) {
                try {
                    ObjektEntity nadObj = objektRepository.findByPrimaryKey(objektId);
                    Hibernate.initialize(entity.getNadObjekty());
                    if (Hibernate.isInitialized(entity.getNadObjekty())) {
                        nadObjekty.add(nadObj);
                    }
                } catch (Exception ex) {
                    logger.error("Failed to set nadObjekt for ObjektEntity", ex);

                }
            }
            entity.setNadObjekty(nadObjekty);
        }

        // =========== PODOBJEKTY ===========
        if (dto.getPodObjekty() != null) {
            Set<ObjektEntity> podObjekty = new HashSet<>();
            for (String objektId : dto.getPodObjekty()) {
                try {
                    ObjektEntity podObj = objektRepository.findByPrimaryKey(objektId);
                    Hibernate.initialize(entity.getPodObjekty());
                    if (Hibernate.isInitialized(entity.getPodObjekty())) {
                        podObjekty.add(podObj);
                    }
                } catch (Exception ex) {
                    logger.error("Failed to set podObjekt for ObjektEntity", ex);
                }
            }
            entity.setPodObjekty(podObjekty);
        }
    }

    @AfterMapping
    protected void mapPodminkaRezeravace(ObjektDto dto, @MappingTarget ObjektEntity entity) {
        try {
            if (dto.getPodminkyRezervaci() != null && !dto.getPodminkyRezervaci().isEmpty()) {
                long priority = 0;
                for (PodminkaRezervaceDto podm : dto.getPodminkyRezervaci()) {
                    PodminkaRezervaceEntity newPodminka = podminkaRezervaceMapper.toEntity(podm);
                    newPodminka.setPriorita(priority++);
                    entity.getPodminkyRezervaci().add(newPodminka);
                }
            }
        } catch (Exception e) {
            logger.error("Cannot set reservation conditions for object while mapping", e);
        }
    }

    // ======================================
    //          ENTITY -> DTO
    // ======================================

    @Mapping(target = "nadObjekty", source = "nadObjekty", qualifiedByName = "mapNadPodObjekty")
    @Mapping(target = "podObjekty", source = "podObjekty", qualifiedByName = "mapNadPodObjekty")
    @Mapping(target = "localeData", source = "localeData", qualifiedByName = "mapLocaleDataToMap")
    @Mapping(target = "sports", source = "objektSports", qualifiedByName = "mapObjektSportsToSports")
    public abstract ObjektDto toDto(ObjektEntity entity);

    @Named("mapNadPodObjekty")
    protected Set<String> mapNadPodObjekty(Set<ObjektEntity> objekty) {
        if (objekty == null) {
            return null;
        }
        return objekty.stream()
                .map(ObjektEntity::getId)
                .collect(Collectors.toSet());
    }

    @Named("mapObjektSportsToSports")
    protected List<SportDto> mapObjektSportsToSports(List<ObjektSportEntity> objektSportsList) {
        if (objektSportsList == null) {
            return null;
        }
        return objektSportsList.stream()
                .sorted(OBJEKT_SPORT_COMPARATOR)
                .map(os -> sportMapper.toDto(os.getSport()))
                .collect(Collectors.toList());
    }
    @Named("mapPodminkaRezervace")
    protected List<PodminkaRezervaceDto> mapPodminkaRezervace(List<PodminkaRezervaceEntity> podminkaRezervaceEntityList) {
        if (podminkaRezervaceEntityList == null) {
            return null;
        }
        return podminkaRezervaceEntityList.stream()
                .sorted(PODMINKY_REZERVACI_COMPARATOR)
                .map(podminka -> podminkaRezervaceMapper.toDto(podminka))
                .collect(Collectors.toList());
    }

    private static final Comparator<ObjektSportEntity> OBJEKT_SPORT_COMPARATOR =
            Comparator.comparingInt(entity -> entity.getEmbeddedId().getIndex());

    private static final Comparator<PodminkaRezervaceEntity> PODMINKY_REZERVACI_COMPARATOR =
            Comparator.comparing(PodminkaRezervaceEntity::getPriorita);
}
