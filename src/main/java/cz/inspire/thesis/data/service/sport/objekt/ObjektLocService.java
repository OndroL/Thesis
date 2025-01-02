package cz.inspire.thesis.data.service.sport.objekt;

import cz.inspire.thesis.data.dto.sport.objekt.ObjektLocDetails;
import cz.inspire.thesis.data.model.sport.objekt.ObjektLocEntity;
import cz.inspire.thesis.data.repository.sport.objekt.ObjektLocRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.deltaspike.jpa.api.transaction.Transactional;
import jakarta.inject.Inject;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Those exceptions are created to mimic functionality and implementation of production exceptions
 * Use your imports
 */
import cz.inspire.thesis.exceptions.CreateException;

/**
 * This is import of simple generateGUID functionality created to mimic real functionality
 * In your implementation use your import of guidGenerator
 */
import static cz.inspire.thesis.data.utils.guidGenerator.generateGUID;

@ApplicationScoped
public class ObjektLocService {

    @Inject
    private ObjektLocRepository objektLocRepository;

    @Transactional
    public String create(ObjektLocDetails details) throws CreateException {
        try {
            ObjektLocEntity entity = new ObjektLocEntity();
            if (details.getId() == null) {
                details.setId(generateGUID(entity));
            }
            entity.setId(details.getId());
            entity.setJazyk(details.getJazyk());
            entity.setNazev(details.getNazev());
            entity.setPopis(details.getPopis());
            entity.setZkracenyNazev(details.getZkracenyNazev());

            objektLocRepository.save(entity);

            return entity.getId();
        } catch (Exception e) {
            throw new CreateException("Failed to create ObjektLoc entity", e);
        }
    }

    public ObjektLocDetails getDetails(ObjektLocEntity entity) {
        return new ObjektLocDetails(
                entity.getId(),
                entity.getJazyk(),
                entity.getNazev(),
                entity.getPopis(),
                entity.getZkracenyNazev()
        );
    }

    /**
     * These two finders are here only for test purposes, they were not in old bean
     */
    public Collection<ObjektLocDetails> findAll() {
        return objektLocRepository.findAll().stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    public ObjektLocDetails findById(String id) {
        return objektLocRepository.findOptionalBy(id)
                .map(this::getDetails)
                .orElseThrow(() -> new IllegalArgumentException("ObjektLoc not found with id: " + id));
    }
}
