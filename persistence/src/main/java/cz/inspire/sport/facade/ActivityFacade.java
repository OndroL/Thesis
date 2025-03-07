package cz.inspire.sport.facade;

import cz.inspire.exception.ApplicationException;
import cz.inspire.exception.SystemException;
import cz.inspire.sport.dto.ActivityDto;
import cz.inspire.sport.entity.ActivityEntity;
import cz.inspire.sport.entity.ActivityWebTabEntity;
import cz.inspire.sport.entity.InstructorEntity;
import cz.inspire.sport.entity.SportEntity;
import cz.inspire.sport.entity.SportInstructorEntity;
import cz.inspire.sport.mapper.ActivityMapper;
import cz.inspire.sport.service.ActivityService;
import cz.inspire.sport.service.ActivityWebTabService;
import cz.inspire.sport.service.SportInstructorService;
import cz.inspire.sport.service.SportService;
import jakarta.ejb.CreateException;
import jakarta.ejb.FinderException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class ActivityFacade {
    @Inject
    ActivityService activityService;
    @Inject
    ActivityMapper activityMapper;
    @Inject
    ActivityWebTabService activityWebTabService;
    @Inject
    SportInstructorService sportInstructorService;
    @Inject
    SportService sportService;

    public ActivityDto create(ActivityDto dto) throws CreateException {
        try {
            ActivityEntity entity = activityMapper.toEntity(dto);

            activityService.create(entity);

            return mapToDto(entity);
        } catch (Exception e) {
            throw new CreateException("Failed to create Activity entity : " + e);
        }
    }

    // Call from ActivityController method - updateActivity(ActivityDetails details) -> Set<LocalInstructor> instructors = ActivityFacade.update(dto);
    public Set<InstructorEntity> update(ActivityDto dto) throws Exception {
        try {
            ActivityEntity entity = activityService.findByPrimaryKey(dto.getId());

            Set<InstructorEntity> instructors = new HashSet<>(entity.getInstructors());
            Set<InstructorEntity> oldInstructors = new HashSet<>(entity.getInstructors());

            entity = activityService.update(activityMapper.toEntity(dto));


            if (entity.getInstructors() != null) {
                entity.getInstructors().forEach(oldInstructors::remove);
                instructors.addAll(entity.getInstructors());
            }

            updateSportInstructor(entity, oldInstructors);


            return instructors;

        } catch (FinderException e) {
            throw new FinderException("Failed to update ActivityEntity. " + e);
        }catch (Exception e) {
            throw new Exception("Failed to update ActivityEntity with id : " + dto.getId(), e);
        }
    }


    private void updateSportInstructor(ActivityEntity entity, Set<InstructorEntity> oldInstructors)
            throws SystemException, FinderException {
        for (SportEntity sport : entity.getSports()) {
            for (SportInstructorEntity sportInstructor :  sport.getSportInstructors()) {
                if (sportInstructor.getInstructor() != null
                        && oldInstructors.contains(sportInstructor.getInstructor())) {
                    sportInstructor.setDeleted(true);
                    sportInstructorService.update(sportInstructor);
                }
            }
            sportInstructorService.checkSportWithoutInstructor(sport);
        }
    }

    public void delete(String id) throws Exception {
        try {
            ActivityEntity entity = activityService.findByPrimaryKey(id);
            List<SportEntity> sports = entity.getSports();

            if (sports != null && !sports.isEmpty()) {
                throw new ApplicationException("ActivityEntity [" + id + "] cannot be removed. "
                        + "It is associated with " + sports.size() + " sports."
                );
            }

            activityService.delete(entity);

            List<ActivityWebTabEntity> webTabEntities = activityWebTabService.findByActivity(id);
            for (ActivityWebTabEntity webTab : webTabEntities) {
                activityWebTabService.delete(webTab);
            }

        } catch (FinderException e) {
            throw new FinderException("Failed to delete ActivityEntity. " + e);
        } catch (Exception e) {
            throw new Exception("Failed to update ActivityEntity with id : " + id, e);
        }
    }

    // ActivityDetails getActivity(String id) -> use return ActivityFacade.findById(String id) in tryCatch block
    // same for rest of finders/getters


    public ActivityDto mapToDto(ActivityEntity entity) {
        return activityMapper.toDto(entity);
    }

    public ActivityDto findById(String id) throws FinderException {
        return mapToDto(activityService.findByPrimaryKey(id));
    }

    public List<ActivityDto> findAll() throws FinderException {
        return activityService.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<ActivityDto> findAll(int offset, int count) throws FinderException {
        return activityService.findAll(offset, count).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<ActivityDto> findAllByInstructor(String instructorId, int offset, int count) throws FinderException {
        return activityService.findAllByInstructor(instructorId, offset, count).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public Long countActivities() throws FinderException {
        return activityService.countActivities();
    }

    public Long countActivitiesByInstructor(String instructorId) throws FinderException {
        return activityService.countActivitiesByInstructor(instructorId);
    }
}
