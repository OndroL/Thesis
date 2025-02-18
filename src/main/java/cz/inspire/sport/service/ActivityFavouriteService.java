package cz.inspire.sport.service;

import cz.inspire.common.service.BaseService;
import cz.inspire.sport.entity.ActivityFavouriteEntity;
import cz.inspire.sport.repository.ActivityFavouriteRepository;
import jakarta.data.Limit;
import jakarta.ejb.FinderException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.NoResultException;

import java.util.List;

import static cz.inspire.common.utils.ExceptionHandler.wrapDBException;

@ApplicationScoped
public class ActivityFavouriteService extends BaseService<ActivityFavouriteEntity, String, ActivityFavouriteRepository> {
    public ActivityFavouriteService(){
    }

    @Inject
    public ActivityFavouriteService (ActivityFavouriteRepository repository) {super(repository);}

    public List<ActivityFavouriteEntity> findByZakaznik(String zakaznikId, int offset, int count) throws FinderException {
        return wrapDBException(
                () -> repository.findByZakaznik(zakaznikId, Limit.range(offset + 1, count)),
                "Error retrieving ActivityFavouriteEntity records for zakaznikId = " + zakaznikId +
                        " with pagination (offset + 1 = " + offset + ", count = " + count + ")"
        );
    }

    public ActivityFavouriteEntity findByZakaznikAktivita(String zakaznikId, String activityId) throws FinderException {
        return wrapDBException(() ->
                        repository.findByZakaznikAktivita(zakaznikId, activityId)
                                .orElseThrow(() -> new NoResultException("No ActivityFavouriteEntity for zakaznikId = "
                                        + zakaznikId + " and activityId = " + activityId)),
                "Error retrieving ActivityFavouriteEntity record for zakaznikId = " + zakaznikId +
                        " and activityId = " + activityId
        );
    }



}
