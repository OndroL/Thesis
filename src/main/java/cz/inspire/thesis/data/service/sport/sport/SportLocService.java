package cz.inspire.thesis.data.service.sport.sport;

import cz.inspire.thesis.data.dto.sport.sport.SportLocDetails;
import cz.inspire.thesis.data.model.sport.sport.SportLocEntity;
import cz.inspire.thesis.data.repository.sport.sport.SportLocRepository;
import cz.inspire.thesis.exceptions.CreateException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import static cz.inspire.thesis.data.utils.guidGenerator.generateGUID;

@ApplicationScoped
public class SportLocService {

    @Inject
    private SportLocRepository sportLocRepository;

    public String ejbCreate(SportLocDetails details) throws CreateException {
        try {
            SportLocEntity entity = new SportLocEntity();
            if (details.getId() == null) {
                details.setId(generateGUID(entity));
            }
            entity.setId(details.getId());
            entity.setJazyk(details.getJazyk());
            entity.setNazev(details.getNazev());
            entity.setPopis(details.getPopis());

            sportLocRepository.save(entity);

            return entity.getId();
        } catch (Exception e) {
            throw new CreateException("Failed to create SportLoc entity", e);
        }
    }

    public SportLocDetails getDetails(SportLocEntity entity) {
        return new SportLocDetails(entity.getId(), entity.getJazyk(), entity.getNazev(), entity.getPopis());
    }

    public Collection<SportLocDetails> findAll() {
        return sportLocRepository.findAll().stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    public Optional<SportLocDetails> findById(String id) {
        return sportLocRepository.findOptionalBy(id).map(this::getDetails);
    }
}
