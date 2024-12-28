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
import java.util.Optional;
import java.util.stream.Collectors;

import static cz.inspire.thesis.data.utils.guidGenerator.generateGUID;

@ApplicationScoped
public class PodminkaRezervaceService {

    @Inject
    private PodminkaRezervaceRepository podminkaRezervaceRepository;

    @Transactional
    public PodminkaRezervaceEntity create(PodminkaRezervaceDetails details) throws CreateException {
        try {
            PodminkaRezervaceEntity entity = new PodminkaRezervaceEntity();
            if (details.getId() == null) {
                details.setId(generateGUID(entity));
            }
            setEntityAttributes(entity, details);

            podminkaRezervaceRepository.save(entity);
            return entity;
        } catch (Exception e) {
            throw new CreateException("Failed to create PodminkaRezervace entity", e);
        }
    }

    @Transactional
    public void save(PodminkaRezervaceEntity entity) throws ApplicationException {
        try {
            podminkaRezervaceRepository.save(entity);
        } catch (Exception e) {
            throw new ApplicationException("Failed while saving PodminkaRezervace Entity ", e);
        }
    }

    public void setEntityAttributes(PodminkaRezervaceEntity entity, PodminkaRezervaceDetails details) {
        entity.setName(details.getName());
        entity.setPriorita(details.getPriorita());
        entity.setObjektRezervaceId(details.getObjektRezervaceId());
        entity.setObjektRezervaceObsazen(details.getObjektRezervaceObsazen());
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


    public Collection<PodminkaRezervaceEntity> findAll() {
        return podminkaRezervaceRepository.findAll();
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

    public Optional<PodminkaRezervaceEntity> findOptionalBy(String id) {
        return podminkaRezervaceRepository.findOptionalBy(id);
    }

}
