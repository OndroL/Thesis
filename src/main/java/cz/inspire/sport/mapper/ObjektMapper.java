package cz.inspire.sport.mapper;

import cz.inspire.sport.dto.ObjektDto;
import cz.inspire.sport.entity.ObjektEntity;
import org.mapstruct.*;

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
    @Mapping(target = "nadobjekty", ignore = true) // Prevent recursion issues
    @Mapping(target = "podobjekty", ignore = true) // Prevent recursion issues
    ObjektEntity toEntity(ObjektDto dto);

    // Map Entity to DTO
    @Mapping(target = "nadobjekty", source = "nadobjekty", qualifiedByName = "mapNadPodObjekty")
    @Mapping(target = "podobjekty", source = "podobjekty", qualifiedByName = "mapNadPodObjekty")
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
