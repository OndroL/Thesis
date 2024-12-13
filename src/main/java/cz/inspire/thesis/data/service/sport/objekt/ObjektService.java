package cz.inspire.thesis.data.service.sport.objekt;

import cz.inspire.thesis.data.dto.sport.objekt.ObjektDetails;
import cz.inspire.thesis.data.model.sport.objekt.ObjektEntity;
import cz.inspire.thesis.data.repository.sport.objekt.ObjektRepository;
import cz.inspire.thesis.exceptions.ApplicationException;
import cz.inspire.thesis.exceptions.CreateException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static cz.inspire.thesis.data.utils.guidGenerator.generateGUID;

@ApplicationScoped
public class ObjektService {

    @Inject
    private ObjektRepository objektRepository;

    public String ejbCreate(ObjektDetails details) throws CreateException {
        try {
            ObjektEntity entity = new ObjektEntity();
            if (details.getId() == null) {
                details.setId(generateGUID(entity));
            }
            setEntityAttributes(entity, details);

            objektRepository.save(entity);
            return entity.getId();
        } catch (Exception e) {
            throw new CreateException("Failed to create Objekt entity", e);
        }
    }

    public void setDetails(ObjektDetails details) throws ApplicationException {
        try {
            ObjektEntity entity = objektRepository.findOptionalBy(details.getId())
                    .orElseThrow(() -> new ApplicationException("Objekt entity not found"));

            setEntityAttributes(entity, details);

            objektRepository.save(entity);
        } catch (Exception e) {
            throw new ApplicationException("Failed to update Objekt entity", e);
        }
    }

    public ObjektDetails getDetails(ObjektEntity entity) {
        ObjektDetails details = new ObjektDetails();
        details.setId(entity.getId());
        details.setKapacita(entity.getKapacita());
        details.setCasovaJednotka(entity.getCasovaJednotka());
        details.setTypRezervace(entity.getTypRezervace());
        details.setPrimyVstup(entity.isPrimyVstup());
        details.setMinDelkaRezervace(entity.getMinDelkaRezervace());
        details.setMaxDelkaRezervace(entity.getMaxDelkaRezervace());
        details.setVolnoPredRezervaci(entity.getVolnoPredRezervaci());
        details.setVolnoPoRezervaci(entity.getVolnoPoRezervaci());
        details.setZarovnatZacatekRezervace(entity.getZarovnatZacatekRezervace());
        details.setDelkaRezervaceNasobkem(entity.getDelkaRezervaceNasobkem());
        details.setVicestavovy(entity.getVicestavovy());
        details.setStav(entity.getStav());
        details.setReservationStart(entity.getReservationStart());
        details.setReservationFinish(entity.getReservationFinish());
        details.setOdcitatProcedury(entity.isOdcitatProcedury());
        details.setRezervaceNaTokeny(entity.isRezervaceNaTokeny());
        details.setRucniUzavreniVstupu(entity.isRucniUzavreniVstupu());
        details.setUpraveniCasuVstupu(entity.isUpraveniCasuVstupu());
        details.setPozastavitVstup(entity.isPozastavitVstup());
        details.setShowProgress(entity.isShowProgress());
        details.setCheckTokensCount(entity.isCheckTokensCount());
        details.setSelectInstructor(entity.isSelectInstructor());
        details.setShowInstructorName(entity.isShowInstructorName());
        details.setShowSportName(entity.isShowSportName());
        details.setVytvoreniRezervacePredZacatkem(entity.getVytvoreniRezervacePredZacatkem());
        details.setEditaceRezervacePredZacatkem(entity.getEditaceRezervacePredZacatkem());
        details.setZruseniRezervacePredZacatkem(entity.getZruseniRezervacePredZacatkem());

        // LocaleData and other nested collections would be handled here

        return details;
    }

    public Collection<ObjektDetails> findAll() {
        return objektRepository.findAll().stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    public List<String> ejbHomeGetObjektIdsOfAreal(String arealId) throws ApplicationException {
        try {
            return objektRepository.findObjektIdsOfAreal(arealId);
        } catch (Exception e) {
            throw new ApplicationException("Failed to retrieve areal IDs ", e);
        }
    }

    private void setEntityAttributes(ObjektEntity entity, ObjektDetails details) {
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
}
