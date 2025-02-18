package cz.inspire.sport.facade;

import cz.inspire.sport.dto.ActivityDto;
import cz.inspire.sport.dto.InstructorDto;
import cz.inspire.sport.entity.ActivityEntity;
import cz.inspire.sport.entity.InstructorEntity;
import cz.inspire.sport.mapper.ActivityMapper;
import cz.inspire.sport.service.ActivityService;
import cz.inspire.sport.service.InstructorService;
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
    InstructorService instructorService;

    public ActivityDto create(ActivityDto dto) throws CreateException {
        try {
            ActivityEntity entity = activityMapper.toEntity(dto);

            activityService.create(entity);

            //postCreate
            setInstructors(entity, dto);

            return mapToDto(entity);
        } catch (Exception e) {
            throw new CreateException("Failed to create Activity entity : " + e);
        }
    }

    private void setInstructors(ActivityEntity entity, ActivityDto dto) throws CreateException {
        List<InstructorDto> instructors = dto.getInstructors();
        if (instructors != null && !instructors.isEmpty()) {
            try {
                Set<InstructorEntity> instructorSet = new HashSet<InstructorEntity>();
                for (InstructorDto instructorDetails : instructors) {
                    InstructorEntity instructor = instructorService.findById(instructorDetails.getId())
                            .orElseThrow(() -> new CreateException("Instructor entity not found for ID: " + instructorDetails.getId()));
                    instructorSet.add(instructor);
                }
                entity.setInstructors(instructorSet.stream().toList());
                activityService.update(entity);
            } catch (Exception ex) {
                throw new CreateException("Failed to associate instructors with activity id : " + entity.getId() + " : " + ex.getMessage() + ex);
            }
        }
    }

    public void update(ActivityDto dto) throws Exception {
        try {


        } catch (Exception e) {
            throw new Exception("Failed to update ActivityEntity with id : " + dto.getId(), e);
        }
    }




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
