package cz.inspire.thesis.data.service.sport.objekt;

import cz.inspire.thesis.data.dto.sport.objekt.OtviraciDobaObjektuDetails;
import cz.inspire.thesis.data.model.sport.objekt.OtviraciDobaObjektuEntity;
import cz.inspire.thesis.data.model.sport.objekt.OtviraciDobaObjektuPK;
import cz.inspire.thesis.data.repository.sport.objekt.OtviraciDobaObjektuRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.deltaspike.jpa.api.transaction.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Those exceptions are created to mimic functionality and implementation of production exceptions
 * Use your imports
 * Plus ApplicationException is additional Exception for update, see setDetails
 */
import cz.inspire.thesis.exceptions.ApplicationException;
import cz.inspire.thesis.exceptions.CreateException;


@ApplicationScoped
public class OtviraciDobaObjektuService {

    @Inject
    private OtviraciDobaObjektuRepository otviraciDobaObjektuRepository;

    @Transactional
    public OtviraciDobaObjektuEntity create(OtviraciDobaObjektuDetails details) throws CreateException {
        try {
            OtviraciDobaObjektuPK pk = new OtviraciDobaObjektuPK(details.getObjektId(), details.getPlatnostOd());

            OtviraciDobaObjektuEntity entity = new OtviraciDobaObjektuEntity();

            entity.setId(pk);
            entity.setOtviraciDoba(details.getOtviraciDoba());

            otviraciDobaObjektuRepository.save(entity);
            return entity;
        } catch (Exception e) {
            throw new CreateException("Failed to create OtviraciDobaObjektu entity", e);
        }
    }

    @Transactional
    public void save(OtviraciDobaObjektuEntity entity) throws ApplicationException {
        try {
            otviraciDobaObjektuRepository.save(entity);
        } catch (Exception e) {
            throw new ApplicationException("Failed while trying to save OtviraciDobaObjektu Entity ", e);
        }
    }

    public OtviraciDobaObjektuEntity findCurrent(String objektId, Date day) {
        return otviraciDobaObjektuRepository.findCurrent(objektId, day);
    }

    public List<OtviraciDobaObjektuEntity> findByObjekt(String objektId) {
        return otviraciDobaObjektuRepository.findByObjekt(objektId);
    }

    public List<OtviraciDobaObjektuEntity> findAfter(String objektId, Date day) {
        return otviraciDobaObjektuRepository.findAfter(objektId, day);
    }

    public List<Date> findCurrentIdsByObjectAndDay(String objektId, Date day) {
        return otviraciDobaObjektuRepository.getCurrentIdsByObjectAndDay(objektId, day);
    }

    public Optional<OtviraciDobaObjektuEntity> findById(OtviraciDobaObjektuPK id) {
        return otviraciDobaObjektuRepository.findById(id);
    }

}