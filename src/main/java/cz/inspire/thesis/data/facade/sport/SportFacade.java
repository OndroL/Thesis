package cz.inspire.thesis.data.facade.sport;

import cz.inspire.thesis.data.dto.sport.sport.*;
import cz.inspire.thesis.data.model.sport.activity.ActivityEntity;
import cz.inspire.thesis.data.model.sport.sport.*;
import cz.inspire.thesis.data.service.sport.activity.ActivityService;
import cz.inspire.thesis.data.service.sport.sport.SportInstructorService;
import cz.inspire.thesis.data.service.sport.sport.SportKategorieService;
import cz.inspire.thesis.data.service.sport.sport.SportLocService;
import cz.inspire.thesis.data.service.sport.sport.SportService;
import cz.inspire.thesis.data.utils.SazbaStorna;
import cz.inspire.thesis.exceptions.ApplicationException;
import cz.inspire.thesis.exceptions.CreateException;
import jakarta.inject.Inject;

import java.util.*;
import java.util.stream.Collectors;

import static cz.inspire.thesis.data.utils.InstructorConstants.ZADNY_INSTRUKTOR_ID;
import static cz.inspire.thesis.data.utils.guidGenerator.generateGUID;

public class SportFacade {
    @Inject
    private SportService sportService;
    @Inject
    private SportInstructorService sportInstructorService;
    @Inject
    private ActivityService activityService;
    @Inject
    private SportKategorieService sportKategorieService;
    @Inject
    private SportLocService sportLocService;

    public String create(SportDetails details) throws CreateException {
        try {

            SportEntity entity = sportService.create(details);

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
                SportEntity navSport = sportService.findOptionalBy(details.getNavazujiciSportId())
                        .orElseThrow(() -> new CreateException("Linked Sport not found"));
                entity.setNavazujiciSport(navSport);
            }

            // Set linked Activity
            if (details.getActivityId() != null) {
                ActivityEntity activity = activityService.findOptionalBy(details.getActivityId())
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

            sportService.save(entity);

            return entity.getId();

        } catch (Exception e) {
            throw new CreateException("Failed to execute post-create logic for Sport", e);
        }
    }

    public String create(SportKategorieDetails details) throws CreateException {
        try {
            SportKategorieEntity entity = sportKategorieService.create(details);

            if (details.getLocaleData() != null) {
                for (SportKategorieLocDetails locDetails : details.getLocaleData().values()) {
                    SportKategorieLocEntity locEntity = new SportKategorieLocEntity();
                    locEntity.setId(generateGUID(locEntity));
                    locEntity.setJazyk(locDetails.getJazyk());
                    locEntity.setNazev(locDetails.getNazev());
                    locEntity.setPopis(locDetails.getPopis());
                }
            }

            if (details.getNadrazenaKategorieId() != null) {
                SportKategorieEntity nadrazenaKategorie = sportKategorieService.findOptionalBy(details.getNadrazenaKategorieId())
                        .orElseThrow(() -> new CreateException("Parent category not found"));
                entity.setNadrazenaKategorie(nadrazenaKategorie);
            }

            sportKategorieService.save(entity);

            return entity.getId();
        } catch (Exception e) {
            throw new CreateException("Failed to create SportKategorie", e);
        }
    }
    public void remove(SportEntity entity) throws ApplicationException {
        try{
            Collection<SportInstructorEntity> sportInstructors = sportInstructorService.findBySport(entity.getId());
            for (SportInstructorEntity localSportInstructor : sportInstructors) {
                localSportInstructor.setDeleted(true);
            }
            sportService.remove(entity);
        } catch (Exception e) {
            throw new ApplicationException("Failed why trying to remove SportEntity : " + e);
        }
    }

    public void setDetails(SportDetails details) throws ApplicationException {
        try {
            SportEntity sport = sportService.findOptionalBy(details.getId())
                    .orElseThrow(() -> new ApplicationException("Sport entity not found"));

            sportService.setBasicAttributes(sport, details);

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
                    SportEntity parentSport = sportService.findOptionalBy(parentId)
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
                    SportEntity navSport = sportService.findOptionalBy(navazujiciSport)
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

            sportService.save(sport);
        } catch (Exception e) {
            throw new ApplicationException("Failed to update Sport entity", e);
        }
    }

