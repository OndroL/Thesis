package cz.inspire.thesis.data.service.sport.activity;

import cz.inspire.thesis.data.dto.sport.activity.ActivityDetails;
import cz.inspire.thesis.data.dto.sport.sport.InstructorDetails;
import cz.inspire.thesis.data.dto.sport.sport.SportDetails;
import cz.inspire.thesis.data.model.sport.activity.ActivityEntity;
import cz.inspire.thesis.data.model.sport.sport.InstructorEntity;
import cz.inspire.thesis.data.model.sport.sport.SportEntity;
import cz.inspire.thesis.data.model.sport.sport.SportInstructorEntity;
import cz.inspire.thesis.data.repository.sport.activity.ActivityRepository;
import cz.inspire.thesis.data.service.sport.sport.InstructorService;
import cz.inspire.thesis.data.service.sport.sport.SportInstructorService;
import cz.inspire.thesis.data.service.sport.sport.SportService;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.deltaspike.jpa.api.transaction.Transactional;
import jakarta.inject.Inject;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Those exceptions are created to mimic functionality and implementation of production exceptions
 * Use your imports
 * Plus ApplicationException is additional Exception for update, see setDetails
 */
import cz.inspire.thesis.exceptions.ApplicationException;
import cz.inspire.thesis.exceptions.CreateException;

/**
 * This is import of simple generateGUID functionality created to mimic real functionality
 * In your implementation use your import of guidGenerator
 */
import static cz.inspire.thesis.data.utils.guidGenerator.generateGUID;

@ApplicationScoped
public class ActivityService {

    @Inject
    private ActivityRepository activityRepository;

    @Transactional
    public ActivityEntity create(ActivityDetails details) throws CreateException {
        try {
            ActivityEntity entity = new ActivityEntity();
            if (details.getId() == null) {
                details.setId(generateGUID(entity));
            }
            entity.setId(details.getId());
            entity.setName(details.getName());
            entity.setDescription(details.getDescription());
            entity.setIndex(details.getIndex());
            entity.setIconId(details.getIconId());

            activityRepository.save(entity);
            return entity;
        } catch (Exception e) {
            throw new CreateException("Failed to create Activity entity", e);
        }
    }

    @Transactional
    public void saveEntity(ActivityEntity entity) throws ApplicationException {
        try {
            activityRepository.save(entity);
        } catch (Exception e) {
            throw new ApplicationException("Failed while saving ActivityEntity : ", e);
        }
    }


    public ActivityDetails getDetails(ActivityEntity entity) {
        ActivityDetails details = new ActivityDetails();
        details.setId(entity.getId());
        details.setName(entity.getName());
        details.setDescription(entity.getDescription());
        details.setIndex(entity.getIndex());
        details.setIconId(entity.getIconId());

        if (entity.getInstructors() != null) {
            Set<InstructorDetails> instructors = entity.getInstructors().stream()
                    .map(inst -> new InstructorDetails(inst.getId(), inst.getFirstName(), inst.getLastName(), inst.getColor()))
                    .collect(Collectors.toSet());
            details.setInstructors(instructors);
        }

        return details;
    }


    public Collection<ActivityEntity> findAll() {
        return activityRepository.findAll();
    }

    public Collection<ActivityEntity> findAll(int offset, int count) {
        return activityRepository.findAll(offset, count);
    }

    public Collection<ActivityEntity> findAllByInstructor(String instructorId, int offset, int count) {
        return activityRepository.findAllByInstructor(instructorId, offset, count);
    }

    public Long countActivities() {
        return activityRepository.countActivities();
    }

    public Long countActivitiesByInstructor(String instructorId) {
        return activityRepository.countActivitiesByInstructor(instructorId);
    }


    //Additional finders for functionality

    public ActivityEntity findById(String id) throws ApplicationException {
        return  activityRepository.findOptionalBy(id)
                .orElseThrow(() -> new ApplicationException("Activity entity not found for ID: " + id));
    }
    public Optional<ActivityEntity> findOptionalBy(String id) {
        return activityRepository.findOptionalBy(id);
    }
}
