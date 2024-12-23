package cz.inspire.thesis.data.service.sport.sport;

import cz.inspire.thesis.data.dto.sport.sport.InstructorDetails;
import cz.inspire.thesis.data.dto.sport.sport.SportDetails;
import cz.inspire.thesis.data.dto.sport.sport.SportInstructorDetails;
import cz.inspire.thesis.data.dto.sport.sport.SportLocDetails;
import cz.inspire.thesis.data.model.sport.activity.ActivityEntity;
import cz.inspire.thesis.data.model.sport.sport.*;
import cz.inspire.thesis.data.repository.sport.activity.ActivityRepository;
import cz.inspire.thesis.data.repository.sport.sport.SportRepository;
import cz.inspire.thesis.data.service.sport.activity.ActivityService;
import cz.inspire.thesis.data.utils.SazbaStorna;
import cz.inspire.thesis.exceptions.ApplicationException;
import cz.inspire.thesis.exceptions.CreateException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.deltaspike.jpa.api.transaction.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static cz.inspire.thesis.data.utils.InstructorConstants.ZADNY_INSTRUKTOR_ID;
import static cz.inspire.thesis.data.utils.guidGenerator.generateGUID;

@ApplicationScoped
public class SportService {

    @Inject
    private SportRepository sportRepository;
    @Inject
    private SportInstructorService sportInstructorService;
    @Inject
    private ActivityRepository activityRepository;
    @Inject
    private ActivityService activityService;
    @Inject
    private SportKategorieService sportKategorieService;
    @Inject
    private SportLocService sportLocService;

    @Transactional
    public String create(SportDetails details) throws CreateException {
        try {
            SportEntity entity = new SportEntity();
            if (details.getId() == null) {
                details.setId(generateGUID(entity));
            }

            entity.setId(details.getId());
            entity.setPodSportyCount(0);
            setBasicAttributes(entity, details);

            sportRepository.save(entity);
            postCreate(details, entity);
            return entity.getId();
        } catch (Exception e) {
            throw new CreateException("Failed to create Sport entity", e);
        }
    }

    @Transactional
    public void postCreate(SportDetails details, SportEntity entity) throws CreateException {
        try {
            // Save locale data
            Map<String, SportLocDetails> locData = details.getLocaleData();
            if (locData != null) {
                for (SportLocDetails locDetails : locData.values()) {
                    SportLocEntity newLocEntity = new SportLocEntity();
                    newLocEntity.setId(locDetails.getId());
                    newLocEntity.setJazyk(locDetails.getJazyk());
                    newLocEntity.setNazev(locDetails.getNazev());
                    newLocEntity.setPopis(locDetails.getPopis());

                    entity.getLocaleData().add(newLocEntity);
                }
            }

            // Set linked Sport
            if (details.getNavazujiciSportId() != null) {
                SportEntity navSport = sportRepository.findOptionalBy(details.getNavazujiciSportId())
                        .orElseThrow(() -> new CreateException("Linked Sport not found"));
                entity.setNavazujiciSport(navSport);
            }

            // Set linked Activity
            if (details.getActivityId() != null) {
                ActivityEntity activity = activityRepository.findOptionalBy(details.getActivityId())
                        .orElseThrow(() -> new CreateException("Activity not found"));
                entity.setActivity(activity);
            }

            // Add instructors
            if (details.getInstructors() != null && !details.getInstructors().isEmpty()) {
                for (InstructorDetails instructorDetails : details.getInstructors()) {
                    SportInstructorDetails sid = new SportInstructorDetails();
                    sid.setActivityId(details.getActivityId());
                    sid.setDeleted(false);
                    sid.setSportId(entity.getId());
                    sid.setInstructorId(ZADNY_INSTRUKTOR_ID.equals(instructorDetails.getId()) ?
                            null : instructorDetails.getId());
                    sportInstructorService.create(sid);
                }
            } else {
                SportInstructorDetails sid = new SportInstructorDetails();
                sid.setActivityId(details.getActivityId());
                sid.setDeleted(false);
                sid.setSportId(entity.getId());
                sportInstructorService.create(sid);
            }

            if (details.getSportKategorie() != null) {
                String kategorieId = details.getSportKategorie().getId();
                SportKategorieEntity localKategorie = sportKategorieService.findOptionalBy(kategorieId)
                            .orElseThrow(() -> new CreateException("Failed to find SportKategorie with id : " + kategorieId));
                entity.setSportKategorie(localKategorie);
            }

        } catch (Exception e) {
            throw new CreateException("Failed to execute post-create logic for Sport", e);
        }
        sportRepository.save(entity);
    }

