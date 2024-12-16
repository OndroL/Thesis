package cz.inspire.thesis.data.service.sport.activity;

import cz.inspire.thesis.data.dto.sport.activity.ActivityDetails;
import cz.inspire.thesis.data.model.sport.activity.ActivityEntity;
import cz.inspire.thesis.data.model.sport.sport.InstructorEntity;
import cz.inspire.thesis.data.dto.sport.sport.InstructorDetails;
import cz.inspire.thesis.data.repository.sport.activity.ActivityRepository;
import cz.inspire.thesis.exceptions.ApplicationException;
import cz.inspire.thesis.exceptions.CreateException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class ActivityService {

    @Inject
    private ActivityRepository activityRepository;

    public String ejbCreate(ActivityDetails details) throws CreateException {
        try {
            ActivityEntity entity = new ActivityEntity();
            entity.setId(details.getId());
            entity.setName(details.getName());
            entity.setDescription(details.getDescription());
            entity.setIndex(details.getIndex());
            entity.setIconId(details.getIconId());

//            if (details.getInstructors() != null) {
//                Set<InstructorEntity> instructors = details.getInstructors().stream()
//                        .map(inst -> {
//                            InstructorEntity instructor = new InstructorEntity();
//                            instructor.setId(inst.getId());
//                            return instructor;
//                        })
//                        .collect(Collectors.toSet());
//                entity.setInstructors(instructors);
//            }

            activityRepository.save(entity);
            return entity.getId();
        } catch (Exception e) {
            throw new CreateException("Failed to create Activity entity", e);
        }
    }

    public void setDetails(ActivityDetails details) throws ApplicationException {
        try {
            ActivityEntity entity = activityRepository.findOptionalBy(details.getId())
                    .orElseThrow(() -> new ApplicationException("Activity entity not found"));

            entity.setName(details.getName());
            entity.setDescription(details.getDescription());
            entity.setIndex(details.getIndex());
            entity.setIconId(details.getIconId());

//            if (details.getInstructors() != null) {
//                Set<InstructorEntity> instructors = details.getInstructors().stream()
//                        .map(inst -> {
//                            InstructorEntity instructor = new InstructorEntity();
//                            instructor.setId(inst.getId());
//                            return instructor;
//                        })
//                        .collect(Collectors.toSet());
//                entity.setInstructors(instructors);
//            }

            activityRepository.save(entity);
        } catch (Exception e) {
            throw new ApplicationException("Failed to update Activity entity", e);
        }
    }

    public ActivityDetails getDetails(ActivityEntity entity) {
        ActivityDetails details = new ActivityDetails();
        details.setId(entity.getId());
        details.setName(entity.getName());
        details.setDescription(entity.getDescription());
        details.setIndex(entity.getIndex());
        details.setIconId(entity.getIconId());

//        if (entity.getInstructors() != null) {
//            Set<InstructorDetails> instructors = entity.getInstructors().stream()
//                    .map(inst -> new InstructorDetails(inst.getId(), inst.getFirstName(), inst.getLastName(), inst.getColor()))
//                    .collect(Collectors.toSet());
//            details.setInstructors(instructors);
//        }

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

    public Long ejbHomeCountActivities() {
        return activityRepository.countActivities();
    }

    public Long ejbHomeCountActivitiesByInstructor(String instructorId) {
        return activityRepository.countActivitiesByInstructor(instructorId);
    }
}
