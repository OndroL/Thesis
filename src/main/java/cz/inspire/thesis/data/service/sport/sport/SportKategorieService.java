package cz.inspire.thesis.data.service.sport.sport;

import cz.inspire.thesis.data.dto.sport.sport.SportKategorieDetails;
import cz.inspire.thesis.data.dto.sport.sport.SportKategorieLocDetails;
import cz.inspire.thesis.data.model.sport.sport.SportKategorieEntity;
import cz.inspire.thesis.data.model.sport.sport.SportKategorieLocEntity;
import cz.inspire.thesis.data.repository.sport.sport.SportKategorieRepository;
import cz.inspire.thesis.exceptions.ApplicationException;
import cz.inspire.thesis.exceptions.CreateException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.deltaspike.jpa.api.transaction.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static cz.inspire.thesis.data.utils.guidGenerator.generateGUID;

@ApplicationScoped
public class SportKategorieService {

    @Inject
    private SportKategorieRepository sportKategorieRepository;

    @Transactional
    public String create(SportKategorieDetails details) throws CreateException {
        try {
            SportKategorieEntity entity = new SportKategorieEntity();
            if (details.getId() == null) {
                details.setId(generateGUID(entity));
            }
            mapToEntity(entity, details);

            sportKategorieRepository.save(entity);
            postCreate(details, entity);
            return entity.getId();
        } catch (Exception e) {
            throw new CreateException("Failed to create SportKategorie", e);
        }
    }

    @Transactional
    public void postCreate(SportKategorieDetails details, SportKategorieEntity entity) throws CreateException {
        try {
            if (details.getLocaleData() != null) {
                for (SportKategorieLocDetails locDetails : details.getLocaleData().values()) {
                    SportKategorieLocEntity locEntity = new SportKategorieLocEntity();
                    locEntity.setId(generateGUID(locEntity));
                    locEntity.setJazyk(locDetails.getJazyk());
                    locEntity.setNazev(locDetails.getNazev());
                    locEntity.setPopis(locDetails.getPopis());
                }
            }

            if (details.getNadrazenaKategorieId() != null) {
                SportKategorieEntity nadrazenaKategorie = sportKategorieRepository.findOptionalBy(details.getNadrazenaKategorieId())
                        .orElseThrow(() -> new CreateException("Parent category not found"));
                entity.setNadrazenaKategorie(nadrazenaKategorie);
            }

            sportKategorieRepository.save(entity);
        } catch (Exception e) {
            throw new CreateException("Failed to execute post-create logic for SportKategorie", e);
        }
    }

    @Transactional
    public void setDetails(SportKategorieDetails details) throws ApplicationException {
        try {
            SportKategorieEntity entity = sportKategorieRepository.findOptionalBy(details.getId())
                    .orElseThrow(() -> new ApplicationException("SportKategorie entity not found"));

            // Clear old locale data
            entity.getLocaleData().clear();

            if (details.getLocaleData() != null) {
                for (SportKategorieLocDetails locDetails : details.getLocaleData().values()) {
                    SportKategorieLocEntity locEntity = new SportKategorieLocEntity();
                    locEntity.setId(generateGUID(locEntity));
                    locEntity.setJazyk(locDetails.getJazyk());
                    locEntity.setNazev(locDetails.getNazev());
                    locEntity.setPopis(locDetails.getPopis());
                }
            }

            // There is missing functionality in Bean for setting podrazenaKategorie for nadrazenKategorieEntity when creating new Entity with
            // nadrazenaKategorie ... or is possible dead functionality and tree is not reversible, and entities only have parent not child/s
            // nadrazenaKategorie.setPodrazenaKategorie(nadrazenaKategorie.getPodrazenzekategorie().add(entity);

            if (details.getNadrazenaKategorieId() != null) {
                SportKategorieEntity nadrazenaKategorie = sportKategorieRepository.findOptionalBy(details.getNadrazenaKategorieId())
                        .orElseThrow(() -> new ApplicationException("Parent category not found"));
                entity.setNadrazenaKategorie(nadrazenaKategorie);
            } else {
                entity.setNadrazenaKategorie(null);
            }

            sportKategorieRepository.save(entity);
        } catch (Exception e) {
            throw new ApplicationException("Failed to update SportKategorie", e);
        }
    }

    public SportKategorieDetails getDetails(SportKategorieEntity entity) {
        SportKategorieDetails details = new SportKategorieDetails();
        details.setId(entity.getId());
        details.setMultisportFacilityId(entity.getMultiSportFacilityId());
        details.setMultisportServiceUUID(entity.getMultiSportServiceUUID());

        if (entity.getNadrazenaKategorie() != null) {
            details.setNadrazenaKategorieId(entity.getNadrazenaKategorie().getId());
        }

        Map<String, SportKategorieLocDetails> localeData = entity.getLocaleData().stream()
                .collect(Collectors.toMap(SportKategorieLocEntity::getJazyk, loc -> {
                    SportKategorieLocDetails locDetails = new SportKategorieLocDetails();
                    locDetails.setJazyk(loc.getJazyk());
                    locDetails.setNazev(loc.getNazev());
                    locDetails.setPopis(loc.getPopis());
                    return locDetails;
                }));
        details.setLocaleData(localeData);

        return details;
    }

    public List<SportKategorieEntity> findAll() {
        return sportKategorieRepository.findAll();
    }

    public List<SportKategorieEntity> findRoot() {
        return sportKategorieRepository.findRoot();
    }

    public List<SportKategorieEntity> findAllByNadrazenaKategorie(String nadrazenaKategorieId) {
        return sportKategorieRepository.findAllByNadrazenaKategorie(nadrazenaKategorieId);
    }

    public Long count() {
        return sportKategorieRepository.count();
    }

    public Long countRoot() {
        return sportKategorieRepository.countRoot();
    }

    public Long countByNadrazenaKategorie(String kategorieId) {
        return sportKategorieRepository.countByNadrazenaKategorie(kategorieId);
    }

    public Optional<SportKategorieEntity> findOptionalBy(String id) {
        return sportKategorieRepository.findOptionalBy(id);
    }

    private void mapToEntity(SportKategorieEntity entity, SportKategorieDetails details) {
        entity.setMultiSportFacilityId(details.getMultisportFacilityId());
        entity.setMultiSportServiceUUID(details.getMultisportServiceUUID());
    }
}
