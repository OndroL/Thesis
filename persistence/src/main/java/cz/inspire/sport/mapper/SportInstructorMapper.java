package cz.inspire.sport.mapper;

import cz.inspire.sport.dto.SportInstructorDto;
import cz.inspire.sport.entity.SportInstructorEntity;
import cz.inspire.sport.service.InstructorService;
import cz.inspire.sport.service.SportService;
import jakarta.ejb.FinderException;
import jakarta.inject.Inject;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI)
public abstract class SportInstructorMapper {
    @Inject
    InstructorService instructorService;
    @Inject
    SportService sportService;

    // Map DTO to Entity
    @Mapping(target = "sport", source = "sportId", ignore = true)
    @Mapping(target = "instructor", source = "instructorId", ignore = true)
    public abstract SportInstructorEntity toEntity(SportInstructorDto dto) throws FinderException;

    @AfterMapping
    protected void mapSportAndInstructor(SportInstructorDto dto, @MappingTarget SportInstructorEntity entity) throws FinderException {

        if (dto.getSportId() != null) {
            entity.setSport(sportService.findByPrimaryKey(dto.getSportId()));
            entity.setActivityId(entity.getSport().getActivity().getId());
        }

        if (dto.getInstructorId() != null) {
            entity.setInstructor(instructorService.findByPrimaryKey(dto.getInstructorId()));
        }
    }

    // Map Entity to DTO
    @Mapping(target = "sportId", source = "sport.id")
    @Mapping(target = "instructorId", source = "instructor.id")
    public abstract SportInstructorDto toDto(SportInstructorEntity entity);
}
