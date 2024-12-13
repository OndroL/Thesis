package cz.inspire.thesis.data.service.sport.objekt;

import cz.inspire.thesis.data.dto.sport.objekt.ArealLocDetails;
import cz.inspire.thesis.data.model.sport.objekt.ArealLocEntity;
import cz.inspire.thesis.data.repository.sport.objekt.ArealLocRepository;
import cz.inspire.thesis.exceptions.CreateException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Collection;
import java.util.stream.Collectors;

import static cz.inspire.thesis.data.utils.guidGenerator.generateGUID;

@ApplicationScoped
public class ArealLocService {

    @Inject
    private ArealLocRepository arealLocRepository;

    public String ejbCreate(ArealLocDetails details) throws CreateException {
        try {
            ArealLocEntity entity = new ArealLocEntity();
            if (details.getId() == null) {
                details.setId(generateGUID(entity));
            }
            entity.setId(details.getId());
            entity.setJazyk(details.getJazyk());
            entity.setNazev(details.getNazev());
            entity.setPopis(details.getPopis());

            arealLocRepository.save(entity);

            return entity.getId();
        } catch (Exception e) {
            throw new CreateException("Failed to create ArealLoc entity", e);
        }
    }

    public ArealLocDetails getDetails(ArealLocEntity entity) {
        return new ArealLocDetails(
                entity.getId(),
                entity.getJazyk(),
                entity.getNazev(),
                entity.getPopis()
        );
    }

    public Collection<ArealLocDetails> findAll() {
        return arealLocRepository.findAll().stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    public ArealLocDetails findById(String id) {
        return arealLocRepository.findOptionalBy(id)
                .map(this::getDetails)
                .orElseThrow(() -> new IllegalArgumentException("ArealLoc not found with id: " + id));
    }
}