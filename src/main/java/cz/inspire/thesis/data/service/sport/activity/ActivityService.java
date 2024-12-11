package cz.inspire.thesis.data.service.sport.activity;

import cz.inspire.thesis.data.dto.sport.ActivityDetails;
import cz.inspire.thesis.data.model.sport.activity.ActivityEntity;
import cz.inspire.thesis.data.model.sport.sport.InstructorEntity;
import cz.inspire.thesis.data.repository.sport.activity.ActivityRepository;
import cz.inspire.thesis.exceptions.CreateException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static cz.inspire.thesis.data.utils.guidGenerator.generateGUID;

@ApplicationScoped
public class ActivityService {

    @Inject
    private ActivityRepository activityRepository;

    @Inject
    private InstructorService instructorService;

    @Transactional
    public String ejbCreate(ActivityDetails activity) throws CreateException {
        try {
            ActivityEntity entity = new ActivityEntity();
            String id = activity.getId();
            if (id == null) {
                id = generateGUID(entity);
            }
            entity.setId(id);
            entity.setName(activity.getName());
            entity.setDescription(activity.getDescription());
            entity.setIndex(activity.getIndex());
            entity.setIconId(activity.getIconId());

            if (activity.getInstructors() != null) {
                Set<InstructorEntity> instructors = activity.getInstructors().stream()
                        .map(details -> instructorService.findById(details.getId()))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toSet());
                entity.setInstructors(instructors);
            }

            activityRepository.save(entity);
            return entity.getId();
        } catch (Exception e) {
            throw new CreateException("Activity creation failed: " + e.getMessage());
        }
    }

    @Transactional
    public void ejbPostCreate(ActivityDetails activity) throws CreateException {
        try {
            Optional<ActivityEntity> optionalEntity = activityRepository.findById(activity.getId());
            if (optionalEntity.isPresent()) {
                ActivityEntity entity = optionalEntity.get();

                if (activity.getInstructors() != null) {
                    Set<InstructorEntity> instructors = activity.getInstructors().stream()
                            .map(details -> instructorService.findById(details.getId()))
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .collect(Collectors.toSet());
                    entity.setInstructors(instructors);
                    activityRepository.save(entity);
                }
            } else {
                throw new CreateException("Post-create failed: Activity not found");
            }
        } catch (Exception e) {
            throw new CreateException("Post-create error: " + e.getMessage());
        }
    }

    public ActivityDetails getDetails(String id) {
        try {
            Optional<ActivityEntity> optionalEntity = activityRepository.findById(id);
            if (optionalEntity.isEmpty()) {
            }

            ActivityEntity entity = optionalEntity.get();
            ActivityDetails details = new ActivityDetails();
            details.setId(entity.getId());
            details.setName(entity.getName());
            details.setDescription(entity.getDescription());
            details.setIndex(entity.getIndex());
            details.setIconId(entity.getIconId());

            if (entity.getInstructors() != null) {
                details.setInstructors(entity.getInstructors().stream()
                        .map(instructor -> {
                            ActivityDetails.InstructorDetails instructorDetails = new ActivityDetails.InstructorDetails();
                            instructorDetails.setId(instructor.getId());
                            instructorDetails.setFirstName(instructor.getFirstName());
                            instructorDetails.setLastName(instructor.getLastName());
                            return instructorDetails;
                        }).collect(Collectors.toSet()));
            }

            return details;
        } catch (Exception e) {
            throw new FinderException("Error retrieving details: " + e.getMessage());
        }
    }

    @Transactional
    public void setDetails(ActivityDetails activity) throws EJBException {
        try {
            Optional<ActivityEntity> optionalEntity = activityRepository.findById(activity.getId());
            if (optionalEntity.isPresent()) {
                ActivityEntity entity = optionalEntity.get();
                entity.setName(activity.getName());
                entity.setDescription(activity.getDescription());
                entity.setIndex(activity.getIndex());
                entity.setIconId(activity.getIconId());

                if (activity.getInstructors() != null) {
                    Set<InstructorEntity> instructors = activity.getInstructors().stream()
                            .map(details -> instructorService.findById(details.getId()))
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .collect(Collectors.toSet());
                    entity.setInstructors(instructors);
                }

                activityRepository.save(entity);
            } else {
                throw new EJBException("Activity entity not found for setDetails");
            }
        } catch (Exception e) {
            throw new EJBException("Error setting Activity details: " + e.getMessage());
        }
    }

    public long countActivities() throws FinderException {
        try {
            return activityRepository.count();
        } catch (Exception e) {
            throw new FinderException("Error counting Activities: " + e.getMessage());
        }
    }

    public long countActivitiesByInstructor(String instructorId) throws FinderException {
        try {
            return activityRepository.countByInstructors_Id(instructorId);
        } catch (Exception e) {
            throw new FinderException("Error counting Activities by Instructor: " + e.getMessage());
        }
    }
}