package cz.inspire.thesis.data.service.sport.objekt;

import cz.inspire.thesis.data.dto.sport.objekt.ObjektDetails;
import cz.inspire.thesis.data.dto.sport.objekt.ObjektLocDetails;
import cz.inspire.thesis.data.model.sport.objekt.*;
import cz.inspire.thesis.data.repository.sport.objekt.ObjektRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.deltaspike.jpa.api.transaction.Transactional;
import jakarta.inject.Inject;

import java.util.*;
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
public class ObjektService {

    @Inject
    private ObjektRepository objektRepository;

    @Transactional
    public ObjektEntity create(ObjektDetails details) throws CreateException {
        try {
            ObjektEntity entity = new ObjektEntity();
            if (details.getId() == null) {
                details.setId(generateGUID(entity));
            }
            setEntityAttributes(entity, details);
            setLocaleData(details, entity);
            objektRepository.save(entity);

            //return entity.getId();
            return entity;
        } catch (Exception e) {
            throw new CreateException("Failed to create Objekt entity", e);
        }
    }

    @Transactional
    public void saveEntity(ObjektEntity entity) throws ApplicationException {
        try {
            objektRepository.save(entity);
        } catch (Exception e) {
            throw new ApplicationException("Failed while saving Objekt Entity.", e );
        }
    }


    public void setNadobjekty(ObjektDetails objektDetails, ObjektEntity objektEntity)  {
        if (objektDetails.getNadobjekty() != null) {
            Collection<ObjektEntity> nadobjekts = objektDetails.getNadobjekty().stream()
                    .map(nadId -> {
                        try {
                            return objektRepository.findOptionalBy(nadId)
                                    .orElseThrow(() -> new ApplicationException("Nadobjekt not found for ID: " + nadId));
                        } catch (ApplicationException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .collect(Collectors.toSet());
            objektEntity.setNadobjekty(nadobjekts);
        }
    }

    public void setPodobjekty(ObjektDetails objektDetails, ObjektEntity objektEntity) {
        if (objektDetails.getPodobjekty() != null) {
            Collection<ObjektEntity> podobjekts = objektDetails.getPodobjekty().stream()
                    .map(podId -> {
                        try {
                            return objektRepository.findOptionalBy(podId)
                                    .orElseThrow(() -> new ApplicationException("Podobjekt not found for ID: " + podId));
                        } catch (ApplicationException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .collect(Collectors.toSet());
            objektEntity.setPodobjekty(podobjekts);
        }
    }

    public void setEntityAttributes(ObjektEntity entity, ObjektDetails details) {
        entity.setKapacita(details.getKapacita());
        entity.setCasovaJednotka(details.getCasovaJednotka());
        entity.setTypRezervace(details.getTypRezervace());
        entity.setPrimyVstup(details.isPrimyVstup());
        entity.setMinDelkaRezervace(details.getMinDelkaRezervace());
        entity.setMaxDelkaRezervace(details.getMaxDelkaRezervace());
        entity.setVolnoPredRezervaci(details.getVolnoPredRezervaci());
        entity.setVolnoPoRezervaci(details.getVolnoPoRezervaci());
        entity.setZarovnatZacatekRezervace(details.getZarovnatZacatekRezervace());
        entity.setDelkaRezervaceNasobkem(details.getDelkaRezervaceNasobkem());
        entity.setVicestavovy(details.getVicestavovy());
        entity.setStav(details.getStav());
        entity.setReservationStart(details.getReservationStart());
        entity.setReservationFinish(details.getReservationFinish());
        entity.setOdcitatProcedury(details.isOdcitatProcedury());
        entity.setRezervaceNaTokeny(details.isRezervaceNaTokeny());
        entity.setRucniUzavreniVstupu(details.isRucniUzavreniVstupu());
        entity.setUpraveniCasuVstupu(details.isUpraveniCasuVstupu());
        entity.setPozastavitVstup(details.isPozastavitVstup());
        entity.setShowProgress(details.isShowProgress());
        entity.setCheckTokensCount(details.isCheckTokensCount());
        entity.setSelectInstructor(details.isSelectInstructor());
        entity.setShowInstructorName(details.isShowInstructorName());
        entity.setShowSportName(details.isShowSportName());
        entity.setVytvoreniRezervacePredZacatkem(details.getVytvoreniRezervacePredZacatkem());
        entity.setEditaceRezervacePredZacatkem(details.getEditaceRezervacePredZacatkem());
        entity.setZruseniRezervacePredZacatkem(details.getZruseniRezervacePredZacatkem());
    }

    public void setLocaleData(ObjektDetails details, ObjektEntity entity) throws CreateException {
        Map<String, ObjektLocDetails> localeData = details.getLocaleData();
        Collection<ObjektLocEntity> newLocData = new ArrayList<>();
        if (localeData != null) {
            for (ObjektLocDetails locDetails : localeData.values()) {
                ObjektLocEntity locEntity = new ObjektLocEntity();
                locEntity.setId(generateGUID(locEntity));
                locEntity.setJazyk(locDetails.getJazyk());
                locEntity.setNazev(locDetails.getNazev());
                locEntity.setPopis(locDetails.getPopis());
                locEntity.setZkracenyNazev(locDetails.getZkracenyNazev());
                newLocData.add(locEntity);

            }
        }
        entity.setLocaleData(newLocData);
    }

    public Collection<ObjektEntity> findAll() {
        return objektRepository.findAll();
    }

    public Collection<ObjektEntity> findByAreal(String arealId, String jazyk) {
        return objektRepository.findByAreal(arealId, jazyk);
    }

    public Collection<ObjektEntity> findBaseByAreal(String arealId, String jazyk) {
        return objektRepository.findBaseByAreal(arealId, jazyk);
    }

    public Collection<ObjektEntity> findByAreal(String arealId, String jazyk, int offset, int count) {
        return objektRepository.findByAreal(arealId, jazyk, offset, count);
    }

    public Collection<ObjektEntity> findByTypRezervace(Integer typRezervace, String jazyk) {
        return objektRepository.findByTypRezervace(typRezervace, jazyk);
    }


    public Collection<ObjektEntity> findBaseByAreal(String arealId, String jazyk, int offset, int count) {
        return objektRepository.findBaseByAreal(arealId, jazyk, offset, count);
    }

    public Collection<ObjektEntity> findBySport(String sportId, String jazyk) {
        return objektRepository.findBySport(sportId, jazyk);
    }

    public Collection<ObjektEntity> findByPrimyVstup(String jazyk, boolean primyVstup) {
        return objektRepository.findByPrimyVstup(jazyk, primyVstup);
    }

    public Collection<ObjektEntity> findByPrimyVstup(String jazyk, int offset, int count, boolean primyVstup) {
        return objektRepository.findByPrimyVstup(jazyk, offset, count, primyVstup);
    }

    public Collection<String> getObjektIdsOfAreal(String arealId) {
        return new ArrayList<>(objektRepository.findObjektIdsOfAreal(arealId));
    }

    public Optional<ObjektEntity> findOptionalBy(String objektId) {
        return objektRepository.findOptionalBy(objektId);
    }

    public ObjektEntity findById(String objektId) throws ApplicationException {
        return objektRepository.findOptionalBy(objektId)
                .orElseThrow(() -> new ApplicationException("Objekt entity not found"));
    }

}