    @Transactional
    public void setDetails(SportDetails details) throws ApplicationException {
        try {
            SportEntity sport = sportRepository.findOptionalBy(details.getId())
                    .orElseThrow(() -> new ApplicationException("Sport entity not found"));

            setBasicAttributes(sport, details);

            if (sport.getSportKategorie() != null) {
                String kategorieId = sport.getSportKategorie().getId();
                try {
                    SportKategorieEntity localKategorie = sportKategorieService.findOptionalBy(kategorieId)
                                    .orElseThrow(() -> new ApplicationException("Failed to find SportKategorie with id : " + kategorieId));
                    sport.setSportKategorie(localKategorie);
                } catch (ApplicationException e) {
                    throw new ApplicationException("SportKategorie does not exists.", e);
                }
            }

            updateSportInstructor(sport);

            sport.setInstructorSet(details.getInstructors());

            // Clean up old locale data
            // Orphan removal will delete everything hanging
            sport.getLocaleData().clear();

            // Insert new locale data
            Map<String, SportLocDetails> locData = details.getLocaleData();
            if (locData != null) {
                for (SportLocDetails locDetails : locData.values()) {
                    SportLocEntity newLocEntity = new SportLocEntity();
                    newLocEntity.setId(locDetails.getId());
                    newLocEntity.setJazyk(locDetails.getJazyk());
                    newLocEntity.setNazev(locDetails.getNazev());
                    newLocEntity.setPopis(locDetails.getPopis());

                    sport.getLocaleData().add(newLocEntity);
                }
            }

            String parentId = details.getNadrazenySportId();
            if (parentId != null) {
                try {
                    SportEntity parentSport = sportRepository.findOptionalBy(parentId)
                                    .orElseThrow(() -> new ApplicationException("Failed to find parent of Sport with parentId : " + parentId));
                    sport.setNadrazenySport(parentSport);
                } catch (ApplicationException e) {
                    throw new ApplicationException("Could not set parent sport.", e);
                }
            } else {
                sport.setNadrazenySport(null);
            }

            String navazujiciSport = details.getNavazujiciSportId();
            if (navazujiciSport != null) {
                try {
                    SportEntity navSport = sportRepository.findOptionalBy(navazujiciSport)
                            .orElseThrow(() -> new ApplicationException("Failed to find navazujiciSport for Sport with navazujiciSportId : " + navazujiciSport));
                    sport.setNavazujiciSport(navSport);
                } catch (ApplicationException e) {
                    throw new ApplicationException("Could not set navazujici sport.", e);
                }
            } else {
                sport.setNavazujiciSport(null);
            }

            String activityId = details.getActivityId();
            if (activityId != null) {
                try {
                    ActivityEntity activity = activityService.findOptionalBy(activityId)
                            .orElseThrow(() -> new ApplicationException("Failed to find Activity for Sport with acitivityId : " + activityId));
                    sport.setActivity(activity);
                } catch (ApplicationException ex) {
                    throw new ApplicationException("Could not set activity.", ex);
                }
            }

            sportRepository.save(sport);
        } catch (Exception e) {
            throw new ApplicationException("Failed to update Sport entity", e);
        }
    }

    @Transactional
    public void remove(SportEntity entity) throws ApplicationException {
        try{
            Collection<SportInstructorEntity> sportInstructors = sportInstructorService.findBySport(entity.getId());
            for (SportInstructorEntity localSportInstructor : sportInstructors) {
                localSportInstructor.setDeleted(true);
            }
            sportRepository.remove(entity);
        } catch (Exception e) {
            throw new ApplicationException("Failed why trying to remove SportEntity : " + e);
        }
    }

