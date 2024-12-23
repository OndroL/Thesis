package cz.inspire.thesis.data.service.sport.sport;

import cz.inspire.thesis.data.dto.sport.sport.SportKategorieLocDetails;
import cz.inspire.thesis.data.model.sport.sport.SportKategorieLocEntity;
import cz.inspire.thesis.data.model.sport.sport.SportKategorieEntity;
import cz.inspire.thesis.data.repository.sport.sport.SportKategorieLocRepository;
import cz.inspire.thesis.exceptions.CreateException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.deltaspike.jpa.api.transaction.Transactional;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import static cz.inspire.thesis.data.utils.guidGenerator.generateGUID;

@ApplicationScoped
public class SportKategorieLocService {

    @Inject
    private SportKategorieLocRepository sportKategorieLocRepository;

    @Transactional
    public String create(SportKategorieLocDetails details, SportKategorieEntity sportKategorie) throws CreateException {
        try {
            SportKategorieLocEntity entity = new SportKategorieLocEntity();
            if (details.getId() == null) {
                details.setId(generateGUID(entity));
            }
            entity.setId(details.getId());
            entity.setJazyk(details.getJazyk());
            entity.setNazev(details.getNazev());
            entity.setPopis(details.getPopis());

            sportKategorieLocRepository.save(entity);

            return entity.getId();
        } catch (Exception e) {
            throw new CreateException("Failed to create SportKategorieLoc entity", e);
        }
    }

    public SportKategorieLocDetails getDetails(SportKategorieLocEntity entity) {
        return new SportKategorieLocDetails(
                entity.getId(),
                entity.getJazyk(),
                entity.getNazev(),
                entity.getPopis()
        );
    }

    public Collection<SportKategorieLocEntity> findAll() {
        return sportKategorieLocRepository.findAll();
    }

    public Optional<SportKategorieLocEntity> findById(String id) {
        return sportKategorieLocRepository.findOptionalBy(id);
    }
}
