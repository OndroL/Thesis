package cz.inspire.thesis.data.service.sport.objekt;

import cz.inspire.thesis.data.dto.sport.objekt.ObjektSportDetails;
import cz.inspire.thesis.data.model.sport.objekt.ObjektSportEntity;
import cz.inspire.thesis.data.model.sport.objekt.ObjektSportPK;
import cz.inspire.thesis.data.model.sport.sport.SportEntity;
import cz.inspire.thesis.data.model.sport.objekt.ObjektEntity;
import cz.inspire.thesis.data.repository.sport.objekt.ObjektSportRepository;
import cz.inspire.thesis.exceptions.ApplicationException;
import cz.inspire.thesis.exceptions.CreateException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Collection;
import java.util.stream.Collectors;

import static cz.inspire.thesis.data.utils.guidGenerator.generateGUID;

@ApplicationScoped
public class ObjektSportService {

    @Inject
    private ObjektSportRepository objektSportRepository;

    public String ejbCreate(ObjektSportDetails details) throws CreateException {
        try {
            ObjektSportEntity entity = new ObjektSportEntity();
            if (details.getId() == null) {
                details.setId(generateGUID(entity));
            }

            entity.setId(new ObjektSportPK(details.getId(), details.getIndex()));

            // Set relationships
            SportEntity sport = new SportEntity();
            sport.setId(details.getSportId());
            entity.setSport(sport);

            ObjektEntity objekt = new ObjektEntity();
            objekt.setId(details.getObjektId());
            entity.setObjekt(objekt);

            objektSportRepository.save(entity);
            return entity.getId().getId();
        } catch (Exception e) {
            throw new CreateException("Failed to create ObjektSport entity", e);
        }
    }

    public void setDetails(ObjektSportDetails details) throws ApplicationException {
        try {
            ObjektSportEntity entity = objektSportRepository.findOptionalBy(details.getId())
                    .orElseThrow(() -> new ApplicationException("ObjektSport entity not found"));

            entity.getId().setIndex(details.getIndex());
            entity.getSport().setId(details.getSportId());
            entity.getObjekt().setId(details.getObjektId());

            objektSportRepository.save(entity);
        } catch (Exception e) {
            throw new ApplicationException("Failed to update ObjektSport entity", e);
        }
    }

    public ObjektSportDetails getDetails(ObjektSportEntity entity) {
        ObjektSportDetails details = new ObjektSportDetails();
        details.setId(entity.getId().getId());
        details.setIndex(entity.getId().getIndex());
        details.setSportId(entity.getSport().getId());
        details.setObjektId(entity.getObjekt().getId());
        return details;
    }

    public Collection<ObjektSportDetails> findAll() {
        return objektSportRepository.findAll().stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    public Collection<ObjektSportDetails> findByObjekt(String objektId) {
        return objektSportRepository.findByObjekt(objektId).stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }
}