    @Transactional
    public void checkSportWithoutInstructor(SportEntity sport) throws CreateException {
        try {
            Long instructorsCount = sportInstructorService.countSportInstructors(sport.getId());
            if (instructorsCount == null || instructorsCount == 0) {
                //add special instructor representing NONE instructor
                SportInstructorDetails sid = new SportInstructorDetails();
                sid.setActivityId(sport.getActivity().getId());
                sid.setDeleted(false);
                sid.setInstructorId(null);
                sportInstructorService.create(sid);
            }
        } catch (CreateException ex) {
            throw new CreateException("Failed to create SportInstructorEntity without Instructor for sport : " + sport.getId(), ex);
        }
//        } catch (ApplicationException ex) {
//            throw new ApplicationException("Nepodarilo sa zistit pocet instruktorov sportu " + sport.getId(), ex);
//        }
    }

    @Transactional
    private void updateSportInstructor(SportEntity sport) throws ApplicationException, CreateException {

        Set<InstructorDetails> instructors = loadInstructors(sport);
        Set<String> oldInstructorIds = new HashSet<String>();
        for (InstructorDetails instructorDetails : instructors) {
            oldInstructorIds.add(instructorDetails.getId());
        }

        Set<String> newInstructorIds = new HashSet<String>();
        for (InstructorDetails instructorDetails : sport.getInstructorSet()) {
            newInstructorIds.add(instructorDetails.getId());
        }

        Set<String> deleted = new HashSet<String>(oldInstructorIds);
        deleted.removeAll(newInstructorIds);
        if (!deleted.isEmpty()) {
            String sportId = sport.getId();
            for (String sportInstructorId : deleted) {
                try {
                    new SportInstructorEntity();
                    SportInstructorEntity sportInstructor;
                    if (ZADNY_INSTRUKTOR_ID.equals(sportInstructorId)) {
                        sportInstructor = sportInstructorService.findBySportWithoutInstructor(sportId)
                                .orElseThrow(() -> new ApplicationException("Failed to find Sport without Instructor in SportInstructor"));
                    } else {
                        sportInstructor = sportInstructorService.findBySportAndInstructor(sportId, sportInstructorId)
                                .orElseThrow(() -> new ApplicationException("Failed to find Sport with Instructor in SportInstructor"));
                    }
                    sportInstructor.setDeleted(true);
                } catch (ApplicationException ex) {
                    throw new ApplicationException ("Failed to mark SportInstructor as deleted for id : " + sportInstructorId, ex);
                }
            }
        }

        newInstructorIds.removeAll(oldInstructorIds);
        if (!newInstructorIds.isEmpty()) {
            for (String instructorId : newInstructorIds) {
                SportInstructorDetails sid = new SportInstructorDetails();
                sid.setActivityId(sport.getActivity().getId());
                sid.setDeleted(false);
                sid.setInstructorId(ZADNY_INSTRUKTOR_ID.equals(instructorId) ? null : instructorId);
                sid.setSportId(sport.getId());
                try {
                    sportInstructorService.create(sid);
                } catch (CreateException ex) {
                    throw new CreateException("Failed to create SportInstructor while updating from SportEntity !", ex);
                }
            }
        }
    }

    private Set<InstructorDetails> loadInstructors(SportEntity entity) {
        //        Odfiltrovani smazanych polozek
        Set<InstructorDetails> instructors = new HashSet<InstructorDetails>();
        Collection<SportInstructorEntity> sportInstructors = entity.getSportInstructors();
        for (SportInstructorEntity localSportInstructor : sportInstructors) {
            if (localSportInstructor.getDeleted()) {
                continue;
            }
            InstructorEntity instructor = localSportInstructor.getInstructor();
            if (instructor == null) {
                //add special instructor standing for none instructor
                InstructorDetails noneInstructor = new InstructorDetails();
                noneInstructor.setId(ZADNY_INSTRUKTOR_ID);
                instructors.add(noneInstructor);
            } else {
                instructors.add(mapInstructorData(instructor));
            }
        }
        return instructors;
    }

