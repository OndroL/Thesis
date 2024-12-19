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

@ApplicationScoped
public class OtviraciDobaObjektuService {

    @Inject
    private OtviraciDobaObjektuRepository repository;

    @Transactional
    public String create(OtviraciDobaObjektuDetails details) throws CreateException {
        try {
            OtviraciDobaObjektuPK pk = new OtviraciDobaObjektuPK(details.getObjektId(), details.getPlatnostOd());

            OtviraciDobaObjektuEntity entity = new OtviraciDobaObjektuEntity();

            entity.setId(pk);
            entity.setOtviraciDoba(details.getOtviraciDoba());

            repository.save(entity);
            return null;
        } catch (Exception e) {
            throw new CreateException("Failed to create OtviraciDobaObjektu entity", e);
        }
    }

    public OtviraciDobaObjektuDetails findCurrent(String objektId, Date day) {
        OtviraciDobaObjektuEntity entity = repository.findCurrent(objektId, day);
        return entity != null ? getDetails(entity) : null;
    }

    public List<OtviraciDobaObjektuDetails> findByObjekt(String objektId) {
        return repository.findByObjekt(objektId).stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    public List<OtviraciDobaObjektuDetails> findAfter(String objektId, Date day) {
        return repository.findAfter(objektId, day).stream()
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
        Optional<OtviraciDobaObjektuEntity> optionalEntity = repository.findById(pk);

        if (optionalEntity.isPresent()) {
            OtviraciDobaObjektuEntity entity = optionalEntity.get();
            entity.setOtviraciDoba(details.getOtviraciDoba());
            repository.save(entity);
        } else {
            throw new EntityNotFoundException("Entity not found for ID: " + pk);
        }
    }

    public List<Date> findCurrentIdsByObjectAndDay(String objektId, Date day) {
        return repository.getCurrentIdsByObjectAndDay(objektId, day);
    }

    public OtviraciDobaObjektuPK getCurrentIdsByObjectAndDay(String objektId, Date day) {
        List<Date> dates = findCurrentIdsByObjectAndDay(objektId, day);
        if (dates == null || dates.isEmpty()) {
            return null;
        }
        return new OtviraciDobaObjektuPK(objektId, dates.get(0));
    }

}