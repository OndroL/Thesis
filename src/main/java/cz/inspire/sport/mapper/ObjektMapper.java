package cz.inspire.sport.mapper;

import cz.inspire.sport.dto.ObjektDto;
import cz.inspire.sport.entity.ObjektEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.mapstruct.Mapping;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI, uses = {
        ArealMapper.class,
        ObjektLocMapper.class,
        ObjektSportMapper.class,
        OvladacObjektuMapper.class,
        PodminkaRezervaceMapper.class
})
public interface ObjektMapper {

    // Map DTO to Entity
    @Mapping(target = "nadobjekty", ignore = true)
    @Mapping(target = "podobjekty", ignore = true)
    @Mapping(target = "localeData", source = "localeData", qualifiedByName = "mapLocaleDataToList")
    ObjektEntity toEntity(ObjektDto dto);

    // Map Entity to DTO
    @Mapping(target = "nadobjekty", source = "nadobjekty", qualifiedByName = "mapNadPodObjekty")
    @Mapping(target = "podobjekty", source = "podobjekty", qualifiedByName = "mapNadPodObjekty")
    @Mapping(target = "localeData", source = "localeData", qualifiedByName = "mapLocaleDataToMap")
    ObjektDto toDto(ObjektEntity entity);

    // Convert Set<ObjektEntity> to Set<String> (IDs)
    @Named("mapNadPodObjekty")
    default Set<String> mapNadPodObjekty(Set<ObjektEntity> objekty) {
        if (objekty == null) {
            return null;
        }
        return objekty.stream().map(ObjektEntity::getId).collect(Collectors.toSet());
    }
}