    private InstructorDetails mapInstructorData(InstructorEntity instructorEntity) {
        InstructorDetails instructorDetails = new InstructorDetails();
        instructorDetails.setId(instructorEntity.getId());
        instructorDetails.setColor(instructorEntity.getColor());
        instructorDetails.setDeleted(instructorEntity.getDeleted());
        instructorDetails.setEmail(instructorEntity.getEmail());
        instructorDetails.setFirstName(instructorEntity.getFirstName());
        instructorDetails.setLastName(instructorEntity.getLastName());
        instructorDetails.setPhoneCode(instructorEntity.getPhoneCode());
        instructorDetails.setPhoneNumber(instructorEntity.getPhoneNumber());
        instructorDetails.setGoogleCalendarId(instructorEntity.getGoogleCalendarId());
        instructorDetails.setGoogleCalendarNotification(instructorEntity.getGoogleCalendarNotification());
        instructorDetails.setGoogleCalendarNotificationBefore(instructorEntity.getGoogleCalendarNotificationBefore());
//        instructorDetails.setPhoto(instructorEntity.getPhoto());
        return instructorDetails;
    }



    public void addSport(SportEntity sport) {
        sport.getPodrazeneSporty().add(sport);
        sport.setPodSportyCount(sport.getPodSportyCount() + 1);
    }

    public SportDetails getDetails(SportEntity entity) {
        SportDetails details = new SportDetails();
        details.setId(entity.getId());
        details.setTyp(entity.getTyp());
        details.setZboziId(entity.getZboziId());
        details.setSkladId(entity.getSkladId());
        details.setPodSportyCount(entity.getPodSportyCount());
        details.setSazbaJednotek(entity.getSazbaJednotek());
        details.setSazbaNaOsobu(entity.getSazbaNaOsobu());
        details.setSazbaNaCas(entity.getSazbaNaCas());
        details.setUctovatZalohu(entity.getUctovatZalohu());
        details.setSazbyStorna((List<SazbaStorna>) entity.getSazbyStorna());
        details.setMinDelkaRezervace(entity.getMinDelkaRezervace());
        details.setMaxDelkaRezervace(entity.getMaxDelkaRezervace());
        details.setObjednavkaZaplniObjekt(entity.getObjednavkaZaplniObjekt());
        details.setMaximalniPocetOsobNaZakaznika(entity.getMaximalniPocetOsobNaZakaznika());
        details.setDelkaRezervaceNasobkem(entity.getDelkaRezervaceNasobkem());

        details.setBarvaPopredi(entity.getBarvaPopredi());
        details.setBarvaPozadi(entity.getBarvaPozadi());
        details.setZobrazitText(entity.getZobrazitText());
        details.setViditelnyWeb(entity.getViditelnyWeb());

        details.setNavRezervaceOffset(entity.getNavRezervaceOffset());
        details.setDelkaHlavniRez(entity.getDelkaHlavniRez());

        details.setMinimalniPocetOsob(entity.getMinimalniPocetOsob());
        details.setMinutyPredVyhodnocenimKapacity(entity.getMinutyPredVyhodnocenimKapacity());

        if (entity.getSportKategorie() != null) {
            details.setSportKategorie(sportKategorieService.getDetails(entity.getSportKategorie()));

        }

        Map<String, SportLocDetails> locData = entity.getLocaleData().stream()
                .map(sportLocService::getDetails)
                .collect(Collectors.toMap(SportLocDetails::getJazyk, sportLocDetails -> sportLocDetails));

        // Set the locale data into the SportDetails object
        details.setLocaleData(locData);

        SportEntity navazujiciSport = entity.getNavazujiciSport();
        if (navazujiciSport != null){
            details.setNavazujiciSportId(navazujiciSport.getId());
        }

        ActivityEntity activity = entity.getActivity();
        if (activity != null) {
            details.setActivityId(activity.getId());
        }

//        Odfiltrovani smazanych polozek
        Set<InstructorDetails> instructors = loadInstructors(entity);
        entity.setInstructorSet(instructors);
        details.setInstructors(instructors);

        return details;

    }

    public String getActivityId(SportEntity sport) {
        ActivityEntity activity = sport.getActivity();
        return activity == null ? null : activity.getId();
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

    public Optional<SportEntity> findOptionalBy(String id) {
        return sportRepository.findOptionalBy(id);
    }


    private void setBasicAttributes(SportEntity entity, SportDetails sport) {
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
