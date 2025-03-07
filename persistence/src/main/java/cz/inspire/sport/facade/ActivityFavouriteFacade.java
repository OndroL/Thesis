package cz.inspire.sport.facade;

import cz.inspire.exception.SystemException;
import cz.inspire.sport.dto.ActivityFavouriteDto;
import cz.inspire.sport.entity.ActivityFavouriteEntity;
import cz.inspire.sport.mapper.ActivityFavouriteMapper;
import cz.inspire.sport.service.ActivityFavouriteService;
import jakarta.ejb.CreateException;
import jakarta.ejb.FinderException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ActivityFavouriteFacade {

    @Inject
    ActivityFavouriteService activityFavouriteService;
    @Inject
    ActivityFavouriteMapper activityFavouriteMapper;

    public String create(ActivityFavouriteDto dto) throws CreateException {
        try {

            ActivityFavouriteEntity entity = activityFavouriteMapper.toEntity(dto);

            activityFavouriteService.create(entity);

            return entity.getId();
        } catch (Exception e) {
            throw new CreateException("Failed to create ActivityFavourite entity : " + e);
        }
    }

    public void update(ActivityFavouriteDto dto) throws SystemException {
        activityFavouriteService.update(activityFavouriteMapper.toEntity(dto));
    }

    public ActivityFavouriteDto mapToDto(ActivityFavouriteEntity entity) {
        return activityFavouriteMapper.toDto(entity);
    }


    public List<ActivityFavouriteDto> findByZakaznik(String zakaznikId, int limit, int offset) throws FinderException {
        return activityFavouriteService.findByZakaznik(zakaznikId, limit, offset).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public ActivityFavouriteDto findByZakaznikAktivita(String zakaznikId, String activityId) throws FinderException {
        return mapToDto(activityFavouriteService.findByZakaznikAktivita(zakaznikId, activityId));
    }



}
