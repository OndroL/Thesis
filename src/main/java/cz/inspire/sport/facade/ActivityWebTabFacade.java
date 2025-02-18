package cz.inspire.sport.facade;

import cz.inspire.sport.dto.ActivityWebTabDto;
import cz.inspire.sport.entity.ActivityWebTabEntity;
import cz.inspire.sport.mapper.ActivityWebTabMapper;
import cz.inspire.sport.service.ActivityWebTabService;
import jakarta.ejb.CreateException;
import jakarta.ejb.FinderException;
import jakarta.ejb.RemoveException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ActivityWebTabFacade {

    @Inject
    ActivityWebTabService activityWebTabService;
    @Inject
    ActivityWebTabMapper activityWebTabMapper;


    public String create(ActivityWebTabDto dto) throws CreateException {
        try {
            ActivityWebTabEntity entity = activityWebTabMapper.toEntity(dto);

            activityWebTabService.create(entity);

            return entity.getId();
        } catch (Exception e) {
            throw new CreateException("Failed to create ActivityWebTab entity : " + e);
        }
    }

    public void delete(ActivityWebTabEntity entity) throws RemoveException {
        activityWebTabService.delete(entity);
    }

    public ActivityWebTabDto mapToDto (ActivityWebTabEntity entity) {
        return activityWebTabMapper.toDto(entity);
    }


    public List<ActivityWebTabDto> findAll() throws FinderException {
        return activityWebTabService.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<ActivityWebTabDto> findBySport(String sportId) throws FinderException {
        return activityWebTabService.findBySport(sportId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<ActivityWebTabDto> findByActivity(String activityId) throws FinderException {
        return activityWebTabService.findByActivity(activityId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<ActivityWebTabDto> findByObject(String objectId) throws FinderException {
        return activityWebTabService.findByObject(objectId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
}
