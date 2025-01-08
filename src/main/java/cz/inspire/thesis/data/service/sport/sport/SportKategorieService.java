package cz.inspire.thesis.data.service.sport.sport;

import cz.inspire.thesis.data.dto.sport.sport.SportKategorieDetails;
import cz.inspire.thesis.data.dto.sport.sport.SportKategorieLocDetails;
import cz.inspire.thesis.data.model.sport.sport.SportInstructorEntity;
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
    public SportKategorieEntity create(SportKategorieDetails details) throws CreateException {
        try {
            SportKategorieEntity entity = new SportKategorieEntity();
            if (details.getId() == null) {
                details.setId(generateGUID(entity));
            }
            mapToEntity(entity, details);

            sportKategorieRepository.save(entity);
            return entity;
        } catch (Exception e) {
            throw new CreateException("Failed to create SportKategorie", e);
        }
    }

    @Transactional
    public void save(SportKategorieEntity entity) throws ApplicationException {
        try {
            sportKategorieRepository.save(entity);
        } catch (Exception e) {
            throw new ApplicationException("Failed while trying to save SportKategorieEntity ", e);
        }
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
