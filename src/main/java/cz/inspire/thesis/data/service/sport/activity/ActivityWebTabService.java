package cz.inspire.thesis.data.service.sport.activity;

import cz.inspire.thesis.data.dto.sport.activity.ActivityWebTabDetails;
import cz.inspire.thesis.data.model.sport.activity.ActivityWebTabEntity;
import cz.inspire.thesis.data.repository.sport.activity.ActivityWebTabRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Those exceptions are created to mimic functionality and implementation of production exceptions
 * Use your imports
 */
import cz.inspire.thesis.exceptions.CreateException;

/**
 * This is import of simple generateGUID functionality created to mimic real functionality
 * In your implementation use your import of guidGenerator
 */
import static cz.inspire.thesis.data.utils.guidGenerator.generateGUID;

@ApplicationScoped
public class ActivityWebTabService {

    @Inject
    private ActivityWebTabRepository activityWebTabRepository;

    public String create(ActivityWebTabDetails details) throws CreateException {
        try {
            ActivityWebTabEntity entity = new ActivityWebTabEntity();
            if (details.getId() == null) {
                details.setId(generateGUID(entity));
            }
            entity.setId(details.getId());
            entity.setSportId(details.getSportId());
            entity.setActivityId(details.getActivityId());
            entity.setObjectId(details.getObjectId());
            entity.setTabIndex(details.getTabIndex());

            activityWebTabRepository.save(entity);

            return entity.getId();
        } catch (Exception e) {
            throw new CreateException("Failed to create ActivityWebTab entity", e);
        }
    }

    public ActivityWebTabDetails getDetails(ActivityWebTabEntity entity) {
        return new ActivityWebTabDetails(
                entity.getId(),
                entity.getSportId(),
                entity.getActivityId(),
                entity.getObjectId(),
                entity.getTabIndex()
        );
    }

    public Collection<ActivityWebTabDetails> findAll() {
        return activityWebTabRepository.findAll().stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    public Collection<ActivityWebTabDetails> findBySport(String sportId) {
        return activityWebTabRepository.findBySport(sportId).stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    public Collection<ActivityWebTabDetails> findByActivity(String activityId) {
        return activityWebTabRepository.findByActivity(activityId).stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    public Collection<ActivityWebTabDetails> findByObject(String objectId) {
        return activityWebTabRepository.findByObject(objectId).stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }
}