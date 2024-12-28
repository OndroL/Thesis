package cz.inspire.thesis.data.facade.sport;

import cz.inspire.thesis.data.dto.sport.objekt.OmezeniRezervaciDetails;
import cz.inspire.thesis.data.dto.sport.objekt.OtviraciDobaObjektuDetails;
import cz.inspire.thesis.data.dto.sport.objekt.PodminkaRezervaceDetails;
import cz.inspire.thesis.data.model.sport.objekt.*;
import cz.inspire.thesis.data.service.sport.objekt.ObjektService;
import cz.inspire.thesis.data.service.sport.objekt.OmezeniRezervaciService;
import cz.inspire.thesis.data.service.sport.objekt.OtviraciDobaObjektuService;
import cz.inspire.thesis.data.service.sport.objekt.PodminkaRezervaceService;
import cz.inspire.thesis.exceptions.ApplicationException;
import cz.inspire.thesis.exceptions.CreateException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class ReservationFacade {
    @Inject
    private OmezeniRezervaciService omezeniRezervaciService;
    @Inject
    private ObjektService objektService;
    @Inject
    private PodminkaRezervaceService podminkaRezervaceService;
    @Inject
    private OtviraciDobaObjektuService otviraciDobaObjektuService;

    public String create(OmezeniRezervaciDetails details) throws CreateException {
        try {
            OmezeniRezervaciEntity entity = omezeniRezervaciService.create(details);
            return entity.getObjektId();
        } catch (Exception e) {
            throw new CreateException("Failed to create OmezeniRezervaci entity", e);
        }
    }

    public String create(PodminkaRezervaceDetails details) throws CreateException {
        try {
            PodminkaRezervaceEntity entity = podminkaRezervaceService.create(details);

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

            podminkaRezervaceService.save(entity);

            return entity.getId();
        } catch (Exception e) {
            throw new CreateException("Failed to create PodminkaRezervace entity", e);
        }
    }
    public String create(OtviraciDobaObjektuDetails details) throws CreateException {
        try {
            OtviraciDobaObjektuEntity entity = otviraciDobaObjektuService.create(details);

            return entity.getId().getObjektId();
        } catch (Exception e) {
            throw new CreateException("Failed to create OtviraciDobaObjektu entity", e);
        }
    }

    public void setDetails(PodminkaRezervaceDetails details) throws ApplicationException {
        try {
            PodminkaRezervaceEntity entity = podminkaRezervaceService.findOptionalBy(details.getId())
                    .orElseThrow(() -> new ApplicationException("PodminkaRezervace entity not found"));

            podminkaRezervaceService.setEntityAttributes(entity, details);

            podminkaRezervaceService.save(entity);
        } catch (Exception e) {
            throw new ApplicationException("Failed to update PodminkaRezervace entity", e);
        }
    }

    public void setDetails(OtviraciDobaObjektuDetails details) throws ApplicationException {
        OtviraciDobaObjektuPK pk = new OtviraciDobaObjektuPK(details.getObjektId(), details.getPlatnostOd());
        Optional<OtviraciDobaObjektuEntity> optionalEntity = otviraciDobaObjektuService.findById(pk);

        if (optionalEntity.isPresent()) {
            OtviraciDobaObjektuEntity entity = optionalEntity.get();
            entity.setOtviraciDoba(details.getOtviraciDoba());
            otviraciDobaObjektuService.save(entity);
        } else {
            throw new EntityNotFoundException("Entity not found for ID: " + pk);
        }
    }

    public OmezeniRezervaciDetails getDetails(OmezeniRezervaciEntity entity) {
        return new OmezeniRezervaciDetails(
                entity.getObjektId(),
                entity.getOmezeni()
        );
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

    private OtviraciDobaObjektuDetails getDetails(OtviraciDobaObjektuEntity entity) {
        return new OtviraciDobaObjektuDetails(
                entity.getId().getObjektId(),
                entity.getId().getPlatnostOd(),
                entity.getOtviraciDoba()
        );
    }

    private void setEntityAttributes(PodminkaRezervaceEntity entity, PodminkaRezervaceDetails details) {
        entity.setName(details.getName());
        entity.setPriorita(details.getPriorita());
        entity.setObjektRezervaceId(details.getObjektRezervaceId());
        entity.setObjektRezervaceObsazen(details.getObjektRezervaceObsazen());
    }

    ////////////////////////
    /////// Queries ///////
    //////////////////////

    public Collection<OmezeniRezervaciDetails> findAll() {
        return omezeniRezervaciService.findAll().stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    // Renamed from findAll to findAllPodminka
    public Collection<PodminkaRezervaceDetails> findAllPodminka() {
        return podminkaRezervaceService.findAll().stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    public Long countAll() {
        return podminkaRezervaceService.countAll();
    }

    public Long countAllByObject(String objectId) {
        return podminkaRezervaceService.countAllByObject(objectId);
    }

    public Collection<String> getObjectIdsByReservationConditionObject(String objectId) {
        return podminkaRezervaceService.getObjectIdsByReservationConditionObject(objectId);
    }

    public OtviraciDobaObjektuDetails findCurrent(String objektId, Date day) {
        OtviraciDobaObjektuEntity entity = otviraciDobaObjektuService.findCurrent(objektId, day);
        return entity != null ? getDetails(entity) : null;
    }

    public List<OtviraciDobaObjektuDetails> findByObjekt(String objektId) {
        return otviraciDobaObjektuService.findByObjekt(objektId).stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    public List<OtviraciDobaObjektuDetails> findAfter(String objektId, Date day) {
        return otviraciDobaObjektuService.findAfter(objektId, day).stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    public List<Date> findCurrentIdsByObjectAndDay(String objektId, Date day) {
        return otviraciDobaObjektuService.findCurrentIdsByObjectAndDay(objektId, day);
    }

    public OtviraciDobaObjektuPK getCurrentIdsByObjectAndDay(String objektId, Date day) {
        List<Date> dates = findCurrentIdsByObjectAndDay(objektId, day);
        if (dates == null || dates.isEmpty()) {
            return null;
        }
        return new OtviraciDobaObjektuPK(objektId, dates.get(0));
    }


}
