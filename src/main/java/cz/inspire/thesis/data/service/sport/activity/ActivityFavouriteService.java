package cz.inspire.thesis.data.service.sport.activity;

import cz.inspire.thesis.data.dto.sport.activity.ActivityFavouriteDetails;
import cz.inspire.thesis.data.model.sport.activity.ActivityFavouriteEntity;
import cz.inspire.thesis.data.repository.sport.activity.ActivityFavouriteRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.deltaspike.jpa.api.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;
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
public class ActivityFavouriteService {

    @Inject
    private ActivityFavouriteRepository activityFavouriteRepository;

    @Transactional
    public String create(ActivityFavouriteDetails details) throws CreateException {
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

    /**
     * Discuss If you want to throw any exceptions while using setDetails.
     * In old Bean there is none.
     */
    @Transactional
    public void setDetails(ActivityFavouriteDetails details) throws ApplicationException {
        try {
            ActivityFavouriteEntity entity = activityFavouriteRepository.findOptionalBy(details.getId())
                    .orElseThrow(() -> new ApplicationException("ActivityFavourite entity not found with id : " + details.getId()));
            entity.setZakaznikId(details.getZakaznikId());
            entity.setActivityId(details.getActivityId());
            entity.setPocet(details.getPocet());
            entity.setDatumPosledniZmeny(details.getDatumPosledniZmeny());

            activityFavouriteRepository.save(entity);
        } catch (Exception e) {
            throw new ApplicationException("Failed to update ActivityFavourite entity", e);
        }
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

