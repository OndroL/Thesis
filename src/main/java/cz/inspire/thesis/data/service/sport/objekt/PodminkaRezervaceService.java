package cz.inspire.thesis.data.service.sport.objekt;

import cz.inspire.thesis.data.dto.sport.objekt.PodminkaRezervaceDetails;
import cz.inspire.thesis.data.model.sport.objekt.PodminkaRezervaceEntity;
import cz.inspire.thesis.data.repository.sport.objekt.PodminkaRezervaceRepository;
import cz.inspire.thesis.exceptions.ApplicationException;
import cz.inspire.thesis.exceptions.CreateException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Collection;
import java.util.stream.Collectors;

import static cz.inspire.thesis.data.utils.guidGenerator.generateGUID;

@ApplicationScoped
public class PodminkaRezervaceService {

    @Inject
    private PodminkaRezervaceRepository podminkaRezervaceRepository;

    public String ejbCreate(PodminkaRezervaceDetails details) throws CreateException {
        try {
            PodminkaRezervaceEntity entity = new PodminkaRezervaceEntity();
            if (details.getId() == null) {
                details.setId(generateGUID(entity));
            }
            setEntityAttributes(entity, details);

            podminkaRezervaceRepository.save(entity);
            return entity.getId();
        } catch (Exception e) {
            throw new CreateException("Failed to create PodminkaRezervace entity", e);
        }
    }

    public void setDetails(PodminkaRezervaceDetails details) throws ApplicationException {
        try {
            PodminkaRezervaceEntity entity = podminkaRezervaceRepository.findOptionalBy(details.getId())
                    .orElseThrow(() -> new ApplicationException("PodminkaRezervace entity not found"));

            setEntityAttributes(entity, details);

            podminkaRezervaceRepository.save(entity);
        } catch (Exception e) {
            throw new ApplicationException("Failed to update PodminkaRezervace entity", e);
        }
    }

    public PodminkaRezervaceDetails getDetails(PodminkaRezervaceEntity entity) {
        PodminkaRezervaceDetails details = new PodminkaRezervaceDetails();
        details.setId(entity.getId());
        details.setName(entity.getName());
        details.setPriorita(entity.getPriorita());
        details.setObjektRezervaceId(entity.getObjektRezervaceId());
        details.setObjektRezervaceObsazen(entity.getObjektRezervaceObsazen());
        details.setObjektId(entity.getObjektId() != null ? entity.getObjektId().getId() : null);
        return details;
    }

    public Collection<PodminkaRezervaceDetails> findAll() {
        return podminkaRezervaceRepository.findAll().stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    public Long ejbHomeCountAll() {
        return podminkaRezervaceRepository.countAll();
    }

    public Long ejbHomeCountAllByObject(String objectId) {
        return podminkaRezervaceRepository.countAllByObject(objectId);
    }

    public Collection<String> ejbHomeGetObjectIdsByReservationConditionObject(String objectId) {
        return podminkaRezervaceRepository.getObjectIdsByReservationConditionObject(objectId);
    }

    private void setEntityAttributes(PodminkaRezervaceEntity entity, PodminkaRezervaceDetails details) {
        entity.setName(details.getName());
        entity.setPriorita(details.getPriorita());
        entity.setObjektRezervaceId(details.getObjektRezervaceId());
        entity.setObjektRezervaceObsazen(details.getObjektRezervaceObsazen());
    }
}
