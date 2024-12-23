package cz.inspire.thesis.data.service.sport.objekt;

import cz.inspire.thesis.data.dto.sport.objekt.OtviraciDobaObjektuDetails;
import cz.inspire.thesis.data.model.sport.objekt.OtviraciDobaObjektuEntity;
import cz.inspire.thesis.data.model.sport.objekt.OtviraciDobaObjektuPK;
import cz.inspire.thesis.data.repository.sport.objekt.OtviraciDobaObjektuRepository;
import cz.inspire.thesis.exceptions.CreateException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import org.apache.deltaspike.jpa.api.transaction.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Those exceptions are created to mimic functionality and implementation of production exceptions
 * Use your imports
 * Plus ApplicationException is additional Exception for update, see setDetails
 */

/**
 * This is import of simple generateGUID functionality created to mimic real functionality
 * In your implementation use your import of guidGenerator
 */

@ApplicationScoped
public class OtviraciDobaObjektuService {

    @Inject
    private OtviraciDobaObjektuRepository otviraciDobaObjektuRepository;

    @Transactional
    public String create(OtviraciDobaObjektuDetails details) throws CreateException {
        try {
            OtviraciDobaObjektuPK pk = new OtviraciDobaObjektuPK(details.getObjektId(), details.getPlatnostOd());

            OtviraciDobaObjektuEntity entity = new OtviraciDobaObjektuEntity();

            entity.setId(pk);
            entity.setOtviraciDoba(details.getOtviraciDoba());

            otviraciDobaObjektuRepository.save(entity);
            return null;
        } catch (Exception e) {
            throw new CreateException("Failed to create OtviraciDobaObjektu entity", e);
        }
    }

    public OtviraciDobaObjektuDetails findCurrent(String objektId, Date day) {
        OtviraciDobaObjektuEntity entity = otviraciDobaObjektuRepository.findCurrent(objektId, day);
        return entity != null ? getDetails(entity) : null;
    }

    public List<OtviraciDobaObjektuDetails> findByObjekt(String objektId) {
        return otviraciDobaObjektuRepository.findByObjekt(objektId).stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    public List<OtviraciDobaObjektuDetails> findAfter(String objektId, Date day) {
        return otviraciDobaObjektuRepository.findAfter(objektId, day).stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    private OtviraciDobaObjektuDetails getDetails(OtviraciDobaObjektuEntity entity) {
        return new OtviraciDobaObjektuDetails(
                entity.getId().getObjektId(),
                entity.getId().getPlatnostOd(),
                entity.getOtviraciDoba()
        );
    }

    public void setDetails(OtviraciDobaObjektuDetails details) throws EntityNotFoundException {
        OtviraciDobaObjektuPK pk = new OtviraciDobaObjektuPK(details.getObjektId(), details.getPlatnostOd());
        Optional<OtviraciDobaObjektuEntity> optionalEntity = otviraciDobaObjektuRepository.findById(pk);

        if (optionalEntity.isPresent()) {
            OtviraciDobaObjektuEntity entity = optionalEntity.get();
            entity.setOtviraciDoba(details.getOtviraciDoba());
            otviraciDobaObjektuRepository.save(entity);
        } else {
            throw new EntityNotFoundException("Entity not found for ID: " + pk);
        }
    }

    public List<Date> findCurrentIdsByObjectAndDay(String objektId, Date day) {
        return otviraciDobaObjektuRepository.getCurrentIdsByObjectAndDay(objektId, day);
    }

    public OtviraciDobaObjektuPK getCurrentIdsByObjectAndDay(String objektId, Date day) {
        List<Date> dates = findCurrentIdsByObjectAndDay(objektId, day);
        if (dates == null || dates.isEmpty()) {
            return null;
        }
        return new OtviraciDobaObjektuPK(objektId, dates.get(0));
    }

}