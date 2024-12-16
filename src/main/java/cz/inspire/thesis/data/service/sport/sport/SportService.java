package cz.inspire.thesis.data.service.sport.sport;

import cz.inspire.thesis.data.dto.sport.sport.InstructorDetails;
import cz.inspire.thesis.data.dto.sport.sport.SportDetails;
import cz.inspire.thesis.data.model.sport.activity.ActivityEntity;
import cz.inspire.thesis.data.model.sport.sport.SportEntity;
import cz.inspire.thesis.data.repository.sport.activity.ActivityRepository;
import cz.inspire.thesis.data.repository.sport.sport.SportRepository;
import cz.inspire.thesis.exceptions.ApplicationException;
import cz.inspire.thesis.exceptions.CreateException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Collection;
import java.util.stream.Collectors;

import static cz.inspire.thesis.data.utils.guidGenerator.generateGUID;

@ApplicationScoped
public class SportService {

    @Inject
    private SportRepository sportRepository;

    @Inject
    private ActivityRepository activityRepository;

    public String ejbCreate(SportDetails details) throws CreateException {
        try {
            SportEntity entity = new SportEntity();
            if (details.getId() == null) {
                details.setId(generateGUID(entity));
            }
            mapToEntity(entity, details);

            sportRepository.save(entity);
            ejbPostCreate(details, entity);
            return entity.getId();
        } catch (Exception e) {
            throw new CreateException("Failed to create Sport entity", e);
        }
    }

    public void ejbPostCreate(SportDetails sport, SportEntity entity) throws CreateException {
        try {
            // Save locale data

            // Set linked Sport
            if (sport.getNavazujiciSportId() != null) {
                SportEntity navSport = sportRepository.findOptionalBy(sport.getNavazujiciSportId())
                        .orElseThrow(() -> new CreateException("Linked Sport not found"));
                entity.setNavazujiciSport(navSport);
            }

            // Set linked Activity
            if (sport.getActivityId() != null) {
                ActivityEntity activity = activityRepository.findOptionalBy(sport.getActivityId())
                        .orElseThrow(() -> new CreateException("Activity not found"));
                entity.setActivity(activity);
            }

            // Add instructors
            if (sport.getInstructors() != null && !sport.getInstructors().isEmpty()) {
                for (InstructorDetails instructorDetails : sport.getInstructors()) {
                    // Logic to add instructors
                }
            }


        } catch (Exception e) {
            throw new CreateException("Failed to execute post-create logic for Sport", e);
        }

        // Link Sport Category
        sportRepository.save(entity);
    }

    public void setDetails(SportDetails details) throws ApplicationException {
        try {
            SportEntity entity = sportRepository.findOptionalBy(details.getId())
                    .orElseThrow(() -> new ApplicationException("Sport entity not found"));
            mapToEntity(entity, details);

            sportRepository.save(entity);
        } catch (Exception e) {
            throw new ApplicationException("Failed to update Sport entity", e);
        }
    }

    public SportDetails getDetails(SportEntity entity) {
        SportDetails details = new SportDetails();
        details.setId(entity.getId());
        details.setTyp(entity.getTyp());
        details.setZboziId(entity.getZboziId());
        details.setSkladId(entity.getSkladId());
        details.setSazbaJednotek(entity.getSazbaJednotek());
        details.setSazbaNaOsobu(entity.getSazbaNaOsobu());
        details.setSazbaNaCas(entity.getSazbaNaCas());
        details.setUctovatZalohu(entity.getUctovatZalohu());
        details.setPodSportyCount(entity.getPodSportyCount());
        details.setSazbyStorna(entity.getSazbyStorna());
        details.setMinDelkaRezervace(entity.getMinDelkaRezervace());
        details.setMaxDelkaRezervace(entity.getMaxDelkaRezervace());
        details.setObjednavkaZaplniObjekt(entity.getObjednavkaZaplniObjekt());
        details.setDelkaRezervaceNasobkem(entity.getDelkaRezervaceNasobkem());
        details.setBarvaPopredi(entity.getBarvaPopredi());
        details.setBarvaPozadi(entity.getBarvaPozadi());
        details.setZobrazitText(entity.getZobrazitText());
        details.setViditelnyWeb(entity.getViditelnyWeb());
        details.setNavRezervaceOffset(entity.getNavRezervaceOffset());
        details.setDelkaHlavniRez(entity.getDelkaHlavniRez());
        details.setMinimalniPocetOsob(entity.getMinimalniPocetOsob());
        details.setMinutyPredVyhodnocenimKapacity(entity.getMinutyPredVyhodnocenimKapacity());
        details.setMaximalniPocetOsobNaZakaznika(entity.getMaximalniPocetOsobNaZakaznika());
        return details;
    }

