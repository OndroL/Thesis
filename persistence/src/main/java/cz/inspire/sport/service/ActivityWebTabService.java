package cz.inspire.sport.service;

import cz.inspire.common.service.BaseService;
import cz.inspire.sport.entity.ActivityWebTabEntity;
import cz.inspire.sport.repository.ActivityWebTabRepository;
import jakarta.ejb.FinderException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

import static cz.inspire.common.utils.ExceptionHandler.wrapDBException;

@ApplicationScoped
public class ActivityWebTabService extends BaseService<ActivityWebTabEntity, String, ActivityWebTabRepository> {

    public ActivityWebTabService() {
    }

    @Inject
    public ActivityWebTabService(ActivityWebTabRepository repository) {
        super(repository);
    }

    public List<ActivityWebTabEntity> findBySport(String sportId) throws FinderException {
        return wrapDBException(
                () -> repository.findBySport(sportId),
                "Error retrieving ActivityWebTabEntity records by sportId = " + sportId
        );
    }

    public List<ActivityWebTabEntity> findByActivity(String activityId) throws FinderException {
        return wrapDBException(
                () -> repository.findByActivity(activityId),
                "Error retrieving ActivityWebTabEntity records by activityId = " + activityId
        );
    }

    public List<ActivityWebTabEntity> findByObject(String objectId) throws FinderException {
        return wrapDBException(
                () -> repository.findByObject(objectId),
                "Error retrieving ActivityWebTabEntity records by objectId = " + objectId
        );
    }
}
