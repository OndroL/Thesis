package cz.inspire.thesis.data.service.sport.activity;

import cz.inspire.thesis.data.dto.sport.activity.ActivityDetails;
import cz.inspire.thesis.data.dto.sport.sport.InstructorDetails;
import cz.inspire.thesis.data.dto.sport.sport.SportDetails;
import cz.inspire.thesis.data.model.sport.activity.ActivityEntity;
import cz.inspire.thesis.data.model.sport.sport.InstructorEntity;
import cz.inspire.thesis.data.model.sport.sport.SportEntity;
import cz.inspire.thesis.data.model.sport.sport.SportInstructorEntity;
import cz.inspire.thesis.data.repository.sport.activity.ActivityRepository;
import cz.inspire.thesis.data.repository.sport.sport.InstructorRepository;
import cz.inspire.thesis.data.repository.sport.sport.SportInstructorRepository;
import cz.inspire.thesis.data.repository.sport.sport.SportRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.deltaspike.jpa.api.transaction.Transactional;
import jakarta.inject.Inject;

import java.util.Collection;
import java.util.HashSet;
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
    @Inject
    private InstructorRepository instructorRepository;
    @Inject
    private SportInstructorRepository sportInstructorRepository;
    @Inject
    private SportRepository sportRepository;

    @Transactional
    public String create(ActivityDetails details) throws CreateException {
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
            postCreate(details, entity);
            return entity.getId();
        } catch (Exception e) {
            throw new CreateException("Failed to create Activity entity", e);
        }
    }

    @Transactional
    public void postCreate(ActivityDetails details, ActivityEntity entity) throws CreateException {
        Set<InstructorDetails> instructors = (Set<InstructorDetails>) details.getInstructors();
        if (instructors != null && !instructors.isEmpty()) {
            try {
                Collection<InstructorEntity> instructorSet = new HashSet<InstructorEntity>();
                for (InstructorDetails instructorDetails : instructors) {
                    InstructorEntity instructor = instructorRepository.findOptionalBy(instructorDetails.getId())
                            .orElseThrow(() -> new ApplicationException("Instructor entity not found for ID: " + instructorDetails.getId()));
                    instructorSet.add(instructor);
                }
                entity.setInstructors(instructorSet);
                activityRepository.save(entity);
            } catch (Exception ex) {
                throw new CreateException("Failed to associate instructors with activity: " + ex.getMessage(), ex);
            }
        }
    }

    @Transactional
    public void setDetails(ActivityDetails details) throws ApplicationException {
        try {
            // Find the activity entity by ID or throw an exception if not found
            ActivityEntity entity = activityRepository.findOptionalBy(details.getId())
                    .orElseThrow(() -> new ApplicationException("Activity entity not found for ID: " + details.getId()));

            // Update basic fields
            entity.setName(details.getName());
            entity.setDescription(details.getDescription());
            entity.setIndex(details.getIndex());
            entity.setIconId(details.getIconId());

            // Fetch current instructor IDs from the entity
            Set<String> oldInstructorIds = entity.getInstructors().stream()
                    .map(InstructorEntity::getId)
                    .collect(Collectors.toSet());

            // Clear existing instructors
            entity.getInstructors().clear();

            // Add new instructors if provided
            if (details.getInstructors() != null) {
                Set<InstructorEntity> instructors = details.getInstructors().stream()
                        .map(instructorDetails -> {
                            try {
                                return instructorRepository.findOptionalBy(instructorDetails.getId())
                                        .orElseThrow(() -> new ApplicationException("Instructor not found for ID: " + instructorDetails.getId()));
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .collect(Collectors.toSet());

                entity.setInstructors(instructors);
            }

            // Save the updated entity
            activityRepository.save(entity);

            // Update related sport-instructor entities
            updateSportInstructor(details, oldInstructorIds);
        } catch (Exception ex) {
            throw new ApplicationException("Failed to update Activity entity: " + ex.getMessage(), ex);
        }
    }

    @Transactional
    private void updateSportInstructor(ActivityDetails activityDetails, Set<String> oldInstructorIds) throws ApplicationException {
        try {
            // Extract new instructor IDs from ActivityDetails
            Set<String> newInstructorIds = activityDetails.getInstructors() != null
                    ? activityDetails.getInstructors().stream()
                    .map(InstructorDetails::getId)
                    .collect(Collectors.toSet())
                    : new HashSet<>();

            // Determine instructors to delete (oldInstructorIds - newInstructorIds)
            oldInstructorIds.removeAll(newInstructorIds);

            // Iterate through the sports associated with the activity
            for (SportDetails sportDetails : activityDetails.getSports()) {
                // Fetch the corresponding SportEntity
                SportEntity sportEntity = sportRepository.findOptionalBy(sportDetails.getId())
                        .orElseThrow(() -> new ApplicationException("Sport not found for ID: " + sportDetails.getId()));

                // Iterate through sport-instructor mappings
                for (SportInstructorEntity sportInstructor : sportEntity.getSportInstructors()) {
                    if (sportInstructor.getInstructor() != null
                            && oldInstructorIds.contains(sportInstructor.getInstructor().getId())) {
                        // Mark sport-instructor entity as deleted
                        sportInstructor.setDeleted(true);
                        sportInstructorRepository.save(sportInstructor); // Save the updated entity
                    }
                }

                // Ensure the sport is not left without any instructors if necessary
                sportEntity.checkSportWithoutInstructor(); // Implement logic for this as needed
            }
        } catch (Exception ex) {
            throw new ApplicationException("Failed to update sport-instructor relationships: " + ex.getMessage(), ex);
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

    public Collection<ActivityDetails> findAll() {
        return activityRepository.findAll().stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    public Collection<ActivityDetails> findAll(int offset, int count) {
        return activityRepository.findAll(offset, count).stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    public Collection<ActivityDetails> findAllByInstructor(String instructorId, int offset, int count) {
        return activityRepository.findAllByInstructor(instructorId, offset, count).stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    public Long countActivities() {
        return activityRepository.countActivities();
    }

    public Long countActivitiesByInstructor(String instructorId) {
        return activityRepository.countActivitiesByInstructor(instructorId);
    }
}
