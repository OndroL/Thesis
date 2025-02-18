package cz.inspire.sport.service;

import cz.inspire.common.service.BaseService;
import cz.inspire.sport.entity.SportInstructorEntity;
import cz.inspire.sport.repository.SportInstructorRepository;
import jakarta.ejb.FinderException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;

import static cz.inspire.common.utils.ExceptionHandler.wrapDBException;

@ApplicationScoped
public class SportInstructorService extends BaseService<SportInstructorEntity, String, SportInstructorRepository> {

    public SportInstructorService() {
    }

    @Inject
    public SportInstructorService(SportInstructorRepository repository) {
        super(repository);
    }

    public List<SportInstructorEntity> findBySport(String sportId) throws FinderException {
        return wrapDBException(
                () -> repository.findBySport(sportId),
                "Error retrieving SportInstructorEntity records for sportId=" + sportId
        );
    }

    public List<SportInstructorEntity> findByInstructor(String instructorId) throws FinderException {
        return wrapDBException(
                () -> repository.findByInstructor(instructorId),
                "Error retrieving SportInstructorEntity records for instructorId=" + instructorId
        );
    }

    public Optional<SportInstructorEntity> findBySportAndInstructor(String sportId, String instructorId) throws FinderException {
        return wrapDBException(
                () -> repository.findBySportAndInstructor(sportId, instructorId),
                "Error retrieving SportInstructorEntity record for sportId=" + sportId + ", instructorId=" + instructorId
        );
    }

    public Optional<SportInstructorEntity> findBySportWithoutInstructor(String sportId) throws FinderException {
        return wrapDBException(
                () -> repository.findBySportWithoutInstructor(sportId),
                "Error retrieving SportInstructorEntity record for sportId=" + sportId + " without instructor"
        );
    }

    public List<SportInstructorEntity> findByActivity(String activityId) throws FinderException {
        return wrapDBException(
                () -> repository.findByActivity(activityId),
                "Error retrieving SportInstructorEntity records for activityId=" + activityId
        );
    }

    public Long countSportInstructors(String sportId) throws FinderException {
        return wrapDBException(
                () -> repository.countSportInstructors(sportId),
                "Error retrieving count of SportInstructorEntity records for sportId=" + sportId
        );
    }
}
