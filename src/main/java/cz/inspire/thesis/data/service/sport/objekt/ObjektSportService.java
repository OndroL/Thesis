package cz.inspire.thesis.data.service.sport.objekt;

import cz.inspire.thesis.data.dto.sport.objekt.ObjektSportDetails;
import cz.inspire.thesis.data.model.sport.objekt.ObjektSportEntity;
import cz.inspire.thesis.data.model.sport.objekt.ObjektSportPK;
import cz.inspire.thesis.data.model.sport.sport.SportEntity;
import cz.inspire.thesis.data.model.sport.objekt.ObjektEntity;
import cz.inspire.thesis.data.repository.sport.objekt.ObjektSportRepository;
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
public class ObjektSportService {

    @Inject
    private ObjektSportRepository objektSportRepository;

    @Transactional
    public ObjektSportEntity create(ObjektSportDetails details) throws CreateException {
        try {
            ObjektSportEntity entity = new ObjektSportEntity();
            if (details.getId() == null) {
                details.setId(generateGUID(entity));
            }

            entity.setId(new ObjektSportPK(details.getId(), details.getIndex()));

            objektSportRepository.save(entity);
            return entity;
        } catch (Exception e) {
            throw new CreateException("Failed to create ObjektSport entity", e);
        }
    }

    @Transactional
    public void remove(ObjektSportPK id) throws ApplicationException {
        try {
            ObjektSportEntity entity = objektSportRepository.findBy(id)
                    .orElseThrow(() -> new ApplicationException("ObjektSport entity not found with id : " + id));

            objektSportRepository.remove(entity);
        } catch (Exception e) {
            throw new ApplicationException("Failed to update ObjektSport entity", e);
        }
    }

    @Transactional
    public void save(ObjektSportEntity entity) throws ApplicationException {
        try {
            objektSportRepository.save(entity);
        } catch (Exception e) {
            throw new ApplicationException("Failed to save ObjektSport entity", e);
        }
    }


    public Collection<ObjektSportEntity> findByObjekt(String objektId) {
        return objektSportRepository.findByObjekt(objektId);
    }


    public Collection<ObjektSportEntity> findAll() {
        return objektSportRepository.findAll();
    }
}
