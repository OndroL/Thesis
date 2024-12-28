package cz.inspire.thesis.data.service.sport.objekt;

import cz.inspire.thesis.data.dto.sport.objekt.OmezeniRezervaciDetails;
import cz.inspire.thesis.data.model.sport.objekt.OmezeniRezervaciEntity;
import cz.inspire.thesis.data.repository.sport.objekt.OmezeniRezervaciRepository;
import cz.inspire.thesis.exceptions.ApplicationException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.deltaspike.jpa.api.transaction.Transactional;

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
public class OmezeniRezervaciService {

    @Inject
    private OmezeniRezervaciRepository omezeniRezervaciRepository;
    @Transactional
    public OmezeniRezervaciEntity create(OmezeniRezervaciDetails details) throws CreateException {
        try {
            OmezeniRezervaciEntity entity = new OmezeniRezervaciEntity();
            if (details.getObjektId() == null) {
                details.setObjektId(generateGUID(entity));
            }
            entity.setObjektId(details.getObjektId());
            entity.setOmezeni(details.getOmezeni());

            omezeniRezervaciRepository.save(entity);

            return entity;
        } catch (Exception e) {
            throw new CreateException("Failed to create OmezeniRezervaci entity", e);
        }
    }

    @Transactional
    public void save(OmezeniRezervaciEntity entity) throws ApplicationException {
        try {
            omezeniRezervaciRepository.save(entity);
        } catch (Exception e) {
            throw new ApplicationException("Failed to save OmezeniRezervaci entity", e);
        }
    }

    public OmezeniRezervaciDetails getDetails(OmezeniRezervaciEntity entity) {
        return new OmezeniRezervaciDetails(
                entity.getObjektId(),
                entity.getOmezeni()
        );
    }

    public Collection<OmezeniRezervaciEntity> findAll() {
        return omezeniRezervaciRepository.findAll();
    }
}
