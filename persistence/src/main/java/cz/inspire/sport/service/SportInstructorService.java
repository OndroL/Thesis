package cz.inspire.sport.service;

import cz.inspire.common.service.BaseService;
import cz.inspire.sport.entity.SportEntity;
import cz.inspire.sport.entity.SportInstructorEntity;
import cz.inspire.sport.repository.SportInstructorRepository;
import jakarta.ejb.CreateException;
import jakarta.ejb.FinderException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static cz.inspire.common.utils.ExceptionHandler.wrapDBException;

@ApplicationScoped
public class SportInstructorService extends BaseService<SportInstructorEntity, String, SportInstructorRepository> {

    Logger logger = LogManager.getLogger(SportInstructorService.class);

    public SportInstructorService() {
    }

    public void checkSportWithoutInstructor(SportEntity entity) {
        try {
            Long instructorCount = countSportInstructors(entity.getId());
            if (instructorCount == null || instructorCount == 0) {
                SportInstructorEntity sid = new SportInstructorEntity();
                sid.setActivityId(entity.getActivity().getId());
                sid.setDeleted(false);
                sid.setInstructor(null);
                sid.setSport(entity);
                create(sid);
            }
        } catch (CreateException ex) {
            logger.error("Unable to create SportInstructorEntity without instructor for sport " + entity.getId(), ex);
        } catch (Exception ex) {
            logger.error("Nepodarilo sa zistit pocet instruktorov sportu " + entity.getId(), ex);
        }
    }

    @Inject
    public SportInstructorService(SportInstructorRepository repository) {
        super(repository);
    }

    public List<SportInstructorEntity> findBySport(String sportId) throws FinderException {
        return wrapDBException(
                () -> repository.findBySport(sportId),
                "Error retrieving SportInstructorEntity records for sportId = " + sportId
        );
    }

    public List<SportInstructorEntity> findByInstructor(String instructorId) throws FinderException {
        return wrapDBException(
                () -> repository.findByInstructor(instructorId),
                "Error retrieving SportInstructorEntity records for instructorId = " + instructorId
        );
    }

    public SportInstructorEntity findBySportAndInstructor(String sportId, String instructorId) throws FinderException {
        return wrapDBException(
                () -> repository.findBySportAndInstructor(sportId, instructorId),
                "Error retrieving SportInstructorEntity record for sportId = " + sportId + ", instructorId = " + instructorId
        );
    }

    public SportInstructorEntity findBySportWithoutInstructor(String sportId) throws FinderException {
        return wrapDBException(
                () -> repository.findBySportWithoutInstructor(sportId),
                "Error retrieving SportInstructorEntity record for sportId = " + sportId + " without instructor"
        );
    }

    public List<SportInstructorEntity> findByActivity(String activityId) throws FinderException {
        return wrapDBException(
                () -> repository.findByActivity(activityId),
                "Error retrieving SportInstructorEntity records for activityId = " + activityId
        );
    }

    public Long countSportInstructors(String sportId) throws FinderException {
        return wrapDBException(
                () -> repository.countSportInstructors(sportId),
                "Error retrieving count of SportInstructorEntity records for sportId = " + sportId
        );
    }
}
