package cz.inspire.sport.facade;

import cz.inspire.sport.dto.SportInstructorDto;
import cz.inspire.sport.entity.SportInstructorEntity;
import cz.inspire.sport.mapper.SportInstructorMapper;
import cz.inspire.sport.service.SportInstructorService;
import jakarta.ejb.FinderException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class SportInstructorFacade {
    @Inject
    SportInstructorService sportInstructorService;
    @Inject
    SportInstructorMapper sportInstructorMapper;

    public SportInstructorDto mapToDto(SportInstructorEntity entity) {
        return sportInstructorMapper.toDto(entity);
    }

    public List<SportInstructorDto> findBySport(String sportId) throws FinderException {
        return sportInstructorService.findBySport(sportId).stream().map(this::mapToDto).toList();
    }

    public List<SportInstructorDto> findByInstructor(String instructorId) throws FinderException {
        return sportInstructorService.findByInstructor(instructorId).stream().map(this::mapToDto).toList();
    }

    public SportInstructorDto findBySportAndInstructor(String sportId, String instructorId) throws FinderException {
        return mapToDto(sportInstructorService.findBySportAndInstructor(sportId, instructorId));
    }

    public SportInstructorDto findBySportWithoutInstructor(String sportId) throws FinderException {
        return mapToDto(sportInstructorService.findBySportWithoutInstructor(sportId));
    }

    public List<SportInstructorDto> findByActivity(String activityId) throws FinderException {
        return sportInstructorService.findByActivity(activityId).stream().map(this::mapToDto).toList();
    }

    public Long countSportInstructors(String sportId) throws FinderException {
        return sportInstructorService.countSportInstructors(sportId);
    }
}
