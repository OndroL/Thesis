package cz.inspire.thesis.data.service.sport.activity;

import cz.inspire.thesis.data.dto.sport.activity.ActivityFavouriteDetails;
import cz.inspire.thesis.data.model.sport.activity.ActivityFavouriteEntity;
import cz.inspire.thesis.data.repository.sport.activity.ActivityFavouriteRepository;
import cz.inspire.thesis.exceptions.CreateException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import static cz.inspire.thesis.data.utils.guidGenerator.generateGUID;

@ApplicationScoped
public class ActivityFavouriteService {

    @Inject
    private ActivityFavouriteRepository activityFavouriteRepository;

    public String ejbCreate(ActivityFavouriteDetails details) throws CreateException {
        try {
            ActivityFavouriteEntity entity = new ActivityFavouriteEntity();
            if (details.getId() == null) {
                details.setId(generateGUID(entity));
            }
            entity.setId(details.getId());
            entity.setZakaznikId(details.getZakaznikId());
            entity.setActivityId(details.getActivityId());
            entity.setPocet(details.getPocet());
            entity.setDatumPosledniZmeny(details.getDatumPosledniZmeny());

            activityFavouriteRepository.save(entity);

            return entity.getId();
        } catch (Exception e) {
            throw new CreateException("Failed to create ActivityFavourite entity", e);
        }
    }

    public ActivityFavouriteDetails getDetails(ActivityFavouriteEntity entity) {
        return new ActivityFavouriteDetails(
                entity.getId(),
                entity.getZakaznikId(),
                entity.getActivityId(),
                entity.getPocet(),
                entity.getDatumPosledniZmeny()
        );
    }

    public Collection<ActivityFavouriteDetails> findByZakaznik(String zakaznikId, int limit, int offset) {
        return activityFavouriteRepository.findByZakaznik(zakaznikId, limit, offset).stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    public Optional<ActivityFavouriteDetails> findByZakaznikAktivita(String zakaznikId, String activityId) {
        return activityFavouriteRepository.findByZakaznikAktivita(zakaznikId, activityId).map(this::getDetails);
    }
}

