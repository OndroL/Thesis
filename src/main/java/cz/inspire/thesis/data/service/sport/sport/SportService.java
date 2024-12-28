package cz.inspire.thesis.data.service.sport.sport;

import cz.inspire.thesis.data.dto.sport.sport.SportDetails;
import cz.inspire.thesis.data.model.sport.activity.ActivityEntity;
import cz.inspire.thesis.data.model.sport.sport.*;
import cz.inspire.thesis.data.repository.sport.sport.SportRepository;
import cz.inspire.thesis.exceptions.ApplicationException;
import cz.inspire.thesis.exceptions.CreateException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.deltaspike.jpa.api.transaction.Transactional;

import java.util.*;

import static cz.inspire.thesis.data.utils.guidGenerator.generateGUID;

@ApplicationScoped
public class SportService {

    @Inject
    private SportRepository sportRepository;

    @Transactional
    public SportEntity create(SportDetails details) throws CreateException {
        try {
            SportEntity entity = new SportEntity();
            if (details.getId() == null) {
                details.setId(generateGUID(entity));
            }

            entity.setId(details.getId());
            entity.setPodSportyCount(0);
            setBasicAttributes(entity, details);

            sportRepository.save(entity);
            return entity;
        } catch (Exception e) {
            throw new CreateException("Failed to create Sport entity", e);
        }
    }

    @Transactional
    public void save(SportEntity entity) throws ApplicationException {
        try {
            sportRepository.save(entity);
        } catch (Exception e) {
            throw new ApplicationException("Failed while trying to save Sport Entity ", e);
        }
    }

    @Transactional
    public void remove(SportEntity entity) throws ApplicationException {
        try{
            sportRepository.remove(entity);
        } catch (Exception e) {
            throw new ApplicationException("Failed why trying to remove SportEntity : " + e);
        }
    }

    public String getActivityId(SportEntity sport) {
        ActivityEntity activity = sport.getActivity();
        return activity == null ? null : activity.getId();
    }

    public Collection<SportEntity> findAll() {
        return sportRepository.findAll();
    }


    public Collection<SportEntity> findByParent(String parentId, String jazyk) {
        return sportRepository.findByParent(parentId, jazyk);
    }

    public Collection<SportEntity> findByParent(String parentId, String jazyk, int offset, int count) {
        return sportRepository.findByParent(parentId, jazyk, offset, count);
    }

    public Collection<SportEntity> findByCategory(String categoryId, int offset, int count) {
        return sportRepository.findByCategory(categoryId, offset, count);
    }

    public Collection<SportEntity> findByZbozi(String zboziId, int offset, int count) {
        return sportRepository.findByZbozi(zboziId, offset, count);
    }

    public Collection<SportEntity> findRoot(String jazyk) {
        return sportRepository.findRoot(jazyk);
    }

    public Collection<SportEntity> findRoot(String jazyk, int offset, int count) {
        return sportRepository.findRoot(jazyk, offset, count);
    }

    public Collection<SportEntity> findCategoryRoot(int offset, int count) {
        return sportRepository.findCategoryRoot(offset, count);
    }

    public Long countAllByParentAndLanguage(String parentId, String language) {
        return sportRepository.countAllByParentAndLanguage(parentId, language);
    }

    public Long countAllByCategory(String categoryId) {
        return sportRepository.countAllByCategory(categoryId);
    }

    public Collection<String> getAllIdsByParentAndLanguage(String parentId, String language) {
        return sportRepository.getAllIdsByParentAndLanguage(parentId, language);
    }

    public Long countRootByLanguage(String language) {
        return sportRepository.countRootByLanguage(language);
    }

    public Long countCategoryRoot() {
        return sportRepository.countCategoryRoot();
    }

    public Collection<String> getRootIdsByLanguage(String language) {
        return sportRepository.getRootIdsByLanguage(language);
    }

    public Optional<SportEntity> findOptionalBy(String id) {
        return sportRepository.findOptionalBy(id);
    }


    public void setBasicAttributes(SportEntity entity, SportDetails sport) {
        entity.setTyp(sport.getTyp());
        entity.setZboziId(sport.getZboziId());
        entity.setSkladId(sport.getSkladId());
        entity.setSazbaJednotek(sport.getSazbaJednotek());
        entity.setSazbaNaOsobu(sport.getSazbaNaOsobu());
        entity.setSazbaNaCas(sport.getSazbaNaCas());
        entity.setUctovatZalohu(sport.getUctovatZalohu());
        entity.setSazbyStorna(sport.getSazbyStorna());
        entity.setMinDelkaRezervace(sport.getMinDelkaRezervace());
        entity.setMaxDelkaRezervace(sport.getMaxDelkaRezervace());
        entity.setObjednavkaZaplniObjekt(sport.getObjednavkaZaplniObjekt());
        entity.setMaximalniPocetOsobNaZakaznika(sport.getMaximalniPocetOsobNaZakaznika());
        entity.setDelkaRezervaceNasobkem(sport.getDelkaRezervaceNasobkem());
        entity.setBarvaPopredi(sport.getBarvaPopredi());
        entity.setBarvaPozadi(sport.getBarvaPozadi());
        entity.setZobrazitText(sport.getZobrazitText());
        entity.setViditelnyWeb(sport.getViditelnyWeb());
        entity.setNavRezervaceOffset(sport.getNavRezervaceOffset());
        entity.setDelkaHlavniRez(sport.getDelkaHlavniRez());
        entity.setMinimalniPocetOsob(sport.getMinimalniPocetOsob());
        entity.setMinutyPredVyhodnocenimKapacity(sport.getMinutyPredVyhodnocenimKapacity());
    }
}