    public Collection<SportDetails> findAll() {
        return sportRepository.findAll().stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }


    public Collection<SportDetails> findByParent(String parentId, String jazyk) {
        return sportRepository.findByParent(parentId, jazyk).stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    public Collection<SportDetails> findByParent(String parentId, String jazyk, int offset, int count) {
        return sportRepository.findByParent(parentId, jazyk, offset, count).stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    public Collection<SportDetails> findByCategory(String categoryId, int offset, int count) {
        return sportRepository.findByCategory(categoryId, offset, count).stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    public Collection<SportDetails> findByZbozi(String zboziId, int offset, int count) {
        return sportRepository.findByZbozi(zboziId, offset, count).stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    public Collection<SportDetails> findRoot(String jazyk) {
        return sportRepository.findRoot(jazyk).stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    public Collection<SportDetails> findRoot(String jazyk, int offset, int count) {
        return sportRepository.findRoot(jazyk, offset, count).stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    public Collection<SportDetails> findCategoryRoot(int offset, int count) {
        return sportRepository.findCategoryRoot(offset, count).stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    public Long CountAllByParentAndLanguage(String parentId, String language) {
        return sportRepository.countAllByParentAndLanguage(parentId, language);
    }

    public Long CountAllByCategory(String categoryId) {
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

    private void mapToEntity(SportEntity entity, SportDetails details) {
        entity.setTyp(details.getTyp());
        entity.setZboziId(details.getZboziId());
        entity.setSkladId(details.getSkladId());
        entity.setSazbaJednotek(details.getSazbaJednotek());
        entity.setSazbaNaOsobu(details.getSazbaNaOsobu());
        entity.setSazbaNaCas(details.getSazbaNaCas());
        entity.setUctovatZalohu(details.getUctovatZalohu());
        entity.setPodSportyCount(details.getPodSportyCount());
        entity.setSazbyStorna(details.getSazbyStorna());
        entity.setMinDelkaRezervace(details.getMinDelkaRezervace());
        entity.setMaxDelkaRezervace(details.getMaxDelkaRezervace());
        entity.setObjednavkaZaplniObjekt(details.getObjednavkaZaplniObjekt());
        entity.setDelkaRezervaceNasobkem(details.getDelkaRezervaceNasobkem());
        entity.setBarvaPopredi(details.getBarvaPopredi());
        entity.setBarvaPozadi(details.getBarvaPozadi());
        entity.setZobrazitText(details.getZobrazitText());
        entity.setViditelnyWeb(details.getViditelnyWeb());
        entity.setNavRezervaceOffset(details.getNavRezervaceOffset());
        entity.setDelkaHlavniRez(details.getDelkaHlavniRez());
        entity.setMinimalniPocetOsob(details.getMinimalniPocetOsob());
        entity.setMinutyPredVyhodnocenimKapacity(details.getMinutyPredVyhodnocenimKapacity());
        entity.setMaximalniPocetOsobNaZakaznika(details.getMaximalniPocetOsobNaZakaznika());
    }
}