    public void setDetails(SportKategorieDetails details) throws ApplicationException {
        try {
            SportKategorieEntity entity = sportKategorieService.findOptionalBy(details.getId())
                    .orElseThrow(() -> new ApplicationException("SportKategorie entity not found"));

            // Clear old locale data
            entity.getLocaleData().clear();

            if (details.getLocaleData() != null) {
                for (SportKategorieLocDetails locDetails : details.getLocaleData().values()) {
                    SportKategorieLocEntity locEntity = new SportKategorieLocEntity();
                    locEntity.setId(generateGUID(locEntity));
                    locEntity.setJazyk(locDetails.getJazyk());
                    locEntity.setNazev(locDetails.getNazev());
                    locEntity.setPopis(locDetails.getPopis());
                }
            }

            // There is missing functionality in Bean for setting podrazenaKategorie for nadrazenKategorieEntity when creating new Entity with
            // nadrazenaKategorie ... or is possible dead functionality and tree is not reversible, and entities only have parent not child/s
            // nadrazenaKategorie.setPodrazenaKategorie(nadrazenaKategorie.getPodrazenzekategorie().add(entity);

            if (details.getNadrazenaKategorieId() != null) {
                SportKategorieEntity nadrazenaKategorie = sportKategorieService.findOptionalBy(details.getNadrazenaKategorieId())
                        .orElseThrow(() -> new ApplicationException("Parent category not found"));
                entity.setNadrazenaKategorie(nadrazenaKategorie);
            } else {
                entity.setNadrazenaKategorie(null);
            }

            sportKategorieService.save(entity);
        } catch (Exception e) {
            throw new ApplicationException("Failed to update SportKategorie", e);
        }
    }

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

    public void addSport(SportEntity sport) throws ApplicationException{
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
            details.setSportKategorie(getDetails(entity.getSportKategorie()));

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

    public SportKategorieDetails getDetails(SportKategorieEntity entity) {
        SportKategorieDetails details = new SportKategorieDetails();
        details.setId(entity.getId());
        details.setMultisportFacilityId(entity.getMultiSportFacilityId());
        details.setMultisportServiceUUID(entity.getMultiSportServiceUUID());

        if (entity.getNadrazenaKategorie() != null) {
            details.setNadrazenaKategorieId(entity.getNadrazenaKategorie().getId());
        }

        Map<String, SportKategorieLocDetails> localeData = entity.getLocaleData().stream()
                .collect(Collectors.toMap(SportKategorieLocEntity::getJazyk, loc -> {
                    SportKategorieLocDetails locDetails = new SportKategorieLocDetails();
                    locDetails.setJazyk(loc.getJazyk());
                    locDetails.setNazev(loc.getNazev());
                    locDetails.setPopis(loc.getPopis());
                    return locDetails;
                }));
        details.setLocaleData(localeData);

        return details;
    }

    ////////////////////////
    /////// Queries ///////
    //////////////////////


    public Collection<SportDetails> findAll() {
        return sportService.findAll().stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }


    public Collection<SportDetails> findByParent(String parentId, String jazyk) {
        return sportService.findByParent(parentId, jazyk).stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    public Collection<SportDetails> findByParent(String parentId, String jazyk, int offset, int count) {
        return sportService.findByParent(parentId, jazyk, offset, count).stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    public Collection<SportDetails> findByCategory(String categoryId, int offset, int count) {
        return sportService.findByCategory(categoryId, offset, count).stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    public Collection<SportDetails> findByZbozi(String zboziId, int offset, int count) {
        return sportService.findByZbozi(zboziId, offset, count).stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    public Collection<SportDetails> findRoot(String jazyk) {
        return sportService.findRoot(jazyk).stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    public Collection<SportDetails> findRoot(String jazyk, int offset, int count) {
        return sportService.findRoot(jazyk, offset, count).stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    public Collection<SportDetails> findCategoryRoot(int offset, int count) {
        return sportService.findCategoryRoot(offset, count).stream()
                .map(this::getDetails)
                .collect(Collectors.toList());
    }

    public Long CountAllByParentAndLanguage(String parentId, String language) {
        return sportService.countAllByParentAndLanguage(parentId, language);
    }

    public Long CountAllByCategory(String categoryId) {
        return sportService.countAllByCategory(categoryId);
    }

    public Collection<String> getAllIdsByParentAndLanguage(String parentId, String language) {
        return sportService.getAllIdsByParentAndLanguage(parentId, language);
    }

    public Long countRootByLanguage(String language) {
        return sportService.countRootByLanguage(language);
    }

    public Long countCategoryRoot() {
        return sportService.countCategoryRoot();
    }

    public Collection<String> getRootIdsByLanguage(String language) {
        return sportService.getRootIdsByLanguage(language);
    }

    // Renamed to from findAll() to findAllKategorie()
    public List<SportKategorieDetails> findAllKategorie() {
        return sportKategorieService.findAll()
                .stream().map(this::getDetails).toList();
    }

    public List<SportKategorieDetails> findRoot() {
        return sportKategorieService.findRoot()
                .stream().map(this::getDetails).toList();
    }

    public List<SportKategorieDetails> findAllByNadrazenaKategorie(String nadrazenaKategorieId) {
        return sportKategorieService.findAllByNadrazenaKategorie(nadrazenaKategorieId)
                .stream().map(this::getDetails).toList();
    }

    public Long count() {
        return sportKategorieService.count();
    }

    public Long countRoot() {
        return sportKategorieService.countRoot();
    }

    public Long countByNadrazenaKategorie(String kategorieId) {
        return sportKategorieService.countByNadrazenaKategorie(kategorieId);
    }


}
