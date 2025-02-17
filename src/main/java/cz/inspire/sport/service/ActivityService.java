package cz.inspire.sport.service;

import cz.inspire.common.service.BaseService;
import cz.inspire.sport.entity.ActivityEntity;
import cz.inspire.sport.repository.ActivityRepository;
import jakarta.data.Limit;
import jakarta.ejb.FinderException;
import jakarta.inject.Inject;

import java.util.List;

import static cz.inspire.common.utils.ExceptionHandler.wrapDBException;

public class ActivityService extends BaseService<ActivityEntity, String, ActivityRepository> {

    public ActivityService() {
    }

    @Inject
    public ActivityService(ActivityRepository repository) {
        super(repository);
    }

    public List<ActivityEntity> findAll() throws FinderException {
        return wrapDBException(
                () -> repository.findAllOrdered(),
                "Error retrieving all ActivityEntity records in ordered manner"
        );
    }

    public List<ActivityEntity> findAll(int offset, int count) throws FinderException {
        return wrapDBException(
                () -> repository.findAll(Limit.range(offset + 1, count)),
                "Error retrieving paginated ActivityEntity records (offset + 1 = " + offset + ", count = " + count + ")"
        );
    }

    public List<ActivityEntity> findAllByInstructor(String instructorId, int offset, int count) throws FinderException {
        return wrapDBException(
                () -> repository.findAllByInstructor(instructorId, Limit.range(offset + 1, count)),
                "Error retrieving all paginated ActivityEntity records by instructorId = " + instructorId +
                        " (offset + 1 = " + offset + ", count = " + count + ")"
        );
    }

    public Long countActivities() throws FinderException {
        return wrapDBException(
                () -> repository.countActivities(),
                "Error retrieving total count of ActivityEntity records"
        );
    }

    public Long countActivitiesByInstructor(String instructorId) throws FinderException {
        return wrapDBException(
                () -> repository.countActivitiesByInstructor(instructorId),
                "Error retrieving count of ActivityEntity records for instructorId = " + instructorId
        );
    }
}
