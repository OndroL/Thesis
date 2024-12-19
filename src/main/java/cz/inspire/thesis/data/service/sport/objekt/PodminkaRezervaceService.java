package cz.inspire.thesis.data.service.sport.objekt;

import cz.inspire.thesis.data.dto.sport.objekt.PodminkaRezervaceDetails;
import cz.inspire.thesis.data.model.sport.objekt.ObjektEntity;
import cz.inspire.thesis.data.model.sport.objekt.PodminkaRezervaceEntity;
import cz.inspire.thesis.data.repository.sport.objekt.PodminkaRezervaceRepository;
import cz.inspire.thesis.exceptions.ApplicationException;
import cz.inspire.thesis.exceptions.CreateException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.deltaspike.jpa.api.transaction.Transactional;

import java.util.Collection;
import java.util.stream.Collectors;

import static cz.inspire.thesis.data.utils.guidGenerator.generateGUID;

@ApplicationScoped
public class PodminkaRezervaceService {

    @Inject
    private PodminkaRezervaceRepository podminkaRezervaceRepository;
    @Inject
    private ObjektService objektService;

    @Transactional
    public String create(PodminkaRezervaceDetails details) throws CreateException {
        try {
            PodminkaRezervaceEntity entity = new PodminkaRezervaceEntity();
            if (details.getId() == null) {
                details.setId(generateGUID(entity));
            }
            setEntityAttributes(entity, details);
            postCreate(entity, details);

            podminkaRezervaceRepository.save(entity);
            return entity.getId();
        } catch (Exception e) {
            throw new CreateException("Failed to create PodminkaRezervace entity", e);
        }
    }

    @Transactional
    public void postCreate(PodminkaRezervaceEntity entity, PodminkaRezervaceDetails details) throws CreateException {
        if (details.getObjektId() == null) {
            throw new CreateException("ObjektId cannot be null to create PodminkaRezervace.");
        }
        try {
            //entity relation
            ObjektEntity objekt = objektService.findOptionalBy(details.getObjektId())
                    .orElseThrow(() -> new CreateException("Couldn't find Objekt for PodminkaRezervace with objekt id: " + details.getObjektId()));
            entity.setObjekt(objekt);
        } catch (Exception ex) {
            throw new CreateException("Couldn't set Objekt for PodminkaRezervace: " + ex);
        }
    }

    @Transactional
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
        details.setObjektId(entity.getObjekt() != null ? entity.getObjekt().getId() : null);
        return details;
    }

    public PodminkaRezervaceDetails getDetailsWithoutObjectId(PodminkaRezervaceEntity entity) {
        PodminkaRezervaceDetails podminka = new PodminkaRezervaceDetails();
        podminka.setId(entity.getId());
        podminka.setName(entity.getName());
        podminka.setPriorita(entity.getPriorita());
        podminka.setObjektRezervaceId(entity.getObjektRezervaceId());
        podminka.setObjektRezervaceObsazen(entity.getObjektRezervaceObsazen());
        return podminka;
    }


    public Collection<PodminkaRezervaceDetails> findAll() {
        return podminkaRezervaceRepository.findAll().stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    public Long countAll() {
        return podminkaRezervaceRepository.countAll();
    }

    public Long countAllByObject(String objectId) {
        return podminkaRezervaceRepository.countAllByObject(objectId);
    }

    public Collection<String> getObjectIdsByReservationConditionObject(String objectId) {
        return podminkaRezervaceRepository.getObjectIdsByReservationConditionObject(objectId);
    }

    private void setEntityAttributes(PodminkaRezervaceEntity entity, PodminkaRezervaceDetails details) {
        entity.setName(details.getName());
        entity.setPriorita(details.getPriorita());
        entity.setObjektRezervaceId(details.getObjektRezervaceId());
        entity.setObjektRezervaceObsazen(details.getObjektRezervaceObsazen());
    }
}
